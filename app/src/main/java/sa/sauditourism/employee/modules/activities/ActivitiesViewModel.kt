package sa.sauditourism.employee.modules.activities

import androidx.lifecycle.DefaultLifecycleObserver
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import sa.sauditourism.employee.base.BaseViewModel
import sa.sauditourism.employee.extensions.launchIO
import sa.sauditourism.employee.managers.network.helpers.ApiNumberCodes
import sa.sauditourism.employee.managers.network.models.UiState
import sa.sauditourism.employee.managers.realtime.RealTimeDatabase
import sa.sauditourism.employee.modules.activities.domain.ActivitiesRepository
import sa.sauditourism.employee.modules.activities.model.RequestComment
import sa.sauditourism.employee.modules.services.model.form.response.Attachment
import timber.log.Timber
import javax.inject.Inject

@HiltViewModel
class ActivitiesViewModel @Inject constructor(
    private val activitiesRepository: ActivitiesRepository,
    realTimeDatabase: RealTimeDatabase
) : BaseViewModel(realTimeDatabase), DefaultLifecycleObserver {

    private val _commentsFlow = MutableStateFlow<UiState<List<RequestComment>>>(UiState.Loading())
    val commentsFlow: StateFlow<UiState<List<RequestComment>>> = _commentsFlow.asStateFlow()

    fun getCommentsFlow() = _commentsFlow

    fun getComments(requestId: String, attachmentsList: List<Attachment>) = launchIO {
        activitiesRepository.getRequestComments(requestId).collect {
            when (it) {
                is UiState.Loading -> {
                    _commentsFlow.emit(UiState.Loading())
                    Timber.d("${ApiNumberCodes.REQUEST_COMMENTS_CODE}: UiState.Loading")
                }

                is UiState.Success<*> -> {
                    it.data?.data?.let { response ->
                        mapCommentToAttachemnts(response.comments, attachmentsList)
                    }


                }

                is UiState.Error -> {
                    it.networkError?.let { error ->
                        _commentsFlow.emit(UiState.Error(error))
                        Timber.d("${ApiNumberCodes.REQUEST_COMMENTS_CODE}: ${error.message}")
                    }
                }
            }
        }
    }

    fun mapCommentToAttachemnts(comments: List<RequestComment>, attachmentsList: List<Attachment>) =
        launchIO {
            comments.forEach { comment ->
                comment.body?.let {
                    val imagePattern = """!([^|]+\.(jpg|png))\|thumbnail!"""
                    val filePattern = """\[\^([^\]]+\.(pdf|docx|xlsx|txt|pptx))\]"""

                    val patternsToRemove = listOf(
                        """!([^|]+\.(jpg|png|jpeg))\|thumbnail!""",
                        """\[\^([^\]]+\.(pdf|docx|xlsx|txt|pptx))\]"""
                    )

                    val regex = imagePattern.toRegex()
                    val fileRegex = filePattern.toRegex()


                    val filterText: String = kotlin.runCatching {
                        var text = ""
                        patternsToRemove.forEach { pattern ->
                            val textRegex = Regex(pattern)
                            text = textRegex.replace(it, "")
                        }
                        text
                    }.getOrElse { "" }
                    val matches = regex.findAll(it)
                    val imageNames = matches.map { it.groupValues[1] }.toList()
                    imageNames.forEach { imageName ->
                        val attachment = attachmentsList.find { it.fileName.contains(imageName) }
                        if (attachment != null) {
                            comment.attachments.add(attachment)
                        }
                    }

                    val fileMatches = fileRegex.findAll(it)
                    val filesNames = fileMatches.map { it.groupValues[1] }.toList()
                    filesNames.forEach { fileName ->
                        val attachment = attachmentsList.find { it.fileName.contains(fileName) }
                        if (attachment != null) {
                            comment.attachments.add(attachment)
                        }


                    }
                    comment.body = filterText
                }
            }
            _commentsFlow.emit(UiState.Success(comments))
        }

    fun updateListWithNewComment(newComment: RequestComment) = launchIO {
        val currentList = _commentsFlow.value.data?.toMutableList()
        currentList?.add(newComment)

        _commentsFlow.emit(UiState.Success(currentList?.toList() ?: emptyList()))

    }

}

