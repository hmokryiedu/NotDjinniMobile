package not.djinni.presentation.core

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Alignment.Companion.BottomCenter
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import kotlinx.coroutines.delay
import org.koin.androidx.compose.koinViewModel
import org.koin.core.parameter.ParametersHolder
import not.djinni.presentation.core.components.base.NotDjinniLoader
import not.djinni.presentation.core.components.base.NotDjinniText
import not.djinni.presentation.core.components.base.model.SnackBarData
import not.djinni.presentation.core.extension.clickableNoRipple
import not.djinni.presentation.core.extension.toTextData
import not.djinni.presentation.theme.NotDjinniTheme

@Composable
inline fun <reified VM : BaseViewModel> Screen(
    content: @Composable (VM) -> Unit
) {
    val viewModel = koinViewModel<VM>()
    Content(
        viewModel = viewModel,
        content = { content(viewModel) }
    )
}

@Composable
inline fun <reified VM : BaseViewModel> Screen(
    noinline parameters: () -> ParametersHolder,
    content: @Composable (VM) -> Unit
) {
    val viewModel = koinViewModel<VM>(parameters = parameters)
    Content(
        viewModel = viewModel,
        content = { content(viewModel) }
    )
}

@Composable
inline fun Content(
    viewModel: BaseViewModel,
    content: @Composable () -> Unit
) {
    val snackBar by viewModel.snackBarData.collectAsStateWithLifecycle(null)
    val loading by viewModel.loading.collectAsStateWithLifecycle(false)

    Box(modifier = Modifier.fillMaxSize()) {
        content()

        AnimatedVisibility(
            visible = loading,
            enter = fadeIn(),
            exit = fadeOut()
        ) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(NotDjinniTheme.colors.forcedBlack.copy(alpha = 0.5f))
                    .clickableNoRipple { },
                contentAlignment = Alignment.Center,
            ) {
                NotDjinniLoader(modifier = Modifier.size(54.dp))
            }
        }

        SnackBar(
            modifier = Modifier.align(BottomCenter),
            snackBarData = snackBar
        )
    }
}

@Composable
fun SnackBar(
    modifier: Modifier = Modifier,
    snackBarData: SnackBarData?
) {
    var previousMessage = remember { "".toTextData() }
    var isVisible by remember { mutableStateOf(false) }
    val animationSpec by remember {
        derivedStateOf { snackBarData?.duration?.animationDuration ?: ANIMATION_DEFAULT_DURATION }
    }

    AnimatedVisibility(
        modifier = modifier
            .padding(bottom = NotDjinniTheme.offsets.giant)
            .navigationBarsPadding(),
        visible = isVisible,
        enter = slideInVertically(
            animationSpec = tween(durationMillis = animationSpec),
            initialOffsetY = { it * TARGET_OFFSET_Y_MULTIPLIER }
        ) + fadeIn(
            animationSpec = tween(durationMillis = animationSpec)
        ),
        exit = slideOutVertically(
            animationSpec = tween(durationMillis = animationSpec),
            targetOffsetY = { it * TARGET_OFFSET_Y_MULTIPLIER }
        ) + fadeOut(
            animationSpec = tween(durationMillis = animationSpec)
        )
    ) {
        Box(
            modifier = Modifier
                .padding(horizontal = NotDjinniTheme.offsets.average)
                .fillMaxWidth()
                .background(
                    shape = NotDjinniTheme.shapes.micro,
                    color = Color(0xFF484849)
                )
                .padding(
                    vertical = NotDjinniTheme.offsets.mid,
                    horizontal = NotDjinniTheme.offsets.medium,
                )
        ) {
            NotDjinniText(
                data = snackBarData?.message ?: previousMessage,
                style = NotDjinniTheme.typography.body3.copy(
                    fontWeight = FontWeight.Medium
                ),
                color = Color(0xFFFFFFFF)
            )
        }
    }

    LaunchedEffect(snackBarData) {
        if (snackBarData == null) return@LaunchedEffect
        val onScreenDuration = snackBarData.duration.onScreenDuration
        val totalAnimationDuration = snackBarData.duration.animationDuration * 2L
        previousMessage = snackBarData.message
        isVisible = true
        delay(onScreenDuration + totalAnimationDuration)
        isVisible = false
    }
}

private const val ANIMATION_DEFAULT_DURATION = 500
private const val TARGET_OFFSET_Y_MULTIPLIER = 3