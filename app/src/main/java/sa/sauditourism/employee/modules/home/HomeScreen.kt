package sa.sauditourism.employee.modules.home

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.PagerDefaults
import androidx.compose.foundation.pager.PagerSnapDistance
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.pullrefresh.PullRefreshIndicator
import androidx.compose.material.pullrefresh.pullRefresh
import androidx.compose.material.pullrefresh.rememberPullRefreshState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.util.lerp
import androidx.compose.ui.zIndex
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.skydoves.landscapist.ImageOptions
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import kotlinx.coroutines.supervisorScope
import sa.sauditourism.employee.EmployeeApplication.Companion.sharedPreferencesManager
import sa.sauditourism.employee.MainViewModel
import sa.sauditourism.employee.R
import sa.sauditourism.employee.components.CustomTextComponent
import sa.sauditourism.employee.components.GalleryType
import sa.sauditourism.employee.components.MediaComponent
import sa.sauditourism.employee.components.MediaContent
import sa.sauditourism.employee.components.ProfileDetailsHeaderComponentModel
import sa.sauditourism.employee.components.ToastPresentable
import sa.sauditourism.employee.components.ToastType
import sa.sauditourism.employee.components.ToastView
import sa.sauditourism.employee.constants.LanguageConstants
import sa.sauditourism.employee.extensions.isNotNull
import sa.sauditourism.employee.extensions.localizeString
import sa.sauditourism.employee.managers.network.models.UiState
import sa.sauditourism.employee.modules.account.avatarList
import sa.sauditourism.employee.modules.common.ChangeStatusBarIconsColors
import sa.sauditourism.employee.modules.home.model.UserData
import sa.sauditourism.employee.modules.home.myDayMeetings.MyMeetingsSection
import sa.sauditourism.employee.modules.home.myDayMeetings.model.MyMeetingsModel
import sa.sauditourism.employee.resources.AppColors
import sa.sauditourism.employee.resources.AppFonts
import sa.sauditourism.employee.modules.home.annoucments.AnnouncementSectionView
import sa.sauditourism.employee.modules.onboarding.PageIndicator
import sa.sauditourism.employee.modules.home.model.Announcement

import sa.sauditourism.employee.modules.home.model.Action
import sa.sauditourism.employee.modules.home.quickActions.QuickActionsSection
import sa.sauditourism.employee.shimmer.shimmerModifier
import kotlin.math.absoluteValue

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun HomeScreen(
    navController: NavController,
    homeViewModel: HomeViewModel = hiltViewModel(),
    mainViewModel: MainViewModel,
    onAnnouncementClick: (Announcement) -> Unit = {}
) {
    val userInfoFlow by homeViewModel.userData.collectAsState()
    val meetingsFlow by homeViewModel.meetingsData.collectAsState()
    val announcementFlow by homeViewModel.announcement.collectAsState()
    val quickActionsState by homeViewModel.quickActinsState.collectAsState()
    val profileFetch by mainViewModel.profileFetched.collectAsState()
    val announcementList = remember { mutableStateOf(listOf<Announcement>()) }
    var itemCount by remember { mutableIntStateOf(3) }
    var userInfo by remember { mutableStateOf(UserData()) }
    var isLoading by remember { mutableStateOf(false) }
    var isAnnouncementLoading by remember { mutableStateOf(false) }
    var isActionsLoading by remember { mutableStateOf(true) }
    val scope = rememberCoroutineScope()
    val quickActionsList = remember { mutableStateListOf<Action>() }
    var isRefreshing by remember { mutableStateOf(false) }
    var showProfileError by remember { mutableStateOf(false) }

    var showMeetingError by remember { mutableStateOf(false) }
    var showActionsError by remember { mutableStateOf(false) }
    var showAnnouncementError by remember { mutableStateOf(false) }
    val selectedAvatarId by rememberSaveable { mutableIntStateOf(sharedPreferencesManager.userAvatar) }
    var accountDetails by remember { mutableStateOf(sharedPreferencesManager.accountDetails) }
    var userImageBitmap by remember { mutableStateOf<Bitmap?>(null) }

    var profileData by remember {
        val value = avatarList.firstOrNull { it.id == selectedAvatarId }

        mutableStateOf(
            ProfileDetailsHeaderComponentModel(
                imageUri = Uri.parse(value?.imageUrl ?: ""),
                isAvatar = accountDetails?.image.isNullOrEmpty(),
            )
        )
    }

    val onFirstLaunch = {
        scope.launch {
            supervisorScope {
                async {
                    homeViewModel.apply {
                        getMyDayMeetings()
                        getAnnouncement()
                        getQuickActions()
                    }
                }
            }
        }
    }

    LaunchedEffect(key1 = true) {
        scope.launch {
            supervisorScope {
                async {
                    homeViewModel.apply {
                        getMyDayMeetings()
                        getAnnouncement()
                        getQuickActions()
                    }
                    mainViewModel.getProfile()
                }
            }
        }
    }

    LaunchedEffect(profileFetch) {
        showProfileError = profileFetch is UiState.Error

        if (profileFetch !is UiState.Loading) {
            if (profileFetch is UiState.Error) {
                showProfileError = true
                mainViewModel.doLogout()
            }
            mainViewModel.clearState()
        }
    }

    val pullRefreshState = rememberPullRefreshState(
        refreshing = isRefreshing,
        onRefresh = {
            isRefreshing = false
            onFirstLaunch.invoke()
        }
    )

    val originalImageOverride: (String?) -> Unit = {
        accountDetails?.image = it
        userImageBitmap =
            if (it.isNullOrEmpty()) null else accountDetails?._delegatedImage?.let { byteArray ->
                BitmapFactory.decodeByteArray(
                    byteArray,
                    0,
                    byteArray.size
                )
            }
        val userImage = runCatching {
            Uri.parse(
                accountDetails?.image
                    ?: avatarList.firstOrNull { it.id == sharedPreferencesManager.userAvatar }?.imageUrl
            )
        }.getOrNull()

        profileData = ProfileDetailsHeaderComponentModel(
            userImage,
            accountDetails?.image.isNullOrEmpty()
        )
    }

    LaunchedEffect(key1 = true) {
        onFirstLaunch.invoke()
        originalImageOverride.invoke(accountDetails?.image)
    }

    LaunchedEffect(userInfoFlow) {
        if (userInfoFlow is UiState.Success) {
            userInfo = userInfoFlow.data!!
            isLoading = false
        } else if (userInfoFlow is UiState.Loading) {
            isLoading = true
        }
    }

    LaunchedEffect(announcementFlow) {
        when (announcementFlow) {
            is UiState.Success -> {
                isAnnouncementLoading = false
                announcementFlow.data?.let { list ->
                    announcementList.value = list.announcements
                    itemCount = if (list.announcements.isEmpty()) 1 else list.announcements.size
                }
            }

            is UiState.Loading -> isAnnouncementLoading = true
            else -> {
                showAnnouncementError = true
                isAnnouncementLoading = false
            }
        }
    }

    LaunchedEffect(meetingsFlow) {
        isLoading = meetingsFlow is UiState.Loading
        if (meetingsFlow is UiState.Error) {
            showMeetingError = true
        }
    }

    LaunchedEffect(quickActionsState) {
        when (quickActionsState) {
            is UiState.Success -> {
                isActionsLoading = false
                quickActionsState.data?.actions?.let {
                    quickActionsList.clear()
                    quickActionsList.addAll(it)
                }
            }

            is UiState.Loading -> isActionsLoading = true
            else -> {
                isActionsLoading = false
                showActionsError = true
            }
        }
    }

    ChangeStatusBarIconsColors(Color.Transparent)

    val configuration = LocalConfiguration.current

    val screenHeight = configuration.screenHeightDp.dp

    Box(
        modifier = Modifier
            .pullRefresh(pullRefreshState)
            .fillMaxSize()
            .background(color = AppColors.accountBg)
    ) {
        Image(
            modifier = Modifier
                .padding(top = 175.dp)
                .heightIn(0.dp, screenHeight),
            painter = painterResource(id = R.drawable.ic_pattern_day),
            contentDescription = "account background",
            contentScale = ContentScale.Crop,
            alignment = Alignment.Center
        )
        Column(
            Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(bottom = 80.dp)
                .background(Color.Transparent),
            horizontalAlignment = Alignment.Start
        ) {

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 24.dp, start = 24.dp, end = 24.dp)
            ) {
                Column(Modifier.weight(1f)) {
                    CustomTextComponent(
                        text = (LanguageConstants.HI.localizeString() + " " + sharedPreferencesManager.accountDetails?.firstName),
                        color = AppColors.ColorsPrimitivesTwo,
                        style = AppFonts.heading6Regular
                    )

                    Spacer(modifier = Modifier.height(5.dp))

                    CustomTextComponent(
                        text = LanguageConstants.MY_DAY_TITLE.localizeString(),
                        maxLines = 2,
                        color = AppColors.BLACK,
                        style = AppFonts.headerBold,
                        lineHeight = 36.75.sp,
                        modifier = Modifier.padding(end = 45.dp)
                    )
                }


                MediaComponent(
                    mediaContent = MediaContent(
                        GalleryType.IMAGE,
                        source = userImageBitmap ?: profileData.imageUri
                    ),
                    modifier = Modifier
                        .size(56.dp)
                        .clip(RoundedCornerShape(16.dp)),
                    placeHolder = if (accountDetails?.gender == "M") R.drawable.male_avatar_rect else R.drawable.ic_female_avatar,
                    showPlaceHolder = true,
                    imageOptions = ImageOptions(
                        contentScale = ContentScale.Crop,
                        alignment = Alignment.Center,
                        contentDescription = null,
                    )
                )
            }

            // my day meetings
            when (meetingsFlow) {
                is UiState.Success -> {
                    if(!meetingsFlow.data?.meetingsList.isNullOrEmpty())
                        MyMeetingsSection(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(top = 40.dp)
                                .padding(start = 24.dp),
                            model = meetingsFlow.data,
                            isLoading = isLoading
                        )
                    }

                is UiState.Loading -> MyMeetingsSection(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 40.dp)
                        .padding(horizontal = 24.dp),
                    model = MyMeetingsModel(),
                    isLoading = isLoading
                )

                is UiState.Error -> showMeetingError = true
            }

            Spacer(Modifier.height(40.dp))

            QuickActionsSection(
                navController = navController,
                actions = quickActionsList.ifEmpty {
                    listOf(
                        Action(orderIndex = 1),
                        Action(orderIndex = 2),
                        Action(orderIndex = 3)
                    )
                }, isLoading = isActionsLoading
            )

            Spacer(Modifier.height(65.dp))

            CustomTextComponent(
                modifier = Modifier
                    .shimmerModifier(isAnnouncementLoading)
                    .align(alignment = Alignment.CenterHorizontally),
                text = LanguageConstants.HAPPENING_TODAY_TITLE.localizeString(),
                maxLines = 2,
                color = AppColors.BLACK,
                style = AppFonts.headerBold,
                lineHeight = 36.75.sp
            )
            Spacer(Modifier.height(8.dp))

            CustomTextComponent(
                modifier = Modifier
                    .shimmerModifier(isAnnouncementLoading)
                    .align(alignment = Alignment.CenterHorizontally),
                text = LanguageConstants.HAPPENING_TODAY_SUBTITLE.localizeString(),
                maxLines = 2,
                color = AppColors.ColorTextSubdued,
                style = AppFonts.body1Regular,
                lineHeight = 36.75.sp
            )

            Spacer(Modifier.height(16.dp))

            val pagerState =
                androidx.compose.foundation.pager.rememberPagerState(
                    pageCount = { if (announcementList.value.isNotEmpty()) announcementList.value.size else itemCount },
                    initialPage = 0
                )
            HorizontalPager(
                state = pagerState,
                modifier = Modifier
                    .zIndex(100f)
                    .fillMaxWidth()
                    .padding(vertical = 8.dp),
                beyondViewportPageCount = 1,
                flingBehavior = PagerDefaults.flingBehavior(
                    state = pagerState,
                    pagerSnapDistance = PagerSnapDistance.atMost(0)
                ),
                contentPadding = PaddingValues(horizontal = 20.dp),
            ) { page ->

                AnnouncementSectionView(
                    announcements = kotlin.runCatching { announcementList.value[page] }
                        .getOrElse { Announcement() },
                    isLoading = isAnnouncementLoading,
                    modifier = Modifier.graphicsLayer {
                        val pageOffSet = (
                                (pagerState.currentPage - page) + pagerState
                                    .currentPageOffsetFraction
                                ).absoluteValue
                        alpha = lerp(
                            start = 0.5f,
                            stop = 1f,
                            fraction = 1f - pageOffSet.coerceIn(0f, 1f)
                        )
                        scaleY = lerp(
                            start = 0.75f,
                            stop = 1f,
                            fraction = 1f - pageOffSet.coerceIn(0f, 1f)
                        )
                    }) { announcementSelected ->
                    if (announcementSelected.subject.isEmpty()) return@AnnouncementSectionView
                    onAnnouncementClick(announcementSelected)
                }
            }

            MediaComponent(
                mediaContent = MediaContent(
                    GalleryType.IMAGE,
                    source = R.drawable.ic_home_pattern
                ),
                modifier = Modifier
                    .fillMaxWidth()
                    .offset(y = ((-50).dp))
                    .height(62.dp),
                showPlaceHolder = true,
                placeHolder = R.drawable.ic_home_pattern,
                imageOptions = ImageOptions(
                    contentScale = ContentScale.FillBounds,
                    alignment = Alignment.Center,
                    contentDescription = null,
                )
            )

            if (announcementList.value.size > 1)
                PageIndicator(
                    numberOfPages = pagerState.pageCount,
                    selectedPage = pagerState.currentPage,
                    selectedColor = AppColors.OnPrimaryDark,
                    defaultColor = AppColors.PrimaryDark50,
                    defaultRadius = 12.dp,
                    defaultHeight = 4.dp,
                    selectedLength = 24.dp,
                    space = 8.dp,
                    modifier = Modifier
                        .offset(y = ((-40).dp))
                        .align(Alignment.CenterHorizontally)
                        .padding(top = 8.dp, bottom = 10.dp)
                )

        }
        if (showAnnouncementError)
            homeViewModel.HandleError(
                error = announcementFlow.networkError,
                isDataEmpty = announcementFlow.data?.announcements.isNullOrEmpty(),
                showBack = false,
                withTab = true,
                dismissCallback = {
                    homeViewModel.clearAnnouncementState()
                    showAnnouncementError = false
                },
            ) {
                homeViewModel.getAnnouncement()
            }


        if (showActionsError)
            homeViewModel.HandleError(
                error = quickActionsState.networkError,
                isDataEmpty = quickActionsState.data?.actions.isNullOrEmpty(),
                showBack = false,
                withTab = true,
                dismissCallback = {
                    homeViewModel.clearActionsState()
                    showActionsError = false
                },
            ) {
                homeViewModel.getQuickActions()
            }


        if (showMeetingError)
            homeViewModel.HandleError(
                error = meetingsFlow.networkError,
                isDataEmpty = meetingsFlow.data?.meetingsList.isNullOrEmpty(),
                showBack = false,
                withTab = true,
                dismissCallback = {
                    homeViewModel.clearMeetingState()
                    showMeetingError = false
                },
            ) {
                homeViewModel.getMyDayMeetings()
            }

        if (showProfileError) {
            val presentable = ToastPresentable(
                type = ToastType.Error,
                title = profileFetch?.networkError?.message
                    ?: LanguageConstants.UNEXPECTED_ERROR_HAS_OCCURRED.localizeString(),
            )
            ToastView(
                presentable = presentable,
                toastDuration = 3000L,
                onDismiss = {
                    showProfileError = false
                },
            )
        }

        PullRefreshIndicator(
            refreshing = isRefreshing,
            state = pullRefreshState,
            modifier = Modifier.align(Alignment.TopCenter),
            backgroundColor = Color.White,
        )

    }
}


//    Column(
//        Modifier
//            .fillMaxSize()
//            .padding(top = 40.dp, bottom = 100.dp)
//            .verticalScroll(rememberScrollState()),
//        horizontalAlignment = Alignment.CenterHorizontally,
//        verticalArrangement = Arrangement.Center
//    ) {
//        ButtonComponent(
//            text = "Test Buttons",
//            clickListener = { navController.navigate("Buttons") },
//            modifier = Modifier,
//            buttonType = ButtonType.PRIMARY,
//            buttonSize = ButtonSize.LARGE,
//        )
//        Spacer(modifier = Modifier.height(16.dp))
//
//        ButtonComponent(
//            text = "Test Toasters",
//            clickListener = { navController.navigate("Toasters") },
//            modifier = Modifier,
//            buttonType = ButtonType.PRIMARY,
//            buttonSize = ButtonSize.LARGE,
//        )
//
//        Spacer(modifier = Modifier.height(16.dp))
//
//        ButtonComponent(
//            text = "Test Popups",
//            clickListener = { navController.navigate("Popups") },
//            modifier = Modifier,
//            buttonType = ButtonType.PRIMARY,
//            buttonSize = ButtonSize.LARGE,
//        )
////
//        Spacer(modifier = Modifier.height(16.dp))
//
//        ButtonComponent(
//            text = "Test Shimmer",
//            clickListener = { navController.navigate("Shimmer") },
//            modifier = Modifier,
//            buttonType = ButtonType.PRIMARY,
//            buttonSize = ButtonSize.LARGE,
//        )
//
//        Spacer(modifier = Modifier.height(16.dp))
//
//        ButtonComponent(
//            modifier = Modifier,
//            text = "Text Fields",
//            clickListener = { navController.navigate("new_components") },
//            buttonType = ButtonType.PRIMARY,
//            buttonSize = ButtonSize.LARGE,
//        )
//
//        Spacer(modifier = Modifier.height(16.dp))
//
//        ButtonComponent(
//            modifier = Modifier,
//            text = "Date Picker",
//            clickListener = { navController.navigate("Date_picker") },
//            buttonType = ButtonType.PRIMARY,
//            buttonSize = ButtonSize.LARGE,
//        )
//        Spacer(modifier = Modifier.height(16.dp))
//        ButtonComponent(
//            modifier = Modifier,
//            text = "Time Picker",
//            clickListener = { navController.navigate("time_picker") },
//            buttonType = ButtonType.PRIMARY,
//            buttonSize = ButtonSize.LARGE,
//        )
//        Spacer(modifier = Modifier.height(16.dp))
//
//        ButtonComponent(
//            modifier = Modifier,
//            text = "Date Time Picker",
//            clickListener = { navController.navigate("date_time_picker") },
//            buttonType = ButtonType.PRIMARY,
//            buttonSize = ButtonSize.LARGE,
//        )
//        Spacer(modifier = Modifier.height(16.dp))
//
//        ButtonComponent(
//            modifier = Modifier,
//            text = "Empty Card",
//            clickListener = { navController.navigate("empty_view") },
//            buttonType = ButtonType.PRIMARY,
//            buttonSize = ButtonSize.LARGE,
//        )
//
//        Spacer(modifier = Modifier.height(16.dp))
//        ButtonComponent(
//            modifier = Modifier,
//            text = "Warning",
//            clickListener = { navController.navigate("Warning_Page") },
//            buttonType = ButtonType.PRIMARY,
//            buttonSize = ButtonSize.LARGE,
//        )
//        Spacer(modifier = Modifier.height(16.dp))
//
//        ButtonComponent(
//            modifier = Modifier,
//            text = "Radio Button",
//            clickListener = { navController.navigate("radio_button") },
//            buttonType = ButtonType.PRIMARY,
//            buttonSize = ButtonSize.LARGE,
//        )
//
//        Spacer(modifier = Modifier.height(16.dp))
//        ButtonComponent(
//            modifier = Modifier,
//            text = "Check Box",
//            clickListener = { navController.navigate("check_box") },
//            buttonType = ButtonType.PRIMARY,
//            buttonSize = ButtonSize.LARGE,
//        )
//        Spacer(modifier = Modifier.height(16.dp))
//        ButtonComponent(
//            modifier = Modifier,
//            text = "Security Warning Popup",
//            clickListener = { navController.navigate("security_popup") },
//            buttonType = ButtonType.PRIMARY,
//            buttonSize = ButtonSize.LARGE,
//        )
//        Spacer(modifier = Modifier.height(16.dp))
//        ButtonComponent(
//            modifier = Modifier,
//            text = "Section Failure",
//            clickListener = { navController.navigate("section_failure") },
//            buttonType = ButtonType.PRIMARY,
//            buttonSize = ButtonSize.LARGE,
//        )
//        Spacer(modifier = Modifier.height(16.dp))
//        ButtonComponent(
//            modifier = Modifier,
//            text = "App Update Popup",
//            clickListener = { navController.navigate("app_update_popup") },
//            buttonType = ButtonType.PRIMARY,
//            buttonSize = ButtonSize.LARGE,
//        )
//        Spacer(modifier = Modifier.height(16.dp))
//
//        ButtonComponent(
//            modifier = Modifier,
//            text = "Maintainance",
//            clickListener = { navController.navigate("maintainance") },
//            buttonType = ButtonType.PRIMARY,
//            buttonSize = ButtonSize.LARGE,
//        )
//        Spacer(modifier = Modifier.height(16.dp))
//
//        ButtonComponent(
//            modifier = Modifier,
//            text = "Test Language Changer",
//            clickListener = { navController.navigate(Routes.LANGUAGE_CHANGER) },
//            buttonType = ButtonType.PRIMARY,
//            buttonSize = ButtonSize.LARGE,
//        )
//        Spacer(modifier = Modifier.height(16.dp))
//
//        ButtonComponent(
//            modifier = Modifier,
//            text = "Test Single Selection",
//            clickListener = { navController.navigate(Routes.SINGLE_SELECTION) },
//            buttonType = ButtonType.PRIMARY,
//            buttonSize = ButtonSize.LARGE,
//        )
//        Spacer(modifier = Modifier.height(16.dp))
//
//        ButtonComponent(
//            modifier = Modifier,
//            text = "Test Multi Selections",
//            clickListener = { navController.navigate(Routes.MULTI_SELECTION) },
//            buttonType = ButtonType.PRIMARY,
//            buttonSize = ButtonSize.LARGE,
//        )
//        Spacer(modifier = Modifier.height(16.dp))
//
//        ButtonComponent(
//            modifier = Modifier,
//            text = "Test Multi Drop Downs",
//            clickListener = { navController.navigate(Routes.MULTI_DROPDOWNS) },
//            buttonType = ButtonType.PRIMARY,
//            buttonSize = ButtonSize.LARGE,
//        )
//        Spacer(modifier = Modifier.height(16.dp))
//
//        ButtonComponent(
//            modifier = Modifier,
//            text = "Test Attachement",
//            clickListener = { navController.navigate(Routes.ATTACHMENT_COMPONENT) },
//            buttonType = ButtonType.PRIMARY,
//            buttonSize = ButtonSize.LARGE,
//        )
//        Spacer(modifier = Modifier.height(16.dp))
//
//        ButtonComponent(
//            modifier = Modifier,
//            text = "Logout",
//            clickListener = { onLogout.invoke() },
//            buttonType = ButtonType.PRIMARY,
//            buttonSize = ButtonSize.LARGE,
//        )
//    }
