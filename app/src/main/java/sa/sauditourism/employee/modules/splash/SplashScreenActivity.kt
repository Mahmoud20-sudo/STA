package sa.sauditourism.employee.modules.splash

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.content.res.Configuration
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.IntentSenderRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalLayoutDirection
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.intl.Locale
import androidx.compose.ui.unit.LayoutDirection
import androidx.compose.ui.unit.dp
import androidx.core.os.ConfigurationCompat
import com.google.android.play.core.appupdate.AppUpdateManagerFactory
import com.google.android.play.core.appupdate.AppUpdateOptions
import com.google.android.play.core.install.model.AppUpdateType
import com.google.android.play.core.install.model.UpdateAvailability
import com.scottyab.rootbeer.RootBeer
import com.skydoves.landscapist.animation.crossfade.CrossfadePlugin
import com.skydoves.landscapist.components.rememberImageComponent
import dagger.hilt.android.AndroidEntryPoint
import sa.sauditourism.employee.BuildConfig
import sa.sauditourism.employee.EmployeeApplication
import sa.sauditourism.employee.LocalLanguage
import sa.sauditourism.employee.MainActivity
import sa.sauditourism.employee.MainViewModel
import sa.sauditourism.employee.R
import sa.sauditourism.employee.components.CustomAlertDialog
import sa.sauditourism.employee.components.DialogType
import sa.sauditourism.employee.components.GalleryType
import sa.sauditourism.employee.components.MediaComponent
import sa.sauditourism.employee.components.MediaContent
import sa.sauditourism.employee.components.ToastPresentable
import sa.sauditourism.employee.components.ToastType
import sa.sauditourism.employee.components.ToastView
import sa.sauditourism.employee.components.WarningPage
import sa.sauditourism.employee.constants.CommonConstants
import sa.sauditourism.employee.constants.LanguageConstants
import sa.sauditourism.employee.di.SharedPreferencesModule
import sa.sauditourism.employee.extensions.getResourceByName
import sa.sauditourism.employee.extensions.isHmsAvailable
import sa.sauditourism.employee.extensions.localizeString
import sa.sauditourism.employee.extensions.observeNonNull
import sa.sauditourism.employee.extensions.openStoreForApp
import sa.sauditourism.employee.managers.language.LanguageManager
import sa.sauditourism.employee.managers.locale.LocaleChanger
import sa.sauditourism.employee.managers.network.models.UiState
import sa.sauditourism.employee.managers.remoteconfig.RemoteConfigService
import sa.sauditourism.employee.managers.sharedprefrences.SharedPreferencesManager
import sa.sauditourism.employee.resources.AppColors
import sa.sauditourism.employee.resources.theme.EmployeeTheme
import sa.sauditourism.employee.resources.theme.ThemeViewModel

@SuppressLint("CustomSplashScreen")
@AndroidEntryPoint
class SplashScreenActivity : ComponentActivity() {

    private val splashViewModel: SplashViewModel by viewModels()
    private val themeViewModel: ThemeViewModel by viewModels()
    private val mainViewModel: MainViewModel by viewModels()

    private val sharedPreferencesManager: SharedPreferencesManager =
        SharedPreferencesModule.provideSharedPreferencesManager(EmployeeApplication.instance.applicationContext)

    private lateinit var activityResultLauncher: ActivityResultLauncher<IntentSenderRequest>
    private var isRemoteConfigFetched = false
    private var isUpdateDialogHidden = true
    private var isAppUnderMaintenance = false
    private var showRootedDialog = false


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        fetchRemoteConfig()
        splashViewModel.checkChinaCountry(this)
        checkRootedDevice()

        // todo test on internal test
        activityResultLauncher =
            registerForActivityResult(ActivityResultContracts.StartIntentSenderForResult()) {
                // handle results here
                if (it.resultCode != RESULT_OK) {
                    openStoreForApp(CommonConstants.VISIT_SAUDI_GOOGLE_PLAY_PACKAGE_NAME)
                } else {
                    isUpdateDialogHidden = true
                    checkAndRedirect()
                }
            }

        sharedPreferencesManager.clearTabsResponses()

        sharedPreferencesManager.openAppCount += 1

        val index = sharedPreferencesManager.openAppCount % 7
        val randomNum = if (index == 0) 7 else index
        val resourceName = "splash_screen_img_$randomNum"

        var headerImageResource =
            getResourceByName(resourceName, "drawable") ?: R.drawable.splash_header
        if (headerImageResource == 0) {
            headerImageResource = R.drawable.splash_header
        }

        var footerImageResource =
            getResourceByName(resourceName, "drawable") ?: R.drawable.splash_footer
        if (footerImageResource == 0) {
            footerImageResource = R.drawable.splash_footer
        }

        setContent {
            if (sharedPreferencesManager.preferredLocale == null) {
                val configuration = LocalConfiguration.current
                val locales = ConfigurationCompat.getLocales(configuration)
                if (locales.size() > 0) {
                    val locale = locales.get(0)
                    sharedPreferencesManager.preferredLocale =
                        locale?.language ?: LanguageConstants.DEFAULT_LOCALE
                }
            }
            if (sharedPreferencesManager.preferredLocale == null) {
                sharedPreferencesManager.preferredLocale = LanguageConstants.DEFAULT_LOCALE
            } else {
                LocaleChanger.overrideLocale(baseContext)
            }

            val welcomeToArabiaText by remember {
                mutableStateOf(
                    MediaContent(
                        GalleryType.IMAGE,
                        R.mipmap.welcome_arabic,
                    )
                )
            }

            var showProfileError by remember { mutableStateOf(false) }
            var showUpdateDialog by remember { mutableStateOf(false) }
            val appUpdateModel by splashViewModel.appUpdateFetched.collectAsState()
            val appMaintenanceModel by splashViewModel.appMaintenanceFetched.collectAsState()
            val languageUpdate by splashViewModel.languageFetched.collectAsState()
            val appConfigurationUpdate by splashViewModel.appconfigurationsFetched.collectAsState()
            val onBoarding by splashViewModel.onBoardingFetched.collectAsState()
//            val profileFetch by mainViewModel.profileFetched.collectAsState()

            val startDelay by splashViewModel.startDelayFetched.collectAsState()
//            val tabAccountModel by splashViewModel.tabAccountFetched.collectAsState()

//            LaunchedEffect(profileFetch) {
//                if (profileFetch !is UiState.Loading) {
//                    if (profileFetch is UiState.Error) {
//                        showProfileError = true
//                        splashViewModel.doLogout()
//                    }
//
//                    mainViewModel.clearState()
//                    val intent = Intent(this@SplashScreenActivity, MainActivity::class.java)
//                    startActivity(intent)
//                    finish()
//                }
//
//            }

            EmployeeTheme(themeViewModel = themeViewModel, systemInDarkTheme = true) {
                CompositionLocalProvider(
                    LocalLanguage provides sharedPreferencesManager.preferredLocale.orEmpty(),
                    LocalLayoutDirection provides LayoutDirection.Ltr
                ) {
                    if (showRootedDialog) {
                        CustomAlertDialog(
                            type = DialogType.Failed,
                            imageResId = R.drawable.ic_attention,
                            title = LanguageConstants.SECURITY_POPUP_TITLE.localizeString(),
                            description = LanguageConstants.SECURITY_POPUP_DESCRIPTION.localizeString(),
                            shouldShowCloseIcon = false
                        )
                    }
                    if (isAppUnderMaintenance && isUpdateDialogHidden) {
                        Box(
                            modifier = Modifier.fillMaxSize(),
                            contentAlignment = Alignment.Center
                        ) {
                            WarningPage(
                                modifier = Modifier.size(width = 300.dp, height = 400.dp),
                                shouldIncludeBackButton = false,
                                arrowImage = R.drawable.ic_arrow_left_tail,
                                mainImageResId = R.drawable.ic_activities,
                                title = LanguageConstants.MOBILE_UNDER_MAINTENANCE.localizeString(),
                                description = LanguageConstants.MOBILE_UNDER_MAINTENANCE_DESCRIPTION.localizeString(),
                                warningBgColor = Color.White,
                                buttonText = LanguageConstants.TRY_AGAIN.localizeString(),
                                buttonClickListener = {},
                                dialogType = DialogType.Normal,
                            )
                        }

                    } else {
                        Box(
                            modifier = Modifier
                                .fillMaxSize()
                                .background(AppColors.OnPrimaryDark),
                            contentAlignment = Alignment.Center
                        ) {
                            Image(
                                painter = painterResource(id = headerImageResource),
                                contentDescription = "splash screen",
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .wrapContentHeight()
                                    .align(Alignment.TopCenter),
                                alignment = Alignment.Center,
                                contentScale = ContentScale.FillWidth,
                            )

                            MediaComponent(
                                mediaContent = welcomeToArabiaText,
                                modifier = Modifier
                                    .wrapContentSize()
                                    .padding(24.dp),
                                imageComponent = rememberImageComponent {
                                    +CrossfadePlugin(
                                        duration = 500,
                                    )
                                },
                                showPlaceHolder = false
                            )

                            Image(
                                painter = painterResource(id = footerImageResource),
                                contentDescription = "splash screen",
                                modifier = Modifier
                                    .wrapContentSize()
                                    .align(Alignment.BottomEnd),
                                alignment = Alignment.Center,
                                contentScale = ContentScale.FillWidth,
                            )

                            val shouldUpdateApp = appUpdateModel != null &&
                                    appUpdateModel!!.shouldUpdateApp(BuildConfig.VERSION_NAME)
                            isAppUnderMaintenance = appMaintenanceModel != null &&
                                    appMaintenanceModel!!.shouldShowAppUnderMaintenance(BuildConfig.VERSION_NAME)

                            if (shouldUpdateApp &&
                                !showUpdateDialog
                            ) {
                                isUpdateDialogHidden = false
                                CustomAlertDialog(
                                    type = DialogType.Normal,
                                    modifier = Modifier.align(Alignment.Center),
                                    imageResId = R.drawable.ic_activities,
                                    title = LanguageConstants.UPDATE_REQUIRED.localizeString(),
                                    description = LanguageConstants.UPDATE_REQUIRED_DESCRIPTION.localizeString(),
                                    shouldShowCloseIcon = !appUpdateModel!!.forceUpdateEnabled,
                                    primaryButtonText = LanguageConstants.UPDATE_NOW.localizeString(),
                                    secondaryButtonText = if (appUpdateModel!!.forceUpdateEnabled) null else LanguageConstants.UPDATE_LATER.localizeString(),
                                    shouldDismissDialogOnPrimary = false,
                                    onPrimaryButtonClick = {
                                        startUpdateProcess(appUpdateModel!!.forceUpdateEnabled)
                                        showUpdateDialog = false
                                        isUpdateDialogHidden = false
                                    },
                                    onSecondaryButtonClick = {
                                        showUpdateDialog = true
                                        isUpdateDialogHidden = true
                                        checkAndRedirect()
                                    },
                                    onDismiss = {
                                        showUpdateDialog = !appUpdateModel!!.forceUpdateEnabled
                                        if (showUpdateDialog) {
                                            isUpdateDialogHidden = true
                                            checkAndRedirect()
                                        }
                                    },
                                )
                            }
                        }
                    }
//                    if (showProfileError) {
//                        val presentable = ToastPresentable(
//                            type = ToastType.Error,
//                            title = profileFetch?.networkError?.message
//                                ?: LanguageConstants.UNEXPECTED_ERROR_HAS_OCCURRED.localizeString(),
//                        )
//                        ToastView(
//                            presentable = presentable,
//                            toastDuration = 3000L,
//                            onDismiss = {
//                                showProfileError = false
//                            },
//                        )
//                    }
                }
            }

            if (languageUpdate != null && appUpdateModel != null && appMaintenanceModel != null && startDelay == true && appConfigurationUpdate != null && onBoarding != null) {
                // get preferred language from shared preferences
                var locale =
                    SharedPreferencesModule.provideSharedPreferencesManager(EmployeeApplication.instance.applicationContext)
                        .preferredLocale
                if (locale.isNullOrEmpty()) {
                    locale = Locale.current.language
                }

                // if preferred language is not supported revert back to default locale
                val filteredSupportedLanguage =
                    LanguageManager.supportedLanguages.filter { item -> item.code == locale }
                if (filteredSupportedLanguage.isNotEmpty()) {
                    sharedPreferencesManager.tabsList = filteredSupportedLanguage[0].tabs
                }
                sharedPreferencesManager.onBoarding = onBoarding
                checkAndRedirect()
            }
        }


//        val analyticsScreenData = HashMap<String, String>()
//        analyticsScreenData[AnalyticsConstants.EventParams.SCREEN_CLASS] = this.javaClass.simpleName
//        analyticsScreenData[AnalyticsConstants.EventParams.SCREEN_NAME] =
//            AnalyticsConstants.ScreenAnalyticsKeys.SPLASH_SCREEN.value

//        AnalyticsManager.trackEvent(
//            AnalyticsConstants.AnalyticsEventsKeys.SCREEN_VIEW.value,
//            analyticsScreenData
//        )

        initObservers()

        // handle links on create
        //checkIntentForDeepLinks(intent)
    }

    private fun checkRootedDevice() {
        val isRooted = RootBeer(this).isRooted && !BuildConfig.DEBUG
        if (isRooted) {
            showRootedDialog = true
        }
    }

    override fun onResume() {
        super.onResume()

        if (!isHmsAvailable()) {
            val appUpdateManager = AppUpdateManagerFactory.create(this)

            appUpdateManager
                .appUpdateInfo
                .addOnSuccessListener { appUpdateInfo ->
                    if (appUpdateInfo.updateAvailability()
                        == UpdateAvailability.DEVELOPER_TRIGGERED_UPDATE_IN_PROGRESS
                    ) {
                        // If an in-app update is already running, resume the update.
                        appUpdateManager.startUpdateFlowForResult(
                            appUpdateInfo,
                            activityResultLauncher,
                            AppUpdateOptions.newBuilder(AppUpdateType.IMMEDIATE).build(),
                        )
                    }
                }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        activityResultLauncher.unregister()
    }

    private fun initObservers() {
        LanguageManager.localizationChanged.observeNonNull(this) {
            if (it) {
                LocaleChanger.overrideLocale(baseContext)
                LocaleChanger.overrideLocale(EmployeeApplication.instance.applicationContext)
            }
        }
    }

    override fun attachBaseContext(base: Context) {
        super.attachBaseContext(LocaleChanger.wrapContext(base))
    }

    override fun onConfigurationChanged(newConfig: Configuration) {
        super.onConfigurationChanged(newConfig)
        LocaleChanger.overrideLocale(this)
    }


    private fun startUpdateProcess(isForceUpdate: Boolean) {
        val appUpdateManager = AppUpdateManagerFactory.create(this)
        //  Returns an intent object that you use to check for an update.
        val appUpdateInfoTask = appUpdateManager.appUpdateInfo

        //  Checks that the platform will allow the specified type of update.
        appUpdateInfoTask.addOnSuccessListener { appUpdateInfo ->
            val appUpdateType = if (isForceUpdate) {
                AppUpdateType.IMMEDIATE
            } else {
                AppUpdateType.FLEXIBLE
            }
            if (appUpdateInfo.updateAvailability() == UpdateAvailability.UPDATE_AVAILABLE &&
                // This example applies an immediate update. To apply a flexible update
                // instead, pass in AppUpdateType.FLEXIBLE
                appUpdateInfo.isUpdateTypeAllowed(appUpdateType)
            ) {
                appUpdateManager.startUpdateFlowForResult(
                    // Pass the intent that is returned by 'getAppUpdateInfo()'.
                    appUpdateInfo,
                    // an activity result launcher registered via registerForActivityResult
                    activityResultLauncher,
                    // Or pass 'AppUpdateType.FLEXIBLE' to newBuilder() for
                    // flexible updates.
                    AppUpdateOptions.newBuilder(appUpdateType).build(),
                )
            } else {
                openStoreForApp(CommonConstants.VISIT_SAUDI_GOOGLE_PLAY_PACKAGE_NAME)
            }
        }.addOnFailureListener {
            openStoreForApp(CommonConstants.VISIT_SAUDI_GOOGLE_PLAY_PACKAGE_NAME)
        }
    }

    private fun fetchRemoteConfig() {
        RemoteConfigService.fetchRemoteConfigResult = {
            isRemoteConfigFetched = true
            checkAndRedirect()
            splashViewModel.initSplash()
        }
    }

    private fun checkAndRedirect() {
        if (splashViewModel.appUpdateFetched.value != null && splashViewModel.languageFetched.value != null &&
            splashViewModel.appMaintenanceFetched.value != null && isUpdateDialogHidden &&
            splashViewModel.startDelayFetched.value == true && !isFinishing && !isAppUnderMaintenance && isRemoteConfigFetched &&
            splashViewModel.appconfigurationsFetched.value == true
            && splashViewModel.onBoardingFetched.value != null
        ) {
            val intent = Intent(this@SplashScreenActivity, MainActivity::class.java)
            startActivity(intent)
            finish()
        }
    }

    // we need to handle new links if new intent has been instantiated
//    override fun onNewIntent(intent: Intent) {
//        super.onNewIntent(intent)
//        checkIntentForDeepLinks(intent)
//    }

    // Handle DynamicLinks & Universal Links
//    private fun checkIntentForDeepLinks(intent: Intent?) {
//        if (intent == null) {
//            return
//        }
//
//        val deepLinksManager = DeepLinksManager()
//        deepLinksManager.initWithIntent(intent)
//        val notificationModel = deepLinksManager.getDeepLinkNotificationModel()
//        notificationModel?.let {
//            sharedPreferencesManager.notificationModel = it
//        }
//    }
}