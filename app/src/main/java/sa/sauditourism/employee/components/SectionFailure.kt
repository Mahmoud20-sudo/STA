package sa.sauditourism.employee.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import sa.sauditourism.employee.LocalLanguage
import sa.sauditourism.employee.R
import sa.sauditourism.employee.constants.AnalyticsConstants
import sa.sauditourism.employee.constants.LanguageConstants
import sa.sauditourism.employee.resources.AppColors
import sa.sauditourism.employee.resources.AppFonts

@Composable
fun SectionFailure(
    type: DialogType,
    showLoading: MutableState<Boolean>,
    modifier: Modifier = Modifier,
    imageResId: Int? = null,
    title: String? = null,
    description: String? = null,
    descriptionTopSpacing: Dp = 8.dp,
    usePlatformDefaultWidth: Boolean = true,
    buttonText: String? = null,
    buttonClick: () -> Unit = {},
    cardElevation: Dp = 10.dp,
    cardColor: Color? = Color.White,
) {
    val colors = if (cardColor == null) {
        CardDefaults.cardColors()
    } else {
        CardDefaults.cardColors(Color.White)
    }
    val cardModifier = if (usePlatformDefaultWidth) {
        modifier.fillMaxWidth()
    } else {
        val cardWidth = LocalConfiguration.current.screenWidthDp - 60
        modifier.width(cardWidth.dp)
    }

    Box(
        modifier = Modifier
            .background(Color.Transparent)
            .padding(all = 24.dp),
    ) {
        Card(
            modifier = cardModifier,
            elevation =
            CardDefaults.cardElevation(
                defaultElevation = cardElevation,
            ),
            colors = colors,
        ) {
            Column(
                modifier = Modifier.padding(
                    start = 24.dp,
                    end = 24.dp,
                    bottom = 24.dp,
                    top = 16.dp,
                ),
                horizontalAlignment = Alignment.CenterHorizontally,
            ) {
                imageResId?.let { image ->
                    Image(
                        painter = painterResource(id = image),
                        contentDescription = null,
                        colorFilter = ColorFilter.tint(MaterialTheme.colorScheme.secondary),
                        modifier = Modifier
                            .size(64.dp),
                    )
                }

                Spacer(modifier = Modifier.height(32.dp))

                if (title != null) {
                    CustomTextComponent(
                        text = title,
                        style = AppFonts.heading4Bold,
                        color = Color.Black,
                        textAlign = TextAlign.Center,
                        lineHeight = 24.sp,
                    )
                }

                if (description != null) {
                    Spacer(modifier = Modifier.height(descriptionTopSpacing))
                    CustomTextComponent(
                        text = description,
                        style = AppFonts.body2Regular,
                        color = AppColors.ColorsPrimitivesOne,
                        textAlign = TextAlign.Center,
                        lineHeight = 14.sp,
                    )
                }

                if (buttonText != null) {
                    Spacer(modifier = Modifier.height(32.dp))

                    ButtonComponent(
                        text = buttonText,
                        clickListener = {
                            val analyticsData = HashMap<String, String>()
                            analyticsData[AnalyticsConstants.EventParams.EVENT_CATEGORY] =
                                "Other Features"
                            buttonClick.invoke()
                        },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(48.dp),
                        buttonType = when (type) {
                            DialogType.Normal -> ButtonType.SECONDARY
                            DialogType.Success -> ButtonType.SUCCESS_SECONDARY
                            DialogType.Failed -> ButtonType.FAILED_SECONDARY
                        },
                        buttonSize = ButtonSize.LARGE,
                        enabled = true,
                        showLoading = showLoading.value,
                    )
                }
            }
        }
    }
}

@Preview(
    showBackground = false,
)
@Composable
fun SectionFailurePreview() {
    CompositionLocalProvider(LocalLanguage provides LanguageConstants.DEFAULT_LOCALE) {
        SectionFailure(
            type = DialogType.Normal,
            showLoading = remember { mutableStateOf(false) },
            imageResId = R.drawable.ic_information_purple,
            title = "Something went wrong",
            description = "Lorem Ipsum is simply dummy text of the printing and typesetting industry.",
            buttonText = "TEST",
            buttonClick = {
            },
        )
    }
}
