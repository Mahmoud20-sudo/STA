package sa.sauditourism.employee.modules.home.annoucments

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.skydoves.landscapist.ImageOptions
import com.skydoves.landscapist.coil.CoilImageState
import sa.sauditourism.employee.R
import sa.sauditourism.employee.components.AttachmentComponent
import sa.sauditourism.employee.components.CustomTextComponent
import sa.sauditourism.employee.components.GalleryType
import sa.sauditourism.employee.components.MediaComponent
import sa.sauditourism.employee.components.MediaContent
import sa.sauditourism.employee.constants.LanguageConstants
import sa.sauditourism.employee.extensions.localizeString
import sa.sauditourism.employee.extensions.openBrowser
import sa.sauditourism.employee.extensions.toFormattedString
import sa.sauditourism.employee.modules.home.model.Announcement
import sa.sauditourism.employee.modules.services.model.form.response.Attachment
import sa.sauditourism.employee.resources.AppColors
import sa.sauditourism.employee.resources.AppFonts
import sa.sauditourism.employee.shimmer.shimmerModifier

@Composable
fun AnnouncementFullScreen(
    onBackClick: () -> Unit = {},
    announcementItem: Announcement? = Announcement()
){


    Column(
        Modifier.fillMaxSize()
    ) {
        ContentSection(onBackClick,announcementItem)
        AttachmentSection(announcementItem)
    }

}

@Composable
fun ContentSection(onBackClick: () -> Unit,announcementItem: Announcement? , isLoading: Boolean = false,) {
    var isCoilLoading by remember { mutableStateOf(true) }
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

    Box (
        modifier = Modifier.background(Color.Black.copy(alpha = .8f)).fillMaxWidth()
    ) {

        MediaComponent(
            modifier = Modifier
                .shimmerModifier(isLoading)
                .height(520.dp).fillMaxWidth().alpha(0.3f),
            mediaContent = MediaContent(
                GalleryType.IMAGE,
                source = announcementItem?.bannerUrl
            ),
            isLoading = isLoading,
            loading = loading,
            placeHolder = R.drawable.empty_annouc,
            imageOptions = ImageOptions(
                contentScale = ContentScale.Crop,
            )
        )

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 16.dp)
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                    contentDescription = "Back",
                    tint = Color.White,
                    modifier = Modifier
                        .size(24.dp)
                        .clickable {onBackClick()}
                )
            }
        }

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .align(Alignment.BottomStart)
                .padding(16.dp)
        ) {


            CustomTextComponent(
                text =announcementItem?.subject,
                maxLines = 2,
                color = AppColors.WHITE,
                style = AppFonts.heading6Bold,
                lineHeight = 36.75.sp
            )
            Spacer(modifier = Modifier.height(8.dp))
            CustomTextComponent(
                text =announcementItem?.time?.toFormattedString("HH:mm:ssZ", "hh:mm a"),
                maxLines = 1,
                color = AppColors.WHITE,
                style = AppFonts.heading6Regular,
                lineHeight = 36.75.sp
            )

        }


    }
}


@Composable
fun AttachmentSection(announcementItem: Announcement?) {
    val context = LocalContext.current

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 32.dp, start = 32.dp, end = 32.dp)
    ) {

        AttachmentComponent(
            title = LanguageConstants.ATTACHMENTS.localizeString(),
            readOnly = true,
            defaultAttachmentsList = mapToAttachmentModel(announcementItem?.attachments ?: emptyList()),
            isOptional = false,
            attachmentListener = { _, _ -> },
            onItemClickListener = {
                context.openBrowser(
                    url = it.content,
                    headerTitle = it.fileName
                )
            }
        )
    }
}


private fun mapToAttachmentModel(attachment:List<sa.sauditourism.employee.modules.home.model.Attachment>):List<Attachment>{
    val mappedAttachment :MutableList<Attachment> = mutableListOf<Attachment>()
    attachment.forEach {
        mappedAttachment.add(
            Attachment(
                attachmentId = it.id,
                fileName = it.name,
                content = it.url,
                id = it.id,
                mimeType = it.mimeType,
                size = 0,
                date = "",
                time = "",
                thumbnail = it.url
            )
        )

    }
    return  mappedAttachment
}



@Composable
@Preview(showBackground = true)
fun PreviewAnnouncementFullScreen(){
    AnnouncementFullScreen()
}