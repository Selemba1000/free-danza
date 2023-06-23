package me.selemba.common.audio

import java.io.File

class AudioFile(path: String, val fadeOut: Int, val cutStart: Int, val cutEnd: Int) {
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

    class InexistentFileException(val path: String) : Exception(){
        override val message: String
            get() = "File does not exist. Requested path: $path."
    }
    class FileIsDirectoryException(val path: String) : Exception(){
        override val message: String
            get() = "Expected file at: $path, but found directory."
    }
    class FileReadException(val path: String) : Exception(){
        override val message: String
            get() = "File at: $path is not readable. Check permissions."
    }
    class FileWriteException(val path: String) : Exception(){
        override val message: String?
            get() = "File at: $path is not writable. Check permissions."
    }
}