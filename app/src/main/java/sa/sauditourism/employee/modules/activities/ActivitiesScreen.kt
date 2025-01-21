package sa.sauditourism.employee.modules.activities

import android.annotation.SuppressLint
import android.view.WindowInsets
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.CornerSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.pullrefresh.PullRefreshIndicator
import androidx.compose.material.pullrefresh.pullRefresh
import androidx.compose.material.pullrefresh.rememberPullRefreshState
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Color.Companion.Transparent
import androidx.compose.ui.graphics.Color.Companion.White
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.zIndex
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.skydoves.landscapist.ImageOptions
import com.skydoves.landscapist.coil.CoilImageState
import kotlinx.coroutines.launch
import sa.sauditourism.employee.R
import sa.sauditourism.employee.components.CustomTextComponent
import sa.sauditourism.employee.components.DetailsHeaderComponent
import sa.sauditourism.employee.components.GalleryType
import sa.sauditourism.employee.components.HeaderComponent
import sa.sauditourism.employee.components.HeaderModel
import sa.sauditourism.employee.components.MediaComponent
import sa.sauditourism.employee.components.MediaContent
import sa.sauditourism.employee.components.RequestActivitiesFooterComponent
import sa.sauditourism.employee.constants.LanguageConstants
import sa.sauditourism.employee.constants.TestTagsConstants.ADD_COMMENT_ACTION_TAG
import sa.sauditourism.employee.constants.TestTagsConstants.COMMENT_BODY_TAG
import sa.sauditourism.employee.constants.TestTagsConstants.COMMENT_DATE_TAG
import sa.sauditourism.employee.constants.TestTagsConstants.COMMENT_TIME_TAG
import sa.sauditourism.employee.constants.TestTagsConstants.COMMENT_USER_NAME_TAG
import sa.sauditourism.employee.constants.TestTagsConstants.NOTE_DATE_TAG
import sa.sauditourism.employee.constants.TestTagsConstants.NOTE_ICON_TAG
import sa.sauditourism.employee.constants.TestTagsConstants.NOTE_TITLE_TAG
import sa.sauditourism.employee.extensions.formatDateFromTo
import sa.sauditourism.employee.extensions.formatTimeFromTo
import sa.sauditourism.employee.extensions.getMimeType
import sa.sauditourism.employee.extensions.localizeString
import sa.sauditourism.employee.extensions.openBrowser
import sa.sauditourism.employee.managers.network.models.UiState
import sa.sauditourism.employee.modules.activities.addComment.AddCommentBottomSheet
import sa.sauditourism.employee.modules.activities.model.AddCommentResponse
import sa.sauditourism.employee.modules.activities.model.RequestComment
import sa.sauditourism.employee.modules.activities.model.RequestCommentUser
import sa.sauditourism.employee.modules.common.ChangeStatusBarIconsColors
import sa.sauditourism.employee.modules.services.model.details.Status
import sa.sauditourism.employee.modules.services.model.form.response.Attachment
import sa.sauditourism.employee.resources.AppColors
import sa.sauditourism.employee.resources.AppFonts
import sa.sauditourism.employee.shimmer.shimmerModifier

@Composable
fun ActivitiesScreen(
    navController: NavController,
    requestId: String = "",
    notes: List<Status> = listOf(),
    attachmentsList: List<Attachment> = listOf(),
    activitiesViewModel: ActivitiesViewModel = hiltViewModel(),
    addAttachment:Boolean? = false,
    addComment:Boolean?=false,
    requestTypeId: String="",
    issueId:String=""

) {
    val onBack: () -> Unit = { navController.popBackStack() }
    val state = activitiesViewModel.commentsFlow.collectAsState()
    var comments = remember { mutableStateOf(listOf<RequestComment>()) }
    val isLoading = remember { mutableStateOf(false) }

    LaunchedEffect(1) {
        activitiesViewModel.getComments(requestId,attachmentsList)
    }

    ActivitiesScreenView(
        onBack = onBack,
        isLoading = isLoading.value,
        requestId =requestId,
        issueId =issueId,
        requestTypeId = requestTypeId,
        comments = comments.value.toMutableList(),
        notes = notes,
        addNewCommment = { newComment->
            activitiesViewModel.updateListWithNewComment(newComment.comment)
        }
    ){
        activitiesViewModel.getComments(requestId,attachmentsList)
    }

    when (state.value) {
        is UiState.Loading -> isLoading.value = true
        is UiState.Success -> {
            isLoading.value = false
            comments.value = state.value.data ?: emptyList()
        }
        else -> {
            isLoading.value = false
            activitiesViewModel.HandleError(
                error = state.value.networkError,
                isDataEmpty = state.value.data?.isEmpty() ?: false,
                showBack = false,
                withTab = true,
                dismissCallback = onBack,
            ) { activitiesViewModel.getComments(requestId,attachmentsList) }
        }
    }
}

@SuppressLint("SuspiciousIndentation")
@OptIn(ExperimentalMaterialApi::class, ExperimentalMaterial3Api::class)
@Composable
fun ActivitiesScreenView(
    onBack: () -> Unit = {},
    isLoading: Boolean = false,
    requestId: String,
    issueId: String,
    requestTypeId: String,
    comments: List<RequestComment> = listOf(),
    notes: List<Status> = arrayListOf(),
    addNewCommment :(AddCommentResponse)->Unit = {},
    onRefresh: () -> Unit = {},
    ) {

    var addComment by remember { mutableStateOf(false) }



    val pullRefreshState = rememberPullRefreshState(
        refreshing = isLoading,
        onRefresh = {
            onRefresh.invoke()
        }
    )

        Box(
            contentAlignment = Alignment.Center
        ){
            ChangeStatusBarIconsColors(Transparent)

            Spacer(modifier = Modifier.height(24.dp))
            Column(
                modifier = Modifier.fillMaxSize().padding(bottom = 50.dp),
                verticalArrangement = Arrangement.Top
            ) {
                DetailsHeaderComponent(
                    headerModel = HeaderModel(
                        title = LanguageConstants.REQUEST_ACTIVITY.localizeString(),
                        showSearch = false,
                        showBackIcon = true,
                        showFavoriteIcon = false
                    ),
                    showTopBarShadow = false,
                    isStandAlone = false,
                    backgroundColor = Transparent,
                    titleMaxLines = 1,
                    showShadow = false,
                    backHandler = { onBack(); true }
                )

                Spacer(modifier = Modifier.height(8.dp))
                LazyColumn (
                    modifier = Modifier.fillMaxSize().pullRefresh(pullRefreshState).padding(bottom = 100.dp),
                ) {

                    items(notes) { NoteItem(it) }

                    if (isLoading) {
                        items(3) { CommentItem(true, RequestComment()) }
                    } else {
                        items(comments) { CommentItem(false, it) }
                    }
                }


            }
            PullRefreshIndicator(
                refreshing = isLoading,
                state = pullRefreshState,
                modifier = Modifier.align(Alignment.TopCenter),
                backgroundColor = White,
            )

            RequestActivitiesFooterComponent(
                title = LanguageConstants.ADD_COMMENT.localizeString(),
                modifier = Modifier
                    .fillMaxWidth()
                    .align(Alignment.BottomCenter)
                    .padding(androidx.compose.foundation.layout.WindowInsets.navigationBars.asPaddingValues())
                    .shadow(24.dp, shape = MaterialTheme.shapes.small.copy(CornerSize(0.dp)), ambientColor = AppColors.BLACK, clip = true)
                    .testTag(ADD_COMMENT_ACTION_TAG),
                enabled = !isLoading,
                onClick = {
                    addComment = !addComment
                }
            )

        }


     val  coroutineScope = rememberCoroutineScope()
        val sheetState = rememberModalBottomSheetState(
            skipPartiallyExpanded = true
        )
        if(addComment){
            ModalBottomSheet(
                containerColor = White,
                sheetState = sheetState,
                onDismissRequest = {
                    coroutineScope.launch {
                        sheetState.hide()
                    }
                    addComment = false
                }

            ) {
                AddCommentBottomSheet(
                    requestTypeId,
                    requestId,
                    issueId,
                    onCancelButtonClick = {
                        coroutineScope.launch {
                            sheetState.hide()
                        }
                        addComment = false
                    },
                    onSaveButtonClick = { newComment->
                        coroutineScope.launch {
                            sheetState.hide()
                        }
                        addComment = false
                        addNewCommment(newComment)
                    }
                )
            }
        }

}

@Composable
private fun NoteItem(note: Status) {
    Card(
        shape = RoundedCornerShape(8.dp),
        modifier = Modifier.padding(vertical = 8.dp, horizontal = 16.dp).fillMaxWidth(),
        elevation = CardDefaults.cardElevation(2.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White, contentColor = Color.White)
    ) {
        Column(modifier = Modifier.padding(vertical = 16.dp, horizontal = 24.dp)) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(painter = painterResource(R.drawable.ic_info), tint = AppColors.ColorTextSubdued, contentDescription = "", modifier = Modifier.testTag(NOTE_ICON_TAG))
                Spacer(modifier = Modifier.width(8.dp))
                CustomTextComponent(
                    modifier = Modifier.testTag(NOTE_TITLE_TAG),
                    text = note.status,
                    color = AppColors.ColorTextSubdued,
                    style = AppFonts.ctaLarge,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                    isLoading = false
                )
            }
            Spacer(modifier = Modifier.height(4.dp))
            Row {
                Spacer(modifier = Modifier.width(27.dp))
                CustomTextComponent(
                    modifier = Modifier.testTag(NOTE_DATE_TAG),
                    text = note.date?.formatDateFromTo(
                        "yyyy-MM-dd",
                        "dd MMM yyyy"
                    ) ?: "",
                    color = AppColors.ColorsPrimitivesFour,
                    style = AppFonts.capsSmallRegular,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                    isLoading = false
                )
                Spacer(modifier = Modifier.width(2.dp))
                CustomTextComponent(
                    text = note.time?.formatTimeFromTo("HH:mm:ss.SSSZ", "hh:mm a") ?: "",
                    color = AppColors.ColorsPrimitivesFour,
                    style = AppFonts.capsSmallRegular,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                    isLoading = false
                )
            }
        }
    }
}

@SuppressLint("SuspiciousIndentation")
@Composable
private fun CommentItem(isLoading: Boolean, comment: RequestComment) {
    var isCoilLoading by remember { mutableStateOf(false) }
    Card(
        shape = RoundedCornerShape(8.dp),
        modifier = Modifier.padding(vertical = 8.dp, horizontal = 16.dp).fillMaxWidth(),
        elevation = CardDefaults.cardElevation(2.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White, contentColor = Color.White)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                MediaComponent(
                    modifier = Modifier.size(40.dp).clip(CircleShape).shimmerModifier(isLoading = isLoading),
                    mediaContent = MediaContent(GalleryType.IMAGE, source = comment.user?.userImage ?: ""),
                    isLoading = isLoading || isCoilLoading,
                    onImageStateChanged = {
                        if (it is CoilImageState.Success || it is CoilImageState.Failure) { isCoilLoading = false }
                    },
                    placeHolder = R.drawable.ic_avatar
                )
                Spacer(modifier = Modifier.width(8.dp))
                Column {
                    CustomTextComponent(
                        modifier = Modifier.testTag(COMMENT_USER_NAME_TAG),
                        text = (comment.user?.displayName ?: comment.user?.name) ?: "",
                        color = AppColors.ColorTextSubdued,
                        style = AppFonts.ctaLarge,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis,
                        isLoading = isLoading
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    Row {
                        CustomTextComponent(
                            modifier = Modifier.testTag(COMMENT_DATE_TAG),
                            text = comment.date?.formatDateFromTo("yyyy-MM-dd", "dd MMM yyyy") ?: "",
                            color = AppColors.ColorsPrimitivesFour,
                            style = AppFonts.capsSmallRegular,
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis,
                            isLoading = isLoading
                        )
                        Spacer(modifier = Modifier.width(2.dp))
                        CustomTextComponent(
                            modifier = Modifier.testTag(COMMENT_TIME_TAG),
                            text = comment.time?.formatTimeFromTo("HH:mm:ss.SSSZ", "hh:mm a") ?: "",
                            color = AppColors.ColorsPrimitivesFour,
                            style = AppFonts.capsSmallRegular,
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis,
                            isLoading = isLoading
                        )
                    }
                }
            }
            Spacer(modifier = Modifier.height(16.dp))
            if(comment.attachments.isEmpty())
            CustomTextComponent(
                modifier = Modifier.testTag(COMMENT_BODY_TAG),
                text = comment.body ?: "",
                color = AppColors.ColorTextSubdued,
                style = AppFonts.body1Regular,
                maxLines = 3,
                overflow = TextOverflow.Ellipsis,
                isLoading = isLoading
            )
            Spacer(modifier = Modifier.height(16.dp))
            comment.attachments.forEach {
                AttachmentItem(false,it)
                 Spacer(Modifier.height(12.dp))
            }
        }
    }
}

@Composable
private fun AttachmentItem(isLoading: Boolean , attachment:Attachment) {
    var isCoilLoading by remember { mutableStateOf(false) }
    val context = LocalContext.current

    val thumbnail by remember {
        mutableStateOf(
            MediaContent(
                type = GalleryType.IMAGE,
                source = attachment.thumbnail
            )
        )
    }

    Card(
        shape = RoundedCornerShape(8.dp),
        modifier = Modifier
            .fillMaxWidth()
            .border(
                width = 1.dp,
                shape = RoundedCornerShape(8.dp),
                color =AppColors.ColorsPrimitivesThree
            ),
        elevation = CardDefaults.cardElevation(0.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White, contentColor = Color.White)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            MediaComponent(
                modifier = Modifier
                    .size(24.dp)
                    .shimmerModifier(isLoading = isLoading),
                mediaContent = thumbnail,
                imageOptions = ImageOptions(
                    contentScale = ContentScale.FillBounds,
                    alignment = Alignment.Center,
                ),
                isLoading = isLoading || isCoilLoading,
                onImageStateChanged = {
                    if (it is CoilImageState.Success || it is CoilImageState.Failure) { isCoilLoading = false }
                },
                placeHolder = if(attachment.fileName.getMimeType().startsWith("image")) R.drawable.ic_thumbnal_image else R.mipmap.ic_document,
            )
            Spacer(modifier = Modifier.width(8.dp))
            CustomTextComponent(
                modifier = Modifier.weight(1f),
                text = attachment.fileName,
                color = AppColors.ColorTextSubdued,
                style = AppFonts.body1Regular,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
                isLoading = isLoading
            )
            IconButton(onClick = {
                context.openBrowser(
                    url = attachment.content,
                    headerTitle = attachment.fileName
                )
            }) {
                Icon(painter = painterResource(R.drawable.ic_eye), tint = AppColors.ColorTextSubdued, contentDescription = "")
            }
        }
    }
}

@Preview
@Composable
private fun ActivitiesScreenPreview() = ActivitiesScreenView(
    requestTypeId = "123",
    requestId = "123",
    issueId = "123",
    comments = mutableListOf(RequestComment(id = "123123", user = RequestCommentUser(
        userImage = "",
        name = "Essam Mohamed",
        displayName = "Essam"
    ), body = "Lorem ipsum Lorem ipsum Lorem ipsum Lorem ipsum Lorem ipsum Lorem ipsum Lorem ipsum Lorem ipsum Lorem ipsum", date = "2024-10-27", time = "16:55:35.877+0300")),
    notes = arrayListOf(Status(status = "Completed", date = "2024-10-27", time = "16:55:35.877+0300"))
)

@Preview
@Composable
private fun NoteItemPreview() = NoteItem(
    Status(status = "Completed", date = "2024-10-27", time = "16:55:35.877+0300")
)

@Preview
@Composable
private fun CommentItemPreview() = CommentItem(
    false,
    RequestComment(
        id = "123123",
        user = RequestCommentUser(
        userImage = "",
        name = "Essam Mohamed",
        displayName = "Essam"
    ), body = "Lorem ipsum Lorem ipsum Lorem ipsum Lorem ipsum Lorem ipsum Lorem ipsum Lorem ipsum Lorem ipsum Lorem ipsum", date = "2024-10-27", time = "16:55:35.877+0300")
)