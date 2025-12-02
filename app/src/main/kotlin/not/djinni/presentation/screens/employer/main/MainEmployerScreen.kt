package not.djinni.presentation.screens.employer.main

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import not.djinni.R
import not.djinni.presentation.core.Screen
import not.djinni.presentation.core.components.base.FullscreenColumn
import not.djinni.presentation.core.components.base.NotDjinniText
import not.djinni.presentation.core.components.base.VacancyCard
import not.djinni.presentation.core.components.base.VerticalSpacer
import not.djinni.presentation.core.extension.collectAsEffect
import not.djinni.presentation.core.extension.toTextData
import not.djinni.presentation.theme.NotDjinniTheme

@Composable
internal fun MainEmployerScreen(
    onVacancyClick: (Long) -> Unit = {},
) {
    Screen<MainEmployerViewModel> { viewModel ->
        val state by viewModel.state.collectAsStateWithLifecycle()

        Content(
            state = state,
            onAction = viewModel::sendAction,
        )

        viewModel.sideEffect.collectAsEffect { effect ->
            when (effect) {
                is MainEmployerSideEffect.NavigateToVacancyDetails -> {
                    onVacancyClick(effect.vacancyId)
                }
            }
        }
    }
}

@Composable
private fun Content(
    state: MainEmployerState,
    onAction: (MainEmployerAction) -> Unit = {},
) {
    FullscreenColumn {
        NotDjinniText(
            data = R.string.employer_vacancies_title.toTextData(),
            style = NotDjinniTheme.typography.title1Bold,
            color = NotDjinniTheme.colors.onBackground,
        )
        VerticalSpacer(NotDjinniTheme.offsets.medium)
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f)
        ) {
            if (state.vacanciesListState is VacanciesListState.Empty) {
                NotDjinniText(
                    modifier = Modifier.align(Alignment.Center),
                    data = R.string.employer_no_vacancies.toTextData(),
                    style = NotDjinniTheme.typography.body2,
                    color = NotDjinniTheme.colors.onBackground.copy(alpha = 0.6f),
                )
            }
            LazyColumn(
                verticalArrangement = Arrangement.spacedBy(NotDjinniTheme.offsets.small)
            ) {
                items(
                    items = state.vacanciesListState.items,
                    key = { it.id }
                ) { vacancy ->
                    VacancyCard(
                        modifier = Modifier.animateItem(),
                        data = vacancy,
                        onClick = { onAction(MainEmployerAction.OpenVacancy(vacancy.id)) }
                    )
                }
            }
        }
    }
}

@Composable
@Preview
private fun Preview() {
    NotDjinniTheme {
        val state = MainEmployerState(vacanciesListState = VacanciesListState.Empty)
        Content(state = state)
    }
}
