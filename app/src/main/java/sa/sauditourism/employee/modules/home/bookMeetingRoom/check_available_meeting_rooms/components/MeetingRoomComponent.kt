package sa.sauditourism.employee.modules.home.bookMeetingRoom.check_available_meeting_rooms.components

import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.scrollable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.VerticalDivider
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.zIndex
import com.skydoves.landscapist.ImageOptions
import sa.sauditourism.employee.R
import sa.sauditourism.employee.components.CustomTextComponent
import sa.sauditourism.employee.components.GalleryType
import sa.sauditourism.employee.components.MediaComponent
import sa.sauditourism.employee.components.MediaContent
import sa.sauditourism.employee.constants.LanguageConstants
import sa.sauditourism.employee.extensions.clickableWithoutRipple
import sa.sauditourism.employee.extensions.localizeString
import sa.sauditourism.employee.modules.home.bookMeetingRoom.model.available_meeting_rooms.Feature
import sa.sauditourism.employee.modules.home.bookMeetingRoom.model.available_meeting_rooms.Room
import sa.sauditourism.employee.modules.login.Routes
import sa.sauditourism.employee.resources.AppFonts
import sa.sauditourism.employee.resources.theme.EmployeeTheme
import sa.sauditourism.employee.shimmer.shimmerModifier
import sa.sauditourism.employee.ui.theme.AppColors

@Composable
fun MeetingRoomComponent(
    isLoading: Boolean = false,
    room: Room,
    date: String = "",
    startTime: String = "",
    endTime: String = "",
    onItemClick: () -> Unit = {},
) {

    val mediaContent by remember(room.image) {
        mutableStateOf(
            MediaContent(
                GalleryType.IMAGE,
                room.image
            )
        )
    }
    Row(
        modifier = Modifier
            .height(108.dp)
            .fillMaxWidth()
            .shadow(3.dp, shape = RoundedCornerShape(16.dp))
            .zIndex(1f)
            .background(Color.White, shape = RoundedCornerShape(16.dp))
            .shimmerModifier(isLoading)
            .clickableWithoutRipple {
                onItemClick.invoke()
            }
    ) {
        MediaComponent(
            contentScale = ContentScale.FillBounds,
            mediaContent = mediaContent,
            modifier = Modifier
                .size(width = 80.dp, height = 200.dp)  // Explicitly set both dimensions
                .clip(
                    RoundedCornerShape(
                        topStart = 16.dp,
                        bottomStart = 16.dp
                    )
                )
                .shimmerModifier(isLoading),
            enabled = true,
            imageOptions = ImageOptions(
                contentScale = ContentScale.FillBounds,
                alignment = Alignment.Center,
                contentDescription = mediaContent.contentDescription
            )
        )
        Column(
            modifier = Modifier
                .weight(1f)
                .fillMaxHeight()
                .padding(16.dp),
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            Box(
                modifier = Modifier
                    .height(24.dp)
                    .fillMaxWidth(),
                contentAlignment = Alignment.Center
            ) {
                CustomTextComponent(
                    modifier = Modifier.fillMaxWidth(),
                    text = room.title,
                    style = AppFonts.subtitle1SemiBold.copy(AppColors.Black100)
                )
            }
            Spacer(modifier = Modifier.height(8.dp))
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(18.dp)
                    .padding(start = 2.dp)
                ,
                verticalAlignment = Alignment.CenterVertically
            ) {
                MediaComponent(
                    modifier = Modifier.size(13.dp),
                    mediaContent = MediaContent(
                        type = GalleryType.IMAGE,
                        source = R.drawable.ic_calender
                    )
                )
                Spacer(modifier = Modifier.width(6.dp))
                CustomTextComponent(
                    text = date,
                    style = AppFonts.capsSmallRegular.copy(
                        color = Color.Black,
                        lineHeight = 17.82.sp,
                    )
                )
                VerticalDivider(
                    modifier = Modifier
                        .padding(horizontal = 8.dp)
                        .height(10.dp)
                        .width(4.dp),
                    color = AppColors.BLACK90,
                    thickness = 1.2.dp
                )
                MediaComponent(
                    modifier = Modifier.size(13.dp),
                    mediaContent = MediaContent(
                        type = GalleryType.IMAGE,
                        source = R.drawable.ic_calender
                    )
                )
                Spacer(modifier = Modifier.width(6.dp))
                CustomTextComponent(
                    text = "$startTime - $endTime",
                    style = AppFonts.capsSmallRegular.copy(
                        color = Color.Black,
                        lineHeight = 17.82.sp,
                    )
                )
            }
            Spacer(modifier = Modifier.height(8.dp))
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(18.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                CustomTextComponent(
                    text = LanguageConstants.FLOOR.localizeString(),
                    style = AppFonts.capsSmallRegular.copy(lineHeight = 17.82.sp),
                    color = AppColors.OnPrimaryColor
                )
                CustomTextComponent(
                    modifier = Modifier.padding(start = 3.dp),
                    text = room.floor,
                    style = AppFonts.capsSmallSemiBold.copy(lineHeight = 17.82.sp),
                    color = AppColors.OnPrimaryColor
                )
                VerticalDivider(
                    modifier = Modifier
                        .padding(horizontal = 8.dp)
                        .height(10.dp)
                        .width(4.dp),
                    color = AppColors.BLACK90,
                    thickness = 1.2.dp
                )
                MediaComponent(
                    modifier = Modifier
                        .padding(end = 4.5.dp)
                        .width(15.dp)
                        .height(11.2.dp),

                    mediaContent = MediaContent(
                        type = GalleryType.IMAGE,
                        source = R.drawable.icon_people,

                        ),
                )
                CustomTextComponent(
                    text = room.capacity.toString(),
                    style = AppFonts.captionSemiBold.copy(lineHeight = 17.82.sp),
                    color = AppColors.AccentBlue1
                )
                VerticalDivider(
                    modifier = Modifier
                        .padding(horizontal = 8.dp)
                        .height(10.dp)
                        .width(4.dp),
                    color = AppColors.BLACK90,
                    thickness = 1.2.dp
                )
                Row(
                    modifier = Modifier
                        .weight(1f)
                        .horizontalScroll(rememberScrollState())
                ) {
                    room.features.forEach { feature ->
                        MediaComponent(
                            modifier = Modifier
                                .padding(end = 10.dp)
                                .size(13.dp),
                            mediaContent = MediaContent(
                                type = GalleryType.IMAGE,
                                source = feature.icon
                            )
                        )
                    }
                }

            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun MeetingRoomComponentPreview() {
    MeetingRoomComponent(
        room = Room(
            title = "Hello Title",
            floor = "1",
            capacity = 6,
            features = listOf(
                Feature(
                    name = "",
                    icon = "https://acc-employee.sta.gov.sa/media/original_images/Full_pic_of_meeting_room.png"
                )
            )
        ),
        date = "20 Oct",
        startTime = "11 : 30 AM",
        endTime = "11 : 30 AM"
    )
}