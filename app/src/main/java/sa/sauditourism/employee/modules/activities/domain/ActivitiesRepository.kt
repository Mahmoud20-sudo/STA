package sa.sauditourism.employee.modules.activities.domain

import kotlinx.coroutines.flow.Flow
import sa.sauditourism.employee.managers.network.models.MainResponseModel
import sa.sauditourism.employee.managers.network.models.UiState
import sa.sauditourism.employee.modules.activities.model.AddCommentRequest
import sa.sauditourism.employee.modules.activities.model.AddCommentResponse
import sa.sauditourism.employee.modules.activities.model.AddCommentWithAttachment
import sa.sauditourism.employee.modules.activities.model.AddUserResponse
import sa.sauditourism.employee.modules.activities.model.RequestCommentResponse
import sa.sauditourism.employee.modules.activities.model.SearchUserResponse

interface ActivitiesRepository {
    fun getRequestComments(requestId: String): Flow<UiState<MainResponseModel<RequestCommentResponse>>>
    fun searchForUser(
        query: String,
        locally:Boolean?=false
    ): Flow<UiState<MainResponseModel<SearchUserResponse>>>

    fun addUsers(
        requestId: String,
        userList: List<String>
    ): Flow<UiState<MainResponseModel<AddUserResponse>>>


    fun addComment(
        requestId: String,
        addCommentRequest: AddCommentRequest
    ): Flow<UiState<MainResponseModel<AddCommentResponse>>>

    fun addCommentWithAttachment(
        requestId: String,
        addCommentWithAttachment: AddCommentWithAttachment
    ): Flow<UiState<MainResponseModel<AddCommentResponse>>>
}