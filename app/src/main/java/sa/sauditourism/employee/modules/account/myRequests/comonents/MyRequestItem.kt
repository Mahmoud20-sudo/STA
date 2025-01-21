package sa.sauditourism.employee.modules.account.myRequests.comonents

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.skydoves.landscapist.ImageOptions
import sa.sauditourism.employee.EmployeeApplication
import sa.sauditourism.employee.R
import sa.sauditourism.employee.components.CustomTextComponent
import sa.sauditourism.employee.components.GalleryType
import sa.sauditourism.employee.components.MediaComponent
import sa.sauditourism.employee.components.MediaContent
import sa.sauditourism.employee.constants.LanguageConstants
import sa.sauditourism.employee.di.SharedPreferencesModule
import sa.sauditourism.employee.extensions.formatDateFromTo
import sa.sauditourism.employee.extensions.localizeString
import sa.sauditourism.employee.extensions.toFormattedString
import sa.sauditourism.employee.managers.sharedprefrences.SharedPreferencesManager
import sa.sauditourism.employee.modules.account.myRequests.model.MyRequest
import sa.sauditourism.employee.resources.AppFonts
import sa.sauditourism.employee.shimmer.shimmerModifier
import sa.sauditourism.employee.ui.theme.AppColors

@Composable
fun MyRequestItem(
    myRequest: MyRequest,
    isLoading: Boolean = false,
    onClick: () -> Unit = {}
) {
    val sharedPreferencesManager: SharedPreferencesManager =
        SharedPreferencesModule.provideSharedPreferencesManager(EmployeeApplication.instance.applicationContext)

    val mediaContent by remember {
        mutableStateOf(
            MediaContent(
                GalleryType.IMAGE,
                if (sharedPreferencesManager.preferredLocale == "ar") R.drawable.arrow_left else R.drawable.arrow_right
            )
        )
    }

    Card(
        shape = RoundedCornerShape(16.dp),
        modifier = Modifier
            .padding(top = 16.dp)
            .fillMaxWidth()
            .clickable { onClick.invoke() },
        elevation = CardDefaults.cardElevation(2.dp),
        colors = CardDefaults.cardColors(
            containerColor = Color.White,
            contentColor = Color.White
        )
    ) {

        Row(
            Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Column(
                modifier = Modifier
                    .weight(1f)
                    .padding(top = 16.dp, bottom = 20.dp, start = 12.dp)
            ) {

                Row(
                    modifier = Modifier.padding(start = 8.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {

                    MediaComponent(
                        mediaContent = MediaContent(GalleryType.IMAGE, source = myRequest.icon),
                        modifier = Modifier
                            .size(20.dp)
                            .shimmerModifier(isLoading),
                        isLoading = isLoading,
                    )

                    CustomTextComponent(
                        modifier = Modifier.padding(start = 8.dp, end = 8.dp),
                        text = myRequest.title,
                        color = sa.sauditourism.employee.resources.AppColors.BLACK,
                        style = AppFonts.ctaLarge,
                        maxLines = 2,
                        overflow = TextOverflow.Ellipsis,
                        isLoading = isLoading
                    )


              }
               Row (
                   modifier = Modifier.padding(top = 8.dp , start = 32.dp)
               ){
                   MediaComponent(
                       mediaContent = MediaContent(
                           GalleryType.IMAGE,
                           source = R.drawable.ic_clock
                       ),
                       modifier = Modifier
                           .size(16.dp)
                           .shimmerModifier(isLoading),
                       imageOptions = ImageOptions(
                           contentScale = ContentScale.Fit,
                           alignment = Alignment.TopCenter
                       ),
                   )


                   CustomTextComponent(
                       modifier = Modifier.padding(start = 4.dp , top = 2.dp),
                       text = myRequest.time.toFormattedString("HH:mm:ss.SSSZ","hh:mm a"),
                       color = sa.sauditourism.employee.resources.AppColors.BLACK,
                       style = AppFonts.capsSmallRegular,
                       isLoading = isLoading
                   )

                    Spacer(Modifier.width(8.dp))

                    MediaComponent(
                        mediaContent = MediaContent(
                            GalleryType.IMAGE,
                            source = R.drawable.ic_calender
                        ),
                        modifier = Modifier
                            .size(15.dp)
                            .shimmerModifier(isLoading),
                        imageOptions = ImageOptions(
                            contentScale = ContentScale.Fit,
                            alignment = Alignment.TopCenter
                        ),
                    )
                    CustomTextComponent(
                        modifier = Modifier.padding(start = 4.dp, top = 2.dp),
                        text = if (myRequest.date.isNotEmpty()) myRequest.date.formatDateFromTo(
                            "yyyy-MM-dd",
                            "dd MMM yyyy"
                        ) else "",
                        color = sa.sauditourism.employee.resources.AppColors.BLACK,
                        style = AppFonts.capsSmallRegular,
                        isLoading = isLoading
                    )
                }

                Row(
                    modifier = Modifier.padding(top = 8.dp, start = 29.dp)
                ) {

                    CustomTextComponent(
                        modifier = Modifier.padding(start = 5.dp, top = 3.dp),
                        text = LanguageConstants.ID.localizeString() + ": " + myRequest.id,
                        color = sa.sauditourism.employee.resources.AppColors.AccentBlue2,
                        style = AppFonts.capsSmallRegular,
                        isLoading = isLoading

                    )

                    Spacer(Modifier.width(12.dp))

                    CustomTextComponent(
                        modifier = Modifier.padding(start = 5.dp, top = 4.dp),
                        text = myRequest.currentStatus,
                        color = sa.sauditourism.employee.resources.AppColors.BLACK,
                        style = AppFonts.menuSemiBold,
                        isLoading = isLoading

                    )

                }


            }
            Spacer(Modifier.width(6.dp))

            MediaComponent(
                mediaContent,
                modifier = Modifier
                    .padding(end = 16.dp)
                    .size(24.dp)
                    .shimmerModifier(isLoading),
                imageOptions = ImageOptions(
                    contentScale = ContentScale.Fit,
                    alignment = Alignment.TopCenter,
                    colorFilter = ColorFilter.tint(
                        AppColors.Black
                    )
                ),
            )


        }


    }

}


@Composable
@Preview(showBackground = true)
fun PreviewMyRequestItem() {
    MyRequestItem(
        MyRequest(
            "123",
            "Stationery Request",
            "https://icon.com/icon2.jpg",
            "Rejected",
            "2024-10-26",
            "10:34"
        ),
        onClick = {}
    )
}