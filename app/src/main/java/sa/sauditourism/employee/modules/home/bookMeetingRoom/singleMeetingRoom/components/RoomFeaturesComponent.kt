package sa.sauditourism.employee.modules.home.bookMeetingRoom.singleMeetingRoom.components

import androidx.compose.foundation.background
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.text.style.TextDirection
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.zIndex
import sa.sauditourism.employee.EmployeeApplication.Companion.sharedPreferencesManager
import sa.sauditourism.employee.R
import sa.sauditourism.employee.components.CustomTextComponent
import sa.sauditourism.employee.components.GalleryType
import sa.sauditourism.employee.components.MediaComponent
import sa.sauditourism.employee.components.MediaContent
import sa.sauditourism.employee.constants.LanguageConstants
import sa.sauditourism.employee.extensions.localizeString
import sa.sauditourism.employee.modules.home.bookMeetingRoom.model.available_meeting_rooms.Feature
import sa.sauditourism.employee.ui.theme.AppColors
import sa.sauditourism.employee.ui.theme.AppFonts

@Composable
fun RowScope.RoomFeaturesComponent(
    features: List<Feature>?
) {
    Box(
        modifier = Modifier
            .weight(0.415f)
            .height(80.dp)
            .background(
                color = AppColors.White,
                shape = RoundedCornerShape(16.dp)
            )
            .shadow(2.dp, shape = RoundedCornerShape(16.dp))
            .background(color = AppColors.White)
            .clip(shape = RoundedCornerShape(16.dp))
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
                    text = LanguageConstants.ROOM_FEATURES.localizeString(),
                    style = AppFonts.heading7Medium.copy(
                        color = AppColors.ColorTextSubdued
                    )
                )
            }
            Row(
                modifier = Modifier
                    .weight(1f)
                    .horizontalScroll(rememberScrollState()),
                verticalAlignment = Alignment.Bottom,
                horizontalArrangement = Arrangement.spacedBy(10.dp),

                ) {
                features?.forEach { feature ->
                    Box(modifier = Modifier) {
                        Column(
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            MediaComponent(
                                modifier = Modifier
                                    .size(16.dp),
                                mediaContent = MediaContent(
                                    GalleryType.IMAGE,
                                    feature.icon
                                )
                            )
                            Box(
                                modifier = Modifier.height(13.dp),
                                contentAlignment = Alignment.Center
                            ) {
                                CustomTextComponent(
                                    text = feature.name,
                                    style = AppFonts.heading7Regular.copy(
                                        fontSize = 9.sp,
                                        color = AppColors.ColorTextSubdued
                                    )
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}