package sa.sauditourism.employee.activity

import android.content.Context
import android.content.res.Configuration
import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.material3.Surface
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import sa.sauditourism.employee.EmployeeApplication
import sa.sauditourism.employee.LocalLanguage
import sa.sauditourism.employee.LocalNavController
import sa.sauditourism.employee.R
import sa.sauditourism.employee.components.WebViewComponent
import sa.sauditourism.employee.constants.CommonConstants
import sa.sauditourism.employee.di.SharedPreferencesModule
import sa.sauditourism.employee.managers.locale.LocaleChanger
import sa.sauditourism.employee.managers.sharedprefrences.SharedPreferencesManager
import sa.sauditourism.employee.resources.theme.EmployeeTheme
import sa.sauditourism.employee.resources.theme.ThemeViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class WebViewActivity : ComponentActivity() {

    private val themeViewModel: ThemeViewModel by viewModels()

    override fun onConfigurationChanged(newConfig: Configuration) {
        super.onConfigurationChanged(newConfig)
        LocaleChanger.overrideLocale(this)
    }

    override fun attachBaseContext(base: Context) {
        super.attachBaseContext(LocaleChanger.wrapContext(base))
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)



        if (Build.VERSION.SDK_INT >= 34) {
            overrideActivityTransition(
                OVERRIDE_TRANSITION_OPEN,
                R.anim.slide_in_from_bottom,
                0
            )
        } else {
            overridePendingTransition(
                0, R.anim.slide_out_to_bottom
            )
        }

        val url = if (intent.hasExtra(CommonConstants.OPEN_URL_INTENT_EXTRA)) {
            intent.getStringExtra(CommonConstants.OPEN_URL_INTENT_EXTRA)
        } else {
            null
        }
        val headerTitle = if (intent.hasExtra(CommonConstants.OPEN_URL_HEADER_INTENT_EXTRA)) {
            intent.getStringExtra(CommonConstants.OPEN_URL_HEADER_INTENT_EXTRA)
        } else {
            null
        }
        val shouldRedirectToDeeplink =
            intent.getBooleanExtra(CommonConstants.OPEN_URL_WITH_REDIRECTION, true)




        setContent {

            val sharedPreferencesManager: SharedPreferencesManager =
                SharedPreferencesModule.provideSharedPreferencesManager(EmployeeApplication.instance.applicationContext)

            val navController: NavHostController = rememberNavController()

            EmployeeTheme(
                themeViewModel = themeViewModel,
                systemInDarkTheme = sharedPreferencesManager.darkMode,
            ) {
                Surface(
                    Modifier
                        .background(Color.White)
                        .statusBarsPadding()
                ) {
                    CompositionLocalProvider(
                        LocalLanguage provides SharedPreferencesManager(this).preferredLocale.orEmpty(),
                        LocalNavController provides navController
                    ) {

                        WebViewComponent(
                            url = url ?: "https://www.visitsaudi.com/en/",
                            title = headerTitle ?: "",
                            shouldRedirectToDeeplink = shouldRedirectToDeeplink,
                            onCloseButtonClicked = {
                                finish()

                                if (Build.VERSION.SDK_INT >= 34) {
                                    overrideActivityTransition(
                                        OVERRIDE_TRANSITION_CLOSE,
                                        0, R.anim.slide_out_to_bottom
                                    )
                                } else {
                                    overridePendingTransition(
                                        0, R.anim.slide_out_to_bottom
                                    )
                                }
                            })
                    }
                }
            }
        }
    }

    override fun finish() {
        super.finish()

        if (Build.VERSION.SDK_INT >= 34) {
            overrideActivityTransition(
                OVERRIDE_TRANSITION_CLOSE,
                0, R.anim.slide_out_to_bottom
            )
        } else {
            overridePendingTransition(
                0, R.anim.slide_out_to_bottom
            )
        }
    }
}
