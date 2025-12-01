package not.djinni.presentation.screens.employer.profile.create

import androidx.activity.compose.BackHandler
import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.input.TextFieldState
import androidx.compose.foundation.text.input.rememberTextFieldState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import not.djinni.R
import not.djinni.model.company.Company
import not.djinni.presentation.core.Screen
import not.djinni.presentation.core.components.base.FullscreenColumn
import not.djinni.presentation.core.components.base.HorizontalSpacer
import not.djinni.presentation.core.components.base.MessageCard
import not.djinni.presentation.core.components.base.NotDjinniButton
import not.djinni.presentation.core.components.base.NotDjinniText
import not.djinni.presentation.core.components.base.NotDjinniTextField
import not.djinni.presentation.core.components.base.VerticalSpacer
import not.djinni.presentation.core.components.base.buildDefaultTextFieldDecorator
import not.djinni.presentation.core.components.base.model.ButtonData
import not.djinni.presentation.core.components.base.model.TextData
import not.djinni.presentation.core.extension.clickableNoRipple
import not.djinni.presentation.core.extension.collectAsEffect
import not.djinni.presentation.core.extension.toTextData
import not.djinni.presentation.screens.employer.profile.create.alert.CreateEmployerProfileAlert
import not.djinni.presentation.theme.NotDjinniIcons
import not.djinni.presentation.theme.NotDjinniTheme

@Composable
internal fun CreateEmployerProfileScreen(
    onHome: () -> Unit,
) {
    Screen<CreateEmployerProfileViewModel> { viewModel ->
        val state by viewModel.state.collectAsStateWithLifecycle()

        val roleFieldState = rememberTextFieldState()
        val searchFieldState = rememberTextFieldState()

        AnimatedContent(
            targetState = state.currentAlert == CreateEmployerProfileAlert.SELECT_COMPANY,
            transitionSpec = { fadeIn() togetherWith fadeOut() }
        ) { isAlertVisible ->
            if (isAlertVisible) {
                SelectCompanyContent(
                    state = state,
                    searchFieldState = searchFieldState,
                    onAction = viewModel::sendAction
                )
            } else {
                Content(
                    state = state,
                    roleFieldState = roleFieldState,
                    onAction = viewModel::sendAction,
                )
            }
        }

        MessageCard(
            modifier = Modifier
                .padding(horizontal = NotDjinniTheme.offsets.large)
                .fillMaxWidth()
                .statusBarsPadding(),
            enter = fadeIn() + slideInVertically { -it / 4 },
            exit = fadeOut() + slideOutVertically { -it / 4 },
            message = state.message
        )

        LaunchedEffect(searchFieldState) {
            snapshotFlow { searchFieldState.text.toString() }.collect { query ->
                viewModel.sendAction(CreateEmployerProfileAction.SearchCompanies(query))
            }
        }

        LaunchedEffect(state.currentAlert) {
            if (state.currentAlert == null) searchFieldState.edit { replace(0, length, "") }
        }

        viewModel.sideEffect.collectAsEffect { effect ->
            when (effect) {
                CreateEmployerProfileSideEffect.NavigateToHome -> onHome()
            }
        }

        BackHandler(enabled = state.currentAlert == CreateEmployerProfileAlert.SELECT_COMPANY) {
            viewModel.sendAction(CreateEmployerProfileAction.HideAlert)
        }
    }
}

@Composable
private fun Content(
    state: CreateEmployerProfileState,
    roleFieldState: TextFieldState = rememberTextFieldState(),
    onAction: (CreateEmployerProfileAction) -> Unit,
) {
    FullscreenColumn {
        NotDjinniText(
            data = R.string.create_employer_profile_title.toTextData(),
            style = NotDjinniTheme.typography.title1Bold,
            color = NotDjinniTheme.colors.onBackground,
        )
        VerticalSpacer(NotDjinniTheme.offsets.huge)
        Column(
            modifier = Modifier
                .weight(1f)
                .imePadding()
                .verticalScroll(rememberScrollState())
        ) {
            ProfileDataInput(
                state = roleFieldState,
                title = R.string.employer_role_title.toTextData(),
                placeholder = R.string.employer_role_hint.toTextData(),
            )
            VerticalSpacer(NotDjinniTheme.offsets.medium)
            NotDjinniText(
                data = R.string.employer_company_title.toTextData(),
                style = NotDjinniTheme.typography.body3,
                color = NotDjinniTheme.colors.onBackground,
            )
            VerticalSpacer(NotDjinniTheme.offsets.tiny)
            CompanySelectionBox(
                selectedCompany = state.selectedCompany,
                onClick = { onAction(CreateEmployerProfileAction.ShowSelectCompanyAlert) }
            )
        }
        VerticalSpacer(NotDjinniTheme.offsets.regular)
        NotDjinniButton(
            modifier = Modifier.fillMaxWidth(),
            data = ButtonData(R.string.create_profile_button.toTextData()),
            onClick = {
                val action = CreateEmployerProfileAction.CreateProfile(
                    role = roleFieldState.text.toString(),
                )
                onAction(action)
            }
        )
    }
}

@Composable
private fun CompanySelectionBox(
    modifier: Modifier = Modifier,
    selectedCompany: Company?,
    onClick: () -> Unit,
) {
    Box(
        modifier = modifier
            .fillMaxWidth()
            .border(
                width = 1.dp,
                color = NotDjinniTheme.colors.onSurface,
                shape = NotDjinniTheme.shapes.small,
            )
            .background(
                color = NotDjinniTheme.colors.primary.copy(alpha = 0.3f),
                shape = NotDjinniTheme.shapes.small,
            )
            .clickableNoRipple(onClick = onClick)
            .padding(
                horizontal = NotDjinniTheme.offsets.medium,
                vertical = NotDjinniTheme.offsets.small,
            ),
    ) {
        NotDjinniText(
            data = selectedCompany?.name?.toTextData()
                ?: R.string.employer_company_hint.toTextData(),
            style = NotDjinniTheme.typography.body1,
            color = if (selectedCompany != null) {
                NotDjinniTheme.colors.onSurface
            } else {
                NotDjinniTheme.colors.onSurface.copy(alpha = 0.5f)
            },
        )
    }
}

@Composable
private fun SelectCompanyContent(
    modifier: Modifier = Modifier,
    state: CreateEmployerProfileState,
    searchFieldState: TextFieldState = rememberTextFieldState(),
    onAction: (CreateEmployerProfileAction) -> Unit,
) {
    FullscreenColumn(
        modifier = modifier.imePadding(),
        scrollable = true
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Icon(
                modifier = Modifier.clickableNoRipple {
                    onAction(CreateEmployerProfileAction.HideAlert)
                },
                imageVector = NotDjinniIcons.back,
                tint = NotDjinniTheme.colors.onBackground,
                contentDescription = null,
            )
            HorizontalSpacer(NotDjinniTheme.offsets.small)
            NotDjinniText(
                data = R.string.employer_select_company.toTextData(),
                color = NotDjinniTheme.colors.onBackground,
                style = NotDjinniTheme.typography.title2
            )
        }
        VerticalSpacer(NotDjinniTheme.offsets.average)
        NotDjinniTextField(
            modifier = Modifier.fillMaxWidth(),
            state = searchFieldState,
            placeholder = R.string.employer_search_company_hint.toTextData(),
            decorator = buildDefaultTextFieldDecorator(
                state = searchFieldState,
                placeholder = R.string.employer_search_company_hint.toTextData(),
                textStyle = NotDjinniTheme.typography.body1.copy(
                    color = NotDjinniTheme.colors.onSurface,
                ),
            ),
        )
        VerticalSpacer(NotDjinniTheme.offsets.medium)
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f),
            contentAlignment = Alignment.Center
        ) {
            if (state.isSearching) {

                CircularProgressIndicator(
                    modifier = Modifier.size(32.dp),
                    color = NotDjinniTheme.colors.primary
                )
            } else if (state.searchResults.isEmpty() && searchFieldState.text.isNotBlank()) {
                NotDjinniText(
                    data = R.string.employer_no_companies_found.toTextData(),
                    style = NotDjinniTheme.typography.body2,
                    color = NotDjinniTheme.colors.onBackground.copy(alpha = 0.6f),
                )
            }
            LazyColumn(
                modifier = Modifier
                    .align(Alignment.TopCenter)
                    .fillMaxWidth(),
                verticalArrangement = Arrangement.spacedBy(NotDjinniTheme.offsets.tiny)
            ) {
                items(items = state.searchResults, key = { it.id }) { company ->
                    CompanyItem(
                        modifier = Modifier.animateItem(),
                        company = company,
                        onClick = { onAction(CreateEmployerProfileAction.SelectCompany(company)) }
                    )
                }
            }
        }
    }
}

@Composable
private fun CompanyItem(
    modifier: Modifier = Modifier,
    company: Company,
    onClick: () -> Unit,
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .border(
                width = 1.dp,
                color = NotDjinniTheme.colors.onSurface,
                shape = NotDjinniTheme.shapes.small,
            )
            .background(
                color = NotDjinniTheme.colors.primary.copy(alpha = 0.3f),
                shape = NotDjinniTheme.shapes.small,
            )
            .clickableNoRipple(onClick = onClick)
            .padding(
                horizontal = NotDjinniTheme.offsets.medium,
                vertical = NotDjinniTheme.offsets.small,
            ),
    ) {
        NotDjinniText(
            data = company.name.toTextData(),
            style = NotDjinniTheme.typography.body1Bold,
            color = NotDjinniTheme.colors.onBackground,
        )
        VerticalSpacer(NotDjinniTheme.offsets.tiny)
        NotDjinniText(
            data = company.description.toTextData(),
            style = NotDjinniTheme.typography.body3,
            color = NotDjinniTheme.colors.onBackground.copy(alpha = 0.7f),
        )
    }
}

@Composable
private fun ProfileDataInput(
    modifier: Modifier = Modifier,
    title: TextData,
    state: TextFieldState,
    placeholder: TextData,
) {
    Column(modifier = modifier) {
        NotDjinniText(
            data = title,
            style = NotDjinniTheme.typography.body3,
            color = NotDjinniTheme.colors.onBackground,
        )
        VerticalSpacer(NotDjinniTheme.offsets.tiny)
        NotDjinniTextField(
            state = state,
            decorator = buildDefaultTextFieldDecorator(
                state = state,
                placeholder = placeholder,
                textStyle = NotDjinniTheme.typography.body1.copy(
                    color = NotDjinniTheme.colors.onSurface,
                ),
            ),
        )
    }
}

@Composable
@Preview
private fun Preview() {
    NotDjinniTheme {
        val state = CreateEmployerProfileState()
        Content(
            state = state,
            onAction = {}
        )
    }
}
