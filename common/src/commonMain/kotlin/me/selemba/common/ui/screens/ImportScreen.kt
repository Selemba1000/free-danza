package me.selemba.common.ui.screens

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.FolderOpen
import androidx.compose.material.icons.outlined.Image
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.unit.dp
import me.selemba.common.ui.elements.interactive.FilePicker
import me.selemba.common.ui.elements.interactive.InteractiveIconButton

@Composable
fun ImportScreen(exit: () -> Unit) {

    val model = remember {
        ImportModel(
            listOf<@Composable (ImportModel) -> Unit>(
                @Composable { FileSelect(it) { it.canContinue = true } },
                @Composable { FileTagging(it) { it.canContinue = true } },
            )
        )
    }

    Surface(tonalElevation = 1.dp) {
        Column(Modifier.fillMaxSize().padding(20.dp), horizontalAlignment = Alignment.CenterHorizontally) {
            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.Center) {
                Text("Import", style = MaterialTheme.typography.headlineMedium)
            }
            Column(
                modifier = Modifier.weight(1f).fillMaxWidth(.8f),
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
    Column(
        Modifier.fillMaxSize(),
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
                            modifier = Modifier.padding(5.dp)
                        ) {
                            Box(Modifier.size(120.dp), Alignment.Center) {
                                if (file.artwork != null) {
                                    Image(file.artwork, "Cover")
                                } else {
                                    Image(Icons.Outlined.Image, "Cover")
                                }
                            }
                        }
                        Column{
                            Text(file.filetitle)
                            Divider(modifier = Modifier.padding(5.dp), thickness = 1.dp, color = MaterialTheme.colorScheme.inverseSurface)
                            //Box(Modifier.fillMaxWidth().height(1.dp).background(MaterialTheme.colorScheme.inverseSurface))
                            Text(file.artist)
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