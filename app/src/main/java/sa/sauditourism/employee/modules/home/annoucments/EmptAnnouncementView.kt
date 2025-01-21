package sa.sauditourism.employee.modules.home.annoucments

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Card
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.flow.flowOf
import sa.sauditourism.employee.R
import sa.sauditourism.employee.components.CustomTextComponent
import sa.sauditourism.employee.constants.LanguageConstants
import sa.sauditourism.employee.extensions.localizeString
import sa.sauditourism.employee.resources.AppColors
import sa.sauditourism.employee.resources.AppFonts
import sa.sauditourism.employee.shimmer.shimmerModifier


@Composable
fun EmptyAnnouncementView(
    isLoading: Boolean = false
) {
    Card(
        modifier = Modifier.padding(8.dp)
            .fillMaxWidth().shimmerModifier(isLoading =isLoading )
        ,
        shape = RoundedCornerShape(16.dp),

        ) {

        Column (
            modifier = Modifier.fillMaxWidth(),
            verticalArrangement = Arrangement.Center

        ) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 8.dp, start = 8.dp, end = 8.dp)
                    .height(330.dp)
                    .clip(RoundedCornerShape(16.dp))
                    .background(Color.LightGray)
            ) {

                Image(
                    painter = painterResource(id = R.drawable.empty_annouc), // Replace with your actual image resource
                    contentDescription = "Background Image",
                    contentScale = ContentScale.Crop,
                    modifier = Modifier.fillMaxSize()
                )

            }
            Spacer(Modifier.height(16.dp))


            CustomTextComponent(
                modifier = Modifier.padding(start = 16.dp, end = 16.dp , bottom = 24.dp),
                text = LanguageConstants.NO_ANNOUNCEMENTS_FOUND.localizeString(),
                color = AppColors.BLACK,
                style = AppFonts.subtitle2Bold
            )

        }


    }

}


@Composable
@Preview(showBackground = true)

fun PreviewEmptyAnnouncementView(){
    EmptyAnnouncementView(isLoading = false)
}