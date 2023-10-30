package me.selemba.common

import androidx.compose.animation.*
import androidx.compose.foundation.background
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.Window
import me.selemba.common.ui.elements.navigation.MainNavigation
import me.selemba.common.ui.elements.navigation.MainNavigationModel
import me.selemba.common.ui.elements.navigation.NavigationTarget
import me.selemba.common.ui.elements.player.PlayerControl
import me.selemba.common.ui.screens.MusicScreen

@Composable
fun App() {
    val model = remember {
        MainNavigationModel(
            listOf(
                NavigationTarget("Start", Icons.Outlined.Home) @Composable { Text("lala") },
                NavigationTarget("Musik", Icons.Outlined.MusicNote) { m -> MusicScreen(m) },
                NavigationTarget("Übungen", Icons.Outlined.Task) @Composable {

                },
                NavigationTarget(
                    "Einheiten",
                    Icons.Outlined.Description
                ) @Composable { model -> Button(onClick = { model.lock() }) { Text("lock") } },
            )
        )
    }
    var scheme: Boolean? by remember { mutableStateOf(null) }
    MaterialTheme(
        colorScheme =
        if (scheme ?: isSystemInDarkTheme())
            darkColorScheme()
        else
            lightColorScheme()
    ) {
        Column {
            MainNavigation(model)
            Surface(Modifier.weight(1f).fillMaxWidth()) {
                AnimatedContent(
                    model.content,
                    transitionSpec = {
                        if(model.screens.indexOf(initialState)<model.screens.indexOf(targetState)){
                            (slideInHorizontally { width -> width } + fadeIn()).togetherWith(slideOutHorizontally { width -> -width } + fadeOut())
                        }else{
                            (slideInHorizontally { width -> -width } + fadeIn()).togetherWith(slideOutHorizontally { width -> width } + fadeOut())
                        }
                    }
                ) { targetState ->
                    targetState.content(model)
                }

            }
            Column(Modifier.fillMaxWidth().background(Color.Red).animateContentSize()) {
                PlayerControl()
                /*if (content == pages[2]) {
                    Box(Modifier.fillMaxWidth().background(Color.Red).size(50.dp)) {}
                }*/
            }
        }
    }
}
