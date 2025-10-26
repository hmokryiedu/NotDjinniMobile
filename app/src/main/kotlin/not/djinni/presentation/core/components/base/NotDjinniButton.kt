package not.djinni.presentation.core.components.base

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.sharp.AccountBox
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonColors
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import not.djinni.presentation.core.components.base.model.ButtonData
import not.djinni.presentation.core.components.base.model.ImageData
import not.djinni.presentation.core.extension.performWithTimeout
import not.djinni.presentation.core.extension.toImageData
import not.djinni.presentation.core.extension.toTextData
import not.djinni.presentation.theme.NotDjinniTheme

// TODO: Adapt to real design system

@Composable
fun NotDjinniButton(
    data: ButtonData,
    modifier: Modifier = Modifier,
    maxLines: Int = 1,
    colors: ButtonColors = buildButtonColors(),
    contentPadding: PaddingValues = PaddingValues(
        horizontal = NotDjinniTheme.offsets.regular,
        vertical = NotDjinniTheme.offsets.small,
    ),
    shape: Shape = NotDjinniTheme.shapes.small,
    textStyle: TextStyle = NotDjinniTheme.typography.title.copy(fontWeight = FontWeight.SemiBold),
    onClick: () -> Unit,
) {
    Button(
        modifier = modifier,
        colors = colors,
        enabled = data.enabled,
        shape = shape,
        contentPadding = contentPadding,
        onClick = { performWithTimeout(action = onClick) },
    ) {
        ButtonContent(
            data = data,
            maxLines = maxLines,
            textStyle = textStyle
        )
    }
}

@Composable
fun OutlinedNotDjinniButton(
    modifier: Modifier = Modifier,
    data: ButtonData,
    maxLines: Int = 1,
    color: ButtonColors = ButtonDefaults.outlinedButtonColors(
        contentColor = NotDjinniTheme.colors.onBackground,
        disabledContentColor = NotDjinniTheme.colors.onBackground.copy(alpha = 0.5f),
    ),
    shape: Shape = NotDjinniTheme.shapes.regular,
    contentPadding: PaddingValues = PaddingValues(
        horizontal = NotDjinniTheme.offsets.regular,
        vertical = NotDjinniTheme.offsets.small,
    ),
    textStyle: TextStyle = NotDjinniTheme.typography.title.copy(fontWeight = FontWeight.SemiBold),
    onClick: () -> Unit,
) {
    OutlinedButton(
        modifier = modifier,
        enabled = data.enabled,
        onClick = { performWithTimeout(action = onClick) },
        colors = color,
        shape = shape,
        contentPadding = contentPadding,
        border = BorderStroke(
            width = 1.dp,
            color = NotDjinniTheme.colors.primary
        ),
    ) {
        ButtonContent(
            data = data,
            maxLines = maxLines,
            textStyle = textStyle,
        )
    }
}

@Composable
private fun ButtonContent(
    data: ButtonData,
    maxLines: Int,
    textStyle: TextStyle = NotDjinniTheme.typography.title.copy(fontWeight = FontWeight.SemiBold),
) {
    AnimatedContent(
        targetState = data,
        transitionSpec = { fadeIn() togetherWith fadeOut() }
    ) { data ->
        Row(
            modifier = Modifier,
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center
        ) {
            if (data.isLoading) {
                NotDjinniLoader(
                    modifier = Modifier.size(26.dp)
                )
                return@Row
            }
            data.startImage?.let {
                ButtonImage(image = it)
                HorizontalSpacer(NotDjinniTheme.offsets.compact)
            }
            NotDjinniText(
                data = data.text,
                maxLines = maxLines,
                style = textStyle
            )
            data.endImage?.let {
                HorizontalSpacer(NotDjinniTheme.offsets.compact)
                ButtonImage(image = it)
            }
        }
    }
}

@Composable
private fun ButtonImage(
    modifier: Modifier = Modifier,
    image: ImageData,
) {
    if (image is ImageData.Vector) {
        Icon(
            imageVector = image.vector,
            contentDescription = null,
            modifier = modifier.size(24.dp),
            tint = Color.Unspecified
        )
    } else {
        NotDjinniImage(
            imageData = image,
            contentDescription = null,
            modifier = modifier.size(24.dp)
        )
    }
}

@Composable
fun buildButtonColors(
    contentColor: Color = Color.White,
    containerColor: Color = NotDjinniTheme.colors.primary,
    disabledContentColor: Color = NotDjinniTheme.colors.onPrimary.copy(alpha = 0.5f),
    disabledContainerColor: Color = NotDjinniTheme.colors.primary.copy(alpha = 0.5f)
): ButtonColors = ButtonDefaults.buttonColors(
    contentColor = contentColor,
    containerColor = containerColor,
    disabledContentColor = disabledContentColor,
    disabledContainerColor = disabledContainerColor
)

@Preview(
    backgroundColor = 0xFFFFFFFF,
    showBackground = true
)
@Composable
private fun OutlinedNotDjinniButtonPreview() {
    NotDjinniTheme {
        OutlinedNotDjinniButton(
            data = ButtonData(
                enabled = true,
                endImage = Icons.Sharp.AccountBox.toImageData(),
                text = "Hello".toTextData()
            ),
            onClick = {}
        )
    }
}

@Preview(
    backgroundColor = 0xFFFFFFFF,
    showBackground = true
)
@Composable
private fun NotDjinniButtonPreview() {
    NotDjinniTheme {
        NotDjinniButton(
            data = ButtonData(
                enabled = true,
                isLoading = true,
                startImage = Icons.Sharp.AccountBox.toImageData(),
                endImage = Icons.Sharp.AccountBox.toImageData(),
                text = "Hello".toTextData()
            ),
            onClick = {},
        )
    }
}
