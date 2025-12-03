@file:OptIn(ExperimentalMaterial3Api::class)

package not.djinni.presentation.screens.employer.vacancy.create.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import kotlinx.coroutines.launch
import not.djinni.R
import not.djinni.model.seeker.vacancy.JobCategoryCode
import not.djinni.presentation.core.components.base.NotDjinniText
import not.djinni.presentation.core.components.base.VerticalSpacer
import not.djinni.presentation.core.extension.clickableNoRipple
import not.djinni.presentation.core.extension.toDisplayName
import not.djinni.presentation.core.extension.toTextData
import not.djinni.presentation.theme.NotDjinniTheme

@Composable
internal fun JobCategoryBottomSheet(
    selected: JobCategoryCode?,
    onDismiss: () -> Unit,
    onSelect: (JobCategoryCode) -> Unit,
) {
    val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)
    val scope = rememberCoroutineScope()

    ModalBottomSheet(
        onDismissRequest = {
            scope.launch {
                sheetState.hide()
                onDismiss()
            }
        },
        sheetState = sheetState,
        containerColor = NotDjinniTheme.colors.surface,
        contentColor = NotDjinniTheme.colors.onSurface,
    ) {
        BottomSheetContent(
            selected = selected,
            onSelect = { category ->
                scope.launch {
                    sheetState.hide()
                    onSelect(category)
                }
            }
        )
    }
}

@Composable
private fun BottomSheetContent(
    selected: JobCategoryCode?,
    onSelect: (JobCategoryCode) -> Unit,
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .navigationBarsPadding()
            .padding(horizontal = NotDjinniTheme.offsets.medium)
            .padding(bottom = NotDjinniTheme.offsets.medium)
    ) {
        NotDjinniText(
            data = R.string.create_vacancy_select_category.toTextData(),
            style = NotDjinniTheme.typography.title2,
            color = NotDjinniTheme.colors.onSurface
        )
        VerticalSpacer(NotDjinniTheme.offsets.medium)
        LazyColumn {
            items(
                items = JobCategoryCode.entries,
                key = { it.name }
            ) { category ->
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickableNoRipple { onSelect(category) }
                        .background(
                            color = if (selected == category) {
                                NotDjinniTheme.colors.highlightedContainer
                            } else {
                                NotDjinniTheme.colors.surface
                            },
                            shape = NotDjinniTheme.shapes.medium
                        )
                        .padding(NotDjinniTheme.offsets.small),
                    contentAlignment = Alignment.CenterStart
                ) {
                    NotDjinniText(
                        data = category.toDisplayName(),
                        style = NotDjinniTheme.typography.body2,
                        color = NotDjinniTheme.colors.onSurface
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
        BottomSheetContent(
            selected = JobCategoryCode.SOFTWARE_DEV,
            onSelect = {}
        )
    }
}
