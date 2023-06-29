package me.selemba.common.persistence

import org.jetbrains.exposed.sql.Database

object Storage {

    val database by lazy {
        Database.connect("jdbc:h2:mem:test;DB_CLOSE_DELAY=-1;", driver = "org.h2.Driver")
    }

    fun initialize():Database{
        return database
    }

}