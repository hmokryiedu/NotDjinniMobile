package not.djinni.presentation.core.components.base

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.EnterTransition
import androidx.compose.animation.ExitTransition
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import not.djinni.presentation.core.components.base.model.TextData
import not.djinni.presentation.theme.NotDjinniTheme

@Composable
fun MessageCard(
    modifier: Modifier = Modifier,
    message: TextData?,
    enter: EnterTransition = fadeIn(),
    exit: ExitTransition = fadeOut(),
    backgroundColor: Color = NotDjinniTheme.colors.primary
) {
    var lastSavedMessage by remember { mutableStateOf<TextData>(TextData.Empty) }

    LaunchedEffect(message) {
        lastSavedMessage = message ?: return@LaunchedEffect
    }

    AnimatedVisibility(
        modifier = modifier,
        visible = message != null,
        enter = enter,
        exit = exit
    ) {
        NotDjinniText(
            modifier = Modifier
                .background(
                    color = backgroundColor,
                    shape = NotDjinniTheme.shapes.small
                )
                .padding(horizontal = NotDjinniTheme.offsets.little)
                .padding(vertical = NotDjinniTheme.offsets.small),
            data = lastSavedMessage,
            textAlign = TextAlign.Center,
            color = NotDjinniTheme.colors.onPrimary,
            style = NotDjinniTheme.typography.body2.copy(
                fontWeight = FontWeight.Medium
            )
        )
    }
}