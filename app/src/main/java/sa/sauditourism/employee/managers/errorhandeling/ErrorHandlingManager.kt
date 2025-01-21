package sa.sauditourism.employee.managers.errorhandeling

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import sa.sauditourism.employee.R
import sa.sauditourism.employee.components.CustomAlertDialog
import sa.sauditourism.employee.components.DialogType
import sa.sauditourism.employee.components.WarningPage
import sa.sauditourism.employee.constants.LanguageConstants
import sa.sauditourism.employee.extensions.localizeString
import sa.sauditourism.employee.managers.network.helpers.ErrorCodes
import sa.sauditourism.employee.managers.network.helpers.NetworkError
import java.net.HttpURLConnection

/**
 * Manager class responsible for error handling
 */

class ErrorHandlingManager {
    /**
     * Handle network error received from api
     */
    @Composable
    fun HandleError(
        error: NetworkError?,
        isDataEmpty: Boolean,
        showBack: Boolean,
        withTab: Boolean,
        dismissCallback: () -> Unit,
        callback: () -> Unit,
    ) {
        var showError by remember { mutableStateOf(false) }
        var message =
            if (error?.message.isNullOrEmpty()) LanguageConstants.UNEXPECTED_ERROR_HAS_OCCURRED.localizeString() else error?.message
        if (error?.errorCode == ErrorCodes.NO_CONNECTION && isDataEmpty) {
            WarningPage(
                modifier = Modifier,
                shouldIncludeBackButton = showBack,
                arrowImage = R.drawable.ic_arrow_left_tail,
                mainImageResId = R.drawable.ic_no_signal,
                title = LanguageConstants.NO_CONNECTION.localizeString(),
                description = LanguageConstants.NO_CONNECTION_MESSAGE.localizeString(),
                warningBgColor = Color.White,
                buttonText = LanguageConstants.TRY_AGAIN.localizeString(),
                buttonClickListener = {
                    callback.invoke()
                },
                dialogType = DialogType.Normal,
            )
        } else if (error?.errorCode == ErrorCodes.NO_CONNECTION) {
            showError = true
            message = LanguageConstants.NO_CONNECTION_MESSAGE.localizeString()
        } else if (error?.code == HttpURLConnection.HTTP_FORBIDDEN) {
            showError = true
            message = LanguageConstants.FORBIDDEN_MESSAGE.localizeString()
        } else {
            showError = true
            message = message ?: LanguageConstants.UNEXPECTED_ERROR_HAS_OCCURRED.localizeString()
        }

        if (showError) {
            CustomAlertDialog(
                type = DialogType.Failed,
                imageResId = R.drawable.ic_attention,
                title = LanguageConstants.ERROR_TITLE.localizeString(),
                description = message,
                primaryButtonText = LanguageConstants.RETRY.localizeString(),
                secondaryButtonText = LanguageConstants.CANCEL.localizeString(),
                onPrimaryButtonClick = {
                    callback.invoke()
                    showError = false
                },
                onSecondaryButtonClick = {
                    dismissCallback.invoke()
                    showError = false
                },
                onDismiss = {
                    dismissCallback.invoke()
                    // Handle dialog dismiss
                    showError = false
                },
            )
        }
    }
}

