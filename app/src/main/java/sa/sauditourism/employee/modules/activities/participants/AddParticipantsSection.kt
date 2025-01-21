package sa.sauditourism.employee.modules.activities.participants

import android.graphics.BitmapFactory
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape

import androidx.compose.material3.Icon

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.imageResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.zIndex
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
import sa.sauditourism.employee.modules.services.model.participants.Participant
import sa.sauditourism.employee.resources.AppColors
import sa.sauditourism.employee.resources.AppColors.BLACK
import sa.sauditourism.employee.resources.AppFonts
import sa.sauditourism.employee.shimmer.shimmerModifier
import sa.sauditourism.employee.ui.theme.AppColors.White


@Composable
fun ParticipantSection(
    modifier: Modifier = Modifier,
    participants: List<Participant>? = emptyList(),
    onClick: () -> Unit = {}
) {
    var isCoilLoading by remember { mutableStateOf(true) }

    Row(
        modifier = modifier
            .fillMaxWidth()
            .background(Color.White, RoundedCornerShape(8.dp))
            .clip(RoundedCornerShape(8.dp))
            .padding(8.dp)
            .clickableWithoutRipple { onClick() },
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(
            modifier = Modifier
                .height(24.dp)
                .width(
                    if (participants.isNullOrEmpty()) 24.dp else (24 + (participants.size.coerceAtMost(
                        3
                    ) - 1) * 16).dp
                ),
            contentAlignment = Alignment.CenterStart
        ) {
            participants?.take(3)?.forEachIndexed { index, participant ->
                Box(
                    modifier = Modifier
                        .offset(x = (index * 12).dp)
                        .zIndex(index.toFloat())
                ) {
                    MediaComponent(
                        mediaContent = MediaContent(
                            GalleryType.IMAGE,
                            source = participant.userImage
                        ),
                        modifier = Modifier
                            .size(24.dp)
                            .clip(CircleShape),
                        placeHolder = R.drawable.ic_person_avatar,
                        showPlaceHolder = true,
                        imageOptions = ImageOptions(
                            contentScale = ContentScale.Crop,
                            alignment = Alignment.Center,
                            contentDescription = "Profile image ${index + 1}"
                        ),
                        onImageStateChanged = {
                            if (it is CoilImageState.Success || it is CoilImageState.Failure) {
                                isCoilLoading = false
                            }
                        }
                    )
                }
            }

            // Remaining count indicator
            if ((participants?.size ?: 0) > 3) {
                val nextParticipant = participants!![3]
                Box(
                    modifier = Modifier
                        .offset(x = (2.3 * 16).dp)
                        .size(24.dp)
                        .clip(CircleShape)
                        .background(Color.Black.copy(alpha = 0.3f), CircleShape)
                        .zIndex(50f),
                    contentAlignment = Alignment.Center
                ) {
                    MediaComponent(
                        mediaContent = MediaContent(
                            GalleryType.IMAGE,
                            source = nextParticipant.userImage
                        ),
                        modifier = Modifier
                            .size(24.dp)
                            .clip(CircleShape)
                            .shadow(4.dp, CircleShape),
                        placeHolder = R.drawable.ic_person_avatar,
                        showPlaceHolder = true,
                        imageOptions = ImageOptions(
                            contentScale = ContentScale.Crop,
                            alignment = Alignment.Center,
                            contentDescription = "Next participant image"
                        ),
                        onImageStateChanged = {
                            if (it is CoilImageState.Success || it is CoilImageState.Failure) {
                                isCoilLoading = false
                            }
                        }
                    )
                    if (participants.size>4)
                    Box(
                        modifier = Modifier
                            .matchParentSize()
                            .background(Color.Black.copy(alpha = 0.3f), CircleShape),
                        contentAlignment = Alignment.Center
                    ) {
                        CustomTextComponent(
                            text = "+4",
                            color = Color.White,
                            style = AppFonts.labelSemiBold,
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis,
                            isLoading = false
                        )
                    }
                }
            }

        }

        Spacer(modifier = Modifier.width(8.dp))

        CustomTextComponent(
            textAlign = TextAlign.Start,
            text = LanguageConstants.PARTICIPANTS.localizeString(),
            color = BLACK,
            style = AppFonts.body2Medium,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis,
            isLoading = false
        )

        Spacer(Modifier.weight(1f))

        Icon(
            painter = painterResource(R.drawable.arrow_right),
            tint = AppColors.OnPrimaryDark,
            contentDescription = "Go to Participants"
        )
    }
}



@Composable
fun AddParticipantSection(
    modifier: Modifier = Modifier,
    paddingTop: Dp? = 24.dp,
    onClick: () -> Unit
) {
    Row(
        modifier = modifier
            .padding(top = paddingTop ?: 24.dp)
            .fillMaxWidth()
            .height(40.dp)
            .background(Color.White, RoundedCornerShape(8.dp))
            .clip(RoundedCornerShape(8.dp))
            .padding(8.dp)
            .clickableWithoutRipple { onClick() }
            .zIndex(100f),
        verticalAlignment = Alignment.CenterVertically
    ) {
        CustomTextComponent(
            textAlign = TextAlign.Start,
            text = LanguageConstants.ADD_NEW_PARTICIPANT.localizeString(),
            color = BLACK,
            style = AppFonts.body2Medium,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis,
            isLoading = false
        )
        Spacer(Modifier.weight(1f))
        Icon(
            painter = painterResource(R.drawable.ic_add_icon),
            tint = AppColors.OnPrimaryDark,
            contentDescription = "Go to Participants",
        )
    }

}

@Composable
@Preview(showBackground = true)
fun PreviewAddParticipantSectionNoData() {
    AddParticipantSection(
    ) {

    }
}

@Composable
@Preview(showBackground = true)
fun PreviewAddParticipantSection() {
//    AddParticipantSection(
//        participantImages = listOf(
//            ImageBitmap.imageResource(R.drawable.ic_avatar),
//            ImageBitmap.imageResource(R.drawable.female_avatar),
//            ImageBitmap.imageResource(R.drawable.male_avatar),
//            ImageBitmap.imageResource(R.drawable.male_avatar),
//            ImageBitmap.imageResource(R.drawable.male_avatar),
//            ImageBitmap.imageResource(R.drawable.male_avatar_rect)
//        ),
//        extraCount = 3,
//        onClick = {}
//    )
}