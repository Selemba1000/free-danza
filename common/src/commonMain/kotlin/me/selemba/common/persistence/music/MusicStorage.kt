package me.selemba.common.persistence.music

import me.selemba.common.audio.AudioFile
import me.selemba.common.persistence.*
import me.selemba.common.persistence.schema.SongFile
import java.io.File

object MusicStorage {

    class ImportFile(val db: SongFile,val file: File)

    fun import(vararg songs: ImportFile):List<FileException>{
        var errors = mutableListOf<FileException>()
        var fine = mutableListOf<ImportFile>()
        for (song in songs){
            if(!song.file.isFile){
                errors.add(FileIsDirectoryException(song.file.path))
                continue
            }
            if(!song.file.canRead()){
                errors.add(FileReadException(song.file.path))
                continue
            }
            val extension = song.file.extension
            if(!listOf("mp3","wav","ogg").contains(extension.lowercase())){
                errors.add(IllegalFileExtensionException(song.file.path, listOf("mp3","wav","ogg")))
                continue
            }
            fine.add(song)
        }
        for (song in fine){
            song.file.copyTo(File(StorageLocation.userDataLocation,"music/${song.db.id.value}.${song.file.extension}"))
            Storage.transaction {
                song.db.extension=song.file.extension
            }

        }
        return errors.toList()
    }

    fun export(location: String,vararg songs: SongFile){

    }

    fun get(song: SongFile): AudioFile {
        val filename = song.filename(song.id.value)
        val file = File(StorageLocation.userDataLocation,"music/$filename")
        if(!file.canRead()){
            throw FileReadException(file.path)
        }
        return AudioFile(file.path)
    }



}