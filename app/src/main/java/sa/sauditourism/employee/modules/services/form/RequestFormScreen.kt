package sa.sauditourism.employee.modules.services.form

import android.content.ContentResolver
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
import androidx.compose.foundation.shape.CornerSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.pullrefresh.PullRefreshIndicator
import androidx.compose.material.pullrefresh.pullRefresh
import androidx.compose.material.pullrefresh.rememberPullRefreshState
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.key
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import sa.sauditourism.employee.R
import sa.sauditourism.employee.components.AttachmentComponent
import sa.sauditourism.employee.components.ButtonComponent
import sa.sauditourism.employee.components.ButtonSize
import sa.sauditourism.employee.components.ButtonType
import sa.sauditourism.employee.components.CheckBoxComponent
import sa.sauditourism.employee.components.CustomAlertDialog
import sa.sauditourism.employee.components.CustomTextComponent
import sa.sauditourism.employee.components.DatePickerComponent
import sa.sauditourism.employee.components.DateTimePickerComponent
import sa.sauditourism.employee.components.DialogType
import sa.sauditourism.employee.components.DropDownModel
import sa.sauditourism.employee.components.EmptyViewComponent
import sa.sauditourism.employee.components.EmptyViewModel
import sa.sauditourism.employee.components.FooterComponent
import sa.sauditourism.employee.components.HeaderComponent
import sa.sauditourism.employee.components.HeaderModel
import sa.sauditourism.employee.components.InputFieldComponent
import sa.sauditourism.employee.components.InputFieldModel
import sa.sauditourism.employee.components.InputFieldType
import sa.sauditourism.employee.components.MultiDropDownComponent
import sa.sauditourism.employee.components.MultiSelectDropDownComponent
import sa.sauditourism.employee.components.RadioButtonComponent
import sa.sauditourism.employee.components.SingleSelectDropDownComponent
import sa.sauditourism.employee.components.TimePickerComponent
import sa.sauditourism.employee.components.ToastPresentable
import sa.sauditourism.employee.components.ToastType
import sa.sauditourism.employee.components.ToastView
import sa.sauditourism.employee.constants.FirebaseConstants
import sa.sauditourism.employee.constants.FirebaseConstants.DATE
import sa.sauditourism.employee.constants.FirebaseConstants.DATE_AND_TIME
import sa.sauditourism.employee.constants.FirebaseConstants.TIME
import sa.sauditourism.employee.constants.LanguageConstants
import sa.sauditourism.employee.extensions.clickableWithoutRipple
import sa.sauditourism.employee.extensions.getFileFromUri
import sa.sauditourism.employee.extensions.isNumeric
import sa.sauditourism.employee.extensions.localizeString
import sa.sauditourism.employee.managers.network.helpers.NetworkError
import sa.sauditourism.employee.managers.network.models.UiState
import sa.sauditourism.employee.modules.PastOrPresentSelectableDates
import sa.sauditourism.employee.modules.common.ChangeStatusBarIconsColors
import sa.sauditourism.employee.modules.services.model.Filed
import sa.sauditourism.employee.modules.services.model.FormModel
import sa.sauditourism.employee.modules.services.model.form.response.Attachment
import sa.sauditourism.employee.resources.AppColors
import sa.sauditourism.employee.resources.AppFonts
import sa.sauditourism.employee.shimmer.shimmerModifier
import java.io.File

@OptIn(ExperimentalMaterial3Api::class, ExperimentalMaterialApi::class)
@Composable
fun RequestFormScreen(
    requestId: String = "",
    title: String? = null,
    navController: NavController,
    viewModel: RequestFormViewModel = hiltViewModel()
) {
    val context = LocalContext.current
    Box(modifier = Modifier.fillMaxSize()) {
        val formState by viewModel.requestFormFlow.collectAsState()
        var isLoading by remember { mutableStateOf(false) }
        var showError by remember { mutableStateOf(false) }
        var showSuccess by remember { mutableStateOf(false) }
        var errorMessage by remember { mutableStateOf<String>("") }
        var form by remember { mutableStateOf<FormModel?>(FormModel()) }
        val bottomPadding by remember { mutableStateOf(150.dp) }
        val attachments by viewModel.attachmentsMap
        val scope = rememberCoroutineScope()
        val state = rememberScrollState()
        var requiredFieldsCount by remember { mutableIntStateOf(0) }
        val formAllFieldsMap by viewModel.formAllFieldsMap.collectAsState()
        val formSubmissionState by viewModel.dropdownSubmitFlow.collectAsState()
        val attachmentsList by viewModel.attachmentsValues.collectAsState((UiState.Loading()))
        var showDialog by remember { mutableStateOf(false) }
        val expanded = remember { mutableStateOf<Boolean?>(null) }
        val attachmentMaxSizeError by viewModel.attachmentMaxSizeError.collectAsState()
        var showAttachmentMaxSizeErrorToast by remember { mutableStateOf(false) }

        LaunchedEffect(attachmentMaxSizeError) {
            showAttachmentMaxSizeErrorToast = attachmentMaxSizeError ?: false
        }

        val focusManager = LocalFocusManager.current
        val keyboardController = LocalSoftwareKeyboardController.current

        LaunchedEffect(formSubmissionState) {
            if (formSubmissionState is UiState.Success) showSuccess = true
            else if (formSubmissionState is UiState.Error) {
                showDialog = false
                errorMessage = formSubmissionState.networkError?.message ?: ""
            }
        }
        LaunchedEffect(attachmentsList) {
            if (attachmentsList is UiState.Success) {
                showDialog = false
            }
            if (attachmentsList is UiState.Error) {
                showDialog = false
                showError = true
                errorMessage = formSubmissionState.networkError?.message ?: ""
            }
        }

        LaunchedEffect(Unit) {
            viewModel.getRequestForm()
        }

        LaunchedEffect(formState) {
            isLoading = formState is UiState.Loading
            if (formState is UiState.Success) {
                form = formState.data?.form
                requiredFieldsCount = form?.fields?.count { field ->
                    viewModel.submitField(field.apply { initPickers() }, null)
                    field.required
                } ?: 0
            }
        }

        if (showError) {
            ToastView(
                presentable = ToastPresentable(
                    type = ToastType.Error,
                    title = errorMessage.ifEmpty { LanguageConstants.UNEXPECTED_ERROR_HAS_OCCURRED.localizeString() },
                ),
                toastDuration = 5000L,
                onDismiss = {
                    showError = false
                },
            )
        }

        if (showDialog) {
            Dialog(
                onDismissRequest = { },
                DialogProperties(dismissOnBackPress = false, dismissOnClickOutside = false)
            ) {
                Box(
                    contentAlignment = Alignment.Center,
                    modifier = Modifier
                        .size(100.dp)
                        .background(Color.White, shape = RoundedCornerShape(8.dp))
                ) {
                    CircularProgressIndicator()
                }
            }
        }

        if (showSuccess) {
            showDialog = false
            CustomAlertDialog(
                type = DialogType.Success,
                imageResId = R.drawable.ic_success,
                title = LanguageConstants.FORM_SUCCESS_TITLE.localizeString(),
                description = LanguageConstants.FORM_SUCCESS_MESSAGE.localizeString(),
                primaryButtonText = LanguageConstants.DONE_BUTTON_TITLE.localizeString(),
                shouldShowCloseIcon = false,
                onPrimaryButtonClick = {
                    // Handle primary button click
                    showSuccess = false
                    navController.popBackStack()
                }
            )
        }

        if (isLoading)
            RequestFormShimmerSkeleton()


        if (formState is UiState.Error || form == null)
            EmptyViewComponent(
                modifier = Modifier.align(Alignment.Center),
                model = EmptyViewModel(
                    title = LanguageConstants.NO_RESULT_FOUND.localizeString(),
                    description = LanguageConstants.NO_RESULT_FOUND_MESSAGE.localizeString(),
                    imageResId = R.mipmap.ic_no_data
                )
            )

        ChangeStatusBarIconsColors(Color.White)

        Column(
            Modifier
                .fillMaxSize()
                .clickableWithoutRipple {
                    scope.launch {
                        expanded.value = false
                        clearAllExpanded(form?.fields)
                        focusManager.clearFocus()
                        keyboardController?.hide()
                        delay(300)
                        expanded.value = null
                    }
                },
            horizontalAlignment = Alignment.Start,
            verticalArrangement = Arrangement.Top
        ) {

            val model = HeaderModel(
                title = title?.replace("$", "/"),
                showFavoriteIcon = false,
            )

            HeaderComponent(
                model,
                isStandAlone = true,
                titleMaxLines = 2,
                backHandler = {
                    navController.popBackStack()
                })

            if (formState is UiState.Success)
                Column(
                    Modifier
                        .fillMaxSize()
                        .verticalScroll(state)
                        .padding(top = 20.dp, bottom = bottomPadding)
                        .imePadding(),
                    verticalArrangement = Arrangement.Top
                ) {

                    if (!form?.formNote.isNullOrBlank()) {
                        Row(
                            modifier = Modifier.padding(start = 20.dp, end = 24.dp).fillMaxWidth(),
                            horizontalArrangement = Arrangement.Start,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Icon(
                                painter = painterResource(id = R.drawable.ic_note),
                                contentDescription = "note",
                                tint = Color.Unspecified,
                            )

                            Spacer(modifier = Modifier.width(10.dp))

                            CustomTextComponent(
                                text = LanguageConstants.NOTE.localizeString(),
                                color = AppColors.BLACK90,
                                style = AppFonts.body2Medium,
                            )
                        }

                        form?.formNote?.let {
                            CustomTextComponent(
                                text = it,
                                color = AppColors.ColorTextSubdued,
                                style = AppFonts.tagsSemiBold,
                                modifier = Modifier.padding(top = 5.dp, start = 55.dp, end = 24.dp)
                            )

                            Spacer(modifier = Modifier.height(30.dp))
                        }
                    }

                    if (!form?.fields.isNullOrEmpty())
                        form?.fields?.onEachIndexed { index, field ->

                            key(field.id) {
                                when (field.component) {
                                    FirebaseConstants.DATE -> {
                                        val datePickerState =
                                            rememberDatePickerState(selectableDates = PastOrPresentSelectableDates)
                                        DatePickerComponent(
                                            informativeMessage = field.note,
                                            label = field.title,
                                            isOptional = !field.required,
                                            showPicker = field.showPicker?.value ?: false,
                                            state = datePickerState,
                                            onPickerStateChange = {
                                                field.showPicker?.value = it
                                            },
                                            modifier = Modifier.padding(start = 24.dp, end = 24.dp),
                                        ) { date ->
                                            viewModel.submitField(field, date)
                                        }

                                        Spacer(modifier = Modifier.height(25.dp))
                                    }

                                    FirebaseConstants.TIME -> {
                                        TimePickerComponent(
                                            informativeMessage = field.note,
                                            label = field.title,
                                            modifier = Modifier,
                                            isOptional = !field.required,
                                            showPicker = field.showPicker?.value ?: false,
                                            onPickerStateChange = {
                                                field.showPicker?.value = it
                                            },
                                            onConfirmRequest = { time ->
                                                viewModel.submitField(field, time)
                                            }, onDismissRequest = {})

                                        Spacer(
                                            modifier = Modifier
                                                .padding(
                                                    start = 24.dp,
                                                    end = 24.dp
                                                )
                                                .height(25.dp)
                                        )
                                    }

                                    FirebaseConstants.DATE_AND_TIME -> {
                                        DateTimePickerComponent(
                                            label = field.title,
                                            informativeMessage = field.note,
                                            isOptional = !field.required,
                                            modifier = Modifier.padding(start = 24.dp, end = 24.dp),
                                            showPicker = field.showPicker?.value ?: false,
                                            onPickerStateChange = {
                                                field.showPicker?.value = it
                                            },
                                        ) { dateTime ->
                                            viewModel.submitField(field, dateTime)
                                        }

                                        Spacer(modifier = Modifier.height(25.dp))
                                    }

                                    FirebaseConstants.TEXTFIELD -> {
                                        InputFieldComponent(
                                            title = field.title,
                                            value = "",
                                            inputFieldModel = InputFieldModel(
                                                placeholderText = "",
                                                type = InputFieldType.Normal,
                                            ),
                                            isOptional = !field.required,
                                            modifier = Modifier.padding(start = 24.dp, end = 24.dp),
                                            onValueChange = { text ->
                                                viewModel.submitField(
                                                    field,
                                                    text.trim().ifEmpty { null })
                                            },
                                            informativeMessage = field.note
                                        )

                                        Spacer(modifier = Modifier.height(25.dp))
                                    }

                                    FirebaseConstants.TEXTFIELD_NUMERIC -> {
                                        InputFieldComponent(
                                            title = field.title,
                                            value = "",
                                            modifier = Modifier.padding(start = 24.dp, end = 24.dp),
                                            inputFieldModel = InputFieldModel(
                                                placeholderText = "",
                                                type = InputFieldType.Numbers,
                                            ),
                                            isOptional = !field.required,
                                            onValueChange = { number ->
                                                if (!number.isNumeric()) return@InputFieldComponent
                                                viewModel.submitField(
                                                    field,
                                                    number.trim().ifEmpty { null })
                                            },
                                            informativeMessage = field.note
                                        )

                                        Spacer(modifier = Modifier.height(25.dp))
                                    }

                                    FirebaseConstants.TEXTAREA -> {
                                        InputFieldComponent(
                                            title = field.title,
                                            value = "",
                                            inputFieldModel = InputFieldModel(
                                                placeholderText = "",
                                                type = InputFieldType.Normal,
                                                showCharCounter = true,
                                                maxLines = 3,
                                                minLines = 3,
                                                singleLine = false,
                                                maxCharLimit = 200,
                                            ),
                                            isOptional = !field.required,
                                            modifier = Modifier.padding(start = 24.dp, end = 24.dp),
                                            onValueChange = { text ->
                                                viewModel.submitField(
                                                    field,
                                                    text.trim().ifEmpty { null })
                                            },
                                            informativeMessage = field.note
                                        )
                                        Spacer(modifier = Modifier.height(25.dp))

                                    }

                                    FirebaseConstants.CHECKBOX -> {
                                        CheckBoxComponent(title = field.title,
                                            modifier = Modifier.padding(start = 24.dp, end = 24.dp),
                                            onCheckedChange = { checked ->
                                                viewModel.submitField(field, checked)
                                            })

                                        Spacer(modifier = Modifier.height(25.dp))
                                    }

                                    FirebaseConstants.RADIO_BUTTON -> {
                                        RadioButtonComponent(
                                            title = field.title,
                                            onSelectedChange = { checked ->
                                                viewModel.submitField(field, checked)
                                            },
                                            modifier = Modifier.padding(start = 24.dp, end = 24.dp)
                                        )
                                        Spacer(modifier = Modifier.height(25.dp))
                                    }

                                    FirebaseConstants.DROPDOWN_SINGLE_SELECT -> {

                                        SingleSelectDropDownComponent(
                                            title = field.title,
                                            informativeMessage = field.note,
                                            isOptional = !field.required,
                                            isExpanded = expanded,
                                            modifier = Modifier.padding(start = 24.dp, end = 24.dp),
                                            isDropDownUserSearch = field.isDropDownUserSearch,
                                            onSearchInvoked = {
                                                if (field.isDropDownUserSearch) {
                                                    //search only if query has a value
                                                    scope.launch {
                                                        viewModel.searchDropDownValues(
                                                            field = field,
                                                            query = it
                                                        ).await()
                                                    }
                                                }
                                            },
                                            onDropDownItemSelected = { item ->
                                                val dropDownModel = item as DropDownModel
                                                viewModel.submitField(
                                                    field,
                                                    if (dropDownModel.stringId == null) null else dropDownModel
                                                )
                                            },
                                            onFocusChanged = {
                                                if (it && index in form?.fields?.size!! - 2..<form?.fields?.size!!)
                                                    scope.launch {
                                                        delay(100)
                                                        state.scrollTo(state.maxValue)
                                                    }
                                            }, list = field.dropDownValues ?: emptyList()
                                        )

                                        Spacer(modifier = Modifier.height(25.dp))
                                    }

                                    FirebaseConstants.DROPDOWN_MULTI_SELECT -> {
                                        MultiSelectDropDownComponent(
                                            title = field.title,
                                            informativeMessage = field.note,
                                            modifier = Modifier.padding(start = 24.dp, end = 24.dp),
                                            isOptional = !field.required,
                                            isDropDownUserSearch = field.isDropDownUserSearch,
                                            onSearchInvoked = {
                                                if (field.isDropDownUserSearch) {
                                                    //search only if query has a value
                                                    scope.launch {
                                                        viewModel.searchDropDownValues(
                                                            field = field,
                                                            query = it
                                                        ).await()
                                                    }
                                                }
                                            },
                                            onFocusChanged = {
                                                if (it && index in form?.fields?.size!! - 2..<form?.fields?.size!!)
                                                    scope.launch {
                                                        delay(100)
                                                        state.scrollTo(state.maxValue)
                                                    }
                                            },
                                            onDropDownItemSelected = { item ->
                                                val dropDownModel = item as DropDownModel
                                                viewModel.submitField(
                                                    field,
                                                    handleMultiSelectionDropDown(
                                                        formAllFieldsMap,
                                                        field,
                                                        dropDownModel
                                                    )
                                                )
                                            },
                                            list = field.dropDownValues ?: emptyList()
                                        )
                                        Spacer(modifier = Modifier.height(25.dp))
                                    }

                                    FirebaseConstants.MULTI_DROPDOWNS -> {
                                        MultiDropDownComponent(
                                            title = field.title,
                                            informativeMessage = field.note,
                                            modifier = Modifier.padding(start = 24.dp, end = 24.dp),
                                            isOptional = !field.required,
                                            numberOfComponents = field.dropDownCount,
                                            onSearchInvoked = {
                                                if (field.isDropDownUserSearch) {
                                                    //search only if query has a value
                                                    scope.launch {
                                                        viewModel.searchDropDownValues(
                                                            field = field,
                                                            query = it
                                                        ).await()
                                                    }
                                                }
                                            },
                                            onFocusChanged = {
                                                if (it && index in form?.fields?.size!! - 2..<form?.fields?.size!!)
                                                    scope.launch {
                                                        delay(100)
                                                        state.scrollTo(state.maxValue)
                                                    }
                                            },
                                            onDropDownItemSelected = { item, id ->
                                                val dropDownModel = item as DropDownModel
                                                viewModel.submitField(
                                                    field,
                                                    handleMultiDropDownLogic(
                                                        formAllFieldsMap,
                                                        field,
                                                        dropDownModel,
                                                        id
                                                    )
                                                )
                                            },
                                            list = field.dropDownValues ?: emptyList()
                                        )
                                        Spacer(modifier = Modifier.height(25.dp))
                                    }

                                    FirebaseConstants.ATTACHMENT -> {
                                        AttachmentComponent(
                                            title = field.title,
                                            attachmentsList = attachments[field.id]?.map { it.attachmentUri }
                                                ?.toList() ?: emptyList(),
                                            isOptional = !field.required,
                                            modifier = Modifier.padding(start = 24.dp, end = 24.dp),
                                            informativeMessage = field.note,
                                            attachmentListener = { attachment, isAdd ->
                                                when (isAdd) {
                                                    true -> {
                                                        attachment.getFileFromUri(
                                                            context
                                                        )?.let { file ->
                                                            viewModel.checkFileSize(
                                                                file = file,
                                                            ) {
                                                                viewModel.uploadAttachment(
                                                                    requestTypeId = requestId,
                                                                    file = it,
                                                                    fileUri = attachment,
                                                                    field = field
                                                                )

                                                                showDialog = true
                                                            }
                                                        }
                                                    }

                                                    else -> {
                                                        viewModel.removeAttachment(
                                                            field.id,
                                                            attachment
                                                        )
                                                    }
                                                }
                                            }
                                        )
                                        Spacer(modifier = Modifier.height(25.dp))
                                    }
                                }
                            }
                        }

                }
        }

        if (formState is UiState.Success && form != null)
            FooterComponent(modifier = Modifier.align(Alignment.BottomCenter)) {
                Column(
                    Modifier
                        .background(Color.White)
                        .fillMaxWidth()
                        .height(138.dp)
                        .padding(start = 16.dp, end = 16.dp, bottom = 8.dp, top = 16.dp),
                    verticalArrangement = Arrangement.Top
                ) {

                    ButtonComponent(
                        LanguageConstants.SUBMIT.localizeString(),
                        clickListener = {
                            showDialog = true
                            viewModel.submitForm()
                        },
                        modifier = Modifier
                            .fillMaxWidth()
                            .weight(1f),
                        buttonType = ButtonType.PRIMARY,
                        buttonSize = ButtonSize.LARGE,
                        enabled = requiredFieldsCount == formAllFieldsMap.count { it.key.required && it.value != null },
                    )

                    ButtonComponent(
                        LanguageConstants.CANCEL.localizeString(),
                        clickListener = { navController.popBackStack() },
                        modifier = Modifier
                            .fillMaxWidth()
                            .weight(1.3f),
                        buttonType = ButtonType.WHITE_BACKGROUND,
                        buttonSize = ButtonSize.LARGE
                    )
                }
            }

        if (formState is UiState.Error || formSubmissionState is UiState.Error) {
            val message =
                if (formState is UiState.Error) formState.networkError?.message else formSubmissionState.networkError?.message

            viewModel.HandleError(
                error = NetworkError(
                    message = message
                        ?: LanguageConstants.UNEXPECTED_ERROR_HAS_OCCURRED.localizeString()
                ),
                isDataEmpty = formState.data?.form?.fields.isNullOrEmpty(),
                showBack = false,
                withTab = true,
                dismissCallback = {
                    navController.popBackStack()
                },
            ) {
                if (formState is UiState.Error)
                    viewModel.getRequestForm()
                else
                    viewModel.submitForm()
            }
        }

        if (showAttachmentMaxSizeErrorToast) {

            CustomAlertDialog(
                type = DialogType.Failed,
                imageResId = R.drawable.ic_attention,
                shouldShowCloseIcon = true,
                title = LanguageConstants.ATTACHMENT_SIZE_VALIDATION_ERROR_TITLE.localizeString(),
                description = LanguageConstants.ATTACHMENT_SIZE_VALIDATION_ERROR_DESCRIPTION.localizeString(),
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

    }
}

fun clearAllExpanded(list: List<Filed>?) {
    list?.onEach {
        if (it.component == DATE || it.component == TIME || it.component == DATE_AND_TIME)
            it.showPicker?.value = false
    }
}

private fun handleMultiDropDownLogic(
    formAllFieldsMap: Map<Filed, Any?>,
    field: Filed,
    item: Any,
    id: Any?
): List<*> {
    val mappedValue = formAllFieldsMap[field] as? List<*>

    return if (mappedValue.isNullOrEmpty()) {
        //none selection exists
        val values: List<*> = listOf(item)
        values
    } else {
        //selection exist
        val tempList = mappedValue.toMutableList()
        if (id != null)
        //evaluate when user unselect the current selection only
            tempList.removeIf { (it as DropDownModel).stringId == id }

        if ((item as DropDownModel).stringId != null)
        //ensure that user select non-none value
            tempList.add(item)

        if (item.stringId == null && field.dropDownValues?.any { it.stringId == id } == true)
        //user has clear selection from the parent dropdown
            tempList.clear()

        tempList
    }
}

private fun handleMultiSelectionDropDown(
    formAllFieldsMap: Map<Filed, Any?>,
    field: Filed,
    item: Any
): List<*> {
    val mappedValue =
        formAllFieldsMap[field] as? List<*>
    return if (mappedValue.isNullOrEmpty()) {
        //none selection exists
        val values: List<*> = listOf(item)
        values
    } else {
        //selection exist
        val tempList =
            mappedValue.toMutableList().apply {
                if (contains(item))
                    remove(item)
                else
                    add(item)
            }
        tempList
    }
}

private fun getFileSize(file: File): Long {
    val fileSizeInKB = file.length() / 1024
    val fileSizeInMB = fileSizeInKB / 1024
    return fileSizeInMB
}


