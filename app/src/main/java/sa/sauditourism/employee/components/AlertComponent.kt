package sa.sauditourism.employee.components

import androidx.compose.material3.AlertDialog
import androidx.compose.runtime.Composable
import androidx.compose.ui.window.DialogProperties
import sa.sauditourism.employee.models.AlertModel

enum class DialogType { Normal, Success, Failed }


@Composable
fun AlertComponent(alert: AlertModel, onDismiss: () -> Unit) {
    AlertDialog(
        onDismissRequest = { onDismiss() },
        properties = if (alert.cancelable) DialogProperties() else DialogProperties(false, false),

        title = {
            alert.title?.let { CustomTextComponent(text = it) }
        },
        text = {
            alert.message?.let { CustomTextComponent(text = it) }
        },
        confirmButton = {
            alert.positiveButton?.let { positiveText ->
                AlertDialogButtonComponent(
                    text = positiveText,
                    onClick = {
                        alert.onPositiveClick()
                        onDismiss()
                    },
                )
            }
        },
        dismissButton = {
            alert.negativeButton?.let { negativeText ->
                AlertDialogButtonComponent(
                    text = negativeText,
                    onClick = {
                        alert.onNegativeClick()
                        onDismiss()
                    },
                )
            }
        },
    )
}

@Composable
fun AlertDialogButtonComponent(text: String, onClick: () -> Unit) {
    ButtonComponent(text, clickListener = { //todo check this button look and feel
        onClick.invoke()
    }, buttonSize = ButtonSize.SMALL)
}
