package sa.sauditourism.employee.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import sa.sauditourism.employee.R
import sa.sauditourism.employee.resources.AppFonts
import sa.sauditourism.employee.ui.theme.AppColors

data class ComingSoonModel(
    val title: String,
    val description: String? = null,
    val imageResId: Int
)


@Composable
fun ComingSoonViewComponent(
    modifier: Modifier = Modifier,
    model: ComingSoonModel,
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
                .width(200.dp)
                .height(200.dp),
        )

        Spacer(modifier = Modifier.height(32.dp))


        CustomTextComponent(
            text = model.title,
            style = AppFonts.heading0Bold,
            color = Color.Black,
            textAlign = TextAlign.Center,
            lineHeight = 54.sp,
        )

        model.description?.let {
            Spacer(modifier = Modifier.height(16.dp))

            CustomTextComponent(
                text = model.description,
                style = AppFonts.capsSmallRegular,
                color = AppColors.Black,
                textAlign = TextAlign.Center,
                lineHeight = 16.sp,
            )
        }
    }
}

@Preview
@Composable
fun ComingSoonViewComponentPreview() {
    ComingSoonViewComponent(
        model = ComingSoonModel(
            title = "No results found",
            description = "Try updating your filters for something else",
            imageResId = R.mipmap.ic_people_soon
        )
    )
}
