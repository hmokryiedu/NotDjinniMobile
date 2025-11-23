package not.djinni.presentation.screens.auth.role

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import not.djinni.model.role.Role
import not.djinni.presentation.core.Screen
import not.djinni.presentation.core.ScreenSizesPreview
import not.djinni.presentation.core.components.base.HorizontalSpacer
import not.djinni.presentation.core.components.base.NotDjinniButton
import not.djinni.presentation.core.components.base.NotDjinniText
import not.djinni.presentation.core.components.base.model.ButtonData
import not.djinni.presentation.core.components.base.model.TextData
import not.djinni.presentation.core.extension.clickableNoRippleWithoutInterval
import not.djinni.presentation.core.extension.collectAsEffect
import not.djinni.presentation.core.extension.toTextData
import not.djinni.presentation.theme.NotDjinniIcons
import not.djinni.presentation.theme.NotDjinniTheme

@Composable
fun ChooseRoleScreen(
    onMain: (Role) -> Unit,
) {
    Screen<ChooseRoleViewModel> { viewModel ->
        val state by viewModel.state.collectAsStateWithLifecycle()

        Content(
            state = state,
            onAction = viewModel::sendAction,
        )

        viewModel.sideEffect.collectAsEffect { effect ->
            when (effect) {
                is ChooseRoleSideEffect.NavigateMain -> onMain(effect.role)
            }
        }
    }
}

@Composable
private fun Content(
    state: ChooseRoleState,
    onAction: (ChooseRoleAction) -> Unit,
) {
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
            data = "Choose your role".toTextData(),
            style = NotDjinniTheme.typography.title1Bold,
            color = NotDjinniTheme.colors.onBackground,
        )
        Spacer(modifier = Modifier.weight(0.1f))
        Role.entries.forEach { role ->
            RoleItem(
                modifier = Modifier.padding(bottom = NotDjinniTheme.offsets.small),
                role = when (role) {
                    Role.SEEKER -> RoleUI.SEEKER
                    Role.EMPLOYER -> RoleUI.EMPLOYER
                },
                isSelected = state.selectedRole == role,
                onClick = { onAction(ChooseRoleAction.SelectRole(role)) },
            )
        }
        Spacer(modifier = Modifier.weight(1f))
        NotDjinniButton(
            modifier = Modifier.fillMaxWidth(),
            data = ButtonData(
                text = "Continue".toTextData(),
                enabled = state.selectedRole != null,
            ),
            onClick = { onAction(ChooseRoleAction.ProceedToMain) },
        )
    }
}

@Composable
private fun RoleItem(
    modifier: Modifier = Modifier,
    role: RoleUI,
    isSelected: Boolean,
    onClick: () -> Unit,
) {
    Row(
        modifier = modifier
            .background(
                color = NotDjinniTheme.colors.surfaceContainer,
                shape = NotDjinniTheme.shapes.medium,
            )
            .border(
                color = NotDjinniTheme.colors.onBackground.copy(
                    alpha = if (isSelected) 1f else 0.1f
                ),
                width = if (isSelected) 2.dp else 1.dp,
                shape = NotDjinniTheme.shapes.medium,
            )
            .clickableNoRippleWithoutInterval(onClick = onClick)
            .padding(
                vertical = NotDjinniTheme.offsets.regular,
                horizontal = NotDjinniTheme.offsets.small
            )
            .fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            modifier = Modifier.size(32.dp),
            imageVector = role.icon,
            contentDescription = null,
            tint = if (isSelected) {
                NotDjinniTheme.colors.primary
            } else {
                NotDjinniTheme.colors.onSurface
            },
        )
        HorizontalSpacer(NotDjinniTheme.offsets.small)
        NotDjinniText(
            data = role.title,
            style = NotDjinniTheme.typography.body1Bold,
            color = if (isSelected) {
                NotDjinniTheme.colors.primary
            } else {
                NotDjinniTheme.colors.onSurface
            }
        )
    }
}

enum class RoleUI(
    val title: TextData,
    val icon: ImageVector,
) {
    EMPLOYER(
        title = "Employer".toTextData(),
        icon = NotDjinniIcons.case,
    ),
    SEEKER(
        title = "Seeker".toTextData(),
        icon = NotDjinniIcons.zoom,
    ),
}

@Composable
@ScreenSizesPreview
private fun Preview() {
    NotDjinniTheme {
        Content(
            state = ChooseRoleState(),
            onAction = {},
        )
    }
}
