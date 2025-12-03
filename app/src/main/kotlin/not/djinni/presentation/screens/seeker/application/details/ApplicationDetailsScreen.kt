@file:OptIn(ExperimentalTime::class)

package not.djinni.presentation.screens.seeker.application.details

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import not.djinni.R
import not.djinni.core.extension.toFormattedFullDate
import not.djinni.presentation.core.Screen
import not.djinni.presentation.core.components.base.FullscreenColumn
import not.djinni.presentation.core.components.base.HorizontalSpacer
import not.djinni.presentation.core.components.base.NotDjinniText
import not.djinni.presentation.core.components.base.VerticalSpacer
import not.djinni.presentation.core.extension.clickableNoRipple
import not.djinni.presentation.core.extension.collectAsEffect
import not.djinni.presentation.core.extension.toTextData
import not.djinni.presentation.screens.seeker.application.list.mapper.toDisplayStringRes
import not.djinni.presentation.screens.seeker.application.list.mapper.toThemeColor
import not.djinni.presentation.theme.NotDjinniIcons
import not.djinni.presentation.theme.NotDjinniTheme
import org.koin.core.parameter.parametersOf
import kotlin.time.ExperimentalTime

@Composable
internal fun ApplicationDetailsScreen(
    applicationId: Long,
    onNavigateBack: () -> Unit,
) {
    Screen<ApplicationDetailsViewModel>(
        parameters = { parametersOf(applicationId) }
    ) { viewModel ->
        val state by viewModel.state.collectAsStateWithLifecycle()

        Content(
            state = state,
            onAction = viewModel::sendAction
        )

        viewModel.sideEffect.collectAsEffect { effect ->
            when (effect) {
                ApplicationDetailsSideEffect.NavigateBack -> onNavigateBack()
            }
        }
    }
}

@Composable
private fun Content(
    state: ApplicationDetailsState,
    onAction: (ApplicationDetailsAction) -> Unit,
) {
    FullscreenColumn(modifier = Modifier.fillMaxSize()) {
        TopBar(onBack = { onAction(ApplicationDetailsAction.NavigateBack) })
        when (state.contentState) {
            is ContentState.Loading -> {}
            is ContentState.Error -> {}
            is ContentState.Data -> ContentState(
                modifier = Modifier.fillMaxSize(),
                state = state.contentState
            )
        }
    }
}

@Composable
private fun ContentState(
    modifier: Modifier = Modifier,
    state: ContentState.Data
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .imePadding()
            .verticalScroll(rememberScrollState())
    ) {
        VerticalSpacer(NotDjinniTheme.offsets.medium)
        Row(verticalAlignment = Alignment.CenterVertically) {
            NotDjinniText(
                data = stringResource(R.string.application_details_status).toTextData(),
                style = NotDjinniTheme.typography.body1Bold,
                color = NotDjinniTheme.colors.onBackground,
            )
            HorizontalSpacer(NotDjinniTheme.offsets.small)
            Box(
                modifier = Modifier
                    .size(STATUS_INDICATOR_SIZE)
                    .clip(CircleShape)
                    .background(state.application.status.toThemeColor(NotDjinniTheme.colors))
            )
            HorizontalSpacer(NotDjinniTheme.offsets.tiny)
            NotDjinniText(
                data = state.application.status.toDisplayStringRes().toTextData(),
                style = NotDjinniTheme.typography.body1,
                color = state.application.status.toThemeColor(NotDjinniTheme.colors),
            )
        }
        VerticalSpacer(NotDjinniTheme.offsets.large)

        NotDjinniText(
            data = stringResource(R.string.application_details_vacancy_summary).toTextData(),
            style = NotDjinniTheme.typography.title2,
            color = NotDjinniTheme.colors.onBackground,
        )
        VerticalSpacer(NotDjinniTheme.offsets.small)
        NotDjinniText(
            data = state.application.vacancy.title.toTextData(),
            style = NotDjinniTheme.typography.body1Bold,
            color = NotDjinniTheme.colors.onBackground,
        )
        VerticalSpacer(NotDjinniTheme.offsets.tiny)
        NotDjinniText(
            data = state.application.vacancy.company.name.toTextData(),
            style = NotDjinniTheme.typography.body2,
            color = NotDjinniTheme.colors.onBackground.copy(alpha = SECONDARY_TEXT_ALPHA),
        )
        VerticalSpacer(NotDjinniTheme.offsets.tiny)
        NotDjinniText(
            data = stringResource(
                R.string.application_seeker_salary,
                state.application.vacancy.salaryMin,
                state.application.vacancy.salaryMax
            ).toTextData(),
            style = NotDjinniTheme.typography.body2,
            color = NotDjinniTheme.colors.onBackground.copy(alpha = SECONDARY_TEXT_ALPHA),
        )
        VerticalSpacer(NotDjinniTheme.offsets.large)

        NotDjinniText(
            data = stringResource(R.string.application_details_cover_letter).toTextData(),
            style = NotDjinniTheme.typography.title2,
            color = NotDjinniTheme.colors.onBackground,
        )
        VerticalSpacer(NotDjinniTheme.offsets.small)
        NotDjinniText(
            data = (state.application.coverLetter
                ?: stringResource(R.string.application_details_no_cover_letter)).toTextData(),
            style = NotDjinniTheme.typography.body2,
            color = NotDjinniTheme.colors.onBackground.copy(alpha = COVER_LETTER_ALPHA),
        )
        VerticalSpacer(NotDjinniTheme.offsets.large)

        NotDjinniText(
            data = stringResource(R.string.application_details_timeline).toTextData(),
            style = NotDjinniTheme.typography.title2,
            color = NotDjinniTheme.colors.onBackground,
        )
        VerticalSpacer(NotDjinniTheme.offsets.small)
        NotDjinniText(
            data = "${stringResource(R.string.application_details_applied_at)} ${
                state.application.createdAt.toFormattedFullDate()
            }".toTextData(),
            style = NotDjinniTheme.typography.body2,
            color = NotDjinniTheme.colors.onBackground.copy(alpha = SECONDARY_TEXT_ALPHA),
        )
        VerticalSpacer(NotDjinniTheme.offsets.tiny)
        NotDjinniText(
            data = "${stringResource(R.string.application_details_updated_at)} ${
                state.application.updatedAt.toFormattedFullDate()
            }".toTextData(),
            style = NotDjinniTheme.typography.body2,
            color = NotDjinniTheme.colors.onBackground.copy(alpha = SECONDARY_TEXT_ALPHA),
        )
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
            data = R.string.application_details_title.toTextData(),
            style = NotDjinniTheme.typography.title2,
            color = NotDjinniTheme.colors.onBackground
        )
    }
}

@Composable
@Preview
private fun Preview() {
    NotDjinniTheme {
        Content(
            state = ApplicationDetailsState(),
            onAction = {}
        )
    }
}

private val ICON_SIZE = 24.dp
private val STATUS_INDICATOR_SIZE = 10.dp
private const val SECONDARY_TEXT_ALPHA = 0.7f
private const val COVER_LETTER_ALPHA = 0.8f
