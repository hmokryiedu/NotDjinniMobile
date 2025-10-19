package not.djinni.presentation.core

import android.content.res.Configuration
import androidx.compose.ui.tooling.preview.Preview


@Preview(name = "Light Mode", showBackground = true, uiMode = Configuration.UI_MODE_NIGHT_NO)
@Preview(name = "Dark Mode", showBackground = true, uiMode = Configuration.UI_MODE_NIGHT_YES)
annotation class ThemePreviews

@Preview(
    name = "Light Mode",
    uiMode = Configuration.UI_MODE_NIGHT_NO,
    showBackground = true
)
annotation class LightPreview

@Preview(
    name = "Dark Mode",
    uiMode = Configuration.UI_MODE_NIGHT_YES,
    showBackground = true
)
annotation class DarkPreview

@Preview(
    group = "Screen Size",
    name = "Small Phone",
    device = "id:Galaxy Nexus"
)
@Preview(
    group = "Screen Size",
    name = "Phone",
    device = "id:pixel_8"
)
@Preview(
    group = "Screen Size",
    name = "Tablet",
    device = "spec:parent=Nexus 10,orientation=portrait"
)
@Preview(
    group = "Screen Size",
    name = "Small Phone",
    device = "id:Galaxy Nexus",
    uiMode = Configuration.UI_MODE_NIGHT_YES,
)
@Preview(
    group = "Screen Size",
    name = "Phone",
    device = "id:pixel_8",
    uiMode = Configuration.UI_MODE_NIGHT_YES,
)
@Preview(
    group = "Screen Size",
    name = "Tablet",
    device = "spec:parent=Nexus 10,orientation=portrait",
    uiMode = Configuration.UI_MODE_NIGHT_YES,
)
annotation class ScreenSizesPreview