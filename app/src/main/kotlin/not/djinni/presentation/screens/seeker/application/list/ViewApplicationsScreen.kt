package not.djinni.presentation.screens.seeker.application.list

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import not.djinni.R
import not.djinni.presentation.core.Screen
import not.djinni.presentation.core.components.base.ApplicationCard
import not.djinni.presentation.core.components.base.FullscreenColumn
import not.djinni.presentation.core.components.base.HorizontalSpacer
import not.djinni.presentation.core.components.base.MessageCard
import not.djinni.presentation.core.components.base.NotDjinniText
import not.djinni.presentation.core.components.base.VerticalSpacer
import not.djinni.presentation.core.extension.clickableNoRipple
import not.djinni.presentation.core.extension.collectAsEffect
import not.djinni.presentation.core.extension.toTextData
import not.djinni.presentation.screens.seeker.application.list.mapper.toCardData
import not.djinni.presentation.theme.NotDjinniIcons
import not.djinni.presentation.theme.NotDjinniTheme

@Composable
internal fun ViewApplicationsScreen(
    onBack: () -> Unit,
    onNavigateToDetails: (Long) -> Unit,
) {
    Screen<ViewApplicationsViewModel> { viewModel ->
        val state by viewModel.state.collectAsStateWithLifecycle()

        Content(
            state = state,
            onBack = onBack,
            onAction = viewModel::sendAction
        )

        viewModel.sideEffect.collectAsEffect { effect ->
            when (effect) {
                is ViewApplicationsSideEffect.NavigateToDetails -> onNavigateToDetails(effect.id)
            }
        }
    }
}

@Composable
private fun Content(
    state: ViewApplicationsState,
    onBack: () -> Unit,
    onAction: (ViewApplicationsAction) -> Unit,
) {
    FullscreenColumn(modifier = Modifier.fillMaxSize()) {
        TopBar(onBack = onBack)
        VerticalSpacer(NotDjinniTheme.offsets.medium)
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f)
        ) {
            if (state.contentState is ContentState.Error) {
                MessageCard(
                    modifier = Modifier
                        .align(Alignment.Center)
                        .clickableNoRipple { onAction(ViewApplicationsAction.LoadApplications) },
                    message = state.contentState.message,
                )
            }
            LazyColumn(
                modifier = Modifier.matchParentSize(),
                verticalArrangement = Arrangement.spacedBy(NotDjinniTheme.offsets.small),
                contentPadding = PaddingValues(NotDjinniTheme.offsets.medium)
            ) {
                items(
                    items = state.contentState.items(),
                    key = { it.id }
                ) { application ->
                    ApplicationCard(
                        modifier = Modifier.animateItem(),
                        data = application.toCardData(),
                        onClick = {
                            onAction(ViewApplicationsAction.OpenApplicationDetails(application.id))
                        }
                    )
                }
            }
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
            data = R.string.my_applications.toTextData(),
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
            state = ViewApplicationsState(),
            onBack = {},
            onAction = {}
        )
    }
}

private val ICON_SIZE = 24.dp
