package sa.sauditourism.employee.modules.home.bookMeetingRoom.check_available_meeting_rooms

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListScope
import androidx.compose.foundation.lazy.grid.LazyGridScope
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.grid.itemsIndexed
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.Text
import androidx.compose.material.pullrefresh.PullRefreshIndicator
import androidx.compose.material.pullrefresh.pullRefresh
import androidx.compose.material.pullrefresh.rememberPullRefreshState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.zIndex
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import sa.sauditourism.employee.R
import sa.sauditourism.employee.components.CustomTextComponent
import sa.sauditourism.employee.components.DetailsHeaderComponent
import sa.sauditourism.employee.components.EmptyViewComponent
import sa.sauditourism.employee.components.EmptyViewModel
import sa.sauditourism.employee.components.HeaderModel
import sa.sauditourism.employee.components.MediumCardComponent
import sa.sauditourism.employee.constants.LanguageConstants
import sa.sauditourism.employee.extensions.clickableWithoutRipple
import sa.sauditourism.employee.extensions.formatDateFromTo
import sa.sauditourism.employee.extensions.formatTimeFromTo
import sa.sauditourism.employee.extensions.localizeString
import sa.sauditourism.employee.managers.network.models.UiState
import sa.sauditourism.employee.modules.account.PaySlipRow
import sa.sauditourism.employee.modules.home.bookMeetingRoom.check_available_meeting_rooms.components.MeetingFloorComponent
import sa.sauditourism.employee.modules.home.bookMeetingRoom.model.available_meeting_rooms.MeetingRoom
import sa.sauditourism.employee.modules.common.ChangeStatusBarIconsColors
import sa.sauditourism.employee.modules.login.Routes
import sa.sauditourism.employee.modules.services.model.ServicesModel
import sa.sauditourism.employee.resources.AppColors
import sa.sauditourism.employee.resources.AppFonts
import sa.sauditourism.employee.ui.theme.AppFonts.captionRegular
import sa.sauditourism.employee.ui.theme.AppFonts.heading2Bold


@OptIn(ExperimentalMaterialApi::class)
@Composable
fun CheckAvailableMeetingRoomsScreen(
    viewModel: CheckAvailableMeetingRoomsViewModel = hiltViewModel(),
    navController: NavController,
    date: String,
    startTime: String,
    endTime: String,
) {
    var isRefreshing by remember { mutableStateOf(false) }
    var isLoading by remember { mutableStateOf(false) }
    var searchQuery by remember { mutableStateOf("") }
    val meetingsFlow by viewModel.meetingRoomsFlow.collectAsState()
    val meetings = remember { mutableStateListOf<MeetingRoom>() }

    val pullRefreshState = rememberPullRefreshState(
        refreshing = isRefreshing,
        onRefresh = {
            isRefreshing = false
            isLoading = true
            viewModel.getAvailableMeetingRoom(
                searchQuery
            )
//            searchQuery = ""
        }
    )

    LaunchedEffect(meetingsFlow) {
        isLoading = meetingsFlow is UiState.Loading
        if (meetingsFlow is UiState.Success) {
            meetingsFlow.data?.meetingRooms?.let {
                meetings.clear()
                meetings.addAll(it)
            }
        }
    }

    LaunchedEffect(searchQuery) {
        if (searchQuery.isNotEmpty()) {
            viewModel.filterMeetings(searchQuery)
        } else {
            viewModel.clearFilter() // Reset filter when search query is cleared
        }
    }

    val scrollState = rememberScrollState()
    val showOverlay by remember {
        derivedStateOf {
            scrollState.value == 0
        }
    }

    ChangeStatusBarIconsColors(Color.Transparent)
    Box(
        modifier = Modifier
            .fillMaxSize()
            .navigationBarsPadding()

    ) {
        Column(
            Modifier
                .pullRefresh(pullRefreshState),
            horizontalAlignment = Alignment.Start,
            verticalArrangement = Arrangement.Top
        ) {
            val headerModel = HeaderModel(
                title = LanguageConstants.AVAILABLITY_TITLE.localizeString(),
                subTitle = "",
                showBackIcon = true,
                showSearch = true,
                searchValue = searchQuery,
            )
            DetailsHeaderComponent(
                modifier = Modifier.zIndex(5f),
                headerModel = headerModel,
                isStandAlone = true,
                backgroundColor = Color.Transparent,
                titleMaxLines = 1,
                backHandler = {
                    navController.popBackStack()
                    true
                },

                searchPlaceHolder = LanguageConstants.SEARCH_FOR_MEETING_ROOMS.localizeString(),
                showOverlay = true,
                onSearchValueChange = {
                    searchQuery = it
                    viewModel.filterMeetings(searchQuery)
                },
                headerBottomComponent = {
                    val count = meetings.sumOf { it.rooms.size }
                    CustomTextComponent(
                        modifier = Modifier.fillMaxWidth().padding(bottom = 12.dp),
                        text = "${if (count == 0) "0" else if (count < 10) "0${count}" else "$count"} ${LanguageConstants.AVAILABILITY_SUBTITLE.localizeString()}",
                        textAlign = TextAlign.Center,
                        style = AppFonts.heading7Regular,
                        color = AppColors.ColorsPrimitivesTwo
                    )
                },
            )

            Column(
                modifier = Modifier
                    .weight(1f)
                    .padding(horizontal = 16.dp)
                    .verticalScroll(scrollState)
                    .imePadding()
                ,
            ) {
                if (isLoading) {
                    (0..7).onEach {
                        MeetingFloorComponent(isLoading = true)
                        Spacer(modifier = Modifier.height(16.dp))
                    }
                    return
                }
                meetings.forEach { meetingRoom ->
                    MeetingFloorComponent(
                        room = meetingRoom,
                        date = date.formatDateFromTo("yyyy-MM-dd", "d MMM"),
                        startTime = startTime.formatTimeFromTo("HH:mm", "hh : mm a"),
                        endTime = endTime.formatTimeFromTo("HH:mm", "hh : mm a"),
                        onItemClick = {room->
                            navController.currentBackStackEntry?.savedStateHandle?.let {
                                it["room"] = room
                            }
                            navController.navigate(
                                "${Routes.SINGLE_MEETING_ROOM}/${startTime}/${endTime}/${date}"
                            )
                        }
                    )
                }
            }
        }

        PullRefreshIndicator(
            refreshing = isRefreshing,
            state = pullRefreshState,
            modifier = Modifier.align(Alignment.TopCenter),
            backgroundColor = Color.White,
        )


        if (meetingsFlow is UiState.Success && meetings.isEmpty() ||meetingsFlow is UiState.Error) {
            EmptyViewComponent(
                model = EmptyViewModel(
                    title = LanguageConstants.SORRY.localizeString(),
                    description = LanguageConstants.NO_ROOMS_AVAILABLE.localizeString(),
                    imageResId = R.mipmap.ic_no_rooms_available,
                ),
                mediaWidth = 218.dp,
                mediaHeight = 179.dp,
                titleFont = heading2Bold,
                descriptionStyle = captionRegular
            )
        }

//        if (meetingsFlow is UiState.Error) {
//            viewModel.HandleError(
//                error = meetingsFlow.networkError,
//                isDataEmpty = meetingsFlow.data?.meetingRooms.isNullOrEmpty(),
//                showBack = false,
//                withTab = true,
//                dismissCallback = {
//                    navController.popBackStack()
//                },
//            ) {
//                viewModel.getAvailableMeetingRoom()
//            }
//        }
    }
}

