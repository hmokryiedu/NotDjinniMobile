package not.djinni.presentation.screens.seeker.profile.view

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import not.djinni.R
import not.djinni.model.User
import not.djinni.model.seeker.SeekerProfile
import not.djinni.model.seeker.vacancy.JobCategoryCode
import not.djinni.presentation.core.Screen
import not.djinni.presentation.core.components.base.FullscreenColumn
import not.djinni.presentation.core.components.base.HorizontalSpacer
import not.djinni.presentation.core.components.base.NotDjinniButton
import not.djinni.presentation.core.components.base.NotDjinniLoader
import not.djinni.presentation.core.components.base.NotDjinniText
import not.djinni.presentation.core.components.base.VerticalSpacer
import not.djinni.presentation.core.components.base.model.ButtonData
import not.djinni.presentation.core.components.base.model.TextData
import not.djinni.presentation.core.extension.collectAsEffect
import not.djinni.presentation.core.extension.toDisplayName
import not.djinni.presentation.core.extension.toTextData
import not.djinni.presentation.theme.NotDjinniTheme

@Composable
internal fun SeekerProfileScreen(
    onChangeRole: () -> Unit,
) {
    Screen<SeekerProfileViewModel> { viewModel ->
        val state by viewModel.state.collectAsStateWithLifecycle()

        Content(
            state = state,
            onAction = viewModel::sendAction,
        )

        viewModel.sideEffect.collectAsEffect { effect ->
            when (effect) {
                SeekerProfileSideEffect.NavigateToChooseRole -> onChangeRole()
            }
        }
    }
}

@Composable
private fun Content(
    state: SeekerProfileState,
    onAction: (SeekerProfileAction) -> Unit = {},
) {
    FullscreenColumn {
        NotDjinniText(
            modifier = Modifier.align(Alignment.Start),
            data = R.string.profile_title.toTextData(),
            style = NotDjinniTheme.typography.title1Bold,
            color = NotDjinniTheme.colors.onBackground,
        )
        VerticalSpacer(NotDjinniTheme.offsets.medium)
        when {
            state.isLoading -> LoadingContent()
            state.hasError -> ErrorContent(onRetry = { onAction(SeekerProfileAction.Retry) })
            else -> ProfileContent(
                user = state.user ?: return@FullscreenColumn,
                profile = state.profile ?: return@FullscreenColumn,
                onChangeRole = { onAction(SeekerProfileAction.ChangeRole) }
            )
        }
    }
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
    profile: SeekerProfile,
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
                label = R.string.profile_speciality.toTextData(),
                value = profile.speciality.toTextData()
            )
            ProfileRow(
                label = R.string.profile_desired_salary.toTextData(),
                value = "$${profile.desiredSalary}".toTextData()
            )
            ProfileRow(
                label = R.string.profile_experience_years.toTextData(),
                value = "${profile.experienceYears} years".toTextData()
            )
            NotDjinniText(
                data = R.string.profile_job_category.toTextData(),
                style = NotDjinniTheme.typography.body2,
                color = NotDjinniTheme.colors.onSurface.copy(alpha = LABEL_ALPHA),
            )
            VerticalSpacer(NotDjinniTheme.offsets.tiny)
            NotDjinniText(
                data = profile.jobCategory.toDisplayName(),
                textAlign = TextAlign.End,
                style = NotDjinniTheme.typography.body2,
                color = NotDjinniTheme.colors.onSurface,
            )
            VerticalSpacer(NotDjinniTheme.offsets.small)
            NotDjinniText(
                data = R.string.profile_about_me.toTextData(),
                style = NotDjinniTheme.typography.body2,
                color = NotDjinniTheme.colors.onSurface.copy(alpha = LABEL_ALPHA),
            )
            VerticalSpacer(NotDjinniTheme.offsets.tiny)
            NotDjinniText(
                data = profile.aboutMe?.toTextData() ?: R.string.profile_no_about_me.toTextData(),
                textAlign = TextAlign.Start,
                style = NotDjinniTheme.typography.body2,
                color = NotDjinniTheme.colors.onSurface,
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
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        NotDjinniText(
            data = label,
            style = NotDjinniTheme.typography.body2,
            color = NotDjinniTheme.colors.onSurface.copy(alpha = LABEL_ALPHA),
        )
        HorizontalSpacer(NotDjinniTheme.offsets.small)
        NotDjinniText(
            data = value,
            textAlign = TextAlign.End,
            style = NotDjinniTheme.typography.body2,
            color = NotDjinniTheme.colors.onSurface,
        )
    }
}

@Composable
@Preview
private fun Preview() {
    NotDjinniTheme {
        val state = SeekerProfileState(
            user = User(id = 1, name = "John Doe", email = "john@example.com"),
            profile = SeekerProfile(
                id = 1,
                aboutMe = "Experienced Android developer",
                speciality = "Android Developer",
                desiredSalary = 5000,
                experienceYears = 5,
                jobCategory = JobCategoryCode.SOFTWARE_DEV
            ),
            isLoading = false,
            hasError = false
        )
        Box(modifier = Modifier.background(NotDjinniTheme.colors.background)) {
            Content(state = state)
        }
    }
}

private const val LABEL_ALPHA = 0.6f
