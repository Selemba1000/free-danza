package me.selemba.common.persistence

import me.selemba.common.persistence.schema.*
import org.jetbrains.exposed.sql.Database
import org.jetbrains.exposed.sql.SchemaUtils
import org.jetbrains.exposed.sql.StdOutSqlLogger
import org.jetbrains.exposed.sql.addLogger
import org.jetbrains.exposed.sql.transactions.transaction

object Storage {

    val database by lazy {
        val tmp = Database.connect("jdbc:h2:mem:test;DB_CLOSE_DELAY=-1;", driver = "org.h2.Driver")
        transaction {
            addLogger(StdOutSqlLogger)
            SchemaUtils.create(SongFileTable,SongTable,UnitTable,ExerciseTable,UnitSongTable,ExerciseSongTable)
        }
        tmp
    }

    fun initialize():Database{
        return database
    }

}