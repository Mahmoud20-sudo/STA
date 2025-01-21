package sa.sauditourism.employee.modules.home.myDayMeetings

import androidx.compose.foundation.background
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
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.zIndex
import sa.sauditourism.employee.components.CustomTextComponent
import sa.sauditourism.employee.modules.home.myDayMeetings.model.MeetingItem
import sa.sauditourism.employee.modules.home.myDayMeetings.model.MyMeetingsModel
import sa.sauditourism.employee.resources.AppColors
import sa.sauditourism.employee.resources.AppFonts
import sa.sauditourism.employee.shimmer.shimmerModifier

@Composable
fun MyMeetingsSection(
    modifier: Modifier = Modifier,
    model: MyMeetingsModel?,
    isLoading: Boolean = false,
) {
    Row(
        modifier = modifier
    ) {
        // date
        if (!isLoading) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier
                    .height(55.dp)
                    .zIndex(1f)
                    .background(Color.White, shape = RoundedCornerShape(12.dp))
                    .padding(all = 4.dp)
            ) {
                Box(
                    modifier = Modifier
                        .padding(top = 3.dp)
                        .background(
                            AppColors.OnPrimaryDark,
                            shape = RoundedCornerShape(12.dp)
                        )
                        .padding(horizontal = 8.dp)
                        .padding(vertical = 2.dp)
                ) {
                    CustomTextComponent(
                        text = model?.month,
                        color = AppColors.WHITE,
                        style = AppFonts.labelSemiBold
                    )
                }
                CustomTextComponent(
                    modifier = Modifier.padding(top = 8.dp),
                    text = model?.day,
                    color = AppColors.BLACK90,
                    style = AppFonts.body2Medium
                )

            }
        } else {
            Box(
                modifier = Modifier
                    .height(55.dp)
                    .width(40.dp)
                    .shimmerModifier(true)
            )
        }
        Spacer(Modifier.width(8.dp))

        // items
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .horizontalScroll(rememberScrollState())
        ) {
            if (isLoading) {
                (0..4).onEachIndexed { index, _ ->
                    MyMeetingItem(model = MeetingItem(), isLoading = true)
                    Spacer(modifier = Modifier.width(if (index == model?.meetingsList?.lastIndex) 24.dp else 8.dp))
                }
            } else
                model?.meetingsList?.onEachIndexed { index, item ->
                    MyMeetingItem(item)
                    Spacer(modifier = Modifier.width(if (index == model.meetingsList.lastIndex) 24.dp else 8.dp))
                }
        }
    }
}

@Composable
@Preview
fun MyMeetingsSectionPreview() {
    MyMeetingsSection(
        model = MyMeetingsModel(
            month = "Dec",
            day = "19",
            meetingsList = listOf(
                MeetingItem(
                    title = "UI/UX Team Huddles 1",
                    time = "11:00 AM - 12 PM"
                ),
                MeetingItem(
                    title = "UI/UX Team Huddles 2 test test test",
                    time = "11:00 AM - 12 PM"
                ),
                MeetingItem(
                    title = "UI/UX Team Huddles 3",
                    time = "11:00 AM - 12 PM"
                ),
                MeetingItem(
                    title = "UI/UX Team Huddles 4",
                    time = "11:00 AM - 12 PM"
                )
            )
        )
    )
}

@Composable
@Preview
fun MyMeetingsSectionNoItemsPreview() {
    MyMeetingsSection(
        model = MyMeetingsModel(
            month = "Dec",
            day = "19",
            meetingsList = listOf()
        )
    )
}

@Composable
fun MyMeetingItem(
    model: MeetingItem,
    isLoading: Boolean = false,
) {
    Row(
        modifier = Modifier
            .shimmerModifier(isLoading)
            .width(187.dp)
            .height(55.dp)
            .background(Color.White, shape = RoundedCornerShape(8.dp))
            .padding(8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(
            modifier = Modifier
                .width(2.dp)
                .height(39.dp)
                .background(
                    AppColors.OnSecondaryLight,
                    shape = RoundedCornerShape(16.dp)
                )
        )
        Column(
            modifier = Modifier
                .weight(1f)
                .fillMaxHeight()
                .padding(start = 8.dp)
        ) {
            CustomTextComponent(
                modifier = Modifier.padding(top = 2.dp),
                text = model.title,
                color = AppColors.BLACK,
                style = AppFonts.body2Medium,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
            Spacer(modifier = Modifier.weight(1f))
            CustomTextComponent(
                modifier = Modifier.padding(bottom = 2.dp),
                text = model.time,
                color = AppColors.ColorBorders,
                style = AppFonts.labelSemiBold,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
        }
    }
}
