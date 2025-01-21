package sa.sauditourism.employee.modules

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import sa.sauditourism.employee.LocalLanguage
import sa.sauditourism.employee.R
import sa.sauditourism.employee.components.ButtonComponent
import sa.sauditourism.employee.components.ButtonSize
import sa.sauditourism.employee.components.ButtonType
import sa.sauditourism.employee.components.CustomAlertDialog
import sa.sauditourism.employee.components.DialogType
import sa.sauditourism.employee.components.ToastPresentable
import sa.sauditourism.employee.components.ToastType
import sa.sauditourism.employee.components.ToastView
import sa.sauditourism.employee.constants.LanguageConstants
import sa.sauditourism.employee.extensions.localizeString

@Composable
fun TextAppUpdate(navController: NavHostController) {
    CompositionLocalProvider(LocalLanguage provides LanguageConstants.DEFAULT_LOCALE) {

        var showDefaultUpdate by remember { mutableStateOf(false) }
        var showForceUpdate by remember { mutableStateOf(false) }


        Column(
            Modifier.fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {

            ButtonComponent(
                text = "Test Update",
                clickListener = { showDefaultUpdate = true },
                modifier = Modifier,
                buttonType = ButtonType.PRIMARY,
                buttonSize = ButtonSize.LARGE,
            )


            Spacer(modifier = Modifier.height(16.dp))

            ButtonComponent(
                text = "Show Force Update",
                clickListener = { showForceUpdate = true },
                modifier = Modifier,
                buttonType = ButtonType.PRIMARY,
                buttonSize = ButtonSize.LARGE,
            )



        }

        if (showDefaultUpdate) {
            CustomAlertDialog(
                type = DialogType.Normal,
                modifier = Modifier,
                imageResId = R.drawable.ic_activities,
                title = LanguageConstants.UPDATE_REQUIRED.localizeString(),
                description = LanguageConstants.UPDATE_REQUIRED_DESCRIPTION.localizeString(),
                shouldShowCloseIcon = true,
                primaryButtonText = LanguageConstants.UPDATE_NOW.localizeString(),
                secondaryButtonText = LanguageConstants.UPDATE_LATER.localizeString(),
                shouldDismissDialogOnPrimary = false,
                onPrimaryButtonClick =
                {
                    showDefaultUpdate = false
                },
                onSecondaryButtonClick = {
                    showDefaultUpdate = false
                },
                onDismiss = {
                    showDefaultUpdate = false
                },
            )
        }

        if (showForceUpdate) {
            CustomAlertDialog(
                type = DialogType.Normal,
                modifier = Modifier,
                imageResId = R.drawable.ic_activities,
                title = LanguageConstants.UPDATE_REQUIRED.localizeString(),
                description = LanguageConstants.UPDATE_REQUIRED_DESCRIPTION.localizeString(),
                shouldShowCloseIcon = false,
                primaryButtonText = LanguageConstants.UPDATE_NOW.localizeString(),
                shouldDismissDialogOnPrimary = false,
                onPrimaryButtonClick =
                {
                    showForceUpdate = false
                },
                onSecondaryButtonClick = {
                    showForceUpdate = false
                },
                onDismiss = {
                },
            )
        }

    }
}
