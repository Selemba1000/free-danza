package me.selemba.common.persistence.schema

import org.jetbrains.exposed.dao.IntEntity
import org.jetbrains.exposed.dao.IntEntityClass
import org.jetbrains.exposed.dao.id.EntityID
import org.jetbrains.exposed.dao.id.IntIdTable
import org.jetbrains.exposed.sql.Table

object ExerciseTable : IntIdTable(){
    val name = varchar("name",50)
    val description = text("description", eagerLoading = true)
}

object ExerciseSongTable : Table(){
    val exercise = reference("exercise", ExerciseTable)
    val song = reference("song", SongFileTable)
    override val primaryKey = PrimaryKey(exercise, song)
}

class Exercise(id: EntityID<Int>) : IntEntity(id){
    companion object : IntEntityClass<Exercise>(ExerciseTable)
    var name by ExerciseTable.name
    var description by ExerciseTable.description
    var songs by SongFile via ExerciseSongTable
}