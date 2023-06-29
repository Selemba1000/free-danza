package me.selemba.common.ui.elements.player

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.desktop.ui.tooling.preview.Preview
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.Orientation
import androidx.compose.foundation.gestures.draggable
import androidx.compose.foundation.gestures.rememberDraggableState
import androidx.compose.foundation.hoverable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsDraggedAsState
import androidx.compose.foundation.interaction.collectIsHoveredAsState
import androidx.compose.foundation.interaction.collectIsPressedAsState
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Pause
import androidx.compose.material.icons.rounded.PlayArrow
import androidx.compose.material.icons.rounded.SkipNext
import androidx.compose.material.icons.rounded.SkipPrevious
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.surfaceColorAtElevation
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.layout
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.async
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import me.selemba.common.audio.AudioFile

@Composable
fun PlayerControl(modifier: Modifier = Modifier.height(100.dp).fillMaxWidth()) {

    val scope = rememberCoroutineScope()

    val model = remember { PlayerModel() }
    val position = model.position
    val state = model.state

    Box(
        modifier,
    ) {
        Surface(
            modifier = Modifier.fillMaxSize()
        ) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Row {
                    val interactPlay = remember { MutableInteractionSource() }
                    val scalePlay by animateFloatAsState(
                        targetValue = if(interactPlay.collectIsPressedAsState().value).8f else 1f,
                        tween(100)
                    )

                    val interactPrev = remember { MutableInteractionSource() }
                    val scalePrev by animateFloatAsState(
                        targetValue = if(interactPrev.collectIsPressedAsState().value).8f else 1f,
                        tween(100)
                    )

                    val interactNext = remember { MutableInteractionSource() }
                    val scaleNext by animateFloatAsState(
                        targetValue = if(interactNext.collectIsPressedAsState().value).8f else 1f,
                        tween(100)
                    )

                    Box(
                        modifier = Modifier
                            .padding(horizontal = 10.dp)
                            .width(50.dp)
                            .height(50.dp)
                            .clickable(interactPrev, null, onClick = { println("play") })
                            .scale(scalePrev)
                    ) {
                        Icon(
                            Icons.Rounded.SkipPrevious,
                            "",
                            tint = MaterialTheme.colorScheme.primary,
                            modifier = Modifier.width(40.dp).height(40.dp).scale(1.2f).padding(0.dp)
                                .align(Alignment.Center)
                        )
                    }

                    Box(
                        modifier = Modifier
                            .padding(0.dp)
                            .width(50.dp)
                            .height(50.dp)
                            .clickable(interactPlay, null, onClick = { scope.launch { model.startPause() } })
                            .scale(scalePlay)
                    ) {
                        Icon(
                            if(model.state==PlayerModel.PlayerState.PLAYING||model.state==PlayerModel.PlayerState.SEEKING&&model.wasPlaying)Icons.Rounded.Pause else Icons.Rounded.PlayArrow,
                            "",
                            tint = MaterialTheme.colorScheme.primary,
                            modifier = Modifier.width(40.dp).height(40.dp).scale(1.6f).padding(0.dp)
                                .align(Alignment.Center)
                        )
                    }

                    Box(
                        modifier = Modifier
                            .padding(horizontal = 10.dp)
                            .width(50.dp)
                            .height(50.dp)
                            .clickable(interactNext, null, onClick = { println("play") })
                            .scale(scaleNext)
                    ) {
                        Icon(
                            Icons.Rounded.SkipNext,
                            "",
                            tint = MaterialTheme.colorScheme.primary,
                            modifier = Modifier.width(40.dp).height(40.dp).scale(1.2f).padding(0.dp)
                                .align(Alignment.Center)
                        )
                    }
                }

                PlayerSlider(
                    onChange = { scope.launch { model.seek(it) } },
                    onChangeEnd = { scope.launch { model.seekEnd() } },
                    onChangeStart = {
                                    scope.launch { model.seekStart() }
                    },
                    playing = state==PlayerModel.PlayerState.PLAYING,
                    value = position,
                )
            }
        }
    }
}

@Composable
fun PlayerSlider(
    modifier: Modifier = Modifier,
    value: Float,
    playing: Boolean = false,
    onChangeStart: () -> Unit,
    onChange: (Float) -> Unit,
    onChangeEnd: (Float) -> Unit,
    height: Dp = 5.dp,
    thumbSize: Dp = 20.dp,
    thumbColor: Color = MaterialTheme.colorScheme.primary,
    trackColor: Color = MaterialTheme.colorScheme.surfaceColorAtElevation(8.dp),
    trackActiveColor: Color = MaterialTheme.colorScheme.primary
) {
    val interact = remember { MutableInteractionSource() }

    Box(
        modifier = modifier.then(Modifier.fillMaxWidth().padding(horizontal = 10.dp).hoverable(interact))
    ) {
        Box(
            modifier = Modifier.height(height).fillMaxWidth()
                .background(color = trackColor, shape = CircleShape)
                .align(Alignment.Center)
        ) {}
        Box(
            modifier = Modifier.fillMaxWidth()
        ) {
            Box(
                modifier = Modifier.height(height).background(trackActiveColor, CircleShape)
                    .align(Alignment.CenterStart).fillMaxWidth(value.coerceIn(0f, 1f))
            )
            BoxWithConstraints(
                modifier = Modifier.fillMaxWidth()//.border(BorderStroke(1.dp,Color.Magenta))
            ) {
                val dragState = rememberDraggableState { onChange(it/constraints.maxWidth) } //value.value += it / constraints.maxWidth }
                val hovered = interact.collectIsDraggedAsState().value||interact.collectIsHoveredAsState().value

                val alpha by animateFloatAsState(
                    targetValue = if(hovered) 1f else 0f,
                    animationSpec = tween( 200)
                )

                Box(
                    modifier = Modifier
                        .alignXRelative(value.coerceIn(0f, 1f))
                        .height(15.dp)
                        .width(15.dp)
                        .background(
                            thumbColor.copy(alpha = alpha),
                            CircleShape
                        )
                        .draggable(
                            dragState,
                            Orientation.Horizontal,
                            interactionSource = interact,
                            onDragStopped = { onChangeEnd(value.coerceIn(0f, 1f)) },
                            onDragStarted = { onChangeStart() }
                            )
                )
            }
        }
    }
}

fun Modifier.alignXRelative(x: Float) = layout { measurable, constraints ->
    val placeable = measurable.measure(constraints)
    val maxWidth = constraints.maxWidth

    layout(placeable.width, placeable.height) {
        placeable.placeRelative(((maxWidth * x).toInt()) - (placeable.width / 2), 0)
    }
}

@Composable
@Preview
private fun prev() {
    PlayerControl()
}