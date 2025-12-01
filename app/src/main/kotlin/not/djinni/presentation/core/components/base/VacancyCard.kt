package not.djinni.presentation.core.components.base

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import not.djinni.presentation.core.components.base.model.VacancyCardData
import not.djinni.presentation.core.extension.clickableNoRipple
import not.djinni.presentation.theme.NotDjinniTheme

@Composable
fun VacancyCard(
    modifier: Modifier = Modifier,
    data: VacancyCardData,
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
            data = data.companyName,
            style = NotDjinniTheme.typography.body2,
            color = NotDjinniTheme.colors.onBackground.copy(alpha = 0.7f),
        )
        VerticalSpacer(NotDjinniTheme.offsets.tiny)
        NotDjinniText(
            data = data.title,
            style = NotDjinniTheme.typography.body1Bold,
            color = NotDjinniTheme.colors.onBackground,
        )
        VerticalSpacer(NotDjinniTheme.offsets.tiny)
        Row(verticalAlignment = Alignment.CenterVertically) {
            NotDjinniText(
                data = data.salaryRange,
                style = NotDjinniTheme.typography.body2,
                color = NotDjinniTheme.colors.onBackground,
            )
            HorizontalSpacer(NotDjinniTheme.offsets.small)
            NotDjinniText(
                data = data.employmentType,
                style = NotDjinniTheme.typography.body3,
                color = NotDjinniTheme.colors.onBackground.copy(alpha = 0.7f),
            )
        }
        VerticalSpacer(NotDjinniTheme.offsets.tiny)
        NotDjinniText(
            data = data.requiredExperience,
            style = NotDjinniTheme.typography.body3,
            color = NotDjinniTheme.colors.onBackground.copy(alpha = 0.7f),
        )
    }
}
