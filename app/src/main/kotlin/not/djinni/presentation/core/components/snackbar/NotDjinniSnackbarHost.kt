package not.djinni.presentation.core.components.snackbar

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Snackbar
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import not.djinni.presentation.core.components.base.NotDjinniText
import not.djinni.presentation.core.extension.toTextData
import not.djinni.presentation.theme.NotDjinniTheme

@Composable
fun NotDjinniSnackbarHost(
    modifier: Modifier = Modifier,
    hostState: SnackbarHostState,
) {
    SnackbarHost(
        modifier = modifier,
        hostState = hostState,
        snackbar = { data ->
            Snackbar(
                modifier = Modifier.padding(horizontal = NotDjinniTheme.offsets.large),
                contentColor = Color.White,
                containerColor = Color(0xFF484849),
                content = {
                    NotDjinniText(
                        data = data.visuals.message.toTextData(),
                        style = NotDjinniTheme.typography.body3.copy(
                            fontWeight = FontWeight.Medium,
                        ),
                    )
                }
            )
        }
    )
}