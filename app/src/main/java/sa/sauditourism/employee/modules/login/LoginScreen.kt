package sa.sauditourism.employee.modules.login

import android.app.Activity
import android.content.Intent
import androidx.activity.ComponentActivity
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.layoutId
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import ir.kaaveh.sdpcompose.sdp
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import sa.sauditourism.employee.EmployeeApplication
import sa.sauditourism.employee.EmployeeApplication.Companion.sharedPreferencesManager
import sa.sauditourism.employee.LocalLanguage
import sa.sauditourism.employee.R
import sa.sauditourism.employee.components.ButtonComponent
import sa.sauditourism.employee.components.ButtonSize
import sa.sauditourism.employee.components.ButtonType
import sa.sauditourism.employee.components.CustomTextComponent
import sa.sauditourism.employee.components.FiltersBottomSheet
import sa.sauditourism.employee.components.GalleryType
import sa.sauditourism.employee.components.Loader
import sa.sauditourism.employee.components.MediaComponent
import sa.sauditourism.employee.components.MediaContent
import sa.sauditourism.employee.constants.LanguageConstants
import sa.sauditourism.employee.constants.LanguageConstants.CHANGE_LANGUAGE_TITLE
import sa.sauditourism.employee.constants.LanguageConstants.LOGIN_BUTTON_TITLE
import sa.sauditourism.employee.constants.LanguageConstants.LOGIN_CHANGE_PASSWORD_TITLE
import sa.sauditourism.employee.constants.LanguageConstants.LOGIN_SUBTITLE
import sa.sauditourism.employee.constants.LanguageConstants.LOGIN_TITLE
import sa.sauditourism.employee.constants.TestTagsConstants.SELECT_LOCAL_TAG
import sa.sauditourism.employee.extensions.localizeString
import sa.sauditourism.employee.extensions.observeNonNull
import sa.sauditourism.employee.extensions.openBrowser
import sa.sauditourism.employee.managers.environment.EnvironmentKeys.CHANGE_PASSWORD_URL
import sa.sauditourism.employee.managers.language.LanguageManager
import sa.sauditourism.employee.managers.language.model.SupportedLanguage
import sa.sauditourism.employee.modules.common.ChangeStatusBarIconsColors
import sa.sauditourism.employee.modules.common.LoadingScreen
import sa.sauditourism.employee.modules.splash.SplashScreenActivity
import sa.sauditourism.employee.resources.AppColors
import sa.sauditourism.employee.resources.AppFonts


@Preview
@Composable
fun LoginScreenPreview() {
    LoginScreen(rememberNavController()) {}
}


@Composable
fun LoginScreen(
    navController: NavController,
    isLoading: Boolean = false,
    onLoginClick: () -> Unit
) {

    ChangeStatusBarIconsColors()

    val originalModels by remember(LanguageManager.supportedLanguages) {
        mutableStateOf(
            LanguageManager.supportedLanguages
        )
    }
    val filteredLocale =
        originalModels.filter { it.code == sharedPreferencesManager.preferredLocale }
    val selectedLocale: SupportedLanguage? by remember { mutableStateOf(filteredLocale.firstOrNull()) }

    var showLanguageSheet by remember { mutableStateOf(false) }

    val configuration = LocalConfiguration.current
    val context = LocalContext.current

    if (originalModels.isEmpty()) {
        LaunchedEffect(Unit) {
            val languageFetched: MutableStateFlow<Boolean?> = MutableStateFlow(false)
            CoroutineScope(Dispatchers.Main).launch {
                languageFetched.collect {
                    originalModels.clear()
                    originalModels.addAll(LanguageManager.supportedLanguages)
                }
            }
            LanguageManager.handleLocalization(languageFetched = languageFetched)
        }
    }

    val topLogo by remember {
        mutableStateOf(
            MediaContent(
                GalleryType.IMAGE,
                R.mipmap.login_logo
            )
        )
    }

    CompositionLocalProvider(LocalLanguage provides LanguageConstants.DEFAULT_LOCALE) {

        Column(
            modifier = Modifier
                .background(color = AppColors.OnPrimaryDark)
                .fillMaxSize()
                .padding(top = 55.sdp)
                .verticalScroll(rememberScrollState()),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            Row(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(horizontal = 24.dp)
            ) {
                MediaComponent(
                    mediaContent = topLogo,
                    modifier = Modifier
                        .size(width = 52.dp, height = 48.dp)
                )
                Spacer(modifier = Modifier.weight(1f))
                ButtonComponent(
                    text = selectedLocale?.code?.uppercase() ?: "",
                    clickListener = { showLanguageSheet = true },
                    modifier = Modifier
                        .padding(top = 7.sdp),
                    buttonType = ButtonType.WHITE_BACKGROUND,
                    buttonSize = ButtonSize.SMALL,
                    startIcon = R.drawable.ic_global,
                    shape = RoundedCornerShape(20.dp),
                    tag = SELECT_LOCAL_TAG
                )
            }

            Spacer(modifier = Modifier.height(50.sdp))

            Loader(
                lottie = R.raw.login_logo,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(217.sdp)
            )

            Spacer(modifier = Modifier.height(0.dp))

            CustomTextComponent(
                text = LOGIN_TITLE.localizeString(),
                style = AppFonts.heading0Medium,
                color = AppColors.WHITE,
                overflow = TextOverflow.Ellipsis,
                modifier = Modifier
                    .padding(horizontal = 24.dp),
                maxLines = 1,
            )

            Spacer(modifier = Modifier.height(24.dp))

            CustomTextComponent(
                text = LOGIN_SUBTITLE.localizeString(),
                style = AppFonts.heading3Medium,
                color = AppColors.WHITE,
                overflow = TextOverflow.Ellipsis,
                modifier = Modifier
                    .padding(horizontal = 24.dp),
                maxLines = 1,
            )

            Spacer(modifier = Modifier.height(56.sdp))

            AnimatedVisibility(
                visible = isLoading, Modifier
                    .padding(horizontal = 24.dp)
                    .fillMaxWidth()
            ) {
                LoadingScreen()
            }

            AnimatedVisibility(
                visible = !isLoading, Modifier
                    .padding(horizontal = 24.dp)
                    .fillMaxSize()
            ) {
                ButtonComponent(
                    LOGIN_BUTTON_TITLE.localizeString(),
                    clickListener = {
                        onLoginClick.invoke()
                    },
                    modifier = Modifier,
                    buttonType = ButtonType.WHITE_BACKGROUND,
                    buttonSize = ButtonSize.LARGE,
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            ButtonComponent(
                LOGIN_CHANGE_PASSWORD_TITLE.localizeString(),
                clickListener = {
                    context.openBrowser(
                        url = EmployeeApplication.instance.environmentManager.getVariable(
                            CHANGE_PASSWORD_URL
                        )
                    )
                },
                modifier = Modifier
                    .padding(horizontal = 24.dp)
                    .fillMaxSize(),
                buttonType = ButtonType.LINK_WHITE,
                buttonSize = ButtonSize.LARGE
            )

            if (showLanguageSheet)
                FiltersBottomSheet(
                    title = CHANGE_LANGUAGE_TITLE.localizeString(),
                    originalModels = originalModels,
                    font = AppFonts.heading6SemiBold,
                    onCloseClick = {showLanguageSheet = false}
                ) { code ->
                    showLanguageSheet = false

                    if (selectedLocale == null || selectedLocale?.code == code) {
                        return@FiltersBottomSheet
                    }

                    LanguageManager.changeLanguage(code)
                    LanguageManager.localizationChanged.observeNonNull(context as ComponentActivity) {
                        if (it) {
                            context.startActivity(
                                Intent(
                                    context,
                                    SplashScreenActivity::class.java
                                )
                            )
                            (context as Activity).finishAffinity()
                            LanguageManager.localizationChanged.removeObservers(context)
                        }
                    }
                }
        }
    }
}