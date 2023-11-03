package me.selemba.common.persistence.schema

import org.jetbrains.exposed.dao.IntEntity
import org.jetbrains.exposed.dao.IntEntityClass
import org.jetbrains.exposed.dao.id.EntityID
import org.jetbrains.exposed.dao.id.IntIdTable

object SongFileTable : IntIdTable() {
    val title = varchar("title",50)
    val artist = varchar("artist", 50)
    val album = varchar("album", 100)
    val bpm = integer("bpm")
    val year = varchar("year", 10)
    val genre = varchar("genre", 50)

    val length = long("length")
    val extension = varchar("extension",4)
}

class SongFile(id: EntityID<Int>) : IntEntity(id){
    companion object : IntEntityClass<SongFile>(SongFileTable)
    var title by SongFileTable.title
    var artist by SongFileTable.artist
    var album by SongFileTable.album
    var bpm by SongFileTable.bpm
    var year by SongFileTable.year
    var genre by SongFileTable.genre
    var length by SongFileTable.length
    var extension by SongFileTable.extension
    fun filename(id: Int)="$id.$extension"
    val unitUsages by Song referrersOn  SongTable.file
    val exerciseUsages by Exercise via ExerciseSongTable
}