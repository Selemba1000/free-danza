package me.selemba.common.ui.elements.interactive

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.border
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsFocusedAsState
import androidx.compose.foundation.interaction.collectIsHoveredAsState
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.LocalTextStyle
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.takeOrElse
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun ReactiveTextField(
    value: String,
    onValueChange: (String) -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    readOnly: Boolean = false,
    textStyle: TextStyle = LocalTextStyle.current,
    keyboardOptions: KeyboardOptions = KeyboardOptions.Default,
    keyboardActions: KeyboardActions = KeyboardActions.Default,
    singleLine: Boolean = false,
    maxLines: Int = if (singleLine) 1 else Int.MAX_VALUE,
    minLines: Int = 1,
    visualTransformation: VisualTransformation = VisualTransformation.None,
    interactionSource: MutableInteractionSource = remember { MutableInteractionSource() },
    cursorBrush: Brush = SolidColor(MaterialTheme.colorScheme.primary),
    isError: Boolean = false,
    errorInputColor: Color = MaterialTheme.colorScheme.error,
    focusInputColor: Color = MaterialTheme.colorScheme.onSurface,
    disabledInputColor: Color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.38f),
    defaultInputColor: Color = MaterialTheme.colorScheme.onSurface,
    defaultOutlineColor: Color = MaterialTheme.colorScheme.outline,
    focusOutlineColor: Color = MaterialTheme.colorScheme.primary,
    hoverOutlineColor: Color = MaterialTheme.colorScheme.onSurface,
    labelText: String = "",
) {

    val textColor = textStyle.color.takeOrElse {
        if (!enabled) disabledInputColor
        else if(isError) errorInputColor
        else if (interactionSource.collectIsFocusedAsState().value) focusInputColor
        else defaultInputColor
    }
    val mergedTextStyle = textStyle.merge(TextStyle(color = textColor))

    val outlineColor = @Composable {
        if(interactionSource.collectIsFocusedAsState().value) focusOutlineColor
        else if (interactionSource.collectIsHoveredAsState().value) hoverOutlineColor
        else defaultOutlineColor
    }

    BasicTextField(
        value,
        onValueChange,
        modifier.padding(2.dp),
        enabled,
        readOnly,
        mergedTextStyle,
        keyboardOptions,
        keyboardActions,
        singleLine,
        maxLines,
        minLines,
        decorationBox = @Composable { innerTextField ->
            Box {
                Box(
                    modifier = Modifier.border(BorderStroke(2.dp, outlineColor()), RoundedCornerShape(5.dp))
                        .padding(horizontal = 5.dp).padding(bottom = 3.dp, top = 1.dp)
                ) {

                    innerTextField()
                }
                Surface(modifier = Modifier.align(Alignment.TopStart).padding(start = 5.dp)) {
                    Text(
                        labelText,
                        style = MaterialTheme.typography.labelSmall.copy(fontSize = 7.sp),
                        modifier = Modifier.height(8.dp).wrapContentHeight(Alignment.Top),
                        lineHeight = 3.sp,
                    )
                }
            }
        },
        visualTransformation = visualTransformation,
        interactionSource = interactionSource,
        cursorBrush = cursorBrush
    )
}