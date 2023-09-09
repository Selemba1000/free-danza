package me.selemba.common.ui.elements.lists

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import me.selemba.common.persistence.Storage
import me.selemba.common.persistence.schema.SongFile
import me.selemba.common.persistence.schema.SongFileTable
import org.jetbrains.exposed.sql.StdOutSqlLogger
import org.jetbrains.exposed.sql.addLogger

class SongListModel {

    val db = Storage.database

    var songs by mutableStateOf(emptyList<SongFile>())

    var loading by mutableStateOf(true)

    private var _search: String? by mutableStateOf<String?>(null)
    var search: String?
        get() = _search
        set(str: String?) {
            _search = str
            load()
        }

    fun load() {
        Storage.transaction {
            addLogger(StdOutSqlLogger)
            songs = if (search != null) {
                SongFile.find { SongFileTable.name like "%$search%" }.toList()
            }else {
                SongFile.all().toList()
            }
            loading=false
        }
    }
}