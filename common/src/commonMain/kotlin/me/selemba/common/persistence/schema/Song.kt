package me.selemba.common.persistence.schema

import org.jetbrains.exposed.dao.IntEntity
import org.jetbrains.exposed.dao.IntEntityClass
import org.jetbrains.exposed.dao.id.EntityID
import org.jetbrains.exposed.dao.id.IntIdTable

object SongTable : IntIdTable() {
    val file = reference("file", SongFileTable)
    val cutStart = integer("cut_start")
    val cutEnd = integer("cut_end")
    val fade = integer("fade")
}

class Song(id: EntityID<Int>) : IntEntity(id){
    companion object : IntEntityClass<Song>(SongTable)
    var file by SongFile referencedOn SongTable.file
    var cutStart by SongTable.cutStart
    var cutEnd by SongTable.cutEnd
    var fade by SongTable.fade
}