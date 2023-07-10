package me.selemba.common.ui.elements.lists

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import me.selemba.common.persistence.Storage
import me.selemba.common.persistence.schema.SongFile
import me.selemba.common.persistence.schema.SongFileTable
import org.jetbrains.exposed.sql.StdOutSqlLogger
import org.jetbrains.exposed.sql.addLogger
import org.jetbrains.exposed.sql.transactions.transaction

class SongListModel {

    val db = Storage.database

    var songs by mutableStateOf(emptyList<SongFile>())

    private var _search: String? by mutableStateOf<String?>(null)
    var search: String?
        get() = _search
        set(str: String?) {
            _search = str
            load()
        }

    private var _descending = false
    var descending: Boolean
        get() = _descending
        set(b: Boolean) {
            _descending = b
            load()
        }

    private var _column: Column = Column.NAME
    var column: Column
        get() = _column
        set(value) {
            _column = value
            load()
        }

    fun load() {
        transaction {
            songs = if (search != null) {
                if (column == Column.NAME) {
                    if (descending)
                        SongFile.find { SongFileTable.name  like "%$search%" }.sortedByDescending { it.name }
                    else
                        SongFile.find { SongFileTable.name like "%$search%" }.sortedBy { it.name }
                } else {
                    if (descending)
                        SongFile.find { SongFileTable.name like "%$search%" }.sortedByDescending { it.length }
                    else
                        SongFile.find { SongFileTable.name like "%$search%" }.sortedBy { it.length }
                }
            }else {
                if (column == Column.NAME) {
                    if (descending)
                        SongFile.all().sortedByDescending { it.name }
                    else
                        SongFile.all().sortedBy { it.name }
                } else {
                    if (descending)
                        SongFile.all().sortedByDescending { it.length }
                    else
                        SongFile.all().sortedBy { it.length }
                }
            }
        }
    }

    enum class Column {
        NAME,
        LENGTH
    }

}