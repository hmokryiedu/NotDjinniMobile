package not.djinni.presentation.core.components.base

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.Dp
import not.djinni.presentation.core.extension.applyIf
import not.djinni.presentation.theme.NotDjinniTheme

@Composable
fun FullscreenColumn(
    modifier: Modifier = Modifier,
    contentPadding: PaddingValues = PaddingValues(
        horizontal = NotDjinniTheme.offsets.average,
        vertical = NotDjinniTheme.offsets.empty
    ),
    backgroundColor: Color = NotDjinniTheme.colors.background,
    applySystemBarsPadding: Boolean = true,
    backgroundApplied: Boolean = false,
    scrollable: Boolean = false,
    verticalArrangement: Arrangement.Vertical = Arrangement.Top,
    horizontalAlignment: Alignment.Horizontal = Alignment.CenterHorizontally,
    content: @Composable ColumnScope.() -> Unit
) {
    val scrollState = rememberScrollState()
    Column(
        modifier = modifier
            .fillMaxSize()
            .applyIf(backgroundApplied) {
                background(backgroundColor)
            }
            .applyIf(scrollable) {
                verticalScroll(scrollState)
            }
            .applyIf(applySystemBarsPadding) {
                systemBarsPadding()
            }
            .padding(contentPadding),
        verticalArrangement = verticalArrangement,
        horizontalAlignment = horizontalAlignment,
        content = content
    )
}

@Composable
fun buildFullscreenColumnPadding(
    start: Dp = NotDjinniTheme.offsets.average,
    end: Dp = NotDjinniTheme.offsets.average,
    top: Dp = NotDjinniTheme.offsets.empty,
    bottom: Dp = NotDjinniTheme.offsets.empty
): PaddingValues = PaddingValues(
    start = start,
    end = end,
    top = top,
    bottom = bottom
)

@Composable
fun buildFullscreenColumnPadding(
    horizontal: Dp = NotDjinniTheme.offsets.average,
    vertical: Dp = NotDjinniTheme.offsets.empty,
): PaddingValues = PaddingValues(
    horizontal = horizontal,
    vertical = vertical
)

@Composable
fun buildFullscreenColumnPadding(
    all: Dp = NotDjinniTheme.offsets.empty
): PaddingValues = PaddingValues(
    all = all
)
