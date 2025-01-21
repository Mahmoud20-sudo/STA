package sa.sauditourism.employee.modules.account

import androidx.hilt.navigation.compose.hiltViewModel
import android.app.Activity
import android.content.Intent
import androidx.activity.ComponentActivity
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLayoutDirection
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.unit.LayoutDirection
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import sa.sauditourism.employee.EmployeeApplication
import sa.sauditourism.employee.EmployeeApplication.Companion.sharedPreferencesManager
import sa.sauditourism.employee.LocalLanguage
import sa.sauditourism.employee.R
import sa.sauditourism.employee.components.ButtonComponent
import sa.sauditourism.employee.components.ButtonSize
import sa.sauditourism.employee.components.ButtonType
import sa.sauditourism.employee.components.CustomTextComponent
import sa.sauditourism.employee.components.DropDownType
import sa.sauditourism.employee.components.HeaderComponent
import sa.sauditourism.employee.components.HeaderModel
import sa.sauditourism.employee.components.InputFieldComponent
import sa.sauditourism.employee.components.InputFieldModel
import sa.sauditourism.employee.components.InputFieldType
import sa.sauditourism.employee.constants.LanguageConstants
import sa.sauditourism.employee.extensions.localizeString
import sa.sauditourism.employee.extensions.observeNonNull
import sa.sauditourism.employee.extensions.openExternalBrowser
import sa.sauditourism.employee.managers.environment.EnvironmentKeys.CHANGE_PASSWORD_URL
import sa.sauditourism.employee.managers.language.LanguageManager
import sa.sauditourism.employee.managers.language.model.SupportedLanguage
import sa.sauditourism.employee.modules.splash.SplashScreenActivity
import sa.sauditourism.employee.resources.AppColors
import sa.sauditourism.employee.resources.AppFonts

@Composable
fun SettingsScreen(
    navController: NavController,
    originalModels: ArrayList<SupportedLanguage>
) {

    val context = LocalContext.current

    val filteredLocale =
        originalModels.first() { it.code == sharedPreferencesManager.preferredLocale }

    val selectedCode = remember { mutableStateOf(filteredLocale.code) }

    val layoutDirection =
        if (sharedPreferencesManager.preferredLocale == "ar") LayoutDirection.Rtl else LayoutDirection.Ltr

    CompositionLocalProvider(
        LocalLanguage provides sharedPreferencesManager.preferredLocale.orEmpty(),
        LocalLayoutDirection provides layoutDirection
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(vertical = 16.dp)
        ) {

            HeaderComponent(
                HeaderModel(
                    title = LanguageConstants.ACCOUNT_SETTINGS.localizeString()
                ),
                isStandAlone = true,
                showShadow = false,
                backgroundColor = Color.Transparent,
                backHandler = { navController.popBackStack() }
            )

            Spacer(modifier = Modifier.height(31.dp))

            Row(
                modifier = Modifier
                    .padding(horizontal = 24.dp)
                    .fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                CustomTextComponent(
                    text = LanguageConstants.LOGIN_CHANGE_PASSWORD_TITLE.localizeString(),
                    color = AppColors.BLACK,
                    style = AppFonts.subtitle2SemiBold,
                    modifier = Modifier.weight(1f)
                )

                ButtonComponent(
                    text = LanguageConstants.LOGIN_CHANGE_PASSWORD_TITLE.localizeString()
                        .split(" ")[0],
                    clickListener = {
                        context.openExternalBrowser(
                            url =
                            EmployeeApplication.instance.environmentManager.getVariable(
                                CHANGE_PASSWORD_URL
                            )
                        )
                    },
                    modifier = Modifier,
                    buttonType = ButtonType.LINK_PRIMARY,
                    buttonSize = ButtonSize.MEDIUM,
                    endIcon = if (filteredLocale.code == "ar") R.drawable.ic_export else R.drawable.ic_export_ar,
                    removePadding = true
                )
            }

            Spacer(modifier = Modifier.height(48.dp))

            InputFieldComponent(
                value = filteredLocale.value,
                title = LanguageConstants.ACCOUNT_PREFERRED_LANGUAGE.localizeString(),
                inputFieldModel = InputFieldModel(
                    type = InputFieldType.Dropdown,
                    dropdownWithSearchIcon = false
                ),
                originalModels = originalModels,
                dropDownType = DropDownType.LanguageSelection,
                readOnly = true,
                clickable = true,
                onDropDownItemSelected = { code,_ ->
                    selectedCode.value = code.toString()
                },
                onValueChange = {},
                selectedDropDownItem = filteredLocale,
                modifier = Modifier
                    .testTag("InputFieldComponent")
                    .padding(horizontal = 24.dp)
            )

            Spacer(modifier = Modifier.height(26.dp))

            ButtonComponent(
                text = LanguageConstants.ACCOUNT_UPDATE_BUTTON.localizeString(),
                clickListener = {
                    if (filteredLocale.code == selectedCode.value) {
                        return@ButtonComponent
                    }

                    LanguageManager.changeLanguage(selectedCode.value)
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
                },
                modifier = Modifier
                    .padding(horizontal = 24.dp)
                    .fillMaxWidth()
                    .height(48.dp),
                buttonType = ButtonType.PRIMARY,
                buttonSize = ButtonSize.LARGE
            )
        }
    }
}