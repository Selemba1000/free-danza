package me.selemba.common.ui.screens

import androidx.compose.runtime.*
import androidx.compose.ui.graphics.ImageBitmap
import me.selemba.common.persistence.music.MusicTagger
import me.selemba.common.persistence.music.TaggedMusic
import org.jetbrains.skia.Bitmap
import java.io.File

class ImportModel(val stages: List<@Composable (ImportModel) -> Unit>) {

    fun nextStage(){
        stage=stages[stages.indexOf(stage)+1]
    }

    fun previousStage(){
        stage=stages[stages.indexOf(stage)-1]
    }

    var stage by mutableStateOf(stages[0])

    var canContinue by mutableStateOf(false)

    var files by mutableStateOf(emptyList<File>())

    var taggedFiles by mutableStateOf(emptyList<TaggedMusic>())

    var taggingReady by mutableStateOf(false)

    fun tag(){
        taggedFiles=files.map { MusicTagger.readTags(it) }
        taggingReady=true
    }

}