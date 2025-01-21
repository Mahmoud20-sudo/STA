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
import sa.sauditourism.employee.LocalLanguage
import sa.sauditourism.employee.components.ButtonComponent
import sa.sauditourism.employee.components.ButtonSize
import sa.sauditourism.employee.components.ButtonType
import sa.sauditourism.employee.components.ToastPresentable
import sa.sauditourism.employee.components.ToastType
import sa.sauditourism.employee.components.ToastView
import sa.sauditourism.employee.constants.LanguageConstants

@Composable
fun TestToasters() {
    CompositionLocalProvider(LocalLanguage provides LanguageConstants.DEFAULT_LOCALE) {

        var showSuccessToaster by remember { mutableStateOf(false) }
        var showDefaultToaster by remember { mutableStateOf(false) }
        var showWarningToaster by remember { mutableStateOf(false) }
        var showErrorToaster by remember { mutableStateOf(false) }

        Column(
            Modifier.fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {

            ButtonComponent(
                text = "Show Success Toast",
                clickListener = { showSuccessToaster = true },
                modifier = Modifier,
                buttonType = ButtonType.PRIMARY,
                buttonSize = ButtonSize.LARGE,
            )


            Spacer(modifier = Modifier.height(16.dp))

            ButtonComponent(
                text = "Show Error Toast",
                clickListener = { showErrorToaster = true },
                modifier = Modifier,
                buttonType = ButtonType.PRIMARY,
                buttonSize = ButtonSize.LARGE,
            )


            Spacer(modifier = Modifier.height(16.dp))

            ButtonComponent(
                text = "Show Default Toast",
                clickListener = { showDefaultToaster = true },
                modifier = Modifier,
                buttonType = ButtonType.PRIMARY,
                buttonSize = ButtonSize.LARGE,
            )

            Spacer(modifier = Modifier.height(16.dp))

            ButtonComponent(
                text = "Show Warning Toast",
                clickListener = { showWarningToaster = true },
                modifier = Modifier,
                buttonType = ButtonType.PRIMARY,
                buttonSize = ButtonSize.LARGE,
            )
        }

        if (showErrorToaster) {
            ToastView(
                presentable = ToastPresentable(
                    type = ToastType.Error,
                    title = "Error Toast Title", "Short Description",
                ),
                toastDuration = 5000L,
                onDismiss = {
                    showErrorToaster = false
                },
            )
        }
        if (showSuccessToaster) {
            ToastView(
                presentable = ToastPresentable(
                    type = ToastType.Success,
                    title = "Success Toast Title", "Short Description",
                ),
                toastDuration = 5000L,
                onDismiss = {
                    showSuccessToaster = false
                },
            )
        }
        if (showWarningToaster) {
            ToastView(
                presentable = ToastPresentable(
                    type = ToastType.Warning,
                    title = "Warning Toast Title", "Short Description",
                ),
                toastDuration = 5000L,
                onDismiss = {
                    showWarningToaster = false
                },
            )
        }
        if (showDefaultToaster) {
            ToastView(
                presentable = ToastPresentable(
                    type = ToastType.Default,
                    title = "Default Toast Title",
                ),
                toastDuration = 5000L,
                onDismiss = {
                    showDefaultToaster = false
                },
            )
        }
    }
}
