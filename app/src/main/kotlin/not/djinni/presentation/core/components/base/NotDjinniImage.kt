package not.djinni.presentation.core.components.base

import androidx.compose.foundation.Image
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.sharp.Add
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.DefaultAlpha
import androidx.compose.ui.graphics.vector.rememberVectorPainter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import not.djinni.presentation.core.components.base.model.ImageData

@Composable
fun NotDjinniImage(
    imageData: ImageData,
    contentDescription: String? = null,
    alignment: Alignment = Alignment.Center,
    contentScale: ContentScale = ContentScale.Crop,
    alpha: Float = DefaultAlpha,
    colorFilter: ColorFilter? = null,
    modifier: Modifier = Modifier,
) {
    when (imageData) {
        is ImageData.Vector -> {
            Image(
                imageVector = imageData.vector,
                alignment = alignment,
                contentScale = contentScale,
                alpha = alpha,
                colorFilter = colorFilter,
                contentDescription = contentDescription,
                modifier = modifier,
            )
        }

        is ImageData.Resource -> {
            Image(
                painter = painterResource(id = imageData.resId),
                contentDescription = contentDescription,
                alignment = alignment,
                contentScale = contentScale,
                alpha = alpha,
                colorFilter = colorFilter,
                modifier = modifier,
            )
        }
    }
}

@Composable
private fun ImageData.toPainter() = when (this) {
    is ImageData.Vector -> rememberVectorPainter(this.vector)
    is ImageData.Resource -> painterResource(id = this.resId)
}

@Preview
@Composable
private fun NotDjinniImagePreview() {
    NotDjinniImage(
        imageData = ImageData.Vector(Icons.Sharp.Add),
        contentDescription = "Image",
    )
}
