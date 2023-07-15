package me.selemba.common.ui.elements.lists

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Archive
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.runBlocking
import me.selemba.common.persistence.Storage
import me.selemba.common.persistence.schema.SongFile
import me.selemba.common.ui.elements.interactive.InteractiveIconButton
import org.jetbrains.exposed.sql.StdOutSqlLogger
import org.jetbrains.exposed.sql.addLogger
import org.jetbrains.exposed.sql.transactions.transaction
import kotlin.time.Duration.Companion.milliseconds

@OptIn(ExperimentalFoundationApi::class, ExperimentalMaterial3Api::class)
@Composable
fun SongList(import: ()->Unit,export: ()->Unit) {
    val model = remember { SongListModel() }
    var songs = model.songs

    LaunchedEffect(null) {
        Storage.suspendTransaction {
            addLogger(StdOutSqlLogger)
            SongFile.new {
                name = "test"
                length = 103550
            }
            SongFile.new {
                name = "abc"
                length = 235663
            }
        }
        model.load()
    }
    Column {
        var text by remember { mutableStateOf("") }
        Row(horizontalArrangement = Arrangement.Center, verticalAlignment = Alignment.CenterVertically) {
            Row(modifier = Modifier.weight(.25f), horizontalArrangement = Arrangement.End) {
            }
            OutlinedTextField(
                model.search ?: "",
                { value -> if (value.isEmpty()) model.search = null else model.search = value },
                maxLines = 1,
                singleLine = true,
                enabled = true,
                modifier = Modifier.height(60.dp).weight(.5f),
                label = { Text("Suche") })
            Row(
                modifier = Modifier.weight(.25f).height(55.dp).padding(end = 20.dp).padding(vertical = 5.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.End
            ) {
                InteractiveIconButton(
                    Icons.Outlined.Archive,
                    import,
                    MaterialTheme.colorScheme.primary
                )
            }
        }
        if (model.loading) {
            Column(Modifier.fillMaxSize().padding(20.dp),horizontalAlignment = Alignment.CenterHorizontally) {
                CircularProgressIndicator(strokeWidth = 5.dp)
            }
        } else {
            SortedList(
                2,
                model.songs.map {
                    SortedListRow(
                        it.id.value,
                        it.name,
                        it.length.milliseconds.toComponents { hours, minutes, seconds, nanoseconds -> if (hours > 0) "$hours:$minutes:$seconds" else "$minutes:$seconds" })
                },
                SortedListRow(0, "Name", "Länge")
            )
        }
    }
}