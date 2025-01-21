package sa.sauditourism.employee.modules

import android.net.Uri
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import sa.sauditourism.employee.LocalLanguage
import sa.sauditourism.employee.R
import sa.sauditourism.employee.components.AttachmentComponent
import sa.sauditourism.employee.components.ButtonComponent
import sa.sauditourism.employee.components.ButtonSize
import sa.sauditourism.employee.components.ButtonType
import sa.sauditourism.employee.components.CustomAlertDialog
import sa.sauditourism.employee.components.DialogType
import sa.sauditourism.employee.constants.LanguageConstants
import sa.sauditourism.employee.extensions.localizeString

@Composable
fun TestAttachment() {
    val attachmentsList = remember { mutableStateListOf<Uri>() }
    AttachmentComponent(
        title = "ATTACHMENT",
        attachmentsList = attachmentsList,
        isOptional = true,
        attachmentListener = { attachment, isAdd ->
            when (isAdd) {
                true -> attachmentsList.add(attachment)
                else -> attachmentsList.remove(attachment)
            }
        }
    )
}

