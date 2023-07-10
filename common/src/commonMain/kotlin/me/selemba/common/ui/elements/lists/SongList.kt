package me.selemba.common.ui.elements.lists

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.focusable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.onClick
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Archive
import androidx.compose.material.icons.rounded.Archive
import androidx.compose.material.icons.rounded.Download
import androidx.compose.material.icons.rounded.ExpandLess
import androidx.compose.material.icons.rounded.ExpandMore
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.unit.dp
import me.selemba.common.persistence.Storage
import me.selemba.common.persistence.schema.SongFile
import org.jetbrains.exposed.sql.StdOutSqlLogger
import org.jetbrains.exposed.sql.addLogger
import org.jetbrains.exposed.sql.transactions.transaction
import kotlin.time.Duration.Companion.milliseconds

@OptIn(ExperimentalFoundationApi::class, ExperimentalMaterial3Api::class)
@Composable
fun SongList() {
    val model = remember { SongListModel() }
    var songs = model.songs

    LaunchedEffect(null){
        Storage.initialize()
        transaction {
            addLogger(StdOutSqlLogger)
            SongFile.new{
                name = "test"
                length = 103550
            }
            SongFile.new{
                name = "abc"
                length = 235663
            }
        }
        model.load()
    }

    Surface(tonalElevation = 1.dp,shadowElevation = 1.dp, shape = RoundedCornerShape(15.dp), modifier = Modifier.fillMaxWidth().padding(top = 300.dp)) {
        Column {
            var text by remember { mutableStateOf("") }
            Row(horizontalArrangement = Arrangement.Center) {
                Row(modifier = Modifier.weight(.25f), horizontalArrangement = Arrangement.End){
                }
                OutlinedTextField(model.search?:"",{value -> if (value.isEmpty())model.search=null else model.search=value}, maxLines = 1, singleLine = true, enabled = true, modifier = Modifier.height(55.dp).weight(.5f), label = { Text("Suche") })
                Row(modifier = Modifier.weight(.25f).height(55.dp).padding(end = 20.dp).padding(vertical = 5.dp), verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.End){
                    Box(modifier = Modifier) {
                        Icon(
                            Icons.Outlined.Archive,
                            "",
                            tint = MaterialTheme.colorScheme.primary,
                            modifier = Modifier.scale(1.2f)
                        )
                    }
                }
            }
            LazyColumn(modifier = Modifier.padding(10.dp)) {
                stickyHeader {
                    Surface(tonalElevation = 20.dp, color = MaterialTheme.colorScheme.primaryContainer) {
                        Row(modifier = Modifier.padding(5.dp), verticalAlignment = Alignment.CenterVertically) {
                            val man = LocalFocusManager.current
                            Row(
                                modifier = Modifier.weight(.5f).onClick {
                                    man.clearFocus()
                                    if (model.column == SongListModel.Column.NAME) model.descending =
                                        !model.descending else model.column = SongListModel.Column.NAME
                                }, verticalAlignment = Alignment.CenterVertically
                            ) {
                                Text("Name")
                                if (model.column == SongListModel.Column.NAME)
                                    Icon(
                                        if (model.descending) Icons.Rounded.ExpandLess else Icons.Rounded.ExpandMore,
                                        ""
                                    )
                            }
                            Row(
                                modifier = Modifier.weight(.5f).onClick {
                                    man.clearFocus()
                                    if (model.column == SongListModel.Column.LENGTH) model.descending =
                                        !model.descending else model.column = SongListModel.Column.LENGTH
                                }, verticalAlignment = Alignment.CenterVertically
                            ) {
                                Text("Länge")
                                if (model.column == SongListModel.Column.LENGTH)
                                    Icon(
                                        if (model.descending) Icons.Rounded.ExpandLess else Icons.Rounded.ExpandMore,
                                        ""
                                    )
                            }
                        }
                    }
                    Divider(thickness = 2.dp)
                }
                if(model.songs.isEmpty()){
                    item {
                        Row(modifier = Modifier.padding(5.dp).fillMaxWidth(), horizontalArrangement = Arrangement.Center) {
                            Text("Keine Elemente gefunden.", modifier = Modifier)
                        }
                    }
                }
                items(songs) { song ->
                    Row(modifier = Modifier.padding(5.dp)) {
                        Text(song.name, modifier = Modifier.weight(.5f))
                        Text(
                            song.length.milliseconds.toComponents { hours, minutes, seconds, nanoseconds -> if (hours > 0) "$hours:$minutes:$seconds" else "$minutes:$seconds" },
                            modifier = Modifier.weight(.5f)
                        )
                    }
                }
            }
        }
    }
}