package sa.sauditourism.employee.modules.activities.addComment

import android.content.Context
import android.net.Uri
import android.provider.OpenableColumns
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import androidx.hilt.navigation.compose.hiltViewModel
import sa.sauditourism.employee.R
import sa.sauditourism.employee.components.AttachmentComponent
import sa.sauditourism.employee.components.ButtonComponent
import sa.sauditourism.employee.components.ButtonSize
import sa.sauditourism.employee.components.ButtonType
import sa.sauditourism.employee.components.CustomAlertDialog
import sa.sauditourism.employee.components.DialogType
import sa.sauditourism.employee.components.FooterComponent
import sa.sauditourism.employee.components.InputFieldComponent
import sa.sauditourism.employee.components.InputFieldModel
import sa.sauditourism.employee.components.InputFieldType
import sa.sauditourism.employee.constants.LanguageConstants
import sa.sauditourism.employee.extensions.getFileFromUri
import sa.sauditourism.employee.extensions.isNotNull
import sa.sauditourism.employee.extensions.localizeString
import sa.sauditourism.employee.managers.network.models.UiState
import sa.sauditourism.employee.modules.activities.model.AddCommentResponse
import java.io.File


@Composable
fun AddCommentBottomSheet(
    requestTypeId:String,
    requestId:String,
    issueId:String,
    addAttachment: Boolean = false,
    onSaveButtonClick:(AddCommentResponse)->Unit,
    onCancelButtonClick:()->Unit,
    viewModel: AddCommentViewModel = hiltViewModel()

) {
    val context = LocalContext.current
    val configuration = LocalConfiguration.current
    val sheetHeight = configuration.screenHeightDp * 0.35f
    val commentText = remember { mutableStateOf("") }
    var showDialog by remember { mutableStateOf(false) }
    val attachmentMaxSizeError by viewModel.attachmentMaxSizeError.collectAsState()
    val showLoading by viewModel.showDialog.collectAsState()
    var showAttachmentMaxSizeErrorToast by remember { mutableStateOf(false) }
    var showError by remember { mutableStateOf(false) }

    val  comment by viewModel.comment.collectAsState()
    val attachmentsList = remember { mutableStateListOf<Uri>() }

    LaunchedEffect (showLoading){
        showDialog =showLoading ?: false
    }
    LaunchedEffect(comment) {
        if (comment is UiState.Success){
            showDialog = false
            showError=false
            comment.data?.let{
                if(it.comment.isNotNull()){
                    viewModel.clearState()
                    onSaveButtonClick(it)
                }

            }

        }
        else if (comment is UiState.Error) {
            showDialog = false
            showError=true
        }
    }


    LaunchedEffect(attachmentMaxSizeError) {
        showAttachmentMaxSizeErrorToast = attachmentMaxSizeError ?: false
    }


    Box(modifier = Modifier.height(sheetHeight.dp)) {

        if (showDialog) {
            Box(
                contentAlignment = Alignment.Center,
                modifier = Modifier
                    .size(sheetHeight.dp)
                    .background(Color.White, shape = RoundedCornerShape(8.dp))
            ) {
                CircularProgressIndicator()
            }

        }

        Column(
            Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(top = 20.dp, start = 24.dp, end = 24.dp, bottom = 20.dp)
                .imePadding(),
            verticalArrangement = Arrangement.Top
        ) {
            InputFieldComponent(
                title = LanguageConstants.ADD_COMMENT.localizeString(),
                value = commentText.value,
                inputFieldModel = InputFieldModel(
                    placeholderText = "",
                    type = InputFieldType.Normal,
                    showCharCounter = true,
                    maxLines = 3,
                    minLines = 3,
                    singleLine = false,
                    maxCharLimit = 200,
                ),
                isOptional = false,
                onValueChange = { text ->
                    commentText.value = text
                },
            )



            Spacer(modifier = Modifier.height(20.dp))
//            AttachmentComponent(
//                title = "Attachment",
//                attachmentsList = attachmentsList,
//                isOptional = true,
//                attachmentListener = { attachment, isAdd ->
//                    when (isAdd) {
//                        true -> {
//                            attachment.getFileFromUri(
//                                context
//                            )?.let {
//                                attachmentsList.add(attachment)
//                                viewModel.checkFileSize(
//                                    file = it,
//                                ) { _ ->
//                                    showDialog = true
//                                    viewModel.uploadAttachment(
//                                        requestId = requestTypeId,
//                                        file = it,
//                                    )
//                                }
//                            }
//                        }
//                        else -> {
//                            attachmentsList.remove(attachment)
//                        }
//                    }
//                }
//
//            )
//            Spacer(modifier = Modifier.height(80.dp))
        }

        FooterComponent(modifier = Modifier.align(Alignment.BottomCenter)) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 12.dp, horizontal = 12.dp),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {

                ButtonComponent(
                    LanguageConstants.CANCEL.localizeString(),
                    clickListener = {
                        onCancelButtonClick()
                    },
                    modifier = Modifier
                        .weight(1f)
                        .fillMaxWidth(),
                    buttonType = ButtonType.SECONDARY,
                    buttonSize = ButtonSize.LARGE
                )
                Spacer(modifier = Modifier.width(24.dp))

                ButtonComponent(LanguageConstants.SEND.localizeString(),
                    clickListener = {
                       viewModel.addComment(requestId = requestId,issueId = issueId,commentText.value  )
                    },
                    modifier = Modifier
                        .weight(1f)
                        .fillMaxWidth(),
                    buttonType = ButtonType.PRIMARY,
                    buttonSize = ButtonSize.LARGE,
                    enabled = commentText.value.isNotEmpty(),
                )

            }
        }

        if (showAttachmentMaxSizeErrorToast) {
            CustomAlertDialog(
                type = DialogType.Failed,
                imageResId = R.drawable.ic_attention,
                shouldShowCloseIcon = true,
                title =LanguageConstants.ATTACHMENT_SIZE_VALIDATION_ERROR_TITLE.localizeString(),
                description =LanguageConstants.ATTACHMENT_SIZE_VALIDATION_ERROR_DESCRIPTION.localizeString(),
                primaryButtonText = LanguageConstants.OK.localizeString(),
                onPrimaryButtonClick = {
                    showAttachmentMaxSizeErrorToast = false
                    viewModel.clearState()
                },
                onDismiss = {
                    showAttachmentMaxSizeErrorToast = false
                    viewModel.clearState()
                }
            )
        }
        if(showError){
            CustomAlertDialog(
                type = DialogType.Failed,
                imageResId = R.drawable.ic_attention,
                shouldShowCloseIcon = true,
                title = LanguageConstants.UNEXPECTED_ERROR_HAS_OCCURRED.localizeString(),
                primaryButtonText = LanguageConstants.OK.localizeString(),
                onPrimaryButtonClick = {
                    showError = false
                },
                onDismiss = {
                    showError = false
                }
            )
        }

    }

}

@Composable
@Preview(showBackground = true)
fun PreviewAddCommentBottomSheet() {
    AddCommentBottomSheet(requestTypeId = "12",requestId = "qwe", issueId = "12",addAttachment = true,{},{})
}