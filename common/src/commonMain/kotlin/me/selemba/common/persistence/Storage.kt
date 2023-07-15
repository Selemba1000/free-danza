package me.selemba.common.persistence

import kotlinx.coroutines.*
import kotlinx.coroutines.runBlocking
import me.selemba.common.persistence.schema.*
import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.transactions.experimental.newSuspendedTransaction
import org.jetbrains.exposed.sql.transactions.transaction

object Storage {

    val scope = CoroutineScope(Dispatchers.IO)

    val database by lazy {
        Database.connect("jdbc:h2:mem:test;DB_CLOSE_DELAY=-1;", driver = "org.h2.Driver")
    }

    private var init = false

    fun <T> transaction(statement: Transaction.() -> T) {
        scope.launch {
            //delay(1000L)
            transaction(db = database){
                initialize()
                statement()
            }
        }
    }

    suspend fun <T> suspendTransaction(statement: Transaction.() -> T){
        scope.launch {
            //delay(1000L)
            transaction(db = database){
                initialize()
                statement()
            }
        }.join()
    }

    fun Transaction.initialize () {
        if (init) return
        addLogger(StdOutSqlLogger)
        SchemaUtils.create(SongFileTable, SongTable, UnitTable, ExerciseTable, UnitSongTable, ExerciseSongTable)
        init = true
    }

}