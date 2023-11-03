package me.selemba.common.ui.screens

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.window.WindowDraggableArea
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.FolderOpen
import androidx.compose.material.icons.outlined.Image
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.DialogWindow
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import me.selemba.common.persistence.Storage
import me.selemba.common.ui.elements.interactive.FilePicker
import me.selemba.common.ui.elements.interactive.InteractiveIconButton

@Composable
fun ImportScreen(exit: () -> Unit) {

    val model = remember {
        ImportModel(
            listOf<@Composable (ImportModel) -> Unit>(
                @Composable { FileSelect(it) { it.canContinue = true } },
                @Composable { FileTagging(it) { it.canContinue = true } },
                @Composable { Importing(it) { it.canContinue = true } },
            ),
            exit
        )
    }

    Surface(tonalElevation = 1.dp) {
        Column(Modifier.fillMaxSize().padding(20.dp), horizontalAlignment = Alignment.CenterHorizontally) {
            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.Center) {
                Text("Import", style = MaterialTheme.typography.headlineMedium)
            }
            Column(
                modifier = Modifier.weight(1f).widthIn(min = 1.dp, max = 1200.dp).fillMaxWidth(.8f),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                AnimatedContent(
                    model.stage,
                    transitionSpec = {
                        if (model.stages.indexOf(initialState) < model.stages.indexOf(targetState)) {
                            (slideInHorizontally { width -> width }).togetherWith(slideOutHorizontally { width -> -width })
                        } else {
                            (slideInHorizontally { width -> -width }).togetherWith(slideOutHorizontally { width -> width })
                        }
                    }
                ) {
                    it(model)
                }

            }
            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.Center) {
                Button(
                    onClick = { model.previousStage(); model.canContinue = true; },
                    enabled = model.stages.indexOf(model.stage) > 0
                ) {
                    Text("Zurück")
                }
                Spacer(Modifier.width(10.dp))
                Button(onClick = exit) {
                    Text("Abbrechen")
                }
                Spacer(Modifier.width(10.dp))
                Button(onClick = { model.nextStage(); model.canContinue = false; }, enabled = model.canContinue) {
                    Text("Weiter")
                }
            }
        }
    }
}

@Composable
fun Importing(model: ImportModel, canContinue: () -> Unit) {
    val open = true
    Box(Modifier.fillMaxSize()) {
        CircularProgressIndicator(Modifier.align(Alignment.Center))
        DialogWindow({}, title = "Importieren?", transparent = true, undecorated = true, content = @Composable {
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
                                Text("Importieren?")
                            }
                        }
                    }
                    Box(Modifier.weight(1f)) {
                        Text(
                            "Tatsächlich ${model.taggedFiles.size} Musiken importieren",
                            textAlign = TextAlign.Center,
                            modifier = Modifier.align(Alignment.Center)
                        )
                    }
                    Row(Modifier.padding(10.dp)) {
                        ElevatedButton({ model.previousStage() }) {
                            Text("Abbrechen")
                        }
                        OutlinedButton({ model.import() }) {
                            Text("Ok")
                        }
                    }
                }
            }
        })
    }
}

@Composable
fun FileSelect(importModel: ImportModel, canContinue: () -> Unit) {

    val focus = LocalFocusManager.current

    Row(
        Modifier.fillMaxSize(),
        horizontalArrangement = Arrangement.Center,
        verticalAlignment = Alignment.CenterVertically
    ) {
        OutlinedTextField(
            importModel.files.map { it.path }.joinToString(","),
            {},
            Modifier.padding(10.dp).clickable {
                focus.clearFocus()
                FilePicker(
                    onSelected = {
                        importModel.files = it
                        importModel.taggingReady = false
                        canContinue()
                    },
                    onAbort = {}
                )
            },
            enabled = false,
            trailingIcon = @Composable() {
                InteractiveIconButton(Icons.Outlined.FolderOpen, onClick = {
                    focus.clearFocus()
                    FilePicker(
                        onSelected = {
                            importModel.files = it
                            importModel.taggingReady = false
                            canContinue()
                        },
                        onAbort = {}
                    )
                }, MaterialTheme.colorScheme.primary)
            },
            singleLine = true
        )
    }
}

@Composable
fun FileTagging(importModel: ImportModel, canContinue: () -> Unit) {
    LaunchedEffect(null) {
        delay(500)
        canContinue()
    }
    Column(
        Modifier.fillMaxSize().verticalScroll(rememberScrollState()),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center,
    ) {
        if (importModel.taggingReady) {
            for (file in importModel.taggedFiles) {
                Surface(
                    tonalElevation = 3.dp,
                    modifier = Modifier.fillMaxWidth().padding(vertical = 2.dp),
                    shape = RoundedCornerShape(5.dp)
                ) {
                    Row {
                        Surface(
                            tonalElevation = 3.dp,
                            shadowElevation = 3.dp,
                            shape = RoundedCornerShape(5.dp),
                            modifier = Modifier.padding(5.dp).fillMaxWidth(.25f).aspectRatio(1f)
                        ) {
                            Box(Modifier.padding(5.dp), Alignment.Center) {
                                if (file.artwork != null) {
                                    Image(file.artwork, "Cover")
                                } else {
                                    Image(Icons.Outlined.Image, "Cover")
                                }
                            }
                        }
                        Column {
                            Text(
                                file.filetitle,
                                maxLines = 1,
                                modifier = Modifier.padding(start = 5.dp),
                                fontSize = 15.sp
                            )
                            Divider(
                                modifier = Modifier.padding(5.dp),
                                thickness = 1.dp,
                                color = MaterialTheme.colorScheme.inverseSurface
                            )
                            Row {
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
                            }
                            Row {
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
                            }
                            Row {
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
                            }
                        }
                    }
                }
            }
        } else {
            CircularProgressIndicator(strokeWidth = 2.dp)
            LaunchedEffect(null) {
                importModel.tag()
            }
        }
    }
}