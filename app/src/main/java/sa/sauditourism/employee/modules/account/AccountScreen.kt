package sa.sauditourism.employee.modules.account

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.pullrefresh.PullRefreshIndicator
import androidx.compose.material.pullrefresh.pullRefresh
import androidx.compose.material.pullrefresh.rememberPullRefreshState
import androidx.compose.material3.Card
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.clipToBounds
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.skydoves.landscapist.ImageOptions
import com.skydoves.landscapist.coil.CoilImageState
import sa.sauditourism.employee.BuildConfig
import sa.sauditourism.employee.EmployeeApplication
import sa.sauditourism.employee.R
import sa.sauditourism.employee.components.AccountRowComponent
import sa.sauditourism.employee.components.AvatarData
import sa.sauditourism.employee.components.ButtonComponent
import sa.sauditourism.employee.components.ButtonSize
import sa.sauditourism.employee.components.ButtonType
import sa.sauditourism.employee.components.CustomAlertDialog
import sa.sauditourism.employee.components.CustomTextComponent
import sa.sauditourism.employee.components.DialogType
import sa.sauditourism.employee.components.GalleryType
import sa.sauditourism.employee.components.MediaComponent
import sa.sauditourism.employee.components.MediaContent
import sa.sauditourism.employee.components.ProfileDetailsHeaderComponentModel
import sa.sauditourism.employee.components.ProfileHeaderComponent
import sa.sauditourism.employee.components.ProfileHeaderModel
import sa.sauditourism.employee.components.SelectProfilePictureBottomSheet
import sa.sauditourism.employee.components.ToastPresentable
import sa.sauditourism.employee.components.ToastType
import sa.sauditourism.employee.components.ToastView
import sa.sauditourism.employee.constants.FirebaseConstants
import sa.sauditourism.employee.constants.LanguageConstants
import sa.sauditourism.employee.constants.LanguageConstants.ACCOUNT_SHOW_MY_BUSINESS_CARD
import sa.sauditourism.employee.constants.TestTagsConstants.ACCOUNT_SIGN_OUT_TAG
import sa.sauditourism.employee.di.SharedPreferencesModule
import sa.sauditourism.employee.extensions.clickableWithoutRipple
import sa.sauditourism.employee.extensions.getFileFromUri
import sa.sauditourism.employee.extensions.isNotNull
import sa.sauditourism.employee.extensions.localizeString
import sa.sauditourism.employee.managers.language.model.SupportedLanguage
import sa.sauditourism.employee.managers.network.models.UiState
import sa.sauditourism.employee.managers.remoteconfig.RemoteConfigService
import sa.sauditourism.employee.managers.sharedprefrences.SharedPreferencesManager
import sa.sauditourism.employee.modules.account.model.AccountDetails
import sa.sauditourism.employee.modules.account.model.AccountSetting
import sa.sauditourism.employee.modules.common.ChangeStatusBarIconsColors
import sa.sauditourism.employee.resources.AppColors
import sa.sauditourism.employee.resources.AppFonts
import sa.sauditourism.employee.shimmer.shimmerModifier
import java.io.File
import java.security.AccessController.getContext


val avatarList = listOf(
    AvatarData(9, "https://i.ibb.co/Jknc7cm/Avatar.png"),
    AvatarData(2, "https://i.ibb.co/rdNFfV9/Avatar-1.png"),
    AvatarData(8, "https://i.ibb.co/p10QfxR/Avatar-7.png"),
    AvatarData(3, "https://i.ibb.co/X2vTXZV/Avatar-2.png"),
    AvatarData(1, "https://i.ibb.co/41WYzBD/Avatar-3x.png"),
    AvatarData(4, "https://i.ibb.co/phbxQ6G/Avatar-3.png"),
    AvatarData(5, "https://i.ibb.co/Nn6m4YJ/Avatar-4.png"),
    AvatarData(6, "https://i.ibb.co/Jm06KL9/Avatar-5.png"),
    AvatarData(7, "https://i.ibb.co/gMWSbqw/Avatar-6.png"),
)

@Composable
@OptIn(ExperimentalMaterialApi::class)
fun AccountScreen(
    onDigitalCardClick: (AccountDetails) -> Unit = {},
    onSettingsClick: () -> Unit = {},
    onPaySlipClick: () -> Unit = {},
    onMyRequestClick: (String) -> Unit = {},
    onLogout: () -> Unit = {},
    viewModel: AccountViewModel = hiltViewModel()
) {
    val sharedPreferencesManager: SharedPreferencesManager =
        SharedPreferencesModule.provideSharedPreferencesManager(EmployeeApplication.instance.applicationContext)

    val context = LocalContext.current
    val event by viewModel.event.collectAsState()
    val deleteImageState by viewModel.imageDeleteStat.collectAsState()
    var accountDetails by remember { mutableStateOf<AccountDetails?>(null) }
    var isLoading by remember { mutableStateOf(false) }
    var isImageStateChanged by remember { mutableStateOf(false) }

    var showProfilePicSheet by remember { mutableStateOf(false) }
    var userImageBitmap by remember { mutableStateOf<Bitmap?>(null) }
    var cameraAccessDenied by remember { mutableStateOf(false) }

    var selectedAvatarId by rememberSaveable { mutableIntStateOf(sharedPreferencesManager.userAvatar) }
    var profileData by remember {
        // TODO: should be changed based on data from the API
        val value = avatarList.firstOrNull { it.id == selectedAvatarId }

        mutableStateOf(
            ProfileDetailsHeaderComponentModel(
                imageUri = Uri.parse(value?.imageUrl ?: ""),
                isAvatar = accountDetails?.image.isNullOrEmpty(),
            )
        )
    }

    var showToaster by remember { mutableStateOf(false) }
    var toastType by remember { mutableStateOf(ToastType.Success) }
    var isRefreshing by remember { mutableStateOf(false) }


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
                    ?: avatarList.firstOrNull { it.id == selectedAvatarId }?.imageUrl
            )
        }.getOrNull()

        profileData = ProfileDetailsHeaderComponentModel(
            userImage,
            accountDetails?.image.isNullOrEmpty()
        )

        sharedPreferencesManager.accountDetails = accountDetails
    }

    LaunchedEffect(key1 = deleteImageState) {
        if (deleteImageState is UiState.Success) {
            isImageStateChanged = false
            originalImageOverride.invoke(deleteImageState.data?.profilePicture)
            sharedPreferencesManager.userAvatar = 0
            selectedAvatarId = 0
            viewModel.clearState()
            showToaster = true
            toastType = ToastType.Success
        } else if (deleteImageState is UiState.Error) {
            viewModel.clearState()
            isImageStateChanged = false
            showToaster = true
            toastType = ToastType.Error
        }
    }

    LaunchedEffect(event) {
//        if (event is UiState.Loading) isLoading = true
        if (event is UiState.Success) {
            isLoading = false
            event.data?.let { account ->
                accountDetails = account
                originalImageOverride.invoke(account.image)
            }
        }
    }

    LaunchedEffect(Unit) {
//        viewModel.getProfile()
        accountDetails = sharedPreferencesManager.accountDetails
        originalImageOverride.invoke(accountDetails?.image)
    }

    val scrollState = rememberScrollState()

    val pullRefreshState = rememberPullRefreshState(
        refreshing = isRefreshing,
        onRefresh = {
            isRefreshing = false
            isLoading = true
            viewModel.getProfile()
        }
    )

    val showOverlay by remember {
        derivedStateOf {
            scrollState.value == 0
        }
    }

    val mediaContent by remember {
        mutableStateOf(
            MediaContent(
                GalleryType.IMAGE,
                if (sharedPreferencesManager.preferredLocale == "ar") R.drawable.comsing_soon_ar else R.drawable.coming_soon_en
            )
        )
    }

    val showMyPersonalProfile by remember {
        val filteredList =
            RemoteConfigService.checkFeature(feature = FirebaseConstants.FIREBASE_MY_PERSONAL_PROFILE)
        mutableStateOf(filteredList)
    }

    val showMyLeaves by remember {
        val filteredList =
            RemoteConfigService.checkFeature(feature = FirebaseConstants.FIREBASE_MY_LEAVES)
        mutableStateOf(filteredList)
    }

    val showMyPaySlip by remember {
        val filteredList =
            RemoteConfigService.checkFeature(feature = FirebaseConstants.FIREBASE_MY_PAYSLIP)
        mutableStateOf(filteredList)
    }

    val showMyRequests by remember {
        val filteredList =
            RemoteConfigService.checkFeature(feature = FirebaseConstants.FIREBASE_MY_REQUESTS)
        mutableStateOf(filteredList)
    }

    if (showToaster) {
        val presentable = when (toastType) {
            ToastType.Success -> ToastPresentable(
                type = ToastType.Success,
                title = LanguageConstants.YOUR_PICTURE_CHANGED_SUCCESSFULLY.localizeString(),
            )

            ToastType.Error -> ToastPresentable(
                type = ToastType.Error,
                title = deleteImageState.networkError?.message
                    ?: LanguageConstants.UNEXPECTED_ERROR_HAS_OCCURRED.localizeString(),
            )

            ToastType.Default -> ToastPresentable(
                type = ToastType.Default,
                title = LanguageConstants.YOUR_PICTURE_IS_NOW_DELETED.localizeString(),
                customIcon = R.drawable.ic_delete_icon
            )

            ToastType.Warning -> null
        }

        ToastView(
            presentable = presentable!!,
            toastDuration = 1000L,
            onDismiss = {
                showToaster = false
            },
        )
    }

    var isCoilLoading by remember { mutableStateOf(false) }
    var showDialog by remember { mutableStateOf(false) }
    var showDeletePicDialog by remember { mutableStateOf(false) }

    ChangeStatusBarIconsColors()

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(color = AppColors.accountBg),
    ) {
        Image(
            modifier = Modifier.fillMaxSize(),
            painter = painterResource(id = R.drawable.pattern_bg),
            contentDescription = "account background",
            contentScale = ContentScale.FillBounds,
        )
        Column(
            Modifier
                .fillMaxSize()
                .background(Color.Transparent)
                .pullRefresh(pullRefreshState),
            horizontalAlignment = Alignment.Start
        ) {

            ProfileHeaderComponent(
                profileHeaderModel = ProfileHeaderModel(
                    LanguageConstants.ACCOUNT_TAB.localizeString(),
                    showOverlay = false,
                    showAvatar = true,
                    avatar = R.mipmap.ic_account_logo
                ),
                showOverlay = showOverlay,
            )

            Column(
                Modifier
                    .fillMaxSize()
                    .padding(horizontal = 24.dp)
                    .verticalScroll(scrollState)
                    .padding(top = 42.dp, bottom = 30.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Top
            ) {
                // Info Card
                Card(
                    shape = RoundedCornerShape(24.dp),
                    modifier = Modifier
                        .clipToBounds()
                        .fillMaxWidth()
                        .shadow(
                            elevation = 30.dp,
                            shape = RoundedCornerShape(24.dp),
                            spotColor = Color(0x20000000),
                            ambientColor = Color(0x20000000)
                        )
                ) {
                    Column(
                        Modifier
                            .fillMaxSize()
                            .background(Color.White),
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.SpaceBetween
                    ) {
                        Spacer(modifier = Modifier.height(16.dp))

                        Box {
                            MediaComponent(
                                mediaContent = MediaContent(
                                    GalleryType.IMAGE,
                                    source = userImageBitmap ?: profileData.imageUri ?: ""
                                ),
                                isLoading = isLoading || isCoilLoading,
                                modifier = Modifier
                                    .size(104.dp)
                                    .clip(CircleShape)
                                    .shimmerModifier(isLoading = isLoading || isImageStateChanged),
                                onImageStateChanged = {
                                    if (it is CoilImageState.Success || it is CoilImageState.Failure) {
                                        isCoilLoading = false
                                    }
                                },
                                placeHolder = if (accountDetails?.gender == "M") R.drawable.male_avatar else R.drawable.ic_female_avatar,
                                loading = {
                                    MediaComponent(
                                        mediaContent = MediaContent(
                                            GalleryType.IMAGE,
                                            source = R.drawable.ic_transparent
                                        ),
                                        modifier = Modifier
                                            .shimmerModifier(isImageStateChanged || isLoading)
                                            .fillMaxSize(),
                                    )
                                }
                            )

                            MediaComponent(
                                mediaContent = MediaContent(
                                    GalleryType.IMAGE,
                                    source = R.drawable.ic_camera
                                ),
                                enabled = !isLoading,
                                modifier = Modifier
                                    .padding(bottom = 8.dp)
                                    .align(Alignment.BottomEnd)
                                    .background(AppColors.OnSecondaryDark, shape = CircleShape)
                                    .border(1.dp, AppColors.WHITE, shape = CircleShape)
                                    .clip(CircleShape)
                                    .shimmerModifier(isLoading, shape = CircleShape)
                                    .size(24.dp)
                                    .padding(5.dp)
                                    .clickableWithoutRipple {
                                        showProfilePicSheet = true
                                    }
                            )
                        }

                        Spacer(modifier = Modifier.height(8.dp))
                        CustomTextComponent(
                            text = accountDetails?.name,
                            color = AppColors.BLACK,
                            style = AppFonts.heading7SemiBold
                        )

                        Spacer(modifier = Modifier.height(10.dp))
                        CustomTextComponent(
                            text = LanguageConstants.ACCOUNT_ID.localizeString() +
                                    if (!accountDetails?.id.isNullOrEmpty()) accountDetails?.id else "",
                            color = AppColors.ColorTextSubdued,
                            style = AppFonts.body2Regular
                        )

                        Spacer(modifier = Modifier.height(16.dp))
                        ButtonComponent(
                            text = ACCOUNT_SHOW_MY_BUSINESS_CARD.localizeString(),
                            clickListener = {
                                if (isLoading) return@ButtonComponent
                                onDigitalCardClick.invoke(accountDetails ?: AccountDetails())
                            },
                            modifier = Modifier,
                            buttonType = ButtonType.WHITE_BACKGROUND,
                            buttonSize = ButtonSize.SMALL,
                            isLoading = isLoading
                        )
                        Spacer(modifier = Modifier.height(10.dp))
                        Image(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clipToBounds()
                                .height(24.dp),
                            contentScale = ContentScale.FillBounds,
                            painter = painterResource(id = R.drawable.card_strip),
                            contentDescription = ""
                        )
                    }
                }

                Spacer(modifier = Modifier.height(24.dp))

                // Settings Card
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clipToBounds()
                        .shadow(
                            elevation = 30.dp,
                            shape = RoundedCornerShape(8.dp),
                            spotColor = Color(0x20000000),
                            ambientColor = Color(0x20000000)
                        )
                        .background(Color.White, shape = RoundedCornerShape(8.dp))
                ) {
                    AccountRowComponent(
                        model = AccountSetting(
                            name = LanguageConstants.ACCOUNT_MY_REQUESTS.localizeString(),
                            image = R.drawable.ic_my_requests_enable

                        )
                    ) {
                        onMyRequestClick.invoke("1234")
                    }
                    if (showMyPaySlip == true) {
                        AccountRowComponent(
                            model = AccountSetting(
                                name = LanguageConstants.ACCOUNT_MY_PAYSLIP.localizeString(),
                                image = R.drawable.ic_my_payslip

                            )
                        ) {
                            onPaySlipClick.invoke()
                        }
                    }
                    AccountRowComponent(
                        model = AccountSetting(
                            name = LanguageConstants.ACCOUNT_SETTINGS.localizeString(),
                            image = R.drawable.ic_settings

                        )
                    ) {
                        onSettingsClick.invoke()
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                Box {
                    Card(
                        modifier = Modifier
                            .clipToBounds()
                            .fillMaxWidth()
                            .shadow(
                                elevation = 30.dp,
                                shape = RoundedCornerShape(8.dp),
                                spotColor = Color(0x20000000),
                                ambientColor = Color(0x20000000)
                            )
                            .background(Color.White, RoundedCornerShape(8.dp))
                    ) {
                        Column {
                            if (showMyPersonalProfile == true) {
                                AccountRowComponent(
                                    model = AccountSetting(
                                        name = LanguageConstants.ACCOUNT_MY_PERSONAL_PROFILE.localizeString(),
                                        image = R.drawable.ic_personal_account,

                                        ),
                                    enabled = false
                                )
                            }
                            if (showMyLeaves == true) {
                                AccountRowComponent(
                                    model = AccountSetting(
                                        name = LanguageConstants.ACCOUNT_MY_LEAVES.localizeString(),
                                        image = R.drawable.ic_my_leaves
                                    ),
                                    enabled = false
                                )
                            }
                        }
                    }

                    MediaComponent(
                        mediaContent = mediaContent,
                        modifier = Modifier
                            .align(Alignment.TopEnd)
                            .offset(y = (-4).dp, x = (5).dp)
                            .height(61.dp),
                        imageOptions = ImageOptions(
                            contentScale = ContentScale.Fit,
                            alignment = Alignment.TopCenter
                        )
                    )

                }
                Spacer(modifier = Modifier.height(16.dp))

                // Logout Card
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(48.dp)
                        .clipToBounds()
                        .testTag(ACCOUNT_SIGN_OUT_TAG)
                        .shadow(
                            elevation = 30.dp,
                            shape = RoundedCornerShape(8.dp),
                            spotColor = Color(0x20000000),
                            ambientColor = Color(0x20000000)
                        )
                        .background(Color.White, shape = RoundedCornerShape(8.dp))
                        .clickable { showDialog = true } // Click to show dialog
                ) {
                    Row(
                        Modifier
                            .fillMaxSize()
                            .background(Color.White),
                        horizontalArrangement = Arrangement.Center,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            painter = painterResource(id = R.drawable.ic_red_logout),
                            tint = AppColors.ColorFeedbackError,
                            contentDescription = ""
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        CustomTextComponent(
                            text = LanguageConstants.ACCOUNT_SIGN_OUT.localizeString(),
                            color = AppColors.ColorFeedbackError,
                            style = AppFonts.ctaLarge
                        )
                    }
                }

                if (showDialog) {
                    CustomAlertDialog(
                        type = DialogType.Normal,
                        imageResId = R.drawable.ic_trip,
                        shouldShowCloseIcon = false,
                        title = LanguageConstants.ACCOUNT_SIGN_OUT_ALERT_TITLE.localizeString(),
                        description = LanguageConstants.ACCOUNT_SIGN_OUT_ALERT_SUBTITLE.localizeString(),
                        primaryButtonText = LanguageConstants.ACCOUNT_SIGN_OUT.localizeString(),
                        secondaryButtonText = LanguageConstants.CANCEL.localizeString(),
                        onPrimaryButtonClick = {
                            onLogout.invoke()
                            showDialog = false
                        },
                        onSecondaryButtonClick = {
                            showDialog = false
                        },
                        onDismiss = {
                            showDialog = false
                        }
                    )
                }

                if (showDeletePicDialog) {
                    CustomAlertDialog(
                        type = DialogType.Normal,
                        imageResId = R.drawable.ic_trip,
                        shouldShowCloseIcon = false,
                        title = LanguageConstants.DELETE_MESSAGE.localizeString(),
                        primaryButtonText = LanguageConstants.DELETE.localizeString(),
                        secondaryButtonText = LanguageConstants.CANCEL.localizeString(),
                        onPrimaryButtonClick = {
                            showDeletePicDialog = false
                            isImageStateChanged = true
                            viewModel.deleteProfileImage(sharedPreferencesManager.accountDetails?.id?: "")
                            profileData = ProfileDetailsHeaderComponentModel(null, true)
                            toastType = ToastType.Default
                            showToaster = true
                            //todo call delete image from erp
                        },
                        onSecondaryButtonClick = {
                            showDeletePicDialog = false
                        },
                        onDismiss = {
                            showDeletePicDialog = false
                        }
                    )
                }

                Spacer(modifier = Modifier.height(12.dp))

                CustomTextComponent(
                    text = (LanguageConstants.ACCOUNT_VERSION.localizeString(BuildConfig.VERSION_NAME)),
                    style = AppFonts.body2Regular,
                    color = AppColors.ColorTextSubdued,
                    modifier = Modifier.fillMaxWidth(),
                    textAlign = TextAlign.Center,
                )

                if (showProfilePicSheet) {
                    val onAvatarSelected: (AvatarData) -> Unit = { selectedAvatar ->
                        if (selectedAvatarId != selectedAvatar.id) {
                            selectedAvatarId = selectedAvatar.id
                            sharedPreferencesManager.userAvatar = selectedAvatar.id
                            avatarList.forEach {
                                if (it.id == selectedAvatarId) {
                                    // TODO: should be changed to update avatar based on API
                                    profileData =
                                        ProfileDetailsHeaderComponentModel(
                                            Uri.parse(it.imageUrl),
                                            true
                                        )
                                    showToaster = true
                                    toastType = ToastType.Success
                                }
                            }
                        }
                    }
                    SelectProfilePictureBottomSheet(
                        isAvatar = profileData.isAvatar,
                        avatarList = avatarList,
                        selectedAvatarId = selectedAvatarId,
                        onAvatarSelected = onAvatarSelected,
                        onImageSelected = { uri ->
                            selectedAvatarId = 0
                            showProfilePicSheet = false

                            uri.getFileFromUri(
                                context
                            )?.let {
                                isImageStateChanged = true
                                viewModel.checkFileSize(
                                    file = File(it.path),
                                ) {
                                    viewModel.updateProfileImage(
                                        personId = accountDetails?.id ?: "",
                                        file = it
                                    )
                                }
                            }
                        },
                        onImageDeleted = {
                            showDeletePicDialog = true
                            showProfilePicSheet = false
                        },
                        onDismiss = {
                            showProfilePicSheet = false
                        },
                        onPermissionDenied = {
                            cameraAccessDenied = true
                            showProfilePicSheet = false
                        }
                    )
                }

                AnimatedVisibility(visible = cameraAccessDenied) {
                    ToastView(
                        presentable = ToastPresentable(
                            type = ToastType.Error,
                            title = LanguageConstants.ATTACHEMNT_SELECT_CAMERA.localizeString(),
                            description = LanguageConstants.CAMERA_USER_PERMISSION.localizeString(),
                        ),
                        toastDuration = 5000L,
                        onDismiss = {
                            cameraAccessDenied = false
                        },
                    )
                }

                if (deleteImageState is UiState.Error) {
                    toastType = ToastType.Error
                    showToaster = true
                }

                Spacer(modifier = Modifier.height(100.dp))
            }
        }

        PullRefreshIndicator(
            refreshing = isRefreshing,
            state = pullRefreshState,
            modifier = Modifier.align(Alignment.TopCenter),
            backgroundColor = Color.White,
        )
    }
}