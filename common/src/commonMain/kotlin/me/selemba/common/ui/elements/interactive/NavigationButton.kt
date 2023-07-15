package me.selemba.common.ui.elements.interactive

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.Crossfade
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.onClick
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp

@OptIn(ExperimentalFoundationApi::class, ExperimentalAnimationApi::class)
@Composable
fun NavigationButton(
    active: Boolean,
    modifier: Modifier = Modifier,
    contentColor: Color = MaterialTheme.colorScheme.onPrimary,
    onClick: () -> Unit,
    content: @Composable RowScope.() -> Unit
) {
    CompositionLocalProvider(LocalContentColor provides contentColor) {
        ProvideTextStyle(value = MaterialTheme.typography.labelLarge) {
            Surface(
                tonalElevation = 3.dp,
                color = if(active)MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.surface,
                shape = RoundedCornerShape(5.dp),
                modifier = modifier.padding(vertical = 5.dp, horizontal = 1.dp).border(
                    1.dp, MaterialTheme.colorScheme.primary,
                    RoundedCornerShape(5.dp)
                ).onClick { onClick() }
            ) {
                Row(
                    modifier = Modifier.defaultMinSize(ButtonDefaults.MinWidth, ButtonDefaults.MinHeight)
                        .padding(ButtonDefaults.ContentPadding),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.Center,
                    content = content
                )
            }
        }
    }
}