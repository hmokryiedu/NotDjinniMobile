package not.djinni.presentation.screens.seeker.main

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.text.input.TextFieldState
import androidx.compose.foundation.text.input.rememberTextFieldState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import not.djinni.R
import not.djinni.presentation.core.Screen
import not.djinni.presentation.core.components.base.FullscreenColumn
import not.djinni.presentation.core.components.base.NotDjinniTabBar
import not.djinni.presentation.core.components.base.NotDjinniText
import not.djinni.presentation.core.components.base.NotDjinniTextField
import not.djinni.presentation.core.components.base.VerticalSpacer
import not.djinni.presentation.core.extension.collectAsEffect
import not.djinni.presentation.core.extension.toTextData
import not.djinni.presentation.screens.seeker.main.component.VacancyCard
import not.djinni.presentation.theme.NotDjinniTheme

@Composable
internal fun MainSeekerScreen(
    onVacancyClick: (Long) -> Unit = {},
) {
    Screen<MainSeekerViewModel> { viewModel ->
        val state by viewModel.state.collectAsStateWithLifecycle()
        val searchState = rememberTextFieldState()

        Content(
            state = state,
            searchState = searchState,
            onAction = viewModel::sendAction,
        )

        viewModel.sideEffect.collectAsEffect { effect ->
            when (effect) {
                is MainSeekerSideEffect.NavigateToVacancyDetails -> {
                    onVacancyClick(effect.vacancyId)
                }
            }
        }

        LaunchedEffect(searchState.text) {
            viewModel.sendAction(MainSeekerAction.Search(searchState.text.toString()))
        }
    }
}

@Composable
private fun Content(
    state: MainSeekerState,
    searchState: TextFieldState = rememberTextFieldState(),
    onAction: (MainSeekerAction) -> Unit,
) {
    FullscreenColumn {
        NotDjinniText(
            data = R.string.vacancies_title.toTextData(),
            style = NotDjinniTheme.typography.title1Bold,
            color = NotDjinniTheme.colors.onBackground,
        )
        VerticalSpacer(NotDjinniTheme.offsets.medium)
        NotDjinniTabBar(
            selectedTab = state.selectedTab,
            onTabSelected = { onAction(MainSeekerAction.SelectTab(it)) }
        )
        VerticalSpacer(NotDjinniTheme.offsets.medium)
        NotDjinniTextField(
            modifier = Modifier.fillMaxWidth(),
            state = searchState,
            placeholder = R.string.vacancy_search_placeholder.toTextData(),
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
                    data = R.string.vacancy_no_results.toTextData(),
                    style = NotDjinniTheme.typography.body2,
                    color = NotDjinniTheme.colors.onBackground.copy(alpha = 0.6f),
                )
            }
            LazyColumn(
                modifier = Modifier.imePadding(),
                verticalArrangement = Arrangement.spacedBy(NotDjinniTheme.offsets.small)
            ) {
                items(
                    items = state.vacanciesListState.items,
                    key = { it.id }
                ) { vacancy ->
                    VacancyCard(
                        modifier = Modifier.animateItem(),
                        data = vacancy,
                        onClick = { onAction(MainSeekerAction.OpenVacancy(vacancy.id)) }
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
        val state = MainSeekerState(vacanciesListState = VacanciesListState.Empty)
        Content(state = state, onAction = {})
    }
}
