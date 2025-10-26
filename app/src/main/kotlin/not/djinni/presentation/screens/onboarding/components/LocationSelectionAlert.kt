package not.djinni.presentation.screens.onboarding.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.RadioButton
import androidx.compose.material3.RadioButtonDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.window.Dialog
import not.djinni.R
import not.djinni.model.Location
import not.djinni.presentation.core.components.base.HorizontalSpacer
import not.djinni.presentation.core.components.base.NotDjinniText
import not.djinni.presentation.core.components.base.VerticalSpacer
import not.djinni.presentation.core.extension.clickableNoRipple
import not.djinni.presentation.core.extension.clickableNoRippleWithoutInterval
import not.djinni.presentation.core.extension.toTextData
import not.djinni.presentation.theme.NotDjinniTheme

@Composable
fun LocationSelectionAlert(
    locations: List<Location>,
    selected: Location?,
    onDismiss: () -> Unit,
    onApply: (Location) -> Unit
) {
    var selectedLocation by remember { mutableStateOf(selected) }

    Dialog(onDismissRequest = onDismiss) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .background(
                    color = NotDjinniTheme.colors.surface,
                    shape = RoundedCornerShape(NotDjinniTheme.shapes.medium.topStart)
                )
                .padding(NotDjinniTheme.offsets.medium)
        ) {
            NotDjinniText(
                data = R.string.work_location_dialog_title.toTextData(),
                style = NotDjinniTheme.typography.title2,
                color = NotDjinniTheme.colors.onSurface,
            )
            VerticalSpacer(NotDjinniTheme.offsets.medium)
            LazyColumn {
                items(items = locations, key = { it.id }) { location ->
                    SelectionItem(
                        location = location,
                        isSelected = location == selectedLocation,
                        onSelect = { selectedLocation = location }
                    )
                }
            }
            VerticalSpacer(NotDjinniTheme.offsets.medium)
            Row(
                modifier = Modifier.align(Alignment.End)
            ) {
                NotDjinniText(
                    modifier = Modifier
                        .clickableNoRipple(onClick = onDismiss)
                        .padding(NotDjinniTheme.offsets.small),
                    data = R.string.cancel.toTextData(),
                    style = NotDjinniTheme.typography.body2,
                    color = NotDjinniTheme.colors.onSurface,
                )
                HorizontalSpacer(NotDjinniTheme.offsets.small)
                NotDjinniText(
                    modifier = Modifier
                        .clickableNoRippleWithoutInterval {
                            onDismiss()
                            onApply(selectedLocation ?: return@clickableNoRippleWithoutInterval)
                        }
                        .padding(NotDjinniTheme.offsets.small),
                    data = R.string.apply.toTextData(),
                    style = NotDjinniTheme.typography.body2,
                    color = if (selectedLocation != null) {
                        NotDjinniTheme.colors.onSurface
                    } else {
                        NotDjinniTheme.colors.onSurface.copy(alpha = 0.5f)
                    },
                )
            }
        }
    }
}

@Composable
private fun SelectionItem(
    location: Location,
    isSelected: Boolean,
    onSelect: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickableNoRipple(onClick = onSelect)
            .padding(vertical = NotDjinniTheme.offsets.small),
        verticalAlignment = Alignment.CenterVertically
    ) {
        RadioButton(
            selected = isSelected,
            onClick = null,
            colors = RadioButtonDefaults.colors(
                selectedColor = NotDjinniTheme.colors.onSurface,
                unselectedColor = NotDjinniTheme.colors.onSurface.copy(alpha = 0.6f)
            )
        )
        HorizontalSpacer(NotDjinniTheme.offsets.small)
        NotDjinniText(
            data = location.name.toTextData(),
            style = NotDjinniTheme.typography.body2,
            color = NotDjinniTheme.colors.onSurface,
        )
    }
}
