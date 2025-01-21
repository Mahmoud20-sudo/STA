package sa.sauditourism.employee

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.content.res.Configuration
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.calculateEndPadding
import androidx.compose.foundation.layout.calculateStartPadding
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.compositionLocalOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.platform.LocalLayoutDirection
import androidx.compose.ui.unit.LayoutDirection
import androidx.compose.ui.unit.dp
import androidx.core.content.ContextCompat
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import dagger.hilt.android.AndroidEntryPoint
import sa.sauditourism.employee.EmployeeApplication.Companion.sharedPreferencesManager
import sa.sauditourism.employee.components.ToastPresentable
import sa.sauditourism.employee.components.ToastType
import sa.sauditourism.employee.components.ToastView
import sa.sauditourism.employee.constants.LanguageConstants
import sa.sauditourism.employee.extensions.isNotNull
import sa.sauditourism.employee.extensions.localizeString
import sa.sauditourism.employee.extensions.toggleFullScreen
import sa.sauditourism.employee.managers.errorhandeling.ErrorHandlingManager
import sa.sauditourism.employee.managers.network.NetworkStatusLiveData
import sa.sauditourism.employee.managers.network.helpers.ErrorCodes
import sa.sauditourism.employee.managers.network.helpers.NetworkError
import sa.sauditourism.employee.managers.network.models.UiState
import sa.sauditourism.employee.modules.BottomNavigation
import sa.sauditourism.employee.modules.login.LoginScreen
import sa.sauditourism.employee.modules.login.NavigationGraph
import sa.sauditourism.employee.modules.login.Routes
import sa.sauditourism.employee.modules.login.Routes.ACTIVITIES_SCREEN
import sa.sauditourism.employee.modules.login.Routes.ANNOUNCEMENTS_SCREEN
import sa.sauditourism.employee.modules.login.Routes.AVAILABLE_MEETING_ROOMS
import sa.sauditourism.employee.modules.login.Routes.BOOK_MEETING_ROOM
import sa.sauditourism.employee.modules.login.Routes.BOOK_MEETING_ROOM
import sa.sauditourism.employee.modules.login.Routes.LOCAL_PARTICIPANTS_SCREEN
import sa.sauditourism.employee.modules.login.Routes.PARTICIPANTS_SCREEN
import sa.sauditourism.employee.modules.login.Routes.SINGLE_MEETING_ROOM
import sa.sauditourism.employee.modules.login.Routes.SUCCESSFULLY_BOOKED_SCREEN
import sa.sauditourism.employee.modules.onboarding.OnboardingScreen
import sa.sauditourism.employee.network.status.model.NetworkStatus
import sa.sauditourism.employee.resources.theme.EmployeeTheme
import sa.sauditourism.employee.resources.theme.ThemeViewModel
import java.net.URI
import java.net.URLDecoder
import javax.inject.Inject


val LocalLanguage = compositionLocalOf<String> { error("No Language provided") }
val LocalNavController = compositionLocalOf<NavHostController> { error("No NavController found!") }
val LocalLoggedIn = compositionLocalOf { sharedPreferencesManager.accessToken.isNullOrEmpty() }

val routesList: List<String>
    get() = listOf(
        "maintainance",
        Routes.PAYSLIP_SCREEN,
        "${Routes.REQUESTS_DETAILS}/{id}",
        "${Routes.REQUESTS_FORM}/{service_id}/{id}/{title}",
        "${Routes.REQUESTS_TYPES}/{service_id}/{service_title}",
        "${Routes.MY_REQUESTS_SCREEN}/{id}",
        "ATTACHMENT_COMPONENT",
        "DIGITAL_CARD",
        "SETTINGS_SCREEN",
        "$ACTIVITIES_SCREEN/{requestId}",
        PARTICIPANTS_SCREEN,
        ANNOUNCEMENTS_SCREEN,
        Routes.BOOK_MEETING_ROOM,
        "$AVAILABLE_MEETING_ROOMS/{date}/{start_time}/{end_time}",
        "${SINGLE_MEETING_ROOM}/{start_time}/{end_time}/{date}",
        LOCAL_PARTICIPANTS_SCREEN,
        SUCCESSFULLY_BOOKED_SCREEN,
        ANNOUNCEMENTS_SCREEN,
    )

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    private lateinit var mainViewModel: MainViewModel
    private val themeViewModel: ThemeViewModel by viewModels()

    @Inject
    lateinit var errorHandlingManager: ErrorHandlingManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            val navController = rememberNavController()
            mainViewModel = viewModel<MainViewModel>()

            var isLoggedIn by remember {
                mutableStateOf(
                    !sharedPreferencesManager.accessToken.isNullOrEmpty()
                )
            }

            // State of bottomBar, set state to false, if current page route is "car_details"
            val bottomBarState = rememberSaveable { (mutableStateOf(true)) }

            // State of onboarding
            val onBoardingVersion =
                rememberSaveable { (mutableStateOf(sharedPreferencesManager.onBoardingVersion)) }

            // Subscribe to navBackStackEntry, required to get current route
            val navBackStackEntry by navController.currentBackStackEntryAsState()
            // Control TopBar and BottomBar

            bottomBarState.value = !routesList.contains(navBackStackEntry?.destination?.route)

            val authState = mainViewModel.event.collectAsState()

            val layoutDirection =
                if (sharedPreferencesManager.preferredLocale.orEmpty() == "ar") LayoutDirection.Rtl else LayoutDirection.Ltr

            val onBoarding =
                sharedPreferencesManager.onBoardingVersion?.let {
                    sharedPreferencesManager.onBoarding?.firstOrNull { it.version == onBoardingVersion.value }
                }
                    ?: sharedPreferencesManager.onBoarding?.firstOrNull { it.version == BuildConfig.VERSION_NAME }

            val isConnected = NetworkStatusLiveData(EmployeeApplication.instance).observeAsState()
            var showToaster by remember { mutableStateOf(false) }
            var showWarningPage by remember { mutableStateOf(false) }
            var showOnBoarding by remember { mutableStateOf(false) }
            showOnBoarding =
                !onBoarding?.list.isNullOrEmpty() && onBoardingVersion.value != BuildConfig.VERSION_NAME

            val networkStatus = mainViewModel.networkStatus.collectAsState()

            LaunchedEffect(isConnected.value) {
                if (isConnected.value != null) {
                    showToaster = true
                }
            }

            LaunchedEffect(networkStatus.value) {
                showWarningPage = networkStatus.value == NetworkStatus.Disconnected
            }

            LaunchedEffect(authState.value) {
                if (authState.value is MainViewModel.SSIDEvent.ShowData) {
                    isLoggedIn = sharedPreferencesManager.accessToken.isNotNull()
                } else if (authState.value is MainViewModel.SSIDEvent.Error) {
                    isLoggedIn = sharedPreferencesManager.accessToken.isNotNull()
                }
            }

            EmployeeTheme(
                themeViewModel = themeViewModel,
                systemInDarkTheme = !isLoggedIn || showOnBoarding
            ) {
                CompositionLocalProvider(
                    LocalLanguage provides sharedPreferencesManager.preferredLocale.orEmpty(),
                    LocalNavController provides navController,
                    LocalLayoutDirection provides layoutDirection,
                    LocalLoggedIn provides isLoggedIn
                ) {
                    println("Access token: ${sharedPreferencesManager.accessToken}")

                    if (showOnBoarding)
                        OnboardingScreen(onBoarding = onBoarding) {
                            sharedPreferencesManager.onBoardingVersion = it
                            onBoardingVersion.value = it
                        }
                    else if (!isLoggedIn)
                        LoginScreen(
                            navController,
                            isLoading = authState.value is MainViewModel.SSIDEvent.Loading
                        ) { mainViewModel.doLogin(true) }
                    else
                    Scaffold(
                        modifier = Modifier
                            .fillMaxSize(),
                        containerColor = MaterialTheme.colorScheme.background,
                        bottomBar = {
                                BottomNavigation(
                                    navController = navController,
                                    modifier = Modifier
                                        .graphicsLayer {
                                            clip = true
                                            shape = RoundedCornerShape(
                                                topStart = 16.dp,
                                                topEnd = 16.dp
                                            )
                                            shadowElevation = 5f
                                        }
                                        .background(Color.White)
                                        .fillMaxWidth()
                                        .navigationBarsPadding(),
                                    showBottomBar = bottomBarState.value,
                                )
                        }
                    ) { innerPadding ->
                        ObserveConnectivity(isConnected.value, showToaster) {
                            showToaster = false
                        }

                        if (showWarningPage)
                            errorHandlingManager.HandleError(
                                error = NetworkError(
                                    errorCode = ErrorCodes.NO_CONNECTION,
                                    code = 401,
                                    message = "Message",
                                    apiNumber = null,
                                    exception = null
                                ),
                                isDataEmpty = true,
                                showBack = false,
                                withTab = false,
                                dismissCallback = {
                                    showWarningPage =
                                        isConnected.value == null || isConnected.value == false
                                },
                                callback = {
                                    showWarningPage =
                                        isConnected.value == null || isConnected.value == false
                                }
                            )
                        else
                            NavigationGraph(
                                navController = navController, Modifier
                                    .padding(
                                        start = innerPadding.calculateStartPadding(
                                            layoutDirection
                                        ),
                                        end = innerPadding.calculateEndPadding(layoutDirection),
                                        top = innerPadding.calculateTopPadding()
                                    ),
                                mainViewModel = mainViewModel,
                                doLogout = { mainViewModel.doLogout() }
                            )
                    }
                }
            }
        }
    }

    fun handleIntentData(data: Intent?) {
        try {
            val url = data?.data.toString() ?: throw IllegalArgumentException("Intent data is null")
            val uri = URI(url)

            // Ensure query part exists
            val query = uri.query ?: throw IllegalArgumentException("No query found in URI")

            // Extract query parameters safely
            val queryParams = query.split("&").associate {
                val parts = it.split("=")
                if (parts.size != 2) throw IllegalArgumentException("Invalid query parameter format")
                val key = parts[0]
                val value = URLDecoder.decode(parts[1], "UTF-8")
                key to value
            }

            // Get tokens
            val jiraAccessToken = queryParams["jira_tokens_access_token"]
            val jiraRefreshToken = queryParams["jira_tokens_refresh_token"]

            // Validate tokens
            if (jiraAccessToken.isNullOrEmpty() || jiraRefreshToken.isNullOrEmpty()) {
                throw IllegalArgumentException("Missing required tokens")
            }

            sharedPreferencesManager.accessToken = jiraAccessToken
            sharedPreferencesManager.refreshToken = jiraRefreshToken
            mainViewModel.getProfile(jiraAccessToken = jiraAccessToken)
        } catch (e: Exception) {
            // Handle exceptions (e.g., log or show an error message)
            mainViewModel.emilFailState(e)
            println("Error handling intent data: ${e.message}")
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        handleIntentData(data)
//        if (requestCode == AUTH_REQ) {
//            val response = data?.let { AuthorizationResponse.fromIntent(it) }
//            val exception = AuthorizationException.fromIntent(data)
//            if (response != null) {
//                // Pass the response to a composable function
//                exchangeAuthorizationCode(response)
//            } else {
//                exception?.let {
//                    print(exception.error)
//                }
//            }
//            mainViewModel.doLogin(false)
//        }
    }


//    private fun exchangeAuthorizationCode(authResponse: AuthorizationResponse) {
//        // Your existing logic to exchange the code
//        val extraParams = mutableMapOf<String, String>()
//        extraParams["token_content_type"] = "jwt"
//        extraParams["client_secret"] =
//            EmployeeApplication.instance.environmentManager.getVariable(EnvironmentKeys.OPEN_AUTH_CLIENT_SECRET)
//        val tokenRequest = authResponse.createTokenExchangeRequest(extraParams)
//        val authService = AuthorizationService(EmployeeApplication.instance)
//
//        authService.performTokenRequest(tokenRequest) { response, exception ->
//            if (exception != null) {
//                print(exception.error)
//            }
//
//            response?.accessToken?.let { accessToken ->
//                // Save tokens and state
//                sharedPreferencesManager.accessToken = accessToken
//                sharedPreferencesManager.refreshToken = response.refreshToken
//                sharedPreferencesManager.authCode = authResponse.authorizationCode
//                sharedPreferencesManager.authState = authResponse.request.state.orEmpty()
//
//                //navigate to Home
//                mainViewModel.getProfileData()
//                mainViewModel.authorizeUser(accessToken)
//            }
//        }
//    }

    override fun onConfigurationChanged(newConfig: Configuration) {
        super.onConfigurationChanged(newConfig)
    }

    @Composable
    private fun ObserveConnectivity(
        isConnected: Boolean?,
        showToaster: Boolean,
        onDismiss: () -> Unit
    ) {

        if (showToaster) {
            val presentable = ToastPresentable(
                type = if (isConnected!!) ToastType.Success else ToastType.Warning,
                title = LanguageConstants.TOAST_CONNECTION.localizeString(),
                description = if (!isConnected) LanguageConstants.NO_CONNECTION_MESSAGE.localizeString() else null,
                customIcon = if (isConnected) R.drawable.ic_wifi else R.drawable.ic_no_wifi,
            )
            ToastView(
                presentable = presentable,
                toastDuration = 3000L,
                onDismiss = {
                    onDismiss.invoke()
                },
            )
        }
    }
}
