package me.selemba.common

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import me.selemba.common.audio.Player
import me.selemba.common.ui.elements.lists.SongList
import me.selemba.common.ui.elements.player.PlayerControl

@Composable
fun App() {
    var text by remember { mutableStateOf("Hello, World!") }
    val platformName = getPlatformName()
    val p = Player(CoroutineScope(Dispatchers.IO))

    Button(onClick = {
        text = "Hello, ${platformName}"

    }) {
        Text(text)
    }
    Text(p.getMixers().map { "${it.key}:${it.value}" }.joinToString())
    PlayerControl(Modifier.padding(top = 100.dp))
    SongList()
}
