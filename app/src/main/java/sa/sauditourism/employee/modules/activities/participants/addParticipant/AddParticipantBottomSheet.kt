package sa.sauditourism.employee.modules.activities.participants.addParticipant

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.systemBars
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color.Companion.White
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import sa.sauditourism.employee.R
import sa.sauditourism.employee.components.ButtonComponent
import sa.sauditourism.employee.components.ButtonSize
import sa.sauditourism.employee.components.ButtonType
import sa.sauditourism.employee.components.CustomAlertDialog
import sa.sauditourism.employee.components.CustomTextComponent
import sa.sauditourism.employee.components.DialogType
import sa.sauditourism.employee.components.DropDownModel
import sa.sauditourism.employee.components.DropDownType
import sa.sauditourism.employee.components.InputFieldComponent
import sa.sauditourism.employee.components.InputFieldModel
import sa.sauditourism.employee.components.InputFieldType
import sa.sauditourism.employee.constants.LanguageConstants
import sa.sauditourism.employee.constants.TestTagsConstants.COMMENT_BODY_TAG
import sa.sauditourism.employee.extensions.clickableWithoutRipple
import sa.sauditourism.employee.extensions.localizeString
import sa.sauditourism.employee.managers.network.models.UiState
import sa.sauditourism.employee.modules.activities.model.User
import sa.sauditourism.employee.resources.AppColors
import sa.sauditourism.employee.resources.AppFonts
import kotlin.random.Random

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddParticipantBottomSheet(
    requestId: String,
    addParticipantViewModel: AddParticipantViewModel = hiltViewModel(),
    locally: Boolean? = false,
    selectedUsers: List<DropDownModel>? = listOf(),
    onAddUsers: (List<DropDownModel>) -> Unit = {},
    onDissmess: () -> Unit = {},
    onAddedSuccess: () -> Unit = {},

    ) {

    var isEnabled by remember { mutableStateOf(false) }
    val focusManager = LocalFocusManager.current
    val keyboardController = LocalSoftwareKeyboardController.current
    val expanded = remember { mutableStateOf<Boolean?>(null) }
    val scope = rememberCoroutineScope()
    val sheetState = rememberModalBottomSheetState(
        skipPartiallyExpanded = true
    )
    val coroutineScope = rememberCoroutineScope()
    val configuration = LocalConfiguration.current
    val fullScreenHeight = (configuration.screenHeightDp * 0.90f).toInt()
    val initialSize = (fullScreenHeight * 0.33f).toInt()
    val afterSelectItem = (fullScreenHeight * 0.43f).toInt()
    var sheetHeight by remember { mutableIntStateOf(initialSize) }
    var searchText by remember { mutableStateOf("") }
    val listItemSelected by remember {
        mutableStateOf(
            selectedUsers!!.toMutableList()
        )
    }
    val users by addParticipantViewModel.usersFlow.collectAsState()
    val addedUsers by addParticipantViewModel.addedFlow.collectAsState()
    val showLoading by addParticipantViewModel.showLoading.collectAsState()
//    val searchResultList = remember { mutableStateListOf<DropDownModel>() }

    var searchResultList by remember { mutableStateOf(mutableStateListOf<DropDownModel>()) }
    val showError = remember { mutableStateOf(false) }
    val showDialog = remember { mutableStateOf(false) }
    val systemBarHeight = WindowInsets.systemBars.asPaddingValues().calculateTopPadding()

    LaunchedEffect(searchText) {
        searchResultList.clear()
        if (searchText.isNotEmpty())
            addParticipantViewModel.searchForUser(searchText,locally)
        else
            addParticipantViewModel.clearUsers()
    }
    LaunchedEffect(showLoading) {
        showDialog.value = showLoading
    }

    LaunchedEffect(users) {
        if (users is UiState.Success) {
            val list = mapItemToDropDownModel(users.data?.users?.filter { item ->
                selectedUsers?.none { it.label == item.displayName } ?: true
            })
            searchResultList.addAll(list)

        }
    }
    LaunchedEffect(addedUsers) {
        when (addedUsers) {
            is UiState.Success -> {
                showError.value = false
                searchResultList.clear()
                scope.launch {
                    sheetState.hide()
                }
                addParticipantViewModel.clearState()
                onAddedSuccess.invoke()
            }

            is UiState.Error -> {
                showError.value = true
            }

            else -> {
                showError.value = false
            }
        }

    }

    ModalBottomSheet(
        containerColor = White,
        sheetState = sheetState,
        onDismissRequest = {
            coroutineScope.launch {
                addParticipantViewModel.clearState()
                sheetState.hide()
            }
            onDissmess.invoke()
        },
        modifier = Modifier.padding(top = systemBarHeight)

    ) {
        Box(
            modifier = Modifier.height(sheetHeight.dp),
            contentAlignment = Alignment.TopStart
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .imePadding()
                    .align(Alignment.TopStart),
            ) {

                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 24.dp, start = 24.dp, end = 24.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    CustomTextComponent(
                        modifier = Modifier.testTag(COMMENT_BODY_TAG),
                        text = LanguageConstants.ADD_PARTICIPANTS_BUTTON_TITLE.localizeString(),
                        color = AppColors.BLACK,
                        style = AppFonts.heading4SemiBold,
                        maxLines = 3,
                        overflow = TextOverflow.Ellipsis,
                        isLoading = false
                    )
                    Spacer(Modifier.weight(1f))
                    Icon(
                        modifier = Modifier
                            .size(20.dp)
                            .clickable {
                                scope.launch {
                                    addParticipantViewModel.clearState()
                                    sheetState.hide()
                                }
                                onDissmess.invoke()

                            },
                        painter = painterResource(R.drawable.ic_popup_close),
                        tint = AppColors.BLACK,
                        contentDescription = ""
                    )

                }

                Box(modifier = Modifier
                    .padding(horizontal = 16.dp)
                    .clickableWithoutRipple {
                        scope.launch {
                            expanded.value = false
                            focusManager.clearFocus()
                            keyboardController?.hide()
                            delay(300)
                            expanded.value = null
                        }
                    }
                ) {
                    InputFieldComponent(
                        value = "",
                        title = "",
                        inputFieldModel = InputFieldModel(
                            placeholderText = LanguageConstants.SEARCH.localizeString(),
                            type = InputFieldType.Dropdown,
                            dropdownWithSearchIcon = true,

                            ),
                        originalModels = searchResultList,
                        isDropDownUserSearch = true,
                        dropDownType = DropDownType.MultiSelection,
                        displayUnAvaiableSection = true,
                        readOnly = false,
                        isExpanded = expanded,
//                        initialSelectedItems = selectedUsers,
                        localFocusManager = focusManager,
                        keyboardController = keyboardController,
                        onDropDownItemSelected = { item, _ ->
                            if (listItemSelected.contains(item)) {
                                listItemSelected.remove(item)
                            } else
                                listItemSelected.add(item as DropDownModel)

                            isEnabled = listItemSelected.isNotEmpty()
                        },
                        onValueChange = { query ->
                            searchText = query
                        },
                        showNoneOption = false,
                        selectedDropDownItem = DropDownModel(id = -1, label = "None"),
                        dropdownWithImage = true,
                        onSearchContainterClick = {
                            sheetHeight = fullScreenHeight
                        }
                    ) {
                        sheetHeight = if (listItemSelected.isNotEmpty()) {
                            afterSelectItem
                        } else {
                            initialSize
                        }
                    }
                }

            }
            if (sheetHeight < fullScreenHeight && listItemSelected.isNotEmpty())
                ButtonComponent(
                    text = LanguageConstants.SAVE.localizeString(),
                    clickListener = {
                        if (locally == true) {
                            scope.launch {
                                addParticipantViewModel.clearState()
                                sheetState.hide()
                                onAddUsers.invoke(listItemSelected)
                            }
                        } else {
                            addParticipantViewModel.addUsers(
                                requestId,
                                listItemSelected
                            )
                        }

                    },
                    enabled = isEnabled,
                    modifier = Modifier
                        .fillMaxWidth()
                        .align(Alignment.BottomCenter)
                        .padding(start = 16.dp, end = 16.dp, bottom = 40.dp)
                        .height(48.dp),
                    buttonType = ButtonType.PRIMARY,
                    buttonSize = ButtonSize.LARGE
                )


        }


        if (showError.value) {
            CustomAlertDialog(
                type = DialogType.Failed,
                imageResId = R.drawable.ic_attention,
                shouldShowCloseIcon = true,
                title = "Error Happen Please Try Again",
                description = "Error Happen Please Try Again",
                primaryButtonText = LanguageConstants.OK.localizeString(),
                onPrimaryButtonClick = {
                    showError.value = false
                },
                onDismiss = {
                    showError.value = false
                }
            )

        }


    }


}

private fun mapItemToDropDownModel(users: List<User>?): List<DropDownModel> {
    val list: MutableList<DropDownModel> = mutableListOf()
    users?.forEach {
        val dropDownModel =
            DropDownModel(
                id = Random.nextInt(),
                label = it.displayName,
                imageUrl = "",
                extra = it.name
            )
        list.add(
            dropDownModel
        )
    }
    return list
}


@Composable
@Preview(showBackground = true)
fun PreviewAddParticipantBottomSheet() {
    AddParticipantBottomSheet(requestId = "123")

}