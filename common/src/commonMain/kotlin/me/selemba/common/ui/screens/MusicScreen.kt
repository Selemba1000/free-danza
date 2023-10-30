package me.selemba.common.ui.screens

import androidx.compose.animation.*
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.FolderOpen
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.TransformOrigin
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.unit.dp
import me.selemba.common.ui.elements.interactive.FilePicker
import me.selemba.common.ui.elements.interactive.InteractiveIconButton
import me.selemba.common.ui.elements.lists.SongList
import me.selemba.common.ui.elements.navigation.MainNavigationModel
import java.io.File

@OptIn(ExperimentalAnimationApi::class)
@Composable
fun MusicScreen(model: MainNavigationModel) {
    var import by remember { mutableStateOf(false) }

    Box {
        SongList({ import = true }, {})
        AnimatedVisibility(
            import,
            enter = scaleIn(transformOrigin = TransformOrigin(1f, 0f)) + fadeIn(),
            exit = scaleOut(transformOrigin = TransformOrigin(1f, 0f)) + fadeOut()
        ) {
            ImportScreen { import = false }
        }
    }

}