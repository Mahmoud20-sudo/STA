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
import sa.sauditourism.employee.R
import sa.sauditourism.employee.components.ButtonComponent
import sa.sauditourism.employee.components.ButtonSize
import sa.sauditourism.employee.components.ButtonType
import sa.sauditourism.employee.components.CustomAlertDialog
import sa.sauditourism.employee.components.DialogType
import sa.sauditourism.employee.constants.LanguageConstants

@Composable
fun TestPopups() {
    var showDialog by remember { mutableStateOf(false) }
    var showOneButtonDialog by remember { mutableStateOf(false) }
    var showNonButtonDialog by remember { mutableStateOf(false) }

    CompositionLocalProvider(LocalLanguage provides LanguageConstants.DEFAULT_LOCALE) {
        Column(
            Modifier.fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {

            ButtonComponent(
                text = "Show Two Buttons Dialog",
                clickListener = { showDialog = true },
                modifier = Modifier,
                buttonType = ButtonType.PRIMARY,
                buttonSize = ButtonSize.MEDIUM,
            )

            Spacer(modifier = Modifier.height(16.dp))

            ButtonComponent(
                text = "Show One Buttons Dialog",
                clickListener = { showOneButtonDialog = true },
                modifier = Modifier,
                buttonType = ButtonType.PRIMARY,
                buttonSize = ButtonSize.MEDIUM,
            )

            Spacer(modifier = Modifier.height(16.dp))

            ButtonComponent(
                text = "Show None Buttons Dialog",
                clickListener = { showOneButtonDialog = true },
                modifier = Modifier,
                buttonType = ButtonType.PRIMARY,
                buttonSize = ButtonSize.MEDIUM,
            )
        }
    }

    if (showDialog)
        CustomAlertDialog(
            type = DialogType.Success,
            imageResId = R.drawable.ic_success,
            title = "Title Will goes\n" +
                    " here.",
            description = "Lorem Ipsum is simply dummy text of the printing and typesetting industry.",
            primaryButtonText = "TEST",
            secondaryButtonText = "TEST",
            onPrimaryButtonClick = {
                // Handle primary button click
                showDialog = false
            },
            onSecondaryButtonClick = {
                showDialog = false
            },
            onDismiss = {
                // Handle alert dismiss
                showDialog = false
            }
        )
    if (showOneButtonDialog)
        CustomAlertDialog(
            type = DialogType.Failed,
            imageResId = R.drawable.ic_attention,
            title = "Title Will goes\n" +
                    " here.",
            description = "Lorem Ipsum is simply dummy text of the printing and typesetting industry.",
            primaryButtonText = "TEST",
            onPrimaryButtonClick = {
                // Handle primary button click
                showOneButtonDialog = false
            },
            onDismiss = {
                // Handle alert dismiss
                showOneButtonDialog = false
            }
        )
    if (showNonButtonDialog)
        CustomAlertDialog(
            type = DialogType.Success,
            imageResId = R.drawable.ic_activities,
            title = "Title Will goes\n" +
                    " here.",
            description = "Lorem Ipsum is simply dummy text of the printing and typesetting industry.",
            onDismiss = {
                // Handle alert dismiss
                showNonButtonDialog = false
            }
        )
}

