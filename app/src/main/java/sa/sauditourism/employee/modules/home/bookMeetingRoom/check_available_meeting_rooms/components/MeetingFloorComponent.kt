package sa.sauditourism.employee.modules.home.bookMeetingRoom.check_available_meeting_rooms.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material3.HorizontalDivider
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import sa.sauditourism.employee.EmployeeApplication.Companion.sharedPreferencesManager
import sa.sauditourism.employee.components.CustomTextComponent
import sa.sauditourism.employee.components.MediumCardComponent
import sa.sauditourism.employee.constants.LanguageConstants
import sa.sauditourism.employee.extensions.localizeString
import sa.sauditourism.employee.modules.home.bookMeetingRoom.model.available_meeting_rooms.Feature
import sa.sauditourism.employee.modules.home.bookMeetingRoom.model.available_meeting_rooms.MeetingRoom
import sa.sauditourism.employee.modules.home.bookMeetingRoom.model.available_meeting_rooms.Room
import sa.sauditourism.employee.modules.services.model.ServicesModel
import sa.sauditourism.employee.resources.AppColors
import sa.sauditourism.employee.resources.AppFonts
import sa.sauditourism.employee.shimmer.shimmerModifier

@Composable
fun MeetingFloorComponent(
    room: MeetingRoom = MeetingRoom(),
    isLoading: Boolean = false,
    date: String = "",
    startTime: String = "",
    endTime: String = "",
    onItemClick: (Room) -> Unit = {}
) {
    val local = (sharedPreferencesManager.preferredLocale ?: "en")
    val floorTitle = if (local == "ar") {
        LanguageConstants.FLOOR.localizeString()
    } else {
        when (room.floor) {
            "1" -> LanguageConstants.FLOOR_1.localizeString()
            "2" -> LanguageConstants.FLOOR_2.localizeString()
            "3" -> LanguageConstants.FLOOR_3.localizeString()
            else -> LanguageConstants.TH_FLOOR.localizeString()
        }
    }
    Column(modifier = Modifier) {
        Spacer(modifier = Modifier.height(8.dp))

        HorizontalDivider(
            color = AppColors.ColorsPrimitivesFour,
            thickness = 1.dp
        )
        Spacer(modifier = Modifier.height(24.dp))
        Row(
            modifier = Modifier
                .height(33.dp)
                .shimmerModifier(isLoading),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            CustomTextComponent(
                text = room.floor,
                style = AppFonts.heading5Bold,
                lineHeight = 33.sp
            )
            CustomTextComponent(
                text = floorTitle,
                style = AppFonts.heading5Medium,
                lineHeight = 33.sp
            )
        }
        Spacer(modifier = Modifier.height(16.dp))


        LazyColumn(
            modifier = Modifier
                .heightIn(
                    min = 0.dp,
                    max = 1000.dp
                )
                .fillMaxWidth(),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            if (isLoading) {
                items(4) {
                    MeetingRoomComponent(
                        room = Room(),
                        isLoading = isLoading,
                        date = date,
                        startTime = startTime,
                        endTime = endTime,
                        onItemClick = {

                        }
                    )
                }
            } else {
                items(room.rooms.size) {
                    MeetingRoomComponent(
                        room = room.rooms[it],
                        isLoading = isLoading,
                        date = date,
                        startTime = startTime,
                        endTime = endTime,
                        onItemClick = {
                            onItemClick.invoke(room.rooms[it])
                        }
                    )
                    if (
                        it == room.rooms.size - 1
                    ) {
                        Spacer(modifier = Modifier.height(16.dp))
                    }

                }
            }
        }


    }
}


@Preview(showBackground = true)
@Composable
fun MeetingFloorComponentPreview() {
    MeetingFloorComponent(
        room = MeetingRoom(
            floor = "1",
            rooms = listOf(
                Room(
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
                Room(
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
                Room(
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
            )
        ),
        date = "20 Oct",
        startTime = "11 : 30 AM",
        endTime = "11 : 30 AM"

    )
}