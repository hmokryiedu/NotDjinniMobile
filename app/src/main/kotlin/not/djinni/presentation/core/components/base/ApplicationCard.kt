package not.djinni.presentation.core.components.base

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import not.djinni.presentation.core.components.base.model.ApplicationCardData
import not.djinni.presentation.core.extension.clickableNoRipple
import not.djinni.presentation.theme.NotDjinniTheme

@Composable
fun ApplicationCard(
    modifier: Modifier = Modifier,
    data: ApplicationCardData,
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
            data = data.speciality,
            style = NotDjinniTheme.typography.body1Bold,
            color = NotDjinniTheme.colors.onBackground,
        )
        VerticalSpacer(NotDjinniTheme.offsets.tiny)
        NotDjinniText(
            data = data.experienceYears,
            style = NotDjinniTheme.typography.body2,
            color = NotDjinniTheme.colors.onBackground.copy(alpha = 0.7f),
        )
        VerticalSpacer(NotDjinniTheme.offsets.tiny)
        Row(verticalAlignment = Alignment.CenterVertically) {
            Box(
                modifier = Modifier
                    .size(STATUS_INDICATOR_SIZE)
                    .clip(CircleShape)
                    .background(data.statusColor)
            )
            HorizontalSpacer(NotDjinniTheme.offsets.tiny)
            NotDjinniText(
                data = data.status,
                style = NotDjinniTheme.typography.body3,
                color = data.statusColor,
            )
        }
        if (data.coverLetterPreview != null) {
            VerticalSpacer(NotDjinniTheme.offsets.tiny)
            NotDjinniText(
                data = data.coverLetterPreview,
                style = NotDjinniTheme.typography.body3,
                color = NotDjinniTheme.colors.onBackground.copy(alpha = 0.6f),
                maxLines = 2,
            )
        }
        VerticalSpacer(NotDjinniTheme.offsets.tiny)
        NotDjinniText(
            data = data.appliedDate,
            style = NotDjinniTheme.typography.body3,
            color = NotDjinniTheme.colors.onBackground.copy(alpha = 0.5f),
        )
    }
}

private val STATUS_INDICATOR_SIZE = 8.dp
