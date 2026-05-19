package not.djinni.presentation.screens.employer.profile.my

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Icon
import androidx.compose.foundation.text.input.rememberTextFieldState
import androidx.compose.foundation.text.input.setTextAndPlaceCursorAtEnd
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.DialogProperties
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import not.djinni.R
import not.djinni.model.User
import not.djinni.model.company.Company
import not.djinni.model.employer.EmployerProfile
import not.djinni.presentation.core.Screen
import not.djinni.presentation.core.components.base.FullscreenColumn
import not.djinni.presentation.core.components.base.HorizontalSpacer
import not.djinni.presentation.core.components.base.NotDjinniButton
import not.djinni.presentation.core.components.base.NotDjinniLoader
import not.djinni.presentation.core.components.base.NotDjinniText
import not.djinni.presentation.core.components.base.NotDjinniTextField
import not.djinni.presentation.core.components.base.VerticalSpacer
import not.djinni.presentation.core.components.base.buildDefaultTextFieldDecorator
import not.djinni.presentation.core.components.base.model.ButtonData
import not.djinni.presentation.core.components.base.model.TextData
import not.djinni.presentation.core.extension.clickableNoRipple
import not.djinni.presentation.core.extension.collectAsEffect
import not.djinni.presentation.core.extension.toTextData
import not.djinni.presentation.theme.NotDjinniIcons
import not.djinni.presentation.theme.NotDjinniTheme

@Composable
internal fun EmployerProfileScreen(
    onBack: () -> Unit,
    onLogout: () -> Unit,
) {
    Screen<EmployerProfileViewModel> { viewModel ->
        val state by viewModel.state.collectAsStateWithLifecycle()

        Content(
            state = state,
            onAction = viewModel::sendAction,
        )

        viewModel.sideEffect.collectAsEffect { effect ->
            when (effect) {
                EmployerProfileSideEffect.NavigateBack -> onBack()
                EmployerProfileSideEffect.NavigateToAuth -> onLogout()
            }
        }
    }
}

@Composable
private fun Content(
    state: EmployerProfileState,
    onAction: (EmployerProfileAction) -> Unit = {},
) {
    FullscreenColumn {
        ProfileHeader(
            onBack = { onAction(EmployerProfileAction.NavigateBack) },
            onLogout = { onAction(EmployerProfileAction.Logout) }
        )
        VerticalSpacer(NotDjinniTheme.offsets.medium)
        when {
            state.isLoading -> LoadingContent()
            state.hasError -> ErrorContent(onRetry = { onAction(EmployerProfileAction.Retry) })
            else -> ProfileContent(
                user = state.user ?: return@FullscreenColumn,
                profile = state.profile ?: return@FullscreenColumn,
                onChangeRole = { onAction(EmployerProfileAction.ShowRoleEditor) }
            )
        }
        if (state.isRoleDialogVisible) {
            RoleEditDialog(state = state, onAction = onAction)
        }
    }
}

@Composable
private fun ProfileHeader(
    onBack: () -> Unit,
    onLogout: () -> Unit,
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Icon(
                modifier = Modifier
                    .size(ICON_SIZE)
                    .clickableNoRipple(onClick = onBack),
                imageVector = NotDjinniIcons.back,
                contentDescription = null,
                tint = NotDjinniTheme.colors.onBackground
            )
            HorizontalSpacer(NotDjinniTheme.offsets.small)
            NotDjinniText(
                data = R.string.profile_title.toTextData(),
                style = NotDjinniTheme.typography.title1Bold,
                color = NotDjinniTheme.colors.onBackground,
            )
        }
        NotDjinniText(
            modifier = Modifier.clickableNoRipple(onClick = onLogout),
            data = R.string.profile_logout.toTextData(),
            style = NotDjinniTheme.typography.body2,
            color = NotDjinniTheme.colors.error,
        )
    }
}

@Composable
private fun RoleEditDialog(
    state: EmployerProfileState,
    onAction: (EmployerProfileAction) -> Unit,
) {
    val roleFieldState = rememberTextFieldState()
    LaunchedEffect(state.editingRole) {
        if (roleFieldState.text.toString() != state.editingRole) {
            roleFieldState.setTextAndPlaceCursorAtEnd(state.editingRole)
        }
    }
    LaunchedEffect(Unit) {
        snapshotFlow { roleFieldState.text.toString() }
            .collect { onAction(EmployerProfileAction.UpdateEditingRole(it)) }
    }

    AlertDialog(
        onDismissRequest = { onAction(EmployerProfileAction.DismissRoleEditor) },
        properties = DialogProperties(dismissOnClickOutside = true),
        containerColor = NotDjinniTheme.colors.surface,
        titleContentColor = NotDjinniTheme.colors.onSurface,
        textContentColor = NotDjinniTheme.colors.onSurface,
        title = {
            NotDjinniText(
                data = R.string.profile_change_role.toTextData(),
                style = NotDjinniTheme.typography.title2,
                color = NotDjinniTheme.colors.onSurface,
            )
        },
        text = {
            Column(modifier = Modifier.fillMaxWidth()) {
                NotDjinniText(
                    data = R.string.profile_role.toTextData(),
                    style = NotDjinniTheme.typography.body3,
                    color = NotDjinniTheme.colors.onSurface,
                )
                VerticalSpacer(NotDjinniTheme.offsets.tiny)
                NotDjinniTextField(
                    state = roleFieldState,
                    decorator = buildDefaultTextFieldDecorator(
                        state = roleFieldState,
                        placeholder = R.string.profile_role.toTextData(),
                        textStyle = NotDjinniTheme.typography.body1.copy(color = NotDjinniTheme.colors.onSurface),
                    ),
                )
            }
        },
        confirmButton = {
            NotDjinniButton(
                modifier = Modifier.fillMaxWidth(),
                data = ButtonData(
                    text = R.string.save.toTextData(),
                    isLoading = state.isSavingRole,
                ),
                onClick = { onAction(EmployerProfileAction.SaveRole) }
            )
        },
        dismissButton = null,
    )
}

@Composable
private fun LoadingContent() {
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        NotDjinniLoader()
    }
}

@Composable
private fun ErrorContent(onRetry: () -> Unit) {
    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        NotDjinniText(
            data = R.string.profile_loading_error.toTextData(),
            style = NotDjinniTheme.typography.body1,
            color = NotDjinniTheme.colors.error
        )
        VerticalSpacer(NotDjinniTheme.offsets.medium)
        NotDjinniButton(
            data = ButtonData(text = R.string.retry.toTextData()),
            onClick = onRetry
        )
    }
}

@Composable
private fun ProfileContent(
    user: User,
    profile: EmployerProfile,
    onChangeRole: () -> Unit,
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
    ) {
        SectionHeader(title = R.string.profile_user_section.toTextData())
        VerticalSpacer(NotDjinniTheme.offsets.small)
        ProfileCard {
            ProfileRow(
                label = R.string.profile_name.toTextData(),
                value = user.name.toTextData()
            )
            ProfileRow(
                label = R.string.profile_email.toTextData(),
                value = user.email.toTextData()
            )
        }
        VerticalSpacer(NotDjinniTheme.offsets.medium)
        SectionHeader(title = R.string.profile_details_section.toTextData())
        VerticalSpacer(NotDjinniTheme.offsets.small)
        ProfileCard {
            ProfileRow(
                label = R.string.profile_role.toTextData(),
                value = profile.role.toTextData()
            )
            ProfileRow(
                label = R.string.profile_company.toTextData(),
                value = profile.company.name.toTextData()
            )
        }
        VerticalSpacer(NotDjinniTheme.offsets.large)
        NotDjinniButton(
            modifier = Modifier.fillMaxWidth(),
            data = ButtonData(text = R.string.profile_change_role.toTextData()),
            onClick = onChangeRole
        )
        VerticalSpacer(NotDjinniTheme.offsets.medium)
    }
}

@Composable
private fun SectionHeader(title: TextData) {
    NotDjinniText(
        data = title,
        style = NotDjinniTheme.typography.title2,
        color = NotDjinniTheme.colors.onBackground,
    )
}

@Composable
private fun ProfileCard(content: @Composable () -> Unit) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .background(
                color = NotDjinniTheme.colors.highlightedContainer,
                shape = NotDjinniTheme.shapes.small
            )
            .padding(NotDjinniTheme.offsets.medium)
    ) {
        content()
    }
}

@Composable
private fun ProfileRow(
    label: TextData,
    value: TextData,
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = NotDjinniTheme.offsets.tiny),
    ) {
        NotDjinniText(
            data = label,
            style = NotDjinniTheme.typography.body2,
            color = NotDjinniTheme.colors.onSurface.copy(alpha = LABEL_ALPHA),
        )
        Spacer(modifier = Modifier.weight(1f))
        HorizontalSpacer(NotDjinniTheme.offsets.small)
        NotDjinniText(
            data = value,
            style = NotDjinniTheme.typography.body2,
            textAlign = TextAlign.End,
            color = NotDjinniTheme.colors.onSurface,
        )
    }
}

@Composable
@Preview
private fun Preview() {
    NotDjinniTheme {
        val state = EmployerProfileState(
            user = User(id = 1, name = "Jane Doe", email = "jane@company.com"),
            profile = EmployerProfile(
                id = 1,
                role = "HR Manager",
                company = Company(
                    id = 1,
                    name = "Tech Corp",
                    website = "https://techcorp.com",
                    description = "A leading technology company"
                )
            ),
            isLoading = false,
            hasError = false
        )
        Box(modifier = Modifier.background(NotDjinniTheme.colors.background)) {
            Content(state = state)
        }
    }
}

private val ICON_SIZE = 24.dp

private const val LABEL_ALPHA = 0.6f
