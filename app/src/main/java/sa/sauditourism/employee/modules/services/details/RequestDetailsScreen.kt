package sa.sauditourism.employee.modules.services.details

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
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
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.pullrefresh.PullRefreshIndicator
import androidx.compose.material.pullrefresh.pullRefresh
import androidx.compose.material.pullrefresh.rememberPullRefreshState
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.key
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import sa.sauditourism.employee.R
import sa.sauditourism.employee.components.AttachmentComponent
import sa.sauditourism.employee.components.CheckBoxComponent
import sa.sauditourism.employee.components.CustomTextComponent
import sa.sauditourism.employee.components.DetailsHeaderComponent
import sa.sauditourism.employee.components.DropDownModel
import sa.sauditourism.employee.components.FilterCheckboxState
import sa.sauditourism.employee.components.HeaderModel
import sa.sauditourism.employee.components.MultiDropDownComponent
import sa.sauditourism.employee.components.MultiSelectDropDownComponent
import sa.sauditourism.employee.components.RadioButtonComponent
import sa.sauditourism.employee.components.ToastPresentable
import sa.sauditourism.employee.components.ToastType
import sa.sauditourism.employee.components.ToastView
import sa.sauditourism.employee.constants.FirebaseConstants
import sa.sauditourism.employee.constants.LanguageConstants
import sa.sauditourism.employee.extensions.formatDateFromTo
import sa.sauditourism.employee.extensions.localizeString
import sa.sauditourism.employee.extensions.openBrowser
import sa.sauditourism.employee.managers.network.models.UiState
import sa.sauditourism.employee.modules.common.ChangeStatusBarIconsColors
import sa.sauditourism.employee.modules.services.form.RequestFormShimmerSkeleton
import sa.sauditourism.employee.modules.services.model.RadioButtonModel
import sa.sauditourism.employee.modules.services.model.details.Request
import sa.sauditourism.employee.modules.services.model.details.Status
import sa.sauditourism.employee.resources.AppColors
import sa.sauditourism.employee.resources.AppFonts
import sa.sauditourism.employee.modules.services.model.details.Field
import sa.sauditourism.employee.modules.services.model.form.response.Attachment
import sa.sauditourism.employee.modules.services.model.participants.Participant

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun RequestDetailsScreen(
    navController: NavController,
    viewModel: RequestDetailsViewModel = hiltViewModel(),
    onParticipantsClick: () -> Unit,
    onActivitiesClick: (id: String, issueId: String, status: List<Status>, attachmentList: List<Attachment>, addComment: Boolean, addAttachment: Boolean, requestTypeId: String) -> Unit,
) {
    val context = LocalContext.current
    val scope = rememberCoroutineScope()

    LaunchedEffect(key1 = true) {
        viewModel.apply {
            scope.launch {
                async { getRequestDetails() }
                async { getParticipates() }
            }
        }
    }

    Box(modifier = Modifier.fillMaxSize()) {
        val formState by viewModel.detailsResultFlow.collectAsState()
        val participantState by viewModel.participantsFlow.collectAsState()
        var isLoading by remember { mutableStateOf(false) }
        var showError by remember { mutableStateOf(false) }
        var isRefreshing by remember { mutableStateOf(false) }
        var errorMessage by remember { mutableStateOf<String>("") }
        val bottomPadding by remember { mutableStateOf(50.dp) }
        val state = rememberScrollState()
        var showDialog by remember { mutableStateOf(false) }
        var requestDetails by remember { mutableStateOf<Request?>(null) }
        var participantsList = remember { mutableStateListOf<Participant?>() }
        val attachmentList = remember { mutableStateListOf<Attachment>() }
        val showOverlay by remember {
            derivedStateOf {
                state.value == 0
            }
        }

        val pullRefreshState = rememberPullRefreshState(
            refreshing = isRefreshing,
            onRefresh = {
                isRefreshing = true
                viewModel.getRequestDetails()
            }
        )

        LaunchedEffect(formState) {
            isLoading = formState is UiState.Loading
            isRefreshing = false
            if (formState is UiState.Success) {
                requestDetails = formState.data?.request
            }
        }

        LaunchedEffect(participantState) {
            if (participantState is UiState.Success) {
                participantsList.clear()
                participantState.data?.participants?.let { participantsList.addAll(it) }
            }
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

        ChangeStatusBarIconsColors(Color.Transparent)

        if (isLoading)
            RequestFormShimmerSkeleton()

        Column(
            Modifier
                .fillMaxSize()
                .pullRefresh(pullRefreshState),
            horizontalAlignment = Alignment.Start,
            verticalArrangement = Arrangement.Top
        ) {

            val model = HeaderModel(
                title = requestDetails?.title,
                subTitle = requestDetails?.serviceCategoryTitle,
                showFavoriteIcon = false,
            )

            Spacer(modifier = Modifier.height(8.dp))

            DetailsHeaderComponent(
                model,
                titleMaxLines = 2,
                showSubtitle = true,
                backgroundColor = Color.Transparent,
                showShadow = false,
                isLoading = isLoading,
                backHandler = {
                    navController.popBackStack()
                },
                showOverlay = showOverlay
            )

            Spacer(modifier = Modifier.height(5.dp))

            if (formState is UiState.Success)
                Column(
                    Modifier
                        .fillMaxSize()
                        .verticalScroll(state)
                        .padding(top = 20.dp, start = 24.dp, end = 24.dp, bottom = bottomPadding)
                        .imePadding(),
                    verticalArrangement = Arrangement.Top
                ) {

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.Start,
                        verticalAlignment = Alignment.CenterVertically
                    ) {

                        CustomTextComponent(
                            text = LanguageConstants.VIEW_ACTIVITIES.localizeString(),
                            color = AppColors.OnPrimaryDark,
                            style = AppFonts.labelSemiBold,
                            modifier = Modifier.clickable {
                                onActivitiesClick(
                                    requestDetails?.id ?: "",
                                    requestDetails?.issueId ?: "",
                                    requestDetails?.status ?: emptyList(),
                                    attachmentList,
                                    requestDetails?.addComment ?: false,
                                    requestDetails?.addAttachment ?: false,
                                    requestDetails?.requestTypeId.toString()
                                )
                            }
                        )

                        Spacer(modifier = Modifier.width(2.dp))

                        Icon(
                            painter = painterResource(id = R.drawable.ic_book),
                            contentDescription = "note",
                            tint = Color.Unspecified,
                        )

                        Spacer(modifier = Modifier.weight(1f))

                        Spacer(modifier = Modifier.width(8.dp))

                        requestDetails?.id?.let {
                            CustomTextComponent(
                                text = "${LanguageConstants.ID.localizeString()}: $it",
                                color = AppColors.ColorTextSubdued,
                                style = AppFonts.capsSmallRegular,
                            )
                        }
                    }

//                    if (participantsList.isNotEmpty())
//                        ParticipantSection(participants = participantState.data?.participants){
//                            onParticipantsClick.invoke()
//                        }
//
//                    else if(formState.data?.request?.addParticipant == true)
//                        AddParticipantSection (
//                            onClick = {
//                               showAddBottomSheet = true
//                            }
//                        )
//                    if (showAddBottomSheet)
//                        AddParticipantBottomSheet(requestId = viewModel.requestTypeId
//                            ,onDissmess = { showAddBottomSheet = false }){
//                            showAddBottomSheet = false
//                            viewModel.getParticipates()
//                        }
//
                    Spacer(modifier = Modifier.height(24.dp))

                    CustomTextComponent(
                        text = LanguageConstants.REQUEST_DETAILS.localizeString(),
                        color = AppColors.BLACK,
                        style = AppFonts.heading6SemiBold,
                        modifier = Modifier.align(Alignment.Start)
                    )

                    Spacer(modifier = Modifier.height(24.dp))

                    if (!requestDetails?.fields.isNullOrEmpty())
                        requestDetails?.fields?.onEachIndexed { _, field ->

                            key(field.id) {
                                when (field.component) {
                                    FirebaseConstants.DATE -> {
                                        Spacer(modifier = Modifier.height(16.dp))

                                        CustomTextComponent(
                                            text = field.title,
                                            style = AppFonts.body2Medium.copy(color = AppColors.BLACK)
                                        )

                                        Spacer(modifier = Modifier.height(4.dp))

                                        CustomTextComponent(
                                            text = kotlin.runCatching {
                                                (field.selectedValue as String).formatDateFromTo(
                                                    "yyyy-MM-dd",
                                                    "dd MMM yyyy"
                                                )
                                            }.getOrElse { field.selectedValue as String },
                                            style = AppFonts.body2Regular.copy(color = AppColors.ColorTextSubdued)
                                        )

                                        Spacer(modifier = Modifier.height(24.dp))

                                        HorizontalDivider(
                                            modifier = Modifier
                                                .fillMaxWidth()
                                                .height(1.dp),
                                            color = AppColors.ColorsPrimitivesFour
                                        )
                                    }

                                    FirebaseConstants.TIME -> {
                                        Spacer(modifier = Modifier.height(16.dp))

                                        CustomTextComponent(
                                            text = field.title,
                                            style = AppFonts.body2Medium.copy(color = AppColors.BLACK)
                                        )

                                        Spacer(modifier = Modifier.height(4.dp))

                                        CustomTextComponent(
                                            text = field.selectedValue as String,
                                            style = AppFonts.body2Regular.copy(color = AppColors.ColorTextSubdued)
                                        )
                                        Spacer(modifier = Modifier.height(24.dp))

                                        HorizontalDivider(
                                            modifier = Modifier
                                                .fillMaxWidth()
                                                .height(1.dp),
                                            color = AppColors.ColorsPrimitivesFour
                                        )
                                    }

                                    FirebaseConstants.DATE_AND_TIME -> {
                                        Spacer(modifier = Modifier.height(16.dp))

                                        CustomTextComponent(
                                            text = field.title,
                                            style = AppFonts.body2Medium.copy(color = AppColors.BLACK)
                                        )

                                        Spacer(modifier = Modifier.height(4.dp))

                                        CustomTextComponent(
                                            text = kotlin.runCatching {
                                                Field.fromDateTime(
                                                    field.selectedValue as String
                                                )
                                            }.getOrElse { field.selectedValue as String },
                                            style = AppFonts.body2Regular.copy(color = AppColors.ColorTextSubdued)
                                        )
                                        Spacer(modifier = Modifier.height(24.dp))

                                        HorizontalDivider(
                                            modifier = Modifier
                                                .fillMaxWidth()
                                                .height(1.dp),
                                            color = AppColors.ColorsPrimitivesFour
                                        )
                                    }

                                    FirebaseConstants.TEXTFIELD -> {
                                        Spacer(modifier = Modifier.height(16.dp))

                                        CustomTextComponent(
                                            text = field.title,
                                            style = AppFonts.body2Medium.copy(color = AppColors.BLACK)
                                        )

                                        Spacer(modifier = Modifier.height(4.dp))

                                        CustomTextComponent(
                                            text = kotlin.runCatching { field.selectedValue as String }
                                                .getOrElse { LanguageConstants.NONE.localizeString() },
                                            style = AppFonts.body2Regular.copy(color = AppColors.ColorTextSubdued)
                                        )
                                        Spacer(modifier = Modifier.height(24.dp))

                                        HorizontalDivider(
                                            modifier = Modifier
                                                .fillMaxWidth()
                                                .height(1.dp),
                                            color = AppColors.ColorsPrimitivesFour
                                        )
                                    }

                                    FirebaseConstants.TEXTFIELD_NUMERIC -> {
                                        Spacer(modifier = Modifier.height(16.dp))

                                        CustomTextComponent(
                                            text = field.title,
                                            style = AppFonts.body2Medium.copy(color = AppColors.BLACK)
                                        )

                                        Spacer(modifier = Modifier.height(4.dp))

                                        CustomTextComponent(
                                            text = kotlin.runCatching { (field.selectedValue as String) }
                                                .getOrElse { LanguageConstants.NONE.localizeString() },
                                            style = AppFonts.body2Regular.copy(color = AppColors.ColorTextSubdued)
                                        )
                                        Spacer(modifier = Modifier.height(24.dp))

                                        HorizontalDivider(
                                            modifier = Modifier
                                                .fillMaxWidth()
                                                .height(1.dp),
                                            color = AppColors.ColorsPrimitivesFour
                                        )
                                    }

                                    FirebaseConstants.TEXTAREA -> {
                                        Spacer(modifier = Modifier.height(16.dp))

                                        CustomTextComponent(
                                            text = field.title,
                                            style = AppFonts.body2Medium.copy(color = AppColors.BLACK)
                                        )

                                        Spacer(modifier = Modifier.height(4.dp))

                                        CustomTextComponent(
                                            text = kotlin.runCatching { field.selectedValue as String }
                                                .getOrElse { LanguageConstants.NONE.localizeString() },
                                            style = AppFonts.body2Regular.copy(color = AppColors.ColorTextSubdued)
                                        )
                                        Spacer(modifier = Modifier.height(24.dp))

                                        HorizontalDivider(
                                            modifier = Modifier
                                                .fillMaxWidth()
                                                .height(1.dp),
                                            color = AppColors.ColorsPrimitivesFour
                                        )
                                    }

                                    FirebaseConstants.CHECKBOX -> {
                                        CheckBoxComponent(
                                            title = field.title,
                                            enabled = false,
                                            checked = kotlin.runCatching { if (field.selectedValue as Boolean) FilterCheckboxState.Checked else FilterCheckboxState.Unchecked }
                                                .getOrElse { FilterCheckboxState.Unchecked },
                                            onCheckedChange = { }
                                        )

                                        Spacer(modifier = Modifier.height(25.dp))
//
                                    }

                                    FirebaseConstants.RADIO_BUTTON -> {
                                        RadioButtonComponent(
                                            title = field.title,
                                            onSelectedChange = {},
                                            modifier = Modifier,
                                            selected = kotlin.runCatching { (field.selectedValue as RadioButtonModel).disabled }
                                                .getOrElse { false },
                                            enabled = false
                                        )
                                        Spacer(modifier = Modifier.height(25.dp))
                                    }

                                    FirebaseConstants.DROPDOWN_SINGLE_SELECT -> {
                                        Spacer(modifier = Modifier.height(16.dp))

                                        CustomTextComponent(
                                            text = field.title,
                                            style = AppFonts.body2Medium.copy(color = AppColors.BLACK)
                                        )

                                        Spacer(modifier = Modifier.height(4.dp))

                                        CustomTextComponent(
                                            text = kotlin.runCatching { Field.from(field).label.ifEmpty { LanguageConstants.NONE.localizeString() } }
                                                .getOrElse { LanguageConstants.NONE.localizeString() },
                                            style = AppFonts.body2Regular.copy(color = AppColors.ColorTextSubdued)
                                        )

                                        Spacer(modifier = Modifier.height(24.dp))

                                        HorizontalDivider(
                                            modifier = Modifier
                                                .fillMaxWidth()
                                                .height(1.dp),
                                            color = AppColors.ColorsPrimitivesFour
                                        )
                                    }

                                    FirebaseConstants.DROPDOWN_MULTI_SELECT -> {
                                        val list = Field.fromMultiSelections((field))
                                        if (list.isNotEmpty()) {
                                            Spacer(modifier = Modifier.height(16.dp))

                                            MultiSelectDropDownComponent(
                                                title = field.title,
                                                readOnly = true,
                                                isOptional = false,
                                                isDropDownUserSearch = field.isDropDownUserSearch,
                                                onSearchInvoked = {},
                                                onFocusChanged = { },
                                                onDropDownItemSelected = { },
                                                list = list
                                            )
                                            Spacer(modifier = Modifier.height(24.dp))

                                            HorizontalDivider(
                                                modifier = Modifier
                                                    .fillMaxWidth()
                                                    .height(1.dp),
                                                color = AppColors.ColorsPrimitivesFour
                                            )
                                        }
                                    }

                                    FirebaseConstants.MULTI_DROPDOWNS -> {
                                        Spacer(modifier = Modifier.height(16.dp))

                                        MultiDropDownComponent(
                                            title = field.title,
                                            readOnly = true,
                                            isOptional = false,
                                            numberOfComponents = field.dropDownCount,
                                            onSearchInvoked = {},
                                            onFocusChanged = { },
                                            onDropDownItemSelected = { _, _ -> },
                                            list = kotlin.runCatching {
                                                Field.fromMultiDropDowns(
                                                    field
                                                )
                                            }
                                                .getOrElse {
                                                    List(size = field.dropDownCount) {
                                                        DropDownModel(label = LanguageConstants.NONE.localizeString())
                                                    }
                                                }
                                        )
                                        Spacer(modifier = Modifier.height(24.dp))
                                    }

                                    FirebaseConstants.ATTACHMENT -> {
                                        Spacer(modifier = Modifier.height(16.dp))

                                        field.attachment?.let { attachmentList.addAll(it) }
                                        if (!field.attachment.isNullOrEmpty()) {
                                            AttachmentComponent(
                                                title = field.title,
                                                readOnly = true,
                                                defaultAttachmentsList = field.attachment,
                                                isOptional = false,
                                                attachmentListener = { _, _ -> },
                                                onItemClickListener = {
                                                    context.openBrowser(
                                                        url = it.content,
                                                        headerTitle = it.fileName
                                                    )
                                                }
                                            )
                                            Spacer(modifier = Modifier.height(24.dp))
                                        }
                                    }
                                }
                            }
                        }

                }
        }

//        if (formState is UiState.Success && requestDetails != null)
//            FooterComponent(modifier = Modifier.align(Alignment.BottomCenter)) {
//                Row(
//                    Modifier
//                        .fillMaxWidth()
//                        .padding(top = 12.dp, bottom = 60.dp, start = 24.dp, end = 24.dp),
//                    horizontalArrangement = Arrangement.Center,
//                    verticalAlignment = Alignment.Top
//                ) {
//                    ButtonComponent(
//                        text = LanguageConstants.DELETE.localizeString(),
//                        clickListener = { },
//                        modifier = Modifier.weight(1f),
//                        buttonType = ButtonType.SECONDARY,
//                        buttonSize = ButtonSize.LARGE,
//                        startIcon = R.drawable.ic_comment_delete,
//                    )
//
//                    Spacer(modifier = Modifier.width(16.dp))
//
//                    ButtonComponent(
//                        text = LanguageConstants.EDIT.localizeString(),
//                        clickListener = { },
//                        modifier = Modifier.weight(1f),
//                        buttonType = ButtonType.PRIMARY,
//                        buttonSize = ButtonSize.LARGE,
//                        startIcon = R.drawable.ic_edit
//                    )
//                }
//            }

        if (formState is UiState.Error) {
            viewModel.HandleError(
                error = formState.networkError,
                isDataEmpty = formState.data?.request == null,
                showBack = false,
                withTab = true,
                dismissCallback = {
                    navController.popBackStack()
                },
            ) {
                viewModel.getRequestDetails()
            }
        }

        PullRefreshIndicator(
            refreshing = isRefreshing,
            state = pullRefreshState,
            modifier = Modifier.align(Alignment.TopCenter),
            backgroundColor = Color.White,
        )
    }
}

