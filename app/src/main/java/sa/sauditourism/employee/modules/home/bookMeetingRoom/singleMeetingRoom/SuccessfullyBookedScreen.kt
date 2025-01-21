package sa.sauditourism.employee.modules.home.bookMeetingRoom.singleMeetingRoom

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import sa.sauditourism.employee.EmployeeApplication.Companion.sharedPreferencesManager
import sa.sauditourism.employee.R
import sa.sauditourism.employee.components.ButtonComponent
import sa.sauditourism.employee.components.ButtonSize
import sa.sauditourism.employee.components.ButtonType
import sa.sauditourism.employee.components.CustomTextComponent
import sa.sauditourism.employee.components.GalleryType
import sa.sauditourism.employee.components.MediaComponent
import sa.sauditourism.employee.components.MediaContent
import sa.sauditourism.employee.constants.LanguageConstants
import sa.sauditourism.employee.extensions.formatDateFromTo
import sa.sauditourism.employee.extensions.formatTimeFromTo
import sa.sauditourism.employee.extensions.localizeString
import sa.sauditourism.employee.modules.BottomNavItem
import sa.sauditourism.employee.modules.common.ChangeStatusBarIconsColors
import sa.sauditourism.employee.modules.login.Routes
import sa.sauditourism.employee.resources.AppColors
import sa.sauditourism.employee.resources.AppFonts

@Composable
fun SuccessfullyBookedScreen(
    title: String,
    date: String,
    startTime: String,
    endTime: String,
    floor: String,
    navController: NavController
) {

    BackHandler {
        navController.popBackStack(
            BottomNavItem.getTabs()[0].screenRoute,
            inclusive = false
        )
    }

    val local = (sharedPreferencesManager.preferredLocale.orEmpty())
    val floorTitle = if (local == "ar") {
        LanguageConstants.FLOOR.localizeString()
    } else {
        when (floor) {
            "1" -> LanguageConstants.FLOOR_1.localizeString()
            "2" -> LanguageConstants.FLOOR_2.localizeString()
            "3" -> LanguageConstants.FLOOR_3.localizeString()
            else -> LanguageConstants.TH_FLOOR.localizeString()
        }
    }

    ChangeStatusBarIconsColors(Color.Transparent)
    Box(
        modifier = Modifier.fillMaxSize()
            .navigationBarsPadding()
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(
                    horizontal = 16.dp
                )
        ) {
            Column(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxWidth()
                    .padding(horizontal = 50.dp),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                MediaComponent(
                    modifier = Modifier
                        .width(170.dp)
                        .height(212.dp),
                    mediaContent = MediaContent(
                        type = GalleryType.IMAGE,
                        source = R.mipmap.ic_successfully_booked,
                    ),
                )
                Spacer(
                    modifier = Modifier
                        .height(42.dp)
                )
                Box(
                    contentAlignment = Alignment.Center
                ) {
                    CustomTextComponent(
                        text = "$title ${LanguageConstants.MEETING_IS_BOOKED.localizeString()}",
                        style = AppFonts.heading2Bold.copy(
                            color = AppColors.ColorsAccentGreen1
                        ),
                        textAlign = TextAlign.Center
                    )
                }
                Spacer(
                    modifier = Modifier
                        .height(8.dp)
                )
                Box(
                    modifier = Modifier.height(42.dp),
                    contentAlignment = Alignment.Center
                ) {
                    CustomTextComponent(
                        textAlign = TextAlign.Center,
                        text = "${LanguageConstants.SUCCESSFULLY_BOOKED_ROOM.localizeString()} ${title} ${LanguageConstants.IN.localizeString()} $floor $floorTitle ",
                        style = AppFonts.body2Regular.copy(
                            color = AppColors.ColorTextSubdued
                        ),
                        lineHeight = 20.79.sp
                    )
                }
                Spacer(
                    modifier = Modifier
                        .height(8.dp)
                )
                Row(
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    MediaComponent(
                        modifier = Modifier.size(13.dp),
                        mediaContent = MediaContent(
                            type = GalleryType.IMAGE,
                            source = R.drawable.ic_calender
                        )
                    )
                    Spacer(
                        modifier = Modifier
                            .width(6.dp)
                    )
                    CustomTextComponent(
                        text = date.formatDateFromTo("yyyy-MM-dd", "d MMM yyyy"),
                        style = AppFonts.captionMedium.copy(
                            color = AppColors.ColorTextSubdued
                        )
                    )
                    Spacer(
                        modifier = Modifier
                            .width(20.dp)
                    )
                    MediaComponent(
                        modifier = Modifier.size(13.dp),
                        mediaContent = MediaContent(
                            type = GalleryType.IMAGE,
                            source = R.drawable.ic_clock,
                        ),
                        tint = AppColors.ColorTextSubdued

                    )
                    Spacer(
                        modifier = Modifier
                            .width(6.dp)
                    )
                    CustomTextComponent(
                        text = "${
                            startTime.formatTimeFromTo(
                                "HH:mm",
                                "hh : mm a"
                            )
                        } - ${endTime.formatTimeFromTo("HH:mm", "hh : mm a")}",
                        style = AppFonts.captionMedium.copy(
                            color = AppColors.ColorTextSubdued
                        )
                    )
                }

            }

            ButtonComponent(
                text = LanguageConstants.BACK_TO_MY_DAY.localizeString(),
                clickListener = {
                    navController.popBackStack(
                        BottomNavItem.getTabs()[0].screenRoute,
                        inclusive = false
                    )
                },
                enabled = true,
                modifier = Modifier
                    .fillMaxWidth(),
                buttonType = ButtonType.PRIMARY,
                buttonSize = ButtonSize.LARGE
            )
            Spacer(
                modifier = Modifier
                    .height(18.dp)
            )
        }
    }
}

@Preview
@Composable
fun SuccessfullyBookedScreenPrev() {
    SuccessfullyBookedScreen(
        title = "title",
        date = "2025-01-08",
        startTime = "13:45",
        endTime = "15:45",
        floor = "1",
        navController = rememberNavController()

    )


}