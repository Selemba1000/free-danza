package me.selemba.common.persistence

import java.io.File

expect object StorageLocationImpl {
    val userDataLocation: String
}

object StorageLocation {
    val userDataLocation: File
        get() {
            val tmp = File(StorageLocationImpl.userDataLocation)
            if(!tmp.isDirectory)throw DirectoryIsFileException(tmp.path)
            if (!tmp.canRead())throw FileReadException(tmp.path)
            if(!tmp.canWrite())throw FileWriteException(tmp.path)
            val dir = File(tmp,"free-danza")
            return dir
        }
}