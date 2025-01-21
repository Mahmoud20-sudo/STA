package sa.sauditourism.employee.modules.login

import android.annotation.SuppressLint
import android.net.Uri
import android.util.Log
import androidx.compose.animation.AnimatedContentTransitionScope
import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.toMutableStateList
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.google.gson.Gson
import kotlinx.serialization.json.Json

import sa.sauditourism.employee.LocalNavController
import sa.sauditourism.employee.MainViewModel
import sa.sauditourism.employee.R
import sa.sauditourism.employee.components.CustomAlertDialog
import sa.sauditourism.employee.components.DateTimePickerComponent
import sa.sauditourism.employee.components.DialogType
import sa.sauditourism.employee.components.EmptyViewComponent
import sa.sauditourism.employee.components.EmptyViewModel
import sa.sauditourism.employee.components.RadioButtonTest
import sa.sauditourism.employee.components.SectionFailure
import sa.sauditourism.employee.constants.LanguageConstants
import sa.sauditourism.employee.extensions.localizeString
import sa.sauditourism.employee.managers.language.LanguageManager
import sa.sauditourism.employee.modules.BottomNavItem
import sa.sauditourism.employee.modules.CheckboxTest
import sa.sauditourism.employee.modules.DateComponentTest
import sa.sauditourism.employee.modules.HandleErrorTest
import sa.sauditourism.employee.modules.NewComponents
import sa.sauditourism.employee.modules.TestAttachment
import sa.sauditourism.employee.modules.TestButtons
import sa.sauditourism.employee.modules.TestChangeLanguage
import sa.sauditourism.employee.modules.TestMultiDropDowns
import sa.sauditourism.employee.modules.TestMultiSelection
import sa.sauditourism.employee.modules.TestPopups
import sa.sauditourism.employee.modules.TestShimmer
import sa.sauditourism.employee.modules.TestSingleSelection
import sa.sauditourism.employee.modules.TestToasters
import sa.sauditourism.employee.modules.TextAppUpdate
import sa.sauditourism.employee.modules.TimePickerComponentTest
import sa.sauditourism.employee.modules.account.DigitalCardScreen
import sa.sauditourism.employee.modules.account.AccountScreen
import sa.sauditourism.employee.modules.account.AccountViewModel
import sa.sauditourism.employee.modules.account.PaySlipScreen
import sa.sauditourism.employee.modules.account.SettingsScreen
import sa.sauditourism.employee.modules.activities.ActivitiesScreen
import sa.sauditourism.employee.modules.home.HomeScreen
import sa.sauditourism.employee.modules.login.Routes.ACTIVITIES_SCREEN
import sa.sauditourism.employee.modules.login.Routes.ATTACHMENT_COMPONENT
import sa.sauditourism.employee.modules.login.Routes.DIGITAL_CARD
import sa.sauditourism.employee.modules.login.Routes.LANGUAGE_CHANGER
import sa.sauditourism.employee.modules.login.Routes.MULTI_DROPDOWNS
import sa.sauditourism.employee.modules.login.Routes.MULTI_SELECTION
import sa.sauditourism.employee.modules.login.Routes.MY_REQUESTS_SCREEN
import sa.sauditourism.employee.modules.login.Routes.SETTINGS_SCREEN
import sa.sauditourism.employee.modules.login.Routes.SINGLE_SELECTION
import sa.sauditourism.employee.modules.maintenance.MaintenancePage
import sa.sauditourism.employee.modules.people.PeopleScreen
import sa.sauditourism.employee.modules.services.form.RequestFormScreen
import sa.sauditourism.employee.modules.services.requeststypes.RequestsTypesScreen
import sa.sauditourism.employee.modules.services.ServicesScreen
import sa.sauditourism.employee.modules.services.details.RequestDetailsScreen
import sa.sauditourism.employee.modules.account.myRequests.MyRequestsScreen
import sa.sauditourism.employee.modules.activities.participants.ParticipantsScreen
import sa.sauditourism.employee.modules.common.ChangeStatusBarIconsColors
import sa.sauditourism.employee.modules.login.Routes.PARTICIPANTS_SCREEN
import sa.sauditourism.employee.modules.login.Routes.PAYSLIP_SCREEN
import sa.sauditourism.employee.modules.login.Routes.REQUESTS_DETAILS
import sa.sauditourism.employee.modules.login.Routes.ANNOUNCEMENTS_SCREEN
import sa.sauditourism.employee.modules.services.details.RequestDetailsViewModel
import sa.sauditourism.employee.modules.services.model.details.Status
import sa.sauditourism.employee.modules.services.model.form.response.Attachment
import sa.sauditourism.employee.modules.home.annoucments.AnnouncementFullScreen
import sa.sauditourism.employee.modules.home.model.Announcement
import sa.sauditourism.employee.modules.login.Routes.BOOK_MEETING_ROOM
import sa.sauditourism.employee.modules.home.bookMeetingRoom.check_available_meeting_rooms.CheckAvailableMeetingRoomsScreen
import sa.sauditourism.employee.modules.home.bookMeetingRoom.model.available_meeting_rooms.Room
import sa.sauditourism.employee.modules.home.bookMeetingRoom.singleMeetingRoom.SingleMeetingRoomScreen
import sa.sauditourism.employee.modules.home.bookMeetingRoom.check_available_meeting_rooms.CheckAvailableMeetingRoomsScreen

import sa.sauditourism.employee.modules.login.Routes.LOCAL_PARTICIPANTS_SCREEN
import java.net.URLDecoder
import java.net.URLEncoder
import java.nio.charset.StandardCharsets
import sa.sauditourism.employee.modules.home.bookMeetingRoom.BookMeetingRoomScreen
import sa.sauditourism.employee.modules.home.bookMeetingRoom.BookMeetingRoomScreen
import sa.sauditourism.employee.modules.home.bookMeetingRoom.singleMeetingRoom.LocalParticipantsScreen
import sa.sauditourism.employee.modules.home.bookMeetingRoom.singleMeetingRoom.SingleMeetingRoomViewModel
import sa.sauditourism.employee.modules.home.bookMeetingRoom.singleMeetingRoom.SuccessfullyBookedScreen
import sa.sauditourism.employee.modules.login.Routes.SUCCESSFULLY_BOOKED_SCREEN

@SuppressLint("UnrememberedMutableState")
@Composable
fun NavigationGraph(
    navController: NavHostController,
    modifier: Modifier = Modifier,
    mainViewModel: MainViewModel,
    doLogout: () -> Unit
) {
    val tabList = remember {
        mutableStateOf(BottomNavItem.getTabs())
    }

    //todo move this to settings viewmodel
    val originalModels by remember(LanguageManager.supportedLanguages) {
        mutableStateOf(
            LanguageManager.supportedLanguages
        )
    }

    NavHost(
        navController,
//        startDestination = BOOK_MEETING_ROOM,
        startDestination = tabList.value[0].screenRoute,
        modifier = modifier,
        enterTransition = {
            slideIntoContainer(
                towards = AnimatedContentTransitionScope.SlideDirection.Companion.Start,
                animationSpec = tween(400),
            )
        },
        exitTransition = {
            slideOutOfContainer(
                towards = AnimatedContentTransitionScope.SlideDirection.Companion.Start,
                animationSpec = tween(400),
            )
        },
        popEnterTransition = {
            slideIntoContainer(
                towards = AnimatedContentTransitionScope.SlideDirection.Companion.End,
                animationSpec = tween(400),
            )
        },
        popExitTransition = {
            slideOutOfContainer(
                towards = AnimatedContentTransitionScope.SlideDirection.Companion.End,
                animationSpec = tween(400),
            )
        },

        ) {
        tabList.value.forEach { navItem ->
            composable(
                route = navItem.screenRoute,
            ) {
                val tabId = navItem.screenRoute
                val navHostController = LocalNavController.current
//                val backStackEntry =
//                    if (navHostController.currentBackStackEntry?.destination == null) {
//                        null
//                    } else {
//                        remember(it) {
//                            navHostController.getBackStackEntry(tabId)
//                        }
//                    }


            }
            composable(tabList.value[0].screenRoute) {

                HomeScreen(navController, mainViewModel = mainViewModel) { annoucmentItem ->
                    navController.currentBackStackEntry?.savedStateHandle?.let {
                        it["annoucmentItem"] = annoucmentItem
                    }
                    navController.navigate(ANNOUNCEMENTS_SCREEN)
                }

            }
            composable(route = tabList.value[1].screenRoute) { ServicesScreen(navController) }
            composable(tabList.value[2].screenRoute) { PeopleScreen(navController) }
            composable(tabList.value[3].screenRoute) {
                AccountScreen(
                    onLogout = doLogout,
                    onSettingsClick = { navController.navigate(SETTINGS_SCREEN) },
                    onMyRequestClick = { id ->
                        navController.navigate(MY_REQUESTS_SCREEN + "/$id")
                    },
                    onPaySlipClick = {
                        navController.navigate(PAYSLIP_SCREEN)
                    },
                    onDigitalCardClick = { accountDetails ->
                        navController.navigate(DIGITAL_CARD)
                    }
                )
            }
            composable("Buttons") { TestButtons() }
            composable("Toasters") { TestToasters() }
            composable("Popups") { TestPopups() }
            composable("Shimmer") { TestShimmer() }
            composable("new_components") { NewComponents() }
            composable("new_components") { NewComponents() }
            composable("Date_picker") { DateComponentTest() }
            composable("time_picker") { TimePickerComponentTest(navController) }
            composable("date_time_picker") { DateTimePickerComponent() }
            composable("Warning_Page") { HandleErrorTest(navController) }
            composable("check_box") { CheckboxTest() }
            composable("app_update_popup") { TextAppUpdate(navController) }
            composable("security_popup") {
                CustomAlertDialog(
                    type = DialogType.Failed,
                    imageResId = R.drawable.ic_attention,
                    title = LanguageConstants.SECURITY_POPUP_TITLE.localizeString(),
                    description = LanguageConstants.SECURITY_POPUP_DESCRIPTION.localizeString(),
                    shouldShowCloseIcon = false
                )
            }
            composable("radio_button") { RadioButtonTest() }
            composable("maintainance") { MaintenancePage() }
            composable("section_failure") {
                Box(modifier = Modifier.fillMaxSize()) {
                    SectionFailure(
                        type = DialogType.Normal,
                        showLoading = remember { mutableStateOf(false) },
                        imageResId = R.drawable.ic_information_purple,
                        title = "Something went wrong",
                        description = "Lorem Ipsum is simply dummy text of the printing and typesetting industry.",
                        buttonText = "TEST",
                        buttonClick = {
                            navController.popBackStack()
                        },
                    )
                }

            }
            composable("empty_view") {
                EmptyViewComponent(
                    model = EmptyViewModel(
                        title = LanguageConstants.SEARCH_RESULT_EMPTY_TITLE.localizeString(),
                        description = LanguageConstants.SEARCH_RESULT_EMPTY_DESCRIPTION.localizeString(),
                        imageResId = R.mipmap.ic_no_data
                    )
                )
            }
            composable(PAYSLIP_SCREEN) {
                PaySlipScreen(navController = navController)
            }
        }
        composable(
            route = "${Routes.REQUESTS_TYPES}/{service_id}/{service_title}",
            arguments = listOf(
                navArgument("service_id") { type = NavType.StringType },
                navArgument("service_title") { type = NavType.StringType })
        ) { backStackEntry ->
            val serviceId = backStackEntry.arguments?.getString("service_id") ?: ""
            val serviceTitle = backStackEntry.arguments?.getString("service_title") ?: ""

            RequestsTypesScreen(navController, serviceId, serviceTitle)
        }
        composable(
            route = "${Routes.REQUESTS_FORM}/{service_id}/{id}/{title}",
            arguments = listOf(
                navArgument("service_id") { type = NavType.StringType },
                navArgument("id") { type = NavType.StringType },
                navArgument("title") { type = NavType.StringType }
            )

        ) { backStackEntry ->
            val id = backStackEntry.arguments?.getString("id") ?: ""
            val title = backStackEntry.arguments?.getString("title") ?: ""

            // Read from our single source of truth
            // This means if that data later becomes *not* static, you'll
            // be able to easily substitute this out for an observable
            // data source
            RequestFormScreen(navController = navController, requestId = id, title = title)
        }
        composable(
            route = "${REQUESTS_DETAILS}/{id}",
            arguments = listOf(
                navArgument("id") { type = NavType.StringType }
            )

        ) { _ ->
            RequestDetailsScreen(navController = navController,
                onParticipantsClick = {
                    navController.navigate(PARTICIPANTS_SCREEN)
                }
            ) { id, issueId, notes, attachmentsList, addComment, addAttachment, requestTypeId ->
                navController.currentBackStackEntry?.savedStateHandle?.let {
                    it["notes"] = notes.toList()
                    it["attachments"] = attachmentsList.toList()
                    it["addComment"] = addComment
                    it["addAttachment"] = addAttachment
                    it["requestTypeId"] = requestTypeId
                    it["issueId"] = issueId

                }
                navController.navigate("${ACTIVITIES_SCREEN}/$id")
            }
        }
        composable(route = LANGUAGE_CHANGER) { TestChangeLanguage() }
        composable(route = SINGLE_SELECTION) { TestSingleSelection() }
        composable(route = MULTI_SELECTION) { TestMultiSelection() }
        composable(route = MULTI_DROPDOWNS) { TestMultiDropDowns(3) }
        composable(route = ATTACHMENT_COMPONENT) { TestAttachment() }
        composable(route = DIGITAL_CARD) {
            val backStackEntry = remember {
                navController.getBackStackEntry(tabList.value[3].screenRoute)
            }
            val viewModel: AccountViewModel = hiltViewModel(backStackEntry)
            DigitalCardScreen(navController, viewModel)
        }
        composable(route = SETTINGS_SCREEN) { SettingsScreen(navController, originalModels) }

        composable(
            route = MY_REQUESTS_SCREEN + "/{id}",
            arguments = listOf(
                navArgument("id") { type = NavType.StringType },
            )
        ) { backStackEntry ->
            val id = backStackEntry.arguments?.getString("id") ?: ""
            MyRequestsScreen(
                onBackButtonClick = {
                    navController.popBackStack()
                },
                onItemClick = { request ->
                    navController.navigate("${REQUESTS_DETAILS}/${request.id}")
                },
                onDismissButtonClicked = {
                    navController.popBackStack()
                }
            )
        }

        composable(
            route = "$ACTIVITIES_SCREEN/{requestId}",
            arguments = listOf(navArgument("requestId") { type = NavType.StringType })
        ) { backStackEntry ->
            val notes = navController.previousBackStackEntry
                ?.savedStateHandle
                ?.get<List<Status>>("notes")
                ?.let { it.toMutableStateList() } ?: mutableStateListOf()

            val attachments = navController.previousBackStackEntry
                ?.savedStateHandle
                ?.get<List<Attachment>>("attachments")
                ?.let { it.toMutableStateList() } ?: mutableStateListOf()

            val addComment =
                navController.previousBackStackEntry?.savedStateHandle?.get<Boolean>("addComment")
            val addAttachment =
                navController.previousBackStackEntry?.savedStateHandle?.get<Boolean>("addAttachment")
            val requestTypeId =
                navController.previousBackStackEntry?.savedStateHandle?.get<String>("requestTypeId")
            val issueId =
                navController.previousBackStackEntry?.savedStateHandle?.get<String>("issueId")

            ActivitiesScreen(
                navController = navController,
                requestId = backStackEntry.arguments?.getString("requestId") ?: "",
                notes = notes,
                attachmentsList = attachments,
                addComment = addComment ?: false,
                addAttachment = addAttachment ?: false,
                requestTypeId = requestTypeId ?: "",
                issueId = issueId ?: ""
            )
        }
        composable(route = PARTICIPANTS_SCREEN) {
            val backStackEntry = remember {
                navController.getBackStackEntry("${REQUESTS_DETAILS}/{id}")
            }
            val viewModel: RequestDetailsViewModel = hiltViewModel(backStackEntry)

            ParticipantsScreen(
                backHandler = { navController.popBackStack() },
                viewModel = viewModel
            )
        }
        composable(route = ANNOUNCEMENTS_SCREEN) {

            val annoucmentItem = navController.previousBackStackEntry
                ?.savedStateHandle
                ?.get<Announcement>("annoucmentItem")
            AnnouncementFullScreen(
                onBackClick = { navController.popBackStack() },
                announcementItem = annoucmentItem
            )
        }


        // for book a new meeting
        composable(route = BOOK_MEETING_ROOM) { BookMeetingRoomScreen(navController) }

        composable(
            route = "${Routes.AVAILABLE_MEETING_ROOMS}/{date}/{start_time}/{end_time}",
            arguments = listOf(
                navArgument("date") { type = NavType.StringType },
                navArgument("start_time") { type = NavType.StringType },
                navArgument("end_time") { type = NavType.StringType },
            )
        ) { backStackEntry ->
            val date = backStackEntry.arguments?.getString("date") ?: ""
            val startTime = backStackEntry.arguments?.getString("start_time") ?: ""
            val endTime = backStackEntry.arguments?.getString("end_time") ?: ""

            CheckAvailableMeetingRoomsScreen(
                navController = navController,
                date = date,
                startTime = startTime,
                endTime = endTime
            )
        }
        composable(
            route = "${Routes.SINGLE_MEETING_ROOM}/{start_time}/{end_time}/{date}",
            arguments = listOf(
                navArgument("start_time") { type = NavType.StringType },
                navArgument("end_time") { type = NavType.StringType },
                navArgument("date") { type = NavType.StringType },
            )
        ) { _ ->
            ChangeStatusBarIconsColors(Color.White)
            SingleMeetingRoomScreen(
                navController = navController,
                onBackClick = { navController.popBackStack() }
            )
        }

        composable(route = LOCAL_PARTICIPANTS_SCREEN) {
            val backStackEntry = remember {
                navController.getBackStackEntry("${Routes.SINGLE_MEETING_ROOM}/{start_time}/{end_time}/{date}")
            }
            val viewModel: SingleMeetingRoomViewModel = hiltViewModel(backStackEntry)

            LocalParticipantsScreen(
                backHandler = { navController.popBackStack() },
                viewModel = viewModel
            )
        }
        composable(route = SUCCESSFULLY_BOOKED_SCREEN) {
            val backStackEntry = remember {
                navController.getBackStackEntry("${Routes.SINGLE_MEETING_ROOM}/{start_time}/{end_time}/{date}")
            }
            val viewModel: SingleMeetingRoomViewModel = hiltViewModel(backStackEntry)
            SuccessfullyBookedScreen(
                title = viewModel.selectedRoom?.title ?: "",
                navController = navController,
                startTime = viewModel.startTime.value,
                date = viewModel.selectedDate.value,
                endTime = viewModel.endTime.value,
                floor = viewModel.selectedRoom?.floor ?: "",
            )
        }

    }
}


object Routes {
    const val REQUESTS_TYPES = "REQUESTS_TYPES"
    const val REQUESTS_FORM = "REQUESTS_FORM"
    const val REQUESTS_DETAILS = "REQUESTS_DETAILS"
    const val LANGUAGE_CHANGER = "LANGUAGE_CHANGER"
    const val SINGLE_SELECTION = "SINGLE_SELECTION"
    const val MULTI_SELECTION = "MULTI_SELECTION"
    const val MULTI_DROPDOWNS = "MULTI_DROPDOWNS"
    const val ATTACHMENT_COMPONENT = "ATTACHMENT_COMPONENT"
    const val DIGITAL_CARD = "DIGITAL_CARD"
    const val SETTINGS_SCREEN = "SETTINGS_SCREEN"
    const val MY_REQUESTS_SCREEN = "MY_REQUESTS_SCREEN"
    const val ACTIVITIES_SCREEN = "ACTIVITIES_SCREEN"
    const val PARTICIPANTS_SCREEN = "PARTICIPANTS_SCREEN"
    const val PAYSLIP_SCREEN = "PAYSLIP_SCREEN"
    const val ANNOUNCEMENTS_SCREEN = "ANNOUNCEMENTS_SCREEN"

    //create a new route for BookMeetingRoom
    const val BOOK_MEETING_ROOM = "BOOK_MEETING_ROOM"
    const val AVAILABLE_MEETING_ROOMS = "AVAILABLE_MEETING_ROOMS"
    const val SINGLE_MEETING_ROOM = "SINGLE_MEETING_ROOM"
    const val LOCAL_PARTICIPANTS_SCREEN = "LOCAL_PARTICIPANTS_SCREEN"
    const val SUCCESSFULLY_BOOKED_SCREEN = "SUCCESSFULLY_BOOKED_SCREEN"
}
