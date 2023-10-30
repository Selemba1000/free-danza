package me.selemba.common.persistence.music

import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.toComposeImageBitmap
import org.jaudiotagger.audio.AudioFileIO
import org.jaudiotagger.tag.FieldKey
import org.jaudiotagger.tag.images.Artwork
import org.jetbrains.skia.Bitmap
import java.awt.Image
import java.awt.image.BufferedImage
import java.io.File

object MusicTagger {

    fun readTags(file: File):TaggedMusic{
        val f = AudioFileIO.read(file)
        val h = f.audioHeader
        val t = f.tag
        val length : Long = (h.preciseTrackLength*1000).toLong()
        val title = t.getFirst(FieldKey.TITLE)
        val album = t.getFirst(FieldKey.ALBUM)
        val artist = t.getFirst(FieldKey.ARTIST)
        //val bpm = t.getFirst(FieldKey.BPM).toInt()
        //val date = t.getFirst(FieldKey.ORIGINALRELEASEDATE)
        val genre = t.getFirst(FieldKey.GENRE)
        return TaggedMusic(f.file.name,title, artist, album, 100, "", genre, length, readArtwork(file))
    }

    fun readArtwork(file: File): ImageBitmap? {
        val a = AudioFileIO.read(file).tag.firstArtwork ?: return null
        return (a.image as BufferedImage).toComposeImageBitmap()
    }

}