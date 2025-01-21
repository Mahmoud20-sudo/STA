package sa.sauditourism.employee.modules.activities.datasource.remote

import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path
import retrofit2.http.Query
import sa.sauditourism.employee.constants.ApiConstants
import sa.sauditourism.employee.constants.ApiConstants.REQUEST_ID
import sa.sauditourism.employee.managers.network.models.MainResponseModel
import sa.sauditourism.employee.modules.activities.model.AddUserResponse
import sa.sauditourism.employee.modules.activities.model.AddUsersRequest
import sa.sauditourism.employee.modules.activities.model.AddCommentRequest
import sa.sauditourism.employee.modules.activities.model.AddCommentResponse
import sa.sauditourism.employee.modules.activities.model.AddCommentWithAttachment
import sa.sauditourism.employee.modules.activities.model.RequestCommentResponse
import sa.sauditourism.employee.modules.activities.model.SearchUserResponse

interface ActivitiesEndpoints {

    @GET(ApiConstants.REQUEST_COMMENTS)
    suspend fun getRequestComments(
        @Path("requestId") requestId: String,
        @Query("locale") locale: String,
    ): Response<MainResponseModel<RequestCommentResponse>>


    @POST(ApiConstants.ADD_COMMENT)
    suspend fun addComment(
        @Path("requestId") requestId: String,
        @Query("locale") locale: String,
        @Body addCommentRequest: AddCommentRequest
    ): Response<MainResponseModel<AddCommentResponse>>

    @POST(ApiConstants.ADD_COMMENT_WITH_ATTACHMENT)
    suspend fun addCommentWithAttachment(
        @Path("requestId") requestId: String,
        @Query("locale") locale: String,
        @Body addCommentWithAttachment: AddCommentWithAttachment
    ): Response<MainResponseModel<AddCommentResponse>>


    @GET(ApiConstants.SEARCH_USER)
    suspend fun searchForUser(
        @Query("locale") locale: String,
        @Query("query") query: String,
    ): Response<MainResponseModel<SearchUserResponse>>


    // here to search for participants for book a meeting room
    @GET(ApiConstants.SEARCH_PARTICIPANTS)
    suspend fun searchForParticipants(
        @Query("locale") locale: String,
        @Query("query") query: String,
    ): Response<MainResponseModel<SearchUserResponse>>

    @POST(ApiConstants.ADD_USER)
    suspend fun addUsers(
        @Path(REQUEST_ID) requestId: String,
        @Query("locale") locale: String,
        @Body addUsersRequest: AddUsersRequest
    ): Response<MainResponseModel<AddUserResponse>>



}