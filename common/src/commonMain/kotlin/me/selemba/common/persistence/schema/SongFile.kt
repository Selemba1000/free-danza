package me.selemba.common.persistence.schema

import org.jetbrains.exposed.dao.IntEntity
import org.jetbrains.exposed.dao.IntEntityClass
import org.jetbrains.exposed.dao.id.EntityID
import org.jetbrains.exposed.dao.id.IntIdTable

object SongFileTable : IntIdTable() {
    val name = varchar("name",50)
    val length = integer("length")
}

class SongFile(id: EntityID<Int>) : IntEntity(id){
    companion object : IntEntityClass<SongFile>(SongFileTable)
    var name by SongFileTable.name
    var length by SongFileTable.length
    val unitUsages by Song referrersOn  SongTable.file
    val exerciseUsages by Exercise via ExerciseSongTable
}