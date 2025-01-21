package sa.sauditourism.employee.modules.home.annoucments
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Card
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.skydoves.landscapist.ImageOptions
import com.skydoves.landscapist.coil.CoilImageState
import sa.sauditourism.employee.R
import sa.sauditourism.employee.components.CustomTextComponent
import sa.sauditourism.employee.components.GalleryType
import sa.sauditourism.employee.components.MediaComponent
import sa.sauditourism.employee.components.MediaContent
import sa.sauditourism.employee.constants.LanguageConstants
import sa.sauditourism.employee.extensions.clickableWithoutRipple
import sa.sauditourism.employee.extensions.localizeString
import sa.sauditourism.employee.modules.home.model.Announcement
import sa.sauditourism.employee.resources.AppColors
import sa.sauditourism.employee.resources.AppFonts
import sa.sauditourism.employee.shimmer.shimmerModifier


@Composable
fun AnnouncementSectionView(
    modifier: Modifier = Modifier,
    isLoading: Boolean = false,
    announcements: Announcement = Announcement(),
    onAnnouncementClick: (Announcement) -> Unit = {}
) {
    var isCoilLoading by remember { mutableStateOf(true) }
    Card(
        modifier = modifier
            .padding(4.dp)
            .fillMaxWidth()
            .wrapContentHeight().clickableWithoutRipple {
                if(isLoading) return@clickableWithoutRipple
                onAnnouncementClick(announcements)
            },

        shape = RoundedCornerShape(28.dp)
    ) {

        Column(
            modifier = Modifier.fillMaxWidth(),
            verticalArrangement = Arrangement.Center

        ) {
            Box(
                modifier = Modifier
                    .padding(10.dp)
                    .fillMaxWidth()
                    .height(237.dp)
                    .clip(RoundedCornerShape(16.dp))
                    .background(Color.LightGray)
            ) {

                val loading: @Composable (BoxScope.(imageState: CoilImageState.Loading) -> Unit) =
                    {
                        MediaComponent(
                            mediaContent = MediaContent(
                                GalleryType.IMAGE,
                                source = R.drawable.empty_annouc
                            ),
                            modifier = Modifier
                                .shimmerModifier(isCoilLoading || isLoading)
                                .fillMaxSize(),
                        )
                    }

                MediaComponent(
                    modifier = Modifier
                        .shimmerModifier(isLoading)
                        .fillMaxSize(),
                    mediaContent = MediaContent(
                        GalleryType.IMAGE,
                        source = announcements?.bannerUrl
                    ),
                    isLoading = isLoading,
                    loading = loading,
                    placeHolder = R.drawable.empty_annouc,
                    imageOptions = ImageOptions(
                        contentScale = ContentScale.Crop,
                    )
                )
            }

            Spacer(Modifier.height(16.dp))

            CustomTextComponent(
                modifier = Modifier
                    .padding(start = 20.dp, end = 16.dp, bottom = 24.dp)
                    .fillMaxWidth()
                    .shimmerModifier(isLoading),
                text = announcements?.subject?.ifEmpty { LanguageConstants.NO_ANNOUNCEMENTS_FOUND.localizeString() },
                color = AppColors.BLACK,
                textAlign = TextAlign.Center,
                maxLines = 2,
                style = AppFonts.subtitle2Bold,
                lineHeight = 20.sp
            )
        }
    }
}


@Composable
@Preview(showBackground = true)
fun PreviewAnnouncementSectionView() {
    AnnouncementSectionView()
}