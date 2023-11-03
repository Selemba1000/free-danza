package me.selemba.common.persistence.music

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.graphics.ImageBitmap
import me.selemba.common.persistence.schema.SongFile
import java.io.File

class TaggedMusic(
    val file: File,
    filetitle: String,
    title: String,
    artist: String,
    album: String,
    bpm: String,
    year: String,
    genre: String,
    val length: Long,
    val artwork: ImageBitmap?,
) {

    companion object {
        fun fromSongFile(sf: SongFile): TaggedMusic {
            return TaggedMusic(MusicStorage.get(sf).file, sf.filename(sf.id.value), sf.title,sf.artist,sf.album,sf.bpm.toString(),sf.year,sf.genre,sf.length,MusicTagger.readArtwork(MusicStorage.get(sf).file))
        }
    }

    var filetitle by mutableStateOf(filetitle)
    var title by mutableStateOf(title)
    var artist by mutableStateOf(artist)
    var album by mutableStateOf(album)
    var bpm by mutableStateOf(bpm)
    var year by mutableStateOf(year)
    var genre by mutableStateOf(genre)
}