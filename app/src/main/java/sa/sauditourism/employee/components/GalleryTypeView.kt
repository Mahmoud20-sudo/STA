package sa.sauditourism.employee.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import sa.sauditourism.employee.LocalLanguage
import sa.sauditourism.employee.R
import sa.sauditourism.employee.constants.LanguageConstants
import sa.sauditourism.employee.ui.theme.AppFonts

enum class GalleryType {
    VIDEO,
    THREE_SIXTY,
    AR,
    VR,
    IMAGE,
    VIEW_MORE,
}

fun GalleryType.getImageIcon(): Int {
    return when (this) {
        GalleryType.VIDEO -> R.drawable.ic_play
        GalleryType.THREE_SIXTY -> R.drawable.ic_360
        GalleryType.AR -> R.drawable.ic_agumnted_reality
        GalleryType.VR -> R.drawable.ic_virtual_reality
        GalleryType.IMAGE -> R.drawable.ic_image_type
        else -> {
            0
        }
    }
}

data class GalleryTypeModel(val type: GalleryType, val text: String? = null)

@Composable
fun GalleryTypeView(presentable: GalleryTypeModel, modifier: Modifier = Modifier) {
    Box(
        modifier = modifier.fillMaxSize(),
        contentAlignment = Alignment.Center,
    ) {
        if (presentable.type == GalleryType.VIEW_MORE && presentable.text != null) {
            Box(
                modifier = Modifier.fillMaxSize() then Modifier.background(Color.Black.copy(alpha = 0.5f)),
            ) {
                CustomTextComponent(
                    text = presentable.text,
                    style = AppFonts.labelSemiBold,
                    color = Color.White,
                )
            }
        } else {
            Box(
                modifier = Modifier.size(32.dp) then Modifier.background(
                    Color.Black.copy(alpha = 0.5f),
                    shape = CircleShape,
                ),
                contentAlignment = Alignment.Center,
            ) {
                Image(
                    painter = painterResource(id = presentable.type.getImageIcon()),
                    contentDescription = "icon",
                    modifier = Modifier.size(16.dp),
                )
            }
        }
    }
}

@Composable
@Preview
fun GalleryTypeViewPreview() {
    CompositionLocalProvider(LocalLanguage provides LanguageConstants.DEFAULT_LOCALE) {
        Column {
            GalleryTypeView(GalleryTypeModel(GalleryType.VIDEO))
            GalleryTypeView(GalleryTypeModel(GalleryType.IMAGE))
            GalleryTypeView(GalleryTypeModel(GalleryType.VIEW_MORE, "+20 More"))
        }
    }
}
