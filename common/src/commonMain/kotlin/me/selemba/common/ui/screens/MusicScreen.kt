package me.selemba.common.ui.screens

import androidx.compose.animation.*
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.TransformOrigin
import androidx.compose.ui.window.Dialog
import me.selemba.common.ui.elements.lists.SongList
import me.selemba.common.ui.elements.navigation.MainNavigationModel

@OptIn(ExperimentalAnimationApi::class)
@Composable
fun MusicScreen(model: MainNavigationModel) {
    var import by remember { mutableStateOf(false) }

    Box {
        SongList({ import = true}, {})
        AnimatedVisibility(
            import,
            enter = scaleIn(transformOrigin = TransformOrigin(1f, 0f)) + fadeIn(),
            exit = scaleOut(transformOrigin = TransformOrigin(1f, 0f)) + fadeOut()
        ) {
            ImportScreen { import = false}
        }
    }

}

@Composable
fun ImportScreen(exit: () -> Unit) {
    Box(Modifier.fillMaxSize().background(Color.Red)) {
        Button(onClick = exit) { Text("exit") }
    }
}