package sa.sauditourism.employee.activity

import android.content.Context
import android.content.Intent
import android.content.res.Configuration
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import sa.sauditourism.employee.EmployeeApplication
import sa.sauditourism.employee.LocalLanguage
import sa.sauditourism.employee.R
import sa.sauditourism.employee.modules.splash.SplashScreenActivity
import sa.sauditourism.employee.components.CustomTextComponent
import sa.sauditourism.employee.components.ListComponent
import sa.sauditourism.employee.components.ListComponentType
import sa.sauditourism.employee.managers.environment.EnvironmentManager
import sa.sauditourism.employee.managers.locale.LocaleChanger
import sa.sauditourism.employee.managers.sharedprefrences.SharedPreferencesManager
import sa.sauditourism.employee.resources.theme.EmployeeTheme
import sa.sauditourism.employee.resources.theme.ThemeViewModel
import sa.sauditourism.employee.ui.theme.AppFonts
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class EnvironmentActivity : ComponentActivity() {

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
        setContent {
            EmployeeTheme(
                themeViewModel = themeViewModel,
                systemInDarkTheme = SharedPreferencesManager(this).darkMode,
            ) {
                CompositionLocalProvider(LocalLanguage provides SharedPreferencesManager(this).preferredLocale.orEmpty()) {
                    EnvironmentScreen()
                }
            }
        }
    }

    @Composable
    fun EnvironmentScreen() {
        Box(
            modifier =
            Modifier
                .fillMaxSize()
                .padding(15.dp),
        ) {
            ListComponent(
                items = listOf(
                    EnvironmentManager.ENV_ACC,
                    EnvironmentManager.ENV_QA,
                    EnvironmentManager.ENV_PROD,
                    EnvironmentManager.ENV_PRODCH,
                ),
                onItemClick = {
                    EmployeeApplication.instance.environmentManager.changeEnv(it)
                    restartApp()
                },
                itemContent = {
                    Row {
                        CustomTextComponent(
                            text = it,
                            style = AppFonts.body1Medium,
                            modifier = Modifier
                                .weight(1f),
                        )
                        if (EmployeeApplication.sharedPreferencesManager.currentEnv == it) {
                            Icon(
                                painter = painterResource(id = R.drawable.ic_check),
                                "",
                                modifier = Modifier.size(24.dp),
                            )
                        }
                    }
                },
                listComponentType = ListComponentType.Vertical
            )
        }
    }

    private fun restartApp() {
        startActivity(
            Intent(
                this,
                SplashScreenActivity::class.java,
            ),
        )
        finishAffinity()
    }
}
