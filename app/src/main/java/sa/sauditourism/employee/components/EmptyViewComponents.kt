package sa.sauditourism.employee.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import sa.sauditourism.employee.R
import sa.sauditourism.employee.resources.AppFonts
import sa.sauditourism.employee.shimmer.shimmerModifier
import sa.sauditourism.employee.ui.theme.AppColors

data class EmptyViewModel(
    val title: String,
    val description: String? = null,
    val imageResId: Int
)


@Composable
fun EmptyViewComponent(
    modifier: Modifier = Modifier,
    model: EmptyViewModel,
    isLoading: MutableState<Boolean> = mutableStateOf(false),
    mediaHeight: Dp = 60.06997.dp,
    mediaWidth: Dp = 121.33334.dp,
    titleFont: TextStyle = AppFonts.heading4Bold,
    descriptionStyle: TextStyle = AppFonts.body2Regular
) {

    val icon by remember {
        mutableStateOf(
            MediaContent(
                GalleryType.IMAGE,
                model.imageResId
            )
        )
    }

    Column(
        modifier = modifier
            .padding(start = 24.dp, end = 24.dp)
            .fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        MediaComponent(
            mediaContent = icon,
            modifier = Modifier
                .width(mediaWidth)
                .height(mediaHeight)
                .shimmerModifier(isLoading = isLoading.value),
        )

        Spacer(modifier = Modifier.height(32.dp))

        CustomTextComponent(
            text = model.title,
            style = titleFont,
            color = Color.Black,
            textAlign = TextAlign.Center,
            lineHeight = 30.sp,
            isLoading = isLoading.value
        )
        model.description?.let {
            Spacer(modifier = Modifier.height(16.dp))

            CustomTextComponent(
                text = model.description,
                style = descriptionStyle,
                color = AppColors.ColorsPrimitivesOne,
                textAlign = TextAlign.Center,
                lineHeight = 16.sp,
                isLoading = isLoading.value
            )
        }
    }
}

@Preview
@Composable
fun preview() {
    EmptyViewComponent(
        model = EmptyViewModel(
            title = "No results found",
            description = "Try updating your filters for something else",
            imageResId = R.mipmap.ic_no_data
        )
    )
}
