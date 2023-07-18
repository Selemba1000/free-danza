package me.selemba.common.persistence

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