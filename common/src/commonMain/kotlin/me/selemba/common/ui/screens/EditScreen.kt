package me.selemba.common.ui.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Image
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import me.selemba.common.persistence.music.TaggedMusic
import me.selemba.common.persistence.schema.SongFile

@Composable
fun EditScreen(sf: SongFile,exit: () -> Unit){
    Surface(Modifier.fillMaxSize(), tonalElevation = 3.dp) {
        val file = remember { TaggedMusic.fromSongFile(sf) }
        Row(Modifier.widthIn(1.dp,500.dp).fillMaxWidth(.8f), horizontalArrangement = Arrangement.SpaceEvenly) {
            Box(Modifier.weight(.5f).fillMaxHeight()){
                Surface(tonalElevation = 3.dp, shape = RoundedCornerShape(5.dp), modifier = Modifier.aspectRatio(1f)) {
                    if (file.artwork == null) {
                        Image(Icons.Outlined.Image, "", Modifier.align(Alignment.Center))
                    } else {
                        Image(file.artwork, "", Modifier.align(Alignment.Center))
                    }
                }
            }
            Column(horizontalAlignment = Alignment.CenterHorizontally, modifier = Modifier.weight(.5f)) {
                OutlinedTextField(
                    file.title,
                    { str -> file.title = str },
                    singleLine = true,
                    label = @Composable { Text("Titel") },
                    modifier = Modifier.weight(1f)
                )
                OutlinedTextField(
                    file.artist,
                    { str -> file.artist = str },
                    singleLine = true,
                    label = @Composable { Text("Künstler") },
                    modifier = Modifier.weight(1f)
                )
                OutlinedTextField(
                    file.album,
                    { str -> file.album = str },
                    singleLine = true,
                    label = @Composable { Text("Album") },
                    modifier = Modifier.weight(1f)
                )
                OutlinedTextField(
                    file.year,
                    { str -> file.year = str },
                    singleLine = true,
                    label = @Composable { Text("Veröffentlichungsdatum") },
                    modifier = Modifier.weight(1f)
                )
                OutlinedTextField(
                    file.genre,
                    { str -> file.genre = str },
                    singleLine = true,
                    label = @Composable { Text("Genre") },
                    modifier = Modifier.weight(1f)
                )
                OutlinedTextField(
                    file.bpm,
                    { str -> file.bpm = str.filter { it.isDigit() } },
                    singleLine = true,
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                    label = @Composable { Text("BPM") },
                    modifier = Modifier.weight(1f)
                )
                Row(horizontalArrangement = Arrangement.Center) {
                    ElevatedButton({ exit() }) {
                        Text("Abbrechen")
                    }
                    OutlinedButton({}) {
                        Text("Speichern")
                    }
                }
            }
        }
    }
}