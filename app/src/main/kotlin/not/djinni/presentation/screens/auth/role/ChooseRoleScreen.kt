package not.djinni.presentation.screens.auth.role

import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.material3.Icon
import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.with
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.dp
import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.spring
import kotlinx.coroutines.launch
import not.djinni.model.role.Role
import not.djinni.presentation.core.Screen
import not.djinni.presentation.core.ScreenSizesPreview
import not.djinni.presentation.core.components.base.NotDjinniText
import not.djinni.presentation.core.extension.collectAsEffect
import not.djinni.presentation.core.extension.toTextData
import not.djinni.presentation.theme.NotDjinniIcons
import not.djinni.presentation.theme.NotDjinniTheme

@Composable
fun ChooseRoleScreen(
    onSeekerCreateProfile: () -> Unit,
    onSeekerMain: () -> Unit,
    onEmployerCreateProfile: () -> Unit,
    onEmployerMain: () -> Unit,
) {
    Screen<ChooseRoleViewModel> { viewModel ->
        Content(
            onAction = viewModel::sendAction,
        )

        viewModel.sideEffect.collectAsEffect { effect ->
            when (effect) {
                ChooseRoleSideEffect.NavigateSeekerCreateProfile -> onSeekerCreateProfile()
                ChooseRoleSideEffect.NavigateSeekerMain -> onSeekerMain()
                ChooseRoleSideEffect.NavigateEmployerCreateProfile -> onEmployerCreateProfile()
                ChooseRoleSideEffect.NavigateEmployerMain -> onEmployerMain()
            }
        }
    }
}

@Composable
private fun Content(
    onAction: (ChooseRoleAction) -> Unit,
) {
    var previewRole by remember { mutableStateOf<Role?>(null) }
    Column(
        modifier = Modifier
            .fillMaxSize()
            .systemBarsPadding()
            .padding(
                horizontal = NotDjinniTheme.offsets.medium,
                vertical = NotDjinniTheme.offsets.small
            ),
    ) {
        NotDjinniText(
            data = "What’s your role?".toTextData(),
            style = NotDjinniTheme.typography.title1Bold,
            color = NotDjinniTheme.colors.onBackground,
        )
        Spacer(modifier = Modifier.height(NotDjinniTheme.offsets.regular))
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            NotDjinniText(
                modifier = Modifier.weight(1f),
                data = "← Employer".toTextData(),
                style = NotDjinniTheme.typography.body2,
                color = NotDjinniTheme.colors.onBackground.copy(
                    alpha = if (previewRole == Role.EMPLOYER) 1f else 0.65f
                ),
            )
            NotDjinniText(
                data = "Seeker →".toTextData(),
                style = NotDjinniTheme.typography.body2,
                color = NotDjinniTheme.colors.onBackground.copy(
                    alpha = if (previewRole == Role.SEEKER) 1f else 0.65f
                ),
            )
        }
        Spacer(modifier = Modifier.weight(1f))
        SwipeRoleCard(
            onPreviewRoleChanged = { role -> previewRole = role },
            onConfirm = { role -> onAction(ChooseRoleAction.ConfirmRole(role)) },
        )
        Spacer(modifier = Modifier.weight(1f))
        Spacer(modifier = Modifier.height(NotDjinniTheme.offsets.small))
    }
}

@Composable
@OptIn(ExperimentalAnimationApi::class)
private fun SwipeRoleCard(
    onPreviewRoleChanged: (Role?) -> Unit,
    onConfirm: (Role) -> Unit,
) {
    val density = LocalDensity.current
    val scope = rememberCoroutineScope()
    var cardOffsetX by remember { mutableFloatStateOf(0f) }
    var cardRotation by remember { mutableFloatStateOf(0f) }
    var isCommitting by remember { mutableStateOf(false) }

    val employerAccent = Color(0xFFFF8A00)
    val seekerAccent = NotDjinniTheme.colors.success
    val idleContent = SwipeRoleCardContent(
        icon = NotDjinniIcons.case,
        title = "Your role?",
        subtitle = "Swipe to choose",
        accent = NotDjinniTheme.colors.onBackground,
    )
    BoxWithConstraints(
        modifier = Modifier.fillMaxWidth(),
    ) {
        val cardWidth = minOf(maxWidth, 320.dp)
        val cardHeight = maxHeight * 0.7f
        val cardWidthPx = with(density) { cardWidth.toPx() }
        val previewThresholdPx = cardWidthPx * 0.15f
        val commitThresholdPx = cardWidthPx * 0.35f
        val progress = (kotlin.math.abs(cardOffsetX) / commitThresholdPx).coerceIn(0f, 1f)
        val previewRole = when {
            kotlin.math.abs(cardOffsetX) < previewThresholdPx -> null
            cardOffsetX < 0f -> Role.EMPLOYER
            else -> Role.SEEKER
        }
        val cardContent = when (previewRole) {
            Role.EMPLOYER -> SwipeRoleCardContent(
                icon = NotDjinniIcons.case,
                title = "Employer",
                subtitle = "Post jobs & hire",
                accent = employerAccent,
            )
            Role.SEEKER -> SwipeRoleCardContent(
                icon = NotDjinniIcons.zoom,
                title = "Seeker",
                subtitle = "Find & apply",
                accent = seekerAccent,
            )
            null -> idleContent
        }
        LaunchedEffect(previewRole) {
            onPreviewRoleChanged(previewRole)
        }
        val glowAlpha = progress * 0.6f
        val glowSizeFactor = 1f + progress
        val glowAccent = when {
            cardOffsetX < 0f -> employerAccent
            cardOffsetX > 0f -> seekerAccent
            else -> Color.Transparent
        }
        val glowColor = glowAccent.copy(alpha = glowAlpha)

        val offsetAnim = remember { Animatable(0f) }
        LaunchedEffect(offsetAnim.value) {
            if (!isCommitting) {
                cardOffsetX = offsetAnim.value
                cardRotation = (cardOffsetX / cardWidthPx).coerceIn(-1f, 1f) * 10f
            }
        }

        Box(
            modifier = Modifier.fillMaxWidth(),
            contentAlignment = Alignment.Center,
        ) {
            Box(
                modifier = Modifier
                    .size(width = cardWidth, height = cardHeight)
                    .graphicsLayer {
                        translationX = cardOffsetX
                        rotationZ = cardRotation
                    }
                    .drawBehind {
                        val baseRadius = 28.dp.toPx()
                        val spreads = listOf(6.dp, 12.dp, 18.dp).map { spread ->
                            (spread.toPx() * glowSizeFactor)
                        }
                        val layers = listOf(0.45f, 0.28f, 0.18f)
                        spreads.zip(layers).forEach { (spread, layerAlpha) ->
                            drawRoundRect(
                                color = glowColor.copy(alpha = glowColor.alpha * layerAlpha),
                                topLeft = Offset(-spread, -spread),
                                size = Size(width = size.width + spread * 2, height = size.height + spread * 2),
                                cornerRadius = CornerRadius(baseRadius + spread, baseRadius + spread),
                            )
                        }
                    }
                    .background(
                        color = NotDjinniTheme.colors.highlightedContainer,
                        shape = NotDjinniTheme.shapes.great,
                    )
                    .pointerInput(isCommitting) {
                        detectDragGestures(
                            onDragStart = {},
                            onDragEnd = {
                                if (isCommitting) return@detectDragGestures
                                val chosenRole = when {
                                    kotlin.math.abs(cardOffsetX) < commitThresholdPx -> null
                                    cardOffsetX < 0f -> Role.EMPLOYER
                                    else -> Role.SEEKER
                                }
                                if (chosenRole == null) {
                                    scope.launch {
                                        offsetAnim.snapTo(cardOffsetX)
                                        offsetAnim.animateTo(
                                            targetValue = 0f,
                                            animationSpec = spring(dampingRatio = 0.75f)
                                        )
                                        cardOffsetX = 0f
                                        cardRotation = 0f
                                    }
                                    return@detectDragGestures
                                }
                                isCommitting = true
                                scope.launch {
                                    offsetAnim.snapTo(cardOffsetX)
                                    offsetAnim.animateTo(
                                        targetValue = if (chosenRole == Role.EMPLOYER) {
                                            -commitThresholdPx * 1.2f
                                        } else {
                                            commitThresholdPx * 1.2f
                                        },
                                        animationSpec = spring(dampingRatio = 0.9f)
                                    )
                                    onConfirm(chosenRole)
                                }
                            },
                            onDragCancel = {
                                if (isCommitting) return@detectDragGestures
                                scope.launch {
                                    offsetAnim.snapTo(cardOffsetX)
                                    offsetAnim.animateTo(
                                        targetValue = 0f,
                                        animationSpec = spring(dampingRatio = 0.75f)
                                    )
                                    cardOffsetX = 0f
                                    cardRotation = 0f
                                }
                            },
                        ) { change, dragAmount ->
                            if (isCommitting) return@detectDragGestures
                            change.consume()
                            cardOffsetX = (cardOffsetX + dragAmount.x)
                                .coerceIn(-commitThresholdPx * 1.2f, commitThresholdPx * 1.2f)
                            cardRotation = (cardOffsetX / cardWidthPx).coerceIn(-1f, 1f) * 10f
                        }
                    }
                    .padding(NotDjinniTheme.offsets.medium),
            ) {
                AnimatedContent(
                    targetState = cardContent,
                    transitionSpec = { fadeIn() with fadeOut() },
                    label = "RoleCardContentTransition",
                ) { animatedContent ->
                    Column(
                        modifier = Modifier.fillMaxSize(),
                        verticalArrangement = androidx.compose.foundation.layout.Arrangement.Center,
                        horizontalAlignment = Alignment.CenterHorizontally,
                    ) {
                        Icon(
                            modifier = Modifier.size(42.dp),
                            imageVector = animatedContent.icon,
                            contentDescription = null,
                            tint = animatedContent.accent,
                        )
                        Spacer(modifier = Modifier.height(NotDjinniTheme.offsets.small))
                        NotDjinniText(
                            data = animatedContent.title.toTextData(),
                            style = NotDjinniTheme.typography.titleBold,
                            color = NotDjinniTheme.colors.onBackground,
                        )
                        Spacer(modifier = Modifier.height(6.dp))
                        NotDjinniText(
                            data = animatedContent.subtitle.toTextData(),
                            style = NotDjinniTheme.typography.body2,
                            color = NotDjinniTheme.colors.onSurface,
                        )
                    }
                }
            }
        }
    }
}

private data class SwipeRoleCardContent(
    val icon: ImageVector,
    val title: String,
    val subtitle: String,
    val accent: Color,
)

@Composable
@ScreenSizesPreview
private fun Preview() {
    NotDjinniTheme {
        Content(
            onAction = {},
        )
    }
}
