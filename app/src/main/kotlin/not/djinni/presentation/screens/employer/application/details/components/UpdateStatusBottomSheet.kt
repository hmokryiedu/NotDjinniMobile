@file:OptIn(ExperimentalMaterial3Api::class)

package not.djinni.presentation.screens.employer.application.details.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.launch
import not.djinni.R
import not.djinni.model.application.ApplicationStatus
import not.djinni.presentation.core.components.base.HorizontalSpacer
import not.djinni.presentation.core.components.base.NotDjinniText
import not.djinni.presentation.core.components.base.VerticalSpacer
import not.djinni.presentation.core.extension.clickableNoRipple
import not.djinni.presentation.core.extension.toTextData
import not.djinni.presentation.theme.NotDjinniTheme

@Composable
internal fun UpdateStatusBottomSheet(
    currentStatus: ApplicationStatus,
    onDismiss: () -> Unit,
    onSelect: (ApplicationStatus) -> Unit,
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
            currentStatus = currentStatus,
            onSelect = { status ->
                scope.launch {
                    sheetState.hide()
                    onSelect(status)
                }
            }
        )
    }
}

@Composable
private fun BottomSheetContent(
    currentStatus: ApplicationStatus,
    onSelect: (ApplicationStatus) -> Unit,
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .navigationBarsPadding()
            .padding(horizontal = NotDjinniTheme.offsets.medium)
            .padding(bottom = NotDjinniTheme.offsets.medium)
    ) {
        NotDjinniText(
            data = R.string.select_application_status.toTextData(),
            style = NotDjinniTheme.typography.title2,
            color = NotDjinniTheme.colors.onSurface
        )
        VerticalSpacer(NotDjinniTheme.offsets.medium)
        LazyColumn {
            items(
                items = ApplicationStatus.entries.filter { it != ApplicationStatus.WITHDRAWN },
                key = { it.name }
            ) { status ->
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickableNoRipple { onSelect(status) }
                        .background(
                            color = if (currentStatus == status) {
                                NotDjinniTheme.colors.highlightedContainer
                            } else {
                                NotDjinniTheme.colors.surface
                            },
                            shape = NotDjinniTheme.shapes.medium
                        )
                        .padding(NotDjinniTheme.offsets.small),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Box(
                        modifier = Modifier
                            .size(STATUS_INDICATOR_SIZE)
                            .background(
                                color = status.toColor(),
                                shape = CircleShape
                            )
                    )
                    HorizontalSpacer(NotDjinniTheme.offsets.small)
                    NotDjinniText(
                        data = status.toDisplayName(),
                        style = NotDjinniTheme.typography.body2,
                        color = NotDjinniTheme.colors.onSurface
                    )
                }
            }
        }
    }
}

private fun ApplicationStatus.toDisplayName() = when (this) {
    ApplicationStatus.APPLIED -> R.string.application_status_applied
    ApplicationStatus.REVIEWING -> R.string.application_status_reviewing
    ApplicationStatus.INTERVIEW -> R.string.application_status_interview
    ApplicationStatus.TEST_TASK -> R.string.application_status_test_task
    ApplicationStatus.OFFER -> R.string.application_status_offer
    ApplicationStatus.HIRED -> R.string.application_status_hired
    ApplicationStatus.REJECTED -> R.string.application_status_rejected
    ApplicationStatus.WITHDRAWN -> R.string.application_status_withdrawn
}.toTextData()

private fun ApplicationStatus.toColor() = when (this) {
    ApplicationStatus.APPLIED,
    ApplicationStatus.REVIEWING,
    ApplicationStatus.INTERVIEW,
    ApplicationStatus.TEST_TASK -> StatusColors.primary
    ApplicationStatus.OFFER,
    ApplicationStatus.HIRED -> StatusColors.success
    ApplicationStatus.REJECTED -> StatusColors.error
    ApplicationStatus.WITHDRAWN -> StatusColors.withdrawn
}

private object StatusColors {
    val primary = Color.White
    val success = Color(0xFF4CAF50)
    val error = Color(0xFFF54927)
    val withdrawn = Color.White.copy(alpha = 0.5f)
}

@Composable
@Preview
private fun Preview() {
    NotDjinniTheme {
        BottomSheetContent(
            currentStatus = ApplicationStatus.APPLIED,
            onSelect = {}
        )
    }
}

private val STATUS_INDICATOR_SIZE = 12.dp
