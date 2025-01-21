package sa.sauditourism.employee.components

import android.webkit.URLUtil
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import sa.sauditourism.employee.LocalLanguage
import sa.sauditourism.employee.R
import sa.sauditourism.employee.constants.LanguageConstants
import sa.sauditourism.employee.extensions.clickableOnce
import sa.sauditourism.employee.extensions.toFlagEmoji
import sa.sauditourism.employee.managers.language.model.SupportedLanguage
import sa.sauditourism.employee.resources.AppFonts
import sa.sauditourism.employee.resources.AppColors

@Composable
fun LanguageItemComponent(
    languageItem: SupportedLanguage,
    modifier: Modifier = Modifier,
    shouldIncludeDivider: Boolean = true,
    selected: Boolean = false,
    withPadding: Boolean = true,
    onItemClick: (() -> Unit)? = null,
) {
    Column {

        Row(
            modifier = modifier
                .wrapContentHeight()
                .fillMaxWidth()
                .padding(horizontal = if (withPadding) 24.dp else 0.dp)
                .clickableOnce(onClick = { onItemClick?.invoke() }),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            if (URLUtil.isValidUrl(languageItem.flag)) {
                MediaComponent(
                    mediaContent = MediaContent(
                        GalleryType.IMAGE,
                        languageItem.flag,
                    ),
                    modifier = Modifier
                        .padding(top = 20.dp, bottom = 20.dp, end = 14.dp)
                        .size(30.dp, 20.dp)
                        .clip(RoundedCornerShape(2.dp)),
                )
            } else {
                CustomTextComponent(
                    text = languageItem.flag.toFlagEmoji(),
                    style = AppFonts.heading7SemiBold,
                    modifier = Modifier
                        .padding(top = 20.dp, bottom = 20.dp, end = 14.dp)
                )
            }
            Row(
                modifier = Modifier
                    .padding(vertical = 20.dp),
                verticalAlignment = Alignment.CenterVertically,
            ) {
                CustomTextComponent(
                    text = languageItem.value,
                    style = AppFonts.body1Medium,
                    color = Color.Black,
                    lineHeight = 24.sp,
                    modifier = Modifier
                        .weight(1f)
                        .padding(start = 2.dp, end = 16.dp)
                )

                if (selected)
                    Icon(
                        painter = painterResource(id = R.drawable.ic_check),
                        "expand",
                        tint = Color.Unspecified,
                    )
            }

        }

        if (shouldIncludeDivider) {
            HorizontalDivider(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 22.dp)
                    .height(1.dp),
                color = AppColors.ColorsPrimitivesFive
            )
        }
    }
}