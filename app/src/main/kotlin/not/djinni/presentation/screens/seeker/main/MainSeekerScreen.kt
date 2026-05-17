package not.djinni.presentation.screens.seeker.main

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.text.input.TextFieldState
import androidx.compose.foundation.text.input.rememberTextFieldState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.outlined.Description
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import not.djinni.R
import not.djinni.presentation.core.Screen
import not.djinni.presentation.core.components.base.FullscreenColumn
import not.djinni.presentation.core.components.base.NotDjinniTabBar
import not.djinni.presentation.core.components.base.NotDjinniText
import not.djinni.presentation.core.components.base.NotDjinniTextField
import not.djinni.presentation.core.components.base.VacancyCard
import not.djinni.presentation.core.components.base.VerticalSpacer
import not.djinni.presentation.core.extension.clickableNoRipple
import not.djinni.presentation.core.extension.collectAsEffect
import not.djinni.presentation.core.extension.toTextData
import not.djinni.presentation.theme.NotDjinniIcons
import not.djinni.presentation.theme.NotDjinniTheme

@Composable
internal fun MainSeekerScreen(
    onVacancyClick: (Long) -> Unit = {},
    onProfileClick: () -> Unit = {},
    onApplicationsClick: () -> Unit = {},
    onFavoriteVacanciesClick: () -> Unit = {},
) {
    Screen<MainSeekerViewModel> { viewModel ->
        val state by viewModel.state.collectAsStateWithLifecycle()
        val searchState = rememberTextFieldState()

        LaunchedEffect(searchState.text) {
            viewModel.sendAction(MainSeekerAction.Search(searchState.text.toString()))
        }

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

                MainSeekerSideEffect.NavigateToProfile -> onProfileClick()
                MainSeekerSideEffect.NavigateToApplications -> onApplicationsClick()
                MainSeekerSideEffect.NavigateToFavoriteVacancies -> onFavoriteVacanciesClick()
            }
        }
    }
}

@Composable
private fun Content(
    state: MainSeekerState,
    searchState: TextFieldState = rememberTextFieldState(),
    onAction: (MainSeekerAction) -> Unit,
) {
    val lazyListState = rememberLazyListState()

    FullscreenColumn {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically,
        ) {
            NotDjinniText(
                data = R.string.vacancies_title.toTextData(),
                style = NotDjinniTheme.typography.title1Bold,
                color = NotDjinniTheme.colors.onBackground,
            )
            Row(
                horizontalArrangement = Arrangement.spacedBy(NotDjinniTheme.offsets.small),
                verticalAlignment = Alignment.CenterVertically,
            ) {
                FavoritesButton(onClick = { onAction(MainSeekerAction.OpenFavoriteVacancies) })
                ApplicationsButton(onClick = { onAction(MainSeekerAction.OpenApplications) })
                ProfileButton(onClick = { onAction(MainSeekerAction.OpenProfile) })
            }
        }
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
                state = lazyListState,
                verticalArrangement = Arrangement.spacedBy(NotDjinniTheme.offsets.small)
            ) {
                items(
                    items = state.vacanciesListState.items,
                    key = { it.id }
                ) { vacancy ->
                    VacancyCard(
                        modifier = Modifier.animateItem(),
                        data = vacancy,
                        onClick = { onAction(MainSeekerAction.OpenVacancy(vacancy.id)) },
                        onFavoriteClick = {
                            onAction(
                                MainSeekerAction.ToggleFavorite(
                                    vacancyId = vacancy.id,
                                    isFavorite = vacancy.isFavorite
                                )
                            )
                        }
                    )
                }
            }
        }
    }

    LaunchedEffect(state.vacanciesListState.items) {
        if (state.vacanciesListState.items.isEmpty()) return@LaunchedEffect
        lazyListState.scrollToItem(0)
    }
}

@Composable
private fun FavoritesButton(onClick: () -> Unit) {
    Box(
        modifier = Modifier
            .size(PROFILE_BUTTON_SIZE)
            .clip(CircleShape)
            .background(NotDjinniTheme.colors.surfaceContainer)
            .clickableNoRipple(onClick = onClick),
        contentAlignment = Alignment.Center
    ) {
        Icon(
            modifier = Modifier.size(PROFILE_ICON_SIZE),
            imageVector = Icons.Filled.Favorite,
            contentDescription = null,
            tint = NotDjinniTheme.colors.onSurface
        )
    }
}

@Composable
private fun ApplicationsButton(onClick: () -> Unit) {
    Box(
        modifier = Modifier
            .size(PROFILE_BUTTON_SIZE)
            .clip(CircleShape)
            .background(NotDjinniTheme.colors.surfaceContainer)
            .clickableNoRipple(onClick = onClick),
        contentAlignment = Alignment.Center
    ) {
        Icon(
            modifier = Modifier.size(PROFILE_ICON_SIZE),
            imageVector = Icons.Outlined.Description,
            contentDescription = null,
            tint = NotDjinniTheme.colors.onSurface
        )
    }
}

@Composable
private fun ProfileButton(onClick: () -> Unit) {
    Box(
        modifier = Modifier
            .size(PROFILE_BUTTON_SIZE)
            .clip(CircleShape)
            .background(NotDjinniTheme.colors.surfaceContainer)
            .clickableNoRipple(onClick = onClick),
        contentAlignment = Alignment.Center
    ) {
        Icon(
            modifier = Modifier.size(PROFILE_ICON_SIZE),
            imageVector = NotDjinniIcons.person,
            contentDescription = null,
            tint = NotDjinniTheme.colors.onSurface
        )
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

private val PROFILE_BUTTON_SIZE = 40.dp
private val PROFILE_ICON_SIZE = 24.dp
