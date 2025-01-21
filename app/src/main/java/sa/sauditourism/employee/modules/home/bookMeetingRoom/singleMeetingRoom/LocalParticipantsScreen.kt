package sa.sauditourism.employee.modules.home.bookMeetingRoom.singleMeetingRoom

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CornerSize
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.unit.dp
import androidx.compose.ui.zIndex
import sa.sauditourism.employee.R
import sa.sauditourism.employee.components.DetailsHeaderComponent
import sa.sauditourism.employee.components.HeaderModel
import sa.sauditourism.employee.constants.LanguageConstants
import sa.sauditourism.employee.constants.TestTagsConstants.ADD_COMMENT_ACTION_TAG
import sa.sauditourism.employee.extensions.localizeString
import sa.sauditourism.employee.modules.account.PaySlipRow
import sa.sauditourism.employee.modules.activities.participants.ParticipantItem
import sa.sauditourism.employee.modules.activities.participants.RequestParticipantFooterComponent
import sa.sauditourism.employee.modules.activities.participants.addParticipant.AddParticipantBottomSheet
import sa.sauditourism.employee.modules.common.ChangeStatusBarIconsColors
import sa.sauditourism.employee.modules.services.model.participants.Participant
import sa.sauditourism.employee.resources.AppColors.BLACK

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun LocalParticipantsScreen(
    modifier: Modifier = Modifier,
    backHandler: (() -> Boolean?)? = null,
    viewModel: SingleMeetingRoomViewModel

) {
    var showAddBottomSheet by remember { mutableStateOf(false) }
    var isAddParticipantAllowed by remember { mutableStateOf(true) }
    var isRemoveParticipantAllowed by remember { mutableStateOf(true) }
    var isLoading by remember { mutableStateOf(false) }
    val selectedParticipants by viewModel.selectedParticipants
    var showError by remember { mutableStateOf(false) }

    val state = rememberScrollState()
    val showOverlay by remember {
        derivedStateOf {
            state.value == 0
        }
    }

    LaunchedEffect(selectedParticipants) {
        if (selectedParticipants.isEmpty()){
            backHandler?.invoke()
        }
    }


    ChangeStatusBarIconsColors(Color.Transparent)

    Box(
        modifier = Modifier
            .fillMaxSize()
            .navigationBarsPadding(),
        contentAlignment = Alignment.Center
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize(),
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
                    selectedParticipants.map {
                        Participant(
                            emailAdress = it.extra.orEmpty(),
                            displayName = it.label,
                            name = it.label,
                        )
                    }.onEachIndexed { index, item ->
                        ParticipantItem(
                            item,
                            isRemoveParticipantAllowed,
                            dialogDeleteIcon = R.drawable.ic_remove_participant,
                            alertIconSize = 52.dp,
                            itemDeleteIcon = R.drawable.ic_cross
                        ) {
                            viewModel.removeParticipant(it)
                        }
                        if (index == selectedParticipants.lastIndex)
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
                requestId = "-1",
                locally = true,
                selectedUsers = viewModel.selectedParticipants.value,
                onAddUsers = {
                    showAddBottomSheet = false
                    viewModel.updateSelectedParticipants(it)
                },
                onDissmess = { showAddBottomSheet = false }) {
                showAddBottomSheet = false
            }


//        if (viewModel.participantsList.isEmpty() && !isLoading)
//            EmptyViewComponent(
//                modifier = Modifier.align(Alignment.Center),
//                model = EmptyViewModel(
//                    title = LanguageConstants.NO_RESULT_FOUND.localizeString(),
//                    description = LanguageConstants.NO_RESULT_FOUND_MESSAGE.localizeString(),
//                    imageResId = R.mipmap.ic_no_data
//                )
//            )
    }
}
