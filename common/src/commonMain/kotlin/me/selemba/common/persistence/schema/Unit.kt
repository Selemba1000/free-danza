package me.selemba.common.persistence.schema

import org.jetbrains.exposed.dao.IntEntity
import org.jetbrains.exposed.dao.id.EntityID
import org.jetbrains.exposed.dao.id.IntIdTable
import org.jetbrains.exposed.sql.Table

object UnitTable : IntIdTable(){
    val name = varchar("name",50)
    val description = text("description", eagerLoading = true)
    val order = text("order", eagerLoading = true)
}

object UnitSongTable : Table(){
    var unit = reference("unit",UnitTable)
    var song = reference("song",SongTable)
    override val primaryKey = PrimaryKey(unit, song)
}

class Unit(id: EntityID<Int>) : IntEntity(id){
    var name by UnitTable.name
    var description by UnitTable.description
    var songs by Song via UnitSongTable
}