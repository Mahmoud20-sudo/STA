package sa.sauditourism.employee.modules.activities.participants

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.CornerSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.pullrefresh.PullRefreshIndicator
import androidx.compose.material.pullrefresh.pullRefresh
import androidx.compose.material.pullrefresh.rememberPullRefreshState
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.zIndex
import androidx.hilt.navigation.compose.hiltViewModel
import com.skydoves.landscapist.coil.CoilImageState
import sa.sauditourism.employee.R
import sa.sauditourism.employee.components.ButtonComponent
import sa.sauditourism.employee.components.ButtonSize
import sa.sauditourism.employee.components.ButtonType
import sa.sauditourism.employee.components.CustomAlertDialog
import sa.sauditourism.employee.components.CustomTextComponent
import sa.sauditourism.employee.components.DetailsHeaderComponent
import sa.sauditourism.employee.components.DialogType
import sa.sauditourism.employee.components.EmptyViewComponent
import sa.sauditourism.employee.components.EmptyViewModel
import sa.sauditourism.employee.components.GalleryType
import sa.sauditourism.employee.components.HeaderComponent
import sa.sauditourism.employee.components.HeaderModel
import sa.sauditourism.employee.components.MediaComponent
import sa.sauditourism.employee.components.MediaContent
import sa.sauditourism.employee.components.ToastPresentable
import sa.sauditourism.employee.components.ToastType
import sa.sauditourism.employee.components.ToastView
import sa.sauditourism.employee.constants.LanguageConstants
import sa.sauditourism.employee.constants.TestTagsConstants.ADD_COMMENT_ACTION_TAG
import sa.sauditourism.employee.extensions.localizeString
import sa.sauditourism.employee.managers.network.models.UiState
import sa.sauditourism.employee.modules.account.PaySlipRow
import sa.sauditourism.employee.modules.activities.participants.addParticipant.AddParticipantBottomSheet
import sa.sauditourism.employee.modules.common.ChangeStatusBarIconsColors
import sa.sauditourism.employee.modules.services.details.RequestDetailsViewModel
import sa.sauditourism.employee.modules.services.model.participants.Participant
import sa.sauditourism.employee.resources.AppColors
import sa.sauditourism.employee.resources.AppColors.BLACK
import sa.sauditourism.employee.resources.AppFonts
import sa.sauditourism.employee.shimmer.shimmerModifier

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun ParticipantsScreen(
    modifier: Modifier = Modifier,
    backHandler: (() -> Boolean?)? = null,
    viewModel: RequestDetailsViewModel
) {
    val requestDetails by viewModel.detailsResultFlow.collectAsState()
    val participantsFlow by viewModel.participantsFlow.collectAsState()
    var showAddBottomSheet by remember { mutableStateOf(false) }
    var isAddParticipantAllowed by remember { mutableStateOf(false) }
    var isRemoveParticipantAllowed by remember { mutableStateOf(false) }
    var isLoading by remember { mutableStateOf(false) }

    var showError by remember { mutableStateOf(false) }
    val participantState by viewModel.participantsFlow.collectAsState()

    val state = rememberScrollState()
    val showOverlay by remember {
        derivedStateOf {
            state.value == 0
        }
    }

    val pullRefreshState = rememberPullRefreshState(
        refreshing = if (participantsFlow.data?.participants?.isNotEmpty() == true) participantsFlow is UiState.Loading else false,
        onRefresh = {
            isLoading = true
            viewModel.getParticipates()
        }
    )

    LaunchedEffect(requestDetails) {
        if (requestDetails is UiState.Success) {
            isAddParticipantAllowed = requestDetails.data?.request?.addParticipant ?: false
            isRemoveParticipantAllowed = requestDetails.data?.request?.removeParticipant ?: false
        }
    }

    LaunchedEffect(participantState) {
        showError = participantState is UiState.Error
        isLoading = participantState is UiState.Loading
    }

    ChangeStatusBarIconsColors(Color.Transparent)

    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .pullRefresh(pullRefreshState),
            verticalArrangement = Arrangement.Top
        ) {
            DetailsHeaderComponent(
                headerModel = HeaderModel(
                    title = LanguageConstants.PARTICIPANTS_HEADER_TITLE.localizeString(),
                ),
                titleMaxLines = 1,
                backHandler = backHandler,
                backgroundColor = Color.Transparent,
                showOverlay = showOverlay
            )

            Column(modifier = Modifier.verticalScroll(state)) {

                Spacer(Modifier.height(10.dp))

                if (isLoading)
                    (0..6).onEach {
                        PaySlipRow(isLoading = true)
                        Spacer(modifier = Modifier.height(16.dp))
                    }
                else
                    viewModel.participantsList.onEachIndexed { index, item ->
                        ParticipantItem(item, isRemoveParticipantAllowed) {
                            viewModel.requestParticipantAction(listOf(it))
                        }
                        if (index == viewModel.participantsList.lastIndex)
                            Spacer(modifier = Modifier.height(100.dp))
                    }
            }
        }

        Spacer(modifier = Modifier.height(30.dp))

        if (isAddParticipantAllowed)
            RequestParticipantFooterComponent(
                title = LanguageConstants.ADD_PARTICIPANTS_BUTTON_TITLE.localizeString(),
                modifier = Modifier
                    .fillMaxWidth()
                    .align(Alignment.BottomCenter)
                    .zIndex(100f)
                    .shadow(
                        24.dp,
                        shape = MaterialTheme.shapes.medium.copy(CornerSize(0.dp)),
                        ambientColor = BLACK,
                        clip = true
                    )
                    .testTag(ADD_COMMENT_ACTION_TAG),
                enabled = true,
                onClick = {
                    showAddBottomSheet = true
                }
            )

        if (showAddBottomSheet)
            AddParticipantBottomSheet(
                requestId = viewModel.requestTypeId,
                onDissmess = { showAddBottomSheet = false }) {
                showAddBottomSheet = false
                viewModel.getParticipates()
            }

        if (showError) {
            ToastView(
                presentable = ToastPresentable(
                    type = ToastType.Error,
                    title = participantState.networkError?.message
                        ?: LanguageConstants.UNEXPECTED_ERROR_HAS_OCCURRED.localizeString(),
                ),
                toastDuration = 5000L,
                onDismiss = {
                    showError = false
                },
            )
        }

        if (viewModel.participantsList.isEmpty() && !isLoading)
            EmptyViewComponent(
                modifier = Modifier.align(Alignment.Center),
                model = EmptyViewModel(
                    title = LanguageConstants.NO_RESULT_FOUND.localizeString(),
                    description = LanguageConstants.NO_RESULT_FOUND_MESSAGE.localizeString(),
                    imageResId = R.mipmap.ic_no_data
                )
            )

        PullRefreshIndicator(
            refreshing = participantsFlow is UiState.Loading,
            state = pullRefreshState,
            modifier = Modifier.align(Alignment.TopCenter),
            backgroundColor = Color.White,
        )
    }
}


@Composable
fun ParticipantItem(
    participant: Participant?,
    isRemoveParticipantAllowed: Boolean,
    dialogDeleteIcon: Int? = R.drawable.ic_delete_icon,
    itemDeleteIcon: Int? = R.drawable.ic_delete,
    dialogDeleteTitle: String? = LanguageConstants.DELETE_MESSAGE.localizeString(),
    primaryButtonText: String? = LanguageConstants.DELETE.localizeString(),
    alertIconSize: Dp? = 80.dp,
    onDelete: (String) -> Unit
) {
    var isCoilLoading by remember { mutableStateOf(false) }
    var showDeleteDialog by remember { mutableStateOf(false) }

    Box {
        Card(
            shape = RoundedCornerShape(8.dp),
            modifier = Modifier
                .zIndex(20f)
                .fillMaxWidth()
                .padding(horizontal = 24.dp, vertical = 6.dp),
            elevation = CardDefaults.cardElevation(2.dp),
            colors = CardDefaults.cardColors(
                containerColor = Color.White,
                contentColor = Color.White
            )
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(start = 8.dp, top = 6.dp, bottom = 6.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                MediaComponent(
                    modifier = Modifier
                        .size(40.dp)
                        .clip(CircleShape),
                    mediaContent = MediaContent(
                        GalleryType.IMAGE,
                        source = participant?.userImage
                    ),
                    isLoading = isCoilLoading,
                    onImageStateChanged = {
                        if (it is CoilImageState.Success || it is CoilImageState.Failure) {
                            isCoilLoading = false
                        }
                    },
                    placeHolder = R.drawable.ic_person_avatar
                )
                Spacer(modifier = Modifier.size(16.dp))
                CustomTextComponent(
                    modifier = Modifier.weight(1f),
                    text = participant?.displayName,
                    color = BLACK,
                    style = AppFonts.heading7Medium,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                    isLoading = false
                )
                if (isRemoveParticipantAllowed)
                    IconButton(onClick = {
                        showDeleteDialog = true
                    }) {
                        Icon(
                            painter = painterResource(itemDeleteIcon ?: R.drawable.ic_delete),
                            tint = BLACK, contentDescription = "",
                            modifier = Modifier.padding(start = 10.dp)
                        )
                    }
            }
        }

        if (showDeleteDialog) {
            CustomAlertDialog(
                type = DialogType.Normal,
                imageResId = dialogDeleteIcon,
                shouldShowCloseIcon = false,
                title = dialogDeleteTitle,
                iconSize = alertIconSize,
                primaryButtonText = primaryButtonText,
                secondaryButtonText = LanguageConstants.CANCEL.localizeString(),
                onPrimaryButtonClick = {
                    showDeleteDialog = false
                    onDelete.invoke(participant?.name!!)
                },
                onSecondaryButtonClick = {
                    showDeleteDialog = false
                },
                onDismiss = {
                    showDeleteDialog = false
                }
            )
        }
    }
}

@Composable
@Preview(showBackground = true)
fun PreviewParticipantItem() {
    ParticipantItem(participant = Participant(), isRemoveParticipantAllowed = false, onDelete = {})
}

@Composable
fun RequestParticipantFooterComponent(
    modifier: Modifier = Modifier,
    onClick: () -> Unit = {},
    enabled: Boolean = false,
    backgroundColor: Color = Color.White,
    title: String = LanguageConstants.ADD_COMMENT.localizeString(),
) {
    Column(
        modifier
            .background(backgroundColor)
            .fillMaxWidth()
            .height(96.dp)
            .padding(start = 16.dp, end = 16.dp, bottom = 8.dp, top = 16.dp),
        verticalArrangement = Arrangement.Top
    ) {
        ButtonComponent(
            title,
            clickListener = onClick,
            modifier = Modifier
                .padding(4.dp)
                .fillMaxWidth()
                .padding(horizontal = 8.dp),
            buttonType = ButtonType.PRIMARY,
            buttonSize = ButtonSize.LARGE,
            enabled = enabled,
            //icon does not exist in design
//            startIcon = R.drawable.ic_add_icon
        )
    }
}
