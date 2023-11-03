package me.selemba.common.ui.elements.lists

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.*
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.window.WindowDraggableArea
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.DialogWindow
import kotlinx.coroutines.launch
import me.selemba.common.persistence.Storage
import me.selemba.common.persistence.music.MusicStorage
import me.selemba.common.persistence.music.MusicTagger
import me.selemba.common.persistence.music.TaggedMusic
import me.selemba.common.persistence.schema.SongFile
import me.selemba.common.ui.elements.interactive.InteractiveIconButton
import me.selemba.common.ui.elements.player.PlayerModel
import kotlin.time.Duration.Companion.milliseconds

@OptIn(ExperimentalFoundationApi::class, ExperimentalMaterial3Api::class)
@Composable
fun SongList(import: () -> Unit, export: () -> Unit, edit: (SongFile) -> Unit, model: SongListModel,playerModel: PlayerModel) {
    var songs = model.songs

    var selected: SongFile? by remember { mutableStateOf(null) }

    LaunchedEffect(null) {
        model.load()
    }
    Column() {
        var text by remember { mutableStateOf("") }
        Row(horizontalArrangement = Arrangement.Center, verticalAlignment = Alignment.CenterVertically) {
            Row(modifier = Modifier.weight(.25f), horizontalArrangement = Arrangement.End) {
            }
            OutlinedTextField(
                model.search ?: "",
                { value -> if (value.isEmpty()) model.search = null else model.search = value },
                maxLines = 1,
                singleLine = true,
                enabled = true,
                modifier = Modifier.height(75.dp).weight(.5f).padding(bottom = 10.dp),
                label = { Text("Suche") })
            Row(
                modifier = Modifier.weight(.25f).height(50.dp).padding(end = 20.dp).padding(vertical = 5.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.End
            ) {
                InteractiveIconButton(
                    Icons.Outlined.FilePresent,
                    import,
                    MaterialTheme.colorScheme.primary,

                )
            }
        }
        if (model.loading) {
            Column(Modifier.fillMaxSize().padding(20.dp), horizontalAlignment = Alignment.CenterHorizontally) {
                CircularProgressIndicator(strokeWidth = 5.dp)
            }
        } else {
            val inter = remember{ MutableInteractionSource() }

            Row(Modifier.padding(horizontal = 10.dp)) {
                Box(Modifier.weight(.7f).clickable(indication = null, interactionSource = inter) { selected = null }.fillMaxHeight()) {
                    SortedList(
                        model.songs.map {
                            SortedListRow(
                                it.id.value,
                                {selected=it},
                                SortedListCell(it.title) { Text(it.title) },
                                SortedListCell(it.title) { Text(it.bpm.toString()) },
                                SortedListCell(it.title) { Text(it.genre) },
                                SortedListCell(it.length) { Text(it.length.milliseconds.toComponents { hours, minutes, seconds, nanoseconds -> if (hours > 0) "$hours:$minutes:$seconds" else "$minutes:$seconds" }) })
                        },
                        listOf(
                            SortedListHeader({ Text("Name") }, true),
                            SortedListHeader({ Text("BPM") }, true,.2f),
                            SortedListHeader({ Text("Genre") }, true,.2f),
                            SortedListHeader({ Text("Länge") }, true, .2f)
                        )
                    )
                }
                AnimatedVisibility(selected != null, modifier = Modifier.weight(.3f, true).fillMaxHeight()) {
                    Surface (tonalElevation = 3.dp){

                        var edit by remember{ mutableStateOf(false) }
                        var delete by remember{ mutableStateOf(false) }

                        if(delete){
                            DialogWindow({}, title = "Löschen?", transparent = true, undecorated = true, content = @Composable {
                                Surface(Modifier.fillMaxSize(), shape = RoundedCornerShape(15.dp)) {
                                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                                        Surface(
                                            tonalElevation = 3.dp,
                                            modifier = Modifier.border(
                                                1.dp,
                                                Color.Transparent,
                                                RoundedCornerShape(topStart = 15.dp, topEnd = 15.dp)
                                            )
                                        ) {
                                            WindowDraggableArea(Modifier.height(40.dp).fillMaxWidth()) {
                                                Row(
                                                    horizontalArrangement = Arrangement.Center,
                                                    verticalAlignment = Alignment.CenterVertically,
                                                    modifier = Modifier.fillMaxSize()
                                                ) {
                                                    Text("Löschen?")
                                                }
                                            }
                                        }
                                        Box(Modifier.weight(1f),) {
                                            Text(
                                                "Tatsächlich Musik löschen?",
                                                textAlign = TextAlign.Center,
                                                modifier = Modifier.align(Alignment.Center)
                                            )
                                        }
                                        Row(Modifier.padding(10.dp)) {
                                            ElevatedButton({ delete = false }) {
                                                Text("Abbrechen")
                                            }
                                            OutlinedButton({
                                                Storage.scope.launch {
                                                    MusicStorage.delete(selected!!)
                                                    Storage.suspendTransaction {
                                                        selected!!.delete()
                                                    }
                                                    selected = null
                                                    model.load()
                                                }
                                            }) {
                                                Text("Ok")
                                            }
                                        }
                                    }
                                }
                            })
                        }

                        Column(Modifier, horizontalAlignment = Alignment.CenterHorizontally) {
                            Surface(
                                tonalElevation = 3.dp,
                                shadowElevation = 3.dp,
                                shape = RoundedCornerShape(5.dp),
                                modifier = Modifier.padding(5.dp).fillMaxWidth(.5f).aspectRatio(1f)
                            ) {
                                Box(Modifier.padding(5.dp), Alignment.Center) {
                                    val art = if(selected == null)null
                                    else MusicTagger.readArtwork(MusicStorage.get(selected!!).file)
                                    if (art != null) {
                                        Image(art, "Cover")
                                    } else {
                                        Image(Icons.Outlined.Image, "Cover")
                                    }
                                }
                            }
                            Text(selected?.title?:"", style = MaterialTheme.typography.headlineSmall, modifier = Modifier.padding(top = 10.dp))
                            Column(Modifier.fillMaxWidth(), horizontalAlignment = Alignment.CenterHorizontally) {
                                Row(horizontalArrangement = Arrangement.SpaceBetween, modifier = Modifier.widthIn(1.dp,250.dp).fillMaxWidth(.8f)) {
                                    Text("Künstler:")
                                    Text(selected?.artist?:"")
                                }
                            }
                            Column(Modifier.fillMaxWidth(), horizontalAlignment = Alignment.CenterHorizontally) {
                                Row(horizontalArrangement = Arrangement.SpaceBetween, modifier = Modifier.widthIn(1.dp,250.dp).fillMaxWidth(.8f)) {
                                    Text("Album:")
                                    Text(selected?.album?:"")
                                }
                            }
                            Column(Modifier.fillMaxWidth(), horizontalAlignment = Alignment.CenterHorizontally) {
                                Row(horizontalArrangement = Arrangement.SpaceBetween, modifier = Modifier.widthIn(1.dp,250.dp).fillMaxWidth(.8f)) {
                                    Text("Genre:")
                                    Text(selected?.genre?:"")
                                }
                            }
                            Box(Modifier.weight(1f))
                            Row (Modifier.padding(10.dp)){

                                FilledTonalIconButton({selected = null}, ){
                                    Icon(Icons.Outlined.Close,"", tint = MaterialTheme.colorScheme.primary)
                                }
                                FilledTonalIconButton({Storage.scope.launch { playerModel.loadFile(MusicStorage.get(selected!!));playerModel.start()}}, ){
                                    Icon(Icons.Outlined.PlayArrow,"", tint = MaterialTheme.colorScheme.primary)
                                }
                                FilledTonalIconButton({edit(selected!!)}, ){
                                    Icon(Icons.Outlined.Edit,"", tint = MaterialTheme.colorScheme.primary)
                                }
                                FilledTonalIconButton({delete = true}, ){
                                    Icon(Icons.Outlined.Delete,"", tint = MaterialTheme.colorScheme.primary)
                                }

                            }
                        }


                    }
                }

            }
        }
    }
}