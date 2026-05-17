package not.djinni.presentation.screens.public.main

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
import androidx.compose.material.icons.outlined.Login
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import not.djinni.R
import not.djinni.presentation.core.Screen
import not.djinni.presentation.core.components.base.FullscreenColumn
import not.djinni.presentation.core.components.base.NotDjinniLoader
import not.djinni.presentation.core.components.base.NotDjinniText
import not.djinni.presentation.core.components.base.NotDjinniTextField
import not.djinni.presentation.core.components.base.VacancyCard
import not.djinni.presentation.core.components.base.VerticalSpacer
import not.djinni.presentation.core.extension.clickableNoRipple
import not.djinni.presentation.core.extension.collectAsEffect
import not.djinni.presentation.core.extension.toTextData
import not.djinni.presentation.theme.NotDjinniTheme

@Composable
internal fun PublicMainScreen(
    onVacancyClick: (Long) -> Unit = {},
    onLoginClick: () -> Unit = {},
) {
    Screen<PublicMainViewModel> { viewModel ->
        val state by viewModel.state.collectAsStateWithLifecycle()
        val searchState = rememberTextFieldState()

        LaunchedEffect(searchState.text) {
            viewModel.sendAction(PublicMainAction.Search(searchState.text.toString()))
        }

        Content(
            state = state,
            searchState = searchState,
            onAction = viewModel::sendAction,
        )

        viewModel.sideEffect.collectAsEffect { effect ->
            when (effect) {
                is PublicMainSideEffect.NavigateToVacancyDetails -> {
                    onVacancyClick(effect.vacancyId)
                }

                PublicMainSideEffect.NavigateToLogin -> onLoginClick()
            }
        }
    }
}

@Composable
private fun Content(
    state: PublicMainState,
    searchState: TextFieldState = rememberTextFieldState(),
    onAction: (PublicMainAction) -> Unit,
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
            LoginButton(onClick = { onAction(PublicMainAction.OpenLogin) })
        }
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
            when (state.vacanciesListState) {
                PublicVacanciesListState.Empty -> {
                    NotDjinniText(
                        modifier = Modifier.align(Alignment.Center),
                        data = R.string.vacancy_no_results.toTextData(),
                        style = NotDjinniTheme.typography.body2,
                        color = NotDjinniTheme.colors.onBackground.copy(alpha = 0.6f),
                    )
                }

                PublicVacanciesListState.Loading -> {
                    NotDjinniLoader(modifier = Modifier.align(Alignment.Center))
                }

                is PublicVacanciesListState.Data -> Unit
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
                        onClick = { onAction(PublicMainAction.OpenVacancy(vacancy.id)) }
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
private fun LoginButton(onClick: () -> Unit) {
    Box(
        modifier = Modifier
            .size(LOGIN_BUTTON_SIZE)
            .clip(CircleShape)
            .background(NotDjinniTheme.colors.surfaceContainer)
            .clickableNoRipple(onClick = onClick),
        contentAlignment = Alignment.Center
    ) {
        Icon(
            modifier = Modifier.size(LOGIN_ICON_SIZE),
            imageVector = Icons.Outlined.Login,
            contentDescription = null,
            tint = NotDjinniTheme.colors.onSurface
        )
    }
}

private val LOGIN_BUTTON_SIZE = 40.dp
private val LOGIN_ICON_SIZE = 24.dp
