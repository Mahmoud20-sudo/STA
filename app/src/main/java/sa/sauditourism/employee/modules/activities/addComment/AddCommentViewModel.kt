package sa.sauditourism.employee.modules.activities.addComment

import android.net.Uri
import androidx.lifecycle.DefaultLifecycleObserver
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.onEmpty
import kotlinx.coroutines.launch
import sa.sauditourism.employee.base.BaseViewModel
import sa.sauditourism.employee.extensions.launchIO
import sa.sauditourism.employee.managers.network.helpers.ApiNumberCodes
import sa.sauditourism.employee.managers.network.helpers.NetworkError
import sa.sauditourism.employee.managers.network.models.UiState
import sa.sauditourism.employee.managers.realtime.RealTimeDatabase
import sa.sauditourism.employee.modules.activities.domain.ActivitiesRepository
import sa.sauditourism.employee.modules.activities.model.AddCommentRequest
import sa.sauditourism.employee.modules.activities.model.AddCommentResponse
import sa.sauditourism.employee.modules.activities.model.AddCommentWithAttachment
import sa.sauditourism.employee.modules.activities.model.AdditionalComment
import sa.sauditourism.employee.modules.services.domain.RequestFormRepository
import sa.sauditourism.employee.modules.services.form.MAX_FILE_SIZE
import sa.sauditourism.employee.modules.services.model.Filed
import sa.sauditourism.employee.modules.services.model.form.response.Attachment
import timber.log.Timber
import java.io.File
import javax.inject.Inject

@HiltViewModel
class AddCommentViewModel @Inject constructor(
    private val activitiesRepository: ActivitiesRepository,
    private val requestFormRepository: RequestFormRepository,
    realTimeDatabase: RealTimeDatabase
) : BaseViewModel(realTimeDatabase), DefaultLifecycleObserver {

    private val _showDialog = MutableStateFlow<Boolean?>(null)
    val showDialog: StateFlow<Boolean?> = _showDialog.asStateFlow()

    private var _comment: MutableStateFlow<UiState<AddCommentResponse>> =
        MutableStateFlow(UiState.Loading())
    val comment: StateFlow<UiState<AddCommentResponse>> = _comment.asStateFlow()

    val attachmentId = mutableListOf<String>()

    fun addComment(requestId: String, issueId: String, content: String) {
        if (attachmentId.isNotEmpty()) {
            addCommentWithAttachment(requestId, issueId, content)
        } else {
            addCommentWithoutAttachment(requestId, issueId, content)
        }
    }

    fun addCommentWithoutAttachment(requestId: String, issueId: String, content: String) =
        viewModelScope.launch {
            activitiesRepository.addComment(issueId, AddCommentRequest(body = content)).collect {
                when (it) {
                    is UiState.Success -> {
                        it.data?.data?.let {
                            _comment.emit(UiState.Success(it))
                        }

                    }

                    is UiState.Error -> {
                        _comment.emit(UiState.Error(NetworkError()))
                    }

                    is UiState.Loading -> {
                        _showDialog.emit(true)
                    }
                }

            }

        }

    fun addCommentWithAttachment(requestId: String, issueId: String, content: String) =
        viewModelScope.launch {

            activitiesRepository.addCommentWithAttachment(
                issueId,
                AddCommentWithAttachment(
                    additionalComment = AdditionalComment(body = content),
                    temporaryAttachmentIds = attachmentId

                )
            ).collect {

                when (it) {
                    is UiState.Success -> {
                        if (it.data?.code == 200) {
                            it.data.data?.let {

                                _comment.emit(UiState.Success(it))
                            }
                        } else {
                            _comment.emit(UiState.Error(NetworkError()))
                        }
                    }

                    is UiState.Error -> {
                        _comment.emit(UiState.Error(NetworkError()))
                    }

                    is UiState.Loading -> {
                        _showDialog.emit(true)
                    }
                }

            }

        }


    fun uploadAttachment(
        requestId: String,
        file: File,
    ) = launchIO {
        requestFormRepository.uploadAttachment(
            requestTypeId = requestId,
            file = file
        ).collect {
            when (it) {
                is UiState.Loading -> {
                    _showDialog.emit(true)
                    Timber.d("${ApiNumberCodes.ADD_ATTACHMENT_API_CODE}: UiState.Loading")
                }

                is UiState.Success<*> -> {
                    it.data?.data?.let { response ->
                        if (it.data.code != 200 || response.attachments.isEmpty()) {
                            return@collect
                        }
                        attachmentId.add(response.attachments.first().attachmentId)
                        _showDialog.emit(false)
                        Timber.d("${ApiNumberCodes.ADD_ATTACHMENT_API_CODE}: UiState.Success")
                    }
                }

                is UiState.Error -> {
                    _showDialog.emit(false)
                    it.networkError?.let { error ->
                        Timber.d("${ApiNumberCodes.ADD_ATTACHMENT_API_CODE}: ${error.message}")
                    }
                }
            }
        }
    }

    override fun clearState() = launchIO {
        super.clearState()
        _comment.emit(UiState.Loading())
        attachmentId.clear()
        _showDialog.emit(false)
    }
}