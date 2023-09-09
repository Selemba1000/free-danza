package me.selemba.common.persistence

abstract class FileException():Exception(){
    abstract val path:String
}

class InexistentFileException(override val path: String) : FileException(){
    override val message: String
        get() = "File does not exist. Requested path: $path."
}
class DirectoryIsFileException(override val path: String): FileException(){
    override val message: String
        get() = "Expected directory at: $path, but found file."
}
class IllegalFileExtensionException(override val path: String, val allowed: List<String>):FileException(){
    override val message: String
        get() = "File at: $path does not have a allowed Extension. Allowed Extensions: $allowed."
}
class FileIsDirectoryException(override val path: String) : FileException(){
    override val message: String
        get() = "Expected file at: $path, but found directory."
}
class FileReadException(override val path: String) : FileException(){
    override val message: String
        get() = "File at: $path is not readable. Check permissions."
}
class FileWriteException(override val path: String) : FileException(){
    override val message: String?
        get() = "File at: $path is not writable. Check permissions."
}