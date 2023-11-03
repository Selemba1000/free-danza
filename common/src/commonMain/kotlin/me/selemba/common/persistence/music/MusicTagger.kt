package me.selemba.common.persistence.music

import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.toComposeImageBitmap
import org.jaudiotagger.audio.AudioFileIO
import org.jaudiotagger.tag.FieldKey
import org.jaudiotagger.tag.images.Artwork
import org.jaudiotagger.tag.reference.GenreTypes
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
        val title = try {
            t.getFirst(FieldKey.TITLE)
        }catch (e: Exception){
            ""
        }
        val album = try {
            t.getFirst(FieldKey.ALBUM)
        }catch (e: Exception){
            ""
        }
        val artist = try {
            t.getFirst(FieldKey.ARTIST)
        }catch (e: Exception){
            ""
        }
        val bpm = try{
            t.getFirst(FieldKey.BPM)
        }catch (e: Exception){
            ""
        }
        val date = try{
            t.getFirst(FieldKey.YEAR)
        }catch (e: Exception){
            ""
        }
        val genre = try {
            t.getFirst(FieldKey.GENRE)
        }catch (e: Exception){
            ""
        }
        return TaggedMusic(file,f.file.name,title, artist, album, bpm, date, genre, length, readArtwork(file))
    }

    fun readArtwork(file: File): ImageBitmap? {
        val a = AudioFileIO.read(file).tag.firstArtwork ?: return null
        return (a.image as BufferedImage).toComposeImageBitmap()
    }

}