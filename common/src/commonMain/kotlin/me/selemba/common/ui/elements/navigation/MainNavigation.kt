package me.selemba.common.ui.elements.navigation

import androidx.compose.foundation.layout.*
import androidx.compose.material3.Icon
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp
import me.selemba.common.ui.elements.interactive.NavigationButton

@Composable
fun MainNavigation(model: MainNavigationModel){
    Surface(modifier = Modifier.height(50.dp).fillMaxWidth(), tonalElevation = 3.dp, shadowElevation = 5.dp){
        Row(horizontalArrangement = Arrangement.Center) {
            for (target in model.screens){
                var active by remember{ mutableStateOf(false) }
                NavigationButton(
                    onClick = {
                        model.navigateTo(target)
                    },
                    modifier = Modifier,
                    active = model.content==target
                ){
                    Icon(target.icon,"",Modifier.padding(end = 5.dp))
                    Text(target.name)
                }
            }
        }
    }
}

class MainNavigationModel(val screens: List<NavigationTarget>){
    private var _lock by mutableStateOf(false)
    var content by mutableStateOf(screens[0])
    private var navPending : NavigationTarget? by mutableStateOf(null)

    fun navigateTo(target: NavigationTarget){
        navPending = target
        if(!_lock){
            content = navPending!!
            navPending = null
        }
    }

    fun lock(lock:Boolean=!_lock) {
        _lock = lock
        if(!_lock&&navPending!=null){
            navigateTo(navPending!!)
        }
    }

}
data class NavigationTarget(var name:String, var icon:ImageVector, var content: @Composable (model: MainNavigationModel) -> Unit)