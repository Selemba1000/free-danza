package me.selemba.common.ui.elements.lists

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.onClick
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.ExpandLess
import androidx.compose.material.icons.rounded.ExpandMore
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.unit.dp

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun SortedList(
    columns: Int,
    items: List<SortedListRow> = emptyList(),
    headers: SortedListRow,
    modifier: Modifier = Modifier.padding(10.dp)
) {
    var sortColumn by remember { mutableStateOf(0) }
    var descending by remember { mutableStateOf(false) }

    LazyColumn(modifier) {
        stickyHeader {
            Surface(tonalElevation = 20.dp, color = MaterialTheme.colorScheme.primaryContainer) {
                Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.padding(5.dp)) {
                    val man = LocalFocusManager.current
                    for (i in 0 until columns) {
                        Row(modifier = Modifier.weight(1f / columns).onClick { man.clearFocus(); if(sortColumn!=i)sortColumn=i else descending=!descending }, verticalAlignment = Alignment.CenterVertically) {
                            Text(headers.items[i])
                            if (sortColumn == i) {
                                Icon(
                                    if (descending) Icons.Rounded.ExpandLess else Icons.Rounded.ExpandMore,
                                    ""
                                )
                            }
                        }
                    }
                }
            }
            Divider(thickness = 2.dp)
        }
        items(if(descending)items.sortedByDescending { it.items[sortColumn] } else items.sortedBy { it.items[sortColumn] },{it.key}) { item ->
            Row(Modifier.padding(5.dp)) {
                for (i in 0 until columns) {
                    Text(item.items[i], modifier = Modifier.weight(1f / columns))
                }
            }
        }
        if (items.isEmpty()) {
            item(-1) {
                Row(modifier = Modifier.padding(5.dp).fillMaxWidth(), horizontalArrangement = Arrangement.Center) {
                    Text("Keine Elemente gefunden.", modifier = Modifier)
                }
            }
        }
    }
}

class SortedListRow(var key: Int, vararg val items: String)