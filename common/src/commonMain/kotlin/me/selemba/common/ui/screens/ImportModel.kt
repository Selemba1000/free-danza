package me.selemba.common.ui.screens

import androidx.compose.runtime.*
import androidx.compose.ui.graphics.ImageBitmap
import kotlinx.coroutines.launch
import me.selemba.common.persistence.Storage
import me.selemba.common.persistence.music.MusicStorage
import me.selemba.common.persistence.music.MusicTagger
import me.selemba.common.persistence.music.TaggedMusic
import me.selemba.common.persistence.schema.SongFile
import org.jetbrains.skia.Bitmap
import java.io.File

class ImportModel(val stages: List<@Composable (ImportModel) -> Unit>,val finish:() -> Unit) {

    fun nextStage(){
        if(stages.indexOf(stage)==stages.lastIndex){
            finish()
            return
        }
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

    fun import(){
         Storage.scope.launch{
            for (file in taggedFiles) {
                var sf: SongFile? = null
                Storage.suspendTransaction {
                     sf = SongFile.new {
                        title = file.title
                        artist = file.artist
                        album = file.album
                        year = file.year
                        bpm = if (file.bpm.isEmpty()) 0 else file.bpm.toInt()
                        genre = file.genre
                        length = file.length
                        extension = file.file.extension
                    }
                }
                MusicStorage.import(MusicStorage.ImportFile(sf!!,file.file))
            }
            finish()
        }
    }

}