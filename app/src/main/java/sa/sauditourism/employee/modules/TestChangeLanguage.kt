package sa.sauditourism.employee.modules

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Intent
import androidx.activity.ComponentActivity
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.intl.Locale
import androidx.compose.ui.unit.dp
import sa.sauditourism.employee.EmployeeApplication.Companion.sharedPreferencesManager
import sa.sauditourism.employee.LocalLanguage
import sa.sauditourism.employee.R
import sa.sauditourism.employee.components.ButtonComponent
import sa.sauditourism.employee.components.ButtonSize
import sa.sauditourism.employee.components.ButtonType
import sa.sauditourism.employee.components.CustomAlertDialog
import sa.sauditourism.employee.components.DialogType
import sa.sauditourism.employee.components.DropDownType
import sa.sauditourism.employee.components.InputFieldComponent
import sa.sauditourism.employee.components.InputFieldModel
import sa.sauditourism.employee.components.InputFieldType
import sa.sauditourism.employee.constants.LanguageConstants
import sa.sauditourism.employee.extensions.clickableWithoutRipple
import sa.sauditourism.employee.extensions.localizeString
import sa.sauditourism.employee.extensions.observeNonNull
import sa.sauditourism.employee.managers.language.LanguageManager
import sa.sauditourism.employee.managers.language.model.SupportedLanguage
import sa.sauditourism.employee.modules.splash.SplashScreenActivity

@SuppressLint("MutableCollectionMutableState")
@Composable
fun TestChangeLanguage() {

    val context = LocalContext.current
    val originalModels by remember(LanguageManager.supportedLanguages) {
        mutableStateOf(
            LanguageManager.supportedLanguages
        )
    }
    val filteredLocale =
        originalModels.filter { it.code == sharedPreferencesManager.preferredLocale }
    val selectedLocale: SupportedLanguage? by remember { mutableStateOf(filteredLocale.firstOrNull()) }

    val expanded = remember { mutableStateOf(false) }

    Box(modifier = Modifier
        .fillMaxSize()
        .padding(horizontal = 24.dp, vertical = 16.dp)) {
        InputFieldComponent(
            title = LanguageConstants.CHANGE_LANGUAGE_TITLE.localizeString(),
            informativeMessage = LanguageConstants.NOTE.localizeString(),
            value = selectedLocale?.value ?: "",
            inputFieldModel = InputFieldModel(
                placeholderText = "Search Here",
                type = InputFieldType.Dropdown,
                dropdownWithSearchIcon = false
            ),
            originalModels = originalModels,
            dropDownType = DropDownType.LanguageSelection,
            readOnly = true,
            clickable = true,
            onDropDownItemSelected = { code,_ ->
                if (selectedLocale == null || selectedLocale?.code == code) {
                    return@InputFieldComponent
                }

//                LanguageManager.changeLanguage(code as String)
//                LanguageManager.localizationChanged.observeNonNull(context as ComponentActivity) {
//                    if (it) {
//                        context.startActivity(
//                            Intent(
//                                context,
//                                SplashScreenActivity::class.java
//                            )
//                        )
//                        (context as Activity).finishAffinity()
//                        LanguageManager.localizationChanged.removeObservers(context)
//                    }
//                }
            },
            onValueChange = {},
            selectedDropDownItem = selectedLocale
        )
    }

}

