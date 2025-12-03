package not.djinni.presentation.screens.seeker.vacancy.details

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import not.djinni.presentation.core.components.base.AlertContainer
import not.djinni.R
import not.djinni.presentation.core.Screen
import not.djinni.presentation.core.components.base.FullscreenColumn
import not.djinni.presentation.core.components.base.HorizontalSpacer
import not.djinni.presentation.core.components.base.NotDjinniButton
import not.djinni.presentation.core.components.base.NotDjinniLoader
import not.djinni.presentation.core.components.base.NotDjinniText
import not.djinni.presentation.core.components.base.VerticalSpacer
import not.djinni.presentation.core.components.base.model.ButtonData
import not.djinni.presentation.core.components.base.model.SnackBarData
import not.djinni.presentation.core.components.base.model.TextData
import not.djinni.presentation.core.extension.clickableNoRipple
import not.djinni.presentation.core.extension.collectAsEffect
import not.djinni.presentation.core.extension.toTextData
import not.djinni.presentation.screens.seeker.vacancy.details.alert.VacancyDetailsAlert
import not.djinni.presentation.screens.seeker.vacancy.details.components.ApplyVacancyBottomSheet
import not.djinni.presentation.theme.NotDjinniIcons
import not.djinni.presentation.theme.NotDjinniTheme
import org.koin.core.parameter.parametersOf

@Composable
internal fun VacancyDetailsScreen(
    vacancyId: Long,
    onNavigateBack: () -> Unit,
) {
    Screen<VacancyDetailsViewModel>(
        parameters = { parametersOf(vacancyId) }
    ) { viewModel ->
        val state by viewModel.state.collectAsStateWithLifecycle()

        Content(
            state = state,
            onAction = viewModel::sendAction
        )

        AlertContainer(state.currentAlert) { alert ->
            when (alert) {
                is VacancyDetailsAlert.Applying -> {
                    ApplyVacancyBottomSheet(
                        vacancyName = alert.vacancyTitle,
                        onDismiss = { viewModel.sendAction(VacancyDetailsAction.HideApplyBottomSheet) },
                        onApply = { coverLetter ->
                            viewModel.sendAction(VacancyDetailsAction.SubmitApplication(coverLetter))
                        }
                    )
                }
            }
        }

        viewModel.sideEffect.collectAsEffect { effect ->
            when (effect) {
                VacancyDetailsSideEffect.NavigateBack -> onNavigateBack()
                VacancyDetailsSideEffect.ApplicationSuccess -> {
                    viewModel.showSnackBar(
                        SnackBarData(message = R.string.apply_vacancy_success.toTextData())
                    )
                }
            }
        }
    }
}

@Composable
private fun Content(
    state: VacancyDetailsState,
    onAction: (VacancyDetailsAction) -> Unit = {},
) {
    FullscreenColumn {
        TopBar(onBack = { onAction(VacancyDetailsAction.NavigateBack) })
        when (val contentState = state.contentState) {
            is VacancyDetailsContentState.Loading -> LoadingContent()
            is VacancyDetailsContentState.Error -> ErrorContent(
                message = contentState.message,
                onRetry = { onAction(VacancyDetailsAction.Load) }
            )

            is VacancyDetailsContentState.Data -> VacancyContent(
                vacancy = contentState.vacancy,
                eligibility = contentState.eligibility,
                isApplied = state.isApplied,
                onApply = { onAction(VacancyDetailsAction.Apply) }
            )
        }
    }
}

@Composable
private fun TopBar(onBack: () -> Unit) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = NotDjinniTheme.offsets.small),
        verticalAlignment = Alignment.CenterVertically
    ) {
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
            data = R.string.vacancy_details_title.toTextData(),
            style = NotDjinniTheme.typography.title2.copy(fontWeight = FontWeight.Bold),
            color = NotDjinniTheme.colors.onBackground
        )
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
private fun ErrorContent(
    message: TextData,
    onRetry: () -> Unit,
) {
    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        NotDjinniText(
            data = message,
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
private fun VacancyContent(
    vacancy: VacancyDisplayData,
    eligibility: EligibilityState?,
    isApplied: Boolean,
    onApply: () -> Unit,
) {
    Column(modifier = Modifier.fillMaxSize()) {
        VacancyHeader(vacancy = vacancy)
        VerticalSpacer(NotDjinniTheme.offsets.medium)
        Column(
            modifier = Modifier
                .weight(1f)
                .verticalScroll(rememberScrollState())
        ) {
            eligibility?.let { EligibilitySection(eligibility = it) }
            VerticalSpacer(NotDjinniTheme.offsets.medium)
            DescriptionSection(description = vacancy.description)
            vacancy.companyDescription?.let {
                VerticalSpacer(NotDjinniTheme.offsets.medium)
                AboutCompanySection(description = it)
            }
        }
        VerticalSpacer(NotDjinniTheme.offsets.large)
        ApplySection(
            eligibility = eligibility,
            isApplied = isApplied,
            onApply = onApply
        )
        VerticalSpacer(NotDjinniTheme.offsets.medium)
    }
}

@Composable
private fun VacancyHeader(vacancy: VacancyDisplayData) {
    Column(modifier = Modifier.fillMaxWidth()) {
        NotDjinniText(
            data = vacancy.companyName,
            style = NotDjinniTheme.typography.body2,
            color = NotDjinniTheme.colors.onBackground.copy(alpha = SECONDARY_TEXT_ALPHA)
        )
        VerticalSpacer(NotDjinniTheme.offsets.tiny)
        NotDjinniText(
            data = vacancy.title,
            style = NotDjinniTheme.typography.title1Bold,
            color = NotDjinniTheme.colors.onBackground
        )
        VerticalSpacer(NotDjinniTheme.offsets.small)
        Row(verticalAlignment = Alignment.CenterVertically) {
            NotDjinniText(
                data = vacancy.salaryRange,
                style = NotDjinniTheme.typography.body1Bold,
                color = NotDjinniTheme.colors.onBackground
            )
            HorizontalSpacer(NotDjinniTheme.offsets.small)
            NotDjinniText(
                data = vacancy.employmentType,
                style = NotDjinniTheme.typography.body2,
                color = NotDjinniTheme.colors.onBackground.copy(alpha = SECONDARY_TEXT_ALPHA)
            )
        }
        VerticalSpacer(NotDjinniTheme.offsets.tiny)
        NotDjinniText(
            data = vacancy.requiredExperience,
            style = NotDjinniTheme.typography.body2,
            color = NotDjinniTheme.colors.onBackground.copy(alpha = SECONDARY_TEXT_ALPHA)
        )
    }
}

@Composable
private fun EligibilitySection(eligibility: EligibilityState) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .background(
                color = NotDjinniTheme.colors.highlightedContainer,
                shape = NotDjinniTheme.shapes.small
            )
            .padding(NotDjinniTheme.offsets.medium)
    ) {
        EligibilityItem(
            isMatch = eligibility.experienceMatch,
            matchText = R.string.eligibility_experience_match.toTextData(),
            mismatchText = R.string.eligibility_experience_mismatch.toTextData()
        )
        eligibility.salaryHint?.let {
            VerticalSpacer(NotDjinniTheme.offsets.small)
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(
                    modifier = Modifier.size(ELIGIBILITY_ICON_SIZE),
                    imageVector = NotDjinniIcons.check,
                    contentDescription = null,
                    tint = NotDjinniTheme.colors.primary
                )
                HorizontalSpacer(NotDjinniTheme.offsets.small)
                NotDjinniText(
                    data = it,
                    style = NotDjinniTheme.typography.body2,
                    color = NotDjinniTheme.colors.primary
                )
            }
        }
    }
}

@Composable
private fun EligibilityItem(
    isMatch: Boolean,
    matchText: not.djinni.presentation.core.components.base.model.TextData,
    mismatchText: not.djinni.presentation.core.components.base.model.TextData,
) {
    val icon = if (isMatch) NotDjinniIcons.check else NotDjinniIcons.close
    val color = if (isMatch) NotDjinniTheme.colors.onSurface else NotDjinniTheme.colors.error
    val text = if (isMatch) matchText else mismatchText

    Row(verticalAlignment = Alignment.CenterVertically) {
        Icon(
            modifier = Modifier.size(ELIGIBILITY_ICON_SIZE),
            imageVector = icon,
            contentDescription = null,
            tint = color
        )
        HorizontalSpacer(NotDjinniTheme.offsets.small)
        NotDjinniText(
            data = text,
            style = NotDjinniTheme.typography.body2,
            color = color
        )
    }
}

@Composable
private fun DescriptionSection(description: not.djinni.presentation.core.components.base.model.TextData) {
    Column(modifier = Modifier.fillMaxWidth()) {
        NotDjinniText(
            data = R.string.vacancy_details_description.toTextData(),
            style = NotDjinniTheme.typography.title2,
            color = NotDjinniTheme.colors.onBackground
        )
        VerticalSpacer(NotDjinniTheme.offsets.small)
        NotDjinniText(
            data = description,
            style = NotDjinniTheme.typography.body2.copy(fontWeight = FontWeight.Thin),
            color = NotDjinniTheme.colors.onBackground
        )
    }
}

@Composable
private fun AboutCompanySection(description: not.djinni.presentation.core.components.base.model.TextData) {
    Column(modifier = Modifier.fillMaxWidth()) {
        NotDjinniText(
            data = R.string.vacancy_details_about_company.toTextData(),
            style = NotDjinniTheme.typography.title2,
            color = NotDjinniTheme.colors.onBackground
        )
        VerticalSpacer(NotDjinniTheme.offsets.small)
        NotDjinniText(
            data = description,
            style = NotDjinniTheme.typography.body2.copy(fontWeight = FontWeight.Thin),
            color = NotDjinniTheme.colors.onBackground
        )
    }
}

@Composable
private fun ApplySection(
    eligibility: EligibilityState?,
    isApplied: Boolean,
    onApply: () -> Unit,
) {
    val canApply = eligibility?.canApply ?: true
    Column(
        modifier = Modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        when {
            isApplied -> {
                NotDjinniText(
                    data = R.string.vacancy_already_applied.toTextData(),
                    style = NotDjinniTheme.typography.body1,
                    color = NotDjinniTheme.colors.primary,
                    textAlign = TextAlign.Center
                )
            }

            canApply -> {
                NotDjinniButton(
                    modifier = Modifier.fillMaxWidth(),
                    data = ButtonData(text = R.string.vacancy_details_apply.toTextData()),
                    onClick = onApply
                )
            }

            else -> {
                NotDjinniText(
                    data = R.string.vacancy_cannot_apply.toTextData(),
                    style = NotDjinniTheme.typography.body1,
                    textAlign = TextAlign.Center,
                    color = NotDjinniTheme.colors.error
                )
            }
        }
    }
}

@Composable
@Preview
private fun Preview() {
    NotDjinniTheme {
        val state = VacancyDetailsState(
            contentState = VacancyDetailsContentState.Data(
                vacancy = VacancyDisplayData(
                    id = 1,
                    title = "Senior Android Developer".toTextData(),
                    companyName = "Tech Company".toTextData(),
                    companyDescription = "A leading technology company".toTextData(),
                    description = "We are looking for an experienced Android developer...".toTextData(),
                    salaryRange = "$5000 - $8000".toTextData(),
                    employmentType = "Full-time".toTextData(),
                    requiredExperience = "5+ years experience".toTextData(),
                    category = "Software Development".toTextData(),
                    postedDate = "Dec 1, 2025".toTextData()
                ),
                eligibility = EligibilityState(
                    canApply = true,
                    experienceMatch = true,
                    salaryMatch = false,
                    salaryHint = "Salary is below your expectations (\$10000)".toTextData()
                )
            )
        )
        Box(modifier = Modifier.background(NotDjinniTheme.colors.background)) {
            Content(state = state)
        }
    }
}

private val ICON_SIZE = 24.dp
private val ELIGIBILITY_ICON_SIZE = 20.dp
private const val SECONDARY_TEXT_ALPHA = 0.7f
