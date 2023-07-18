package me.selemba.common.audio

import me.selemba.common.persistence.FileIsDirectoryException
import me.selemba.common.persistence.FileReadException
import me.selemba.common.persistence.FileWriteException
import me.selemba.common.persistence.InexistentFileException
import java.io.File

class AudioFile(path: String, val fadeOut: Int = 0, val cutStart: Int = 0, val cutEnd: Int = 0) {
    val file: File
    init {
        this.file=File(path)
        if(!file.exists()) throw InexistentFileException(path)
        if(file.isDirectory) throw FileIsDirectoryException(path)
        if(!file.canRead()) throw FileReadException(path)
    }

    fun requestWrite(){
        if(!file.canWrite()) throw FileWriteException(file.path)
        return
    }
}