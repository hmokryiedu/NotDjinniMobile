package not.djinni.presentation.core.components.base

import androidx.compose.animation.animateColorAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.tooling.preview.Preview
import not.djinni.presentation.core.extension.clickableNoRipple
import not.djinni.presentation.core.extension.toTextData
import not.djinni.presentation.screens.seeker.main.model.VacancyTab
import not.djinni.presentation.theme.NotDjinniTheme

@Composable
fun NotDjinniTabBar(
    modifier: Modifier = Modifier,
    selectedTab: VacancyTab,
    onTabSelected: (VacancyTab) -> Unit,
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .clip(NotDjinniTheme.shapes.small)
            .background(NotDjinniTheme.colors.primary.copy(alpha = 0.2f))
    ) {
        VacancyTab.entries.forEach { tab ->
            val isSelected = selectedTab == tab
            val backgroundColor by animateColorAsState(
                targetValue = NotDjinniTheme.colors.primary.copy(alpha = if (isSelected) 1f else 0f)
            )
            val textColor by animateColorAsState(
                targetValue = if (isSelected) {
                    NotDjinniTheme.colors.onPrimary
                } else {
                    NotDjinniTheme.colors.onBackground
                }
            )
            NotDjinniText(
                modifier = Modifier
                    .weight(1f)
                    .clip(NotDjinniTheme.shapes.small)
                    .background(backgroundColor)
                    .clickableNoRipple { onTabSelected(tab) }
                    .padding(
                        horizontal = NotDjinniTheme.offsets.medium,
                        vertical = NotDjinniTheme.offsets.small
                    ),
                data = tab.titleResId.toTextData(),
                style = NotDjinniTheme.typography.body2,
                color = textColor,
            )
        }
    }
}

@Composable
@Preview
private fun Preview() {
    NotDjinniTheme {
        NotDjinniTabBar(
            selectedTab = VacancyTab.ALL,
            onTabSelected = {}
        )
    }
}
