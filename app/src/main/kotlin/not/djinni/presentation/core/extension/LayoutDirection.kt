package not.djinni.presentation.core.extension

import androidx.compose.ui.unit.LayoutDirection

val LayoutDirection.isRtl: Boolean
    get() = this == LayoutDirection.Rtl