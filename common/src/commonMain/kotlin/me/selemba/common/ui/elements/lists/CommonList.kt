package me.selemba.common.ui.elements.lists

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.unit.dp

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun SortedList(
    items: List<SortedListRow> = emptyList(),
    headers: List<SortedListHeader>,
    modifier: Modifier = Modifier.padding(horizontal = 10.dp)
) {
    var sortColumn by remember { mutableStateOf(0) }
    var descending by remember { mutableStateOf(false) }

    LazyColumn(modifier) {
        stickyHeader {
            Surface(tonalElevation = 20.dp, color = MaterialTheme.colorScheme.primaryContainer) {
                Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.padding(5.dp)) {
                    val man = LocalFocusManager.current
                    for (i in headers.indices) {
                        if(headers[i].sortable) {
                            Row(modifier = Modifier.weight(headers[i].weight ?: (1f / headers.size)).onClick {
                                man.clearFocus(); if (sortColumn != i) sortColumn = i else descending = !descending
                            }, verticalAlignment = Alignment.CenterVertically) {
                                headers[i].head()
                                if (sortColumn == i) {
                                    Icon(
                                        if (descending) Icons.Rounded.ExpandLess else Icons.Rounded.ExpandMore,
                                        ""
                                    )
                                }
                            }
                        }else{
                            Row(Modifier.weight(headers[i].weight ?: (1f / headers.size)),verticalAlignment = Alignment.CenterVertically) {
                                headers[i].head()
                            }
                        }
                    }
                }
            }
            Divider(thickness = 2.dp)
        }
        items(if(descending)items.sortedByDescending { it.items[sortColumn].sortKey as Comparable<Any> } else items.sortedBy { it.items[sortColumn].sortKey as Comparable<Any> },{it.key}) { item ->
            Row(Modifier.padding(5.dp)) {
                for (i in headers.indices
                ) {
                    Box(modifier = Modifier.weight(headers[i].weight ?: (1f / headers.size))){
                        item.items[i].content()
                    }
                    //Text(item.items[i], modifier = Modifier.weight(1f / columns))
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

class SortedListHeader(val head: @Composable ()->Unit,val sortable: Boolean,val weight: Float? = null)
class SortedListRow(var key: Int, vararg val items: SortedListCell)
data class SortedListCell(var sortKey: Any,val content: @Composable ()->Unit)