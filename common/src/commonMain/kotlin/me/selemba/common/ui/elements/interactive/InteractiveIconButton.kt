package me.selemba.common.ui.elements.interactive

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsPressedAsState
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

@Composable
fun InteractiveIconButton(
    icon: ImageVector, onClick: () -> Unit, tint: Color, size: Dp = 50.dp, modifier: Modifier = Modifier
) {

    val interact = remember { MutableInteractionSource() }
    val scale by animateFloatAsState(
        targetValue = if (interact.collectIsPressedAsState().value) .8f else 1f, tween(100)
    )
    Box(modifier = modifier.width(size).height(size).clickable(interact, indication = null, onClick = onClick)) {
        Icon(
            icon,
            "",
            tint = tint,
            modifier = Modifier.scale(scale).align(Alignment.Center).width(size).height(size)
        )
    }
}