package me.selemba.common.ui.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.ScrollState
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Image
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.launch
import me.selemba.common.persistence.Storage
import me.selemba.common.persistence.music.TaggedMusic
import me.selemba.common.persistence.schema.SongFile

@Composable
fun EditScreen(sf: SongFile,exit: () -> Unit){
    Surface(Modifier.fillMaxSize(), tonalElevation = 3.dp) {
        val file = remember { TaggedMusic.fromSongFile(sf) }
        Row(Modifier.widthIn(1.dp,500.dp).fillMaxWidth(.8f), horizontalArrangement = Arrangement.SpaceAround, verticalAlignment = Alignment.CenterVertically) {
            Box(Modifier.weight(.5f).fillMaxHeight()){
                Surface(tonalElevation = 3.dp, shape = RoundedCornerShape(5.dp), modifier = Modifier.fillMaxHeight(.5f).aspectRatio(1f).align(Alignment.Center)) {
                    if (file.artwork == null) {
                        Image(Icons.Outlined.Image, "", Modifier.align(Alignment.Center).padding(5.dp))
                    } else {
                        Image(file.artwork, "", Modifier.align(Alignment.Center).padding(5.dp))
                    }
                }
            }

            val scroll = remember { ScrollState(0) }

            Column(horizontalAlignment = Alignment.CenterHorizontally, modifier = Modifier.weight(.5f).verticalScroll(scroll)) {
                OutlinedTextField(
                    file.title,
                    { str -> file.title = str },
                    singleLine = true,
                    label = @Composable { Text("Titel") },
                    modifier = Modifier.fillMaxWidth(.8f)
                )
                OutlinedTextField(
                    file.artist,
                    { str -> file.artist = str },
                    singleLine = true,
                    label = @Composable { Text("Künstler") },
                    modifier = Modifier.fillMaxWidth(.8f)
                )
                OutlinedTextField(
                    file.album,
                    { str -> file.album = str },
                    singleLine = true,
                    label = @Composable { Text("Album") },
                    modifier = Modifier.fillMaxWidth(.8f)
                )
                OutlinedTextField(
                    file.year,
                    { str -> file.year = str },
                    singleLine = true,
                    label = @Composable { Text("Veröffentlichungsdatum") },
                    modifier = Modifier.fillMaxWidth(.8f)
                )
                OutlinedTextField(
                    file.genre,
                    { str -> file.genre = str },
                    singleLine = true,
                    label = @Composable { Text("Genre") },
                    modifier = Modifier.fillMaxWidth(.8f)
                )
                OutlinedTextField(
                    file.bpm,
                    { str -> file.bpm = str.filter { it.isDigit() } },
                    singleLine = true,
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                    label = @Composable { Text("BPM") },
                    modifier = Modifier.fillMaxWidth(.8f)
                )
                Box(Modifier.weight(1f,true))
                Row(horizontalArrangement = Arrangement.Center) {
                    ElevatedButton({ exit() }) {
                        Text("Abbrechen")
                    }
                    OutlinedButton({
                        Storage.scope.launch {
                            Storage.suspendTransaction {
                                sf.title=file.title
                                sf.artist=file.artist
                                sf.album=file.album
                                sf.genre=file.genre
                                sf.year=file.year
                                sf.bpm=file.bpm.toInt()
                            }
                            exit()
                        }
                    }) {
                        Text("Speichern")
                    }
                }
            }
        }
    }
}