package sa.sauditourism.employee.modules.home.bookMeetingRoom.singleMeetingRoom.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.zIndex
import sa.sauditourism.employee.R
import sa.sauditourism.employee.components.CustomTextComponent
import sa.sauditourism.employee.components.GalleryType
import sa.sauditourism.employee.components.MediaComponent
import sa.sauditourism.employee.components.MediaContent
import sa.sauditourism.employee.constants.LanguageConstants
import sa.sauditourism.employee.extensions.localizeString
import sa.sauditourism.employee.ui.theme.AppColors
import sa.sauditourism.employee.ui.theme.AppFonts

@Composable
fun RowScope.RoomCapacityComponent(
    capacity: Int? = 0
) {
    Box(
        modifier = Modifier
            .shadow(2.dp, shape = RoundedCornerShape(10.dp))
            .weight(0.353f)
            .width(105.dp)
            .height(80.dp)
            .background(color = AppColors.White)
            .clip(shape = RoundedCornerShape(10.dp))
            .zIndex(1f)
            .padding(
                top = 8.dp,
                bottom = 12.dp,
                start = 8.dp,
                end = 8.dp
            )
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(27.dp),
                contentAlignment = Alignment.Center
            ) {
                CustomTextComponent(
                    text = LanguageConstants.ROOM_CAPACITY.localizeString(),
                    style = AppFonts.heading7Medium.copy(
                        color = AppColors.ColorTextSubdued
                    )
                )
            }
            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
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
                    text = capacity.toString(),
                    style = AppFonts.captionMedium,
                    color = AppColors.AccentBlue1
                )
            }
        }
    }
}

@Preview
@Composable
fun RoomCapacityComponentPreview() {
    Row {
        RoomCapacityComponent()
    }
}