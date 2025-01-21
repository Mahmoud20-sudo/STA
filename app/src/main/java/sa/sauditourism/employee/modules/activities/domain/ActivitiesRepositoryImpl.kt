package sa.sauditourism.employee.modules.activities.domain

import com.huawei.agconnect.config.ktx.fetch
import kotlinx.coroutines.flow.Flow
import sa.sauditourism.employee.constants.LanguageConstants
import sa.sauditourism.employee.managers.network.helpers.ApiNumberCodes
import sa.sauditourism.employee.managers.network.helpers.ApiResult
import sa.sauditourism.employee.managers.network.models.MainResponseModel
import sa.sauditourism.employee.managers.network.models.UiState
import sa.sauditourism.employee.managers.network.networkBoundNoCacheResource
import sa.sauditourism.employee.managers.sharedprefrences.SharedPreferencesManager
import sa.sauditourism.employee.modules.activities.model.AddCommentRequest
import sa.sauditourism.employee.modules.activities.model.AddCommentWithAttachment
import sa.sauditourism.employee.modules.activities.datasource.remote.ActivitiesEndpoints
import sa.sauditourism.employee.modules.activities.model.AddUserResponse
import sa.sauditourism.employee.modules.activities.model.AddUsersRequest
import sa.sauditourism.employee.modules.activities.model.AddCommentResponse
import sa.sauditourism.employee.modules.activities.model.RequestCommentResponse
import sa.sauditourism.employee.modules.activities.model.SearchUserResponse

class ActivitiesRepositoryImpl(
    private val activitiesEndpoints: ActivitiesEndpoints,
    private val sharedPreferences: SharedPreferencesManager
) : ActivitiesRepository {

    override fun getRequestComments(requestId: String): Flow<UiState<MainResponseModel<RequestCommentResponse>>> =
        networkBoundNoCacheResource(
            fetch = {
                ApiResult.create(
                    activitiesEndpoints.getRequestComments(
                        requestId = requestId,
                        locale = sharedPreferences.preferredLocale
                            ?: LanguageConstants.DEFAULT_LOCALE,
                    ),
                    ApiNumberCodes.SERVICES_API_CODE,
                )
            },
        )

    override fun searchForUser(
        query: String,
        locally: Boolean?
    ): Flow<UiState<MainResponseModel<SearchUserResponse>>> = networkBoundNoCacheResource(
        fetch = {

            if (locally == true) {
                ApiResult.create(
                    activitiesEndpoints.searchForParticipants(
                        query = query,
                        locale = sharedPreferences.preferredLocale
                            ?: LanguageConstants.DEFAULT_LOCALE,
                    ),
                    ApiNumberCodes.SERVICES_API_CODE,
                )
            } else {
                ApiResult.create(
                    activitiesEndpoints.searchForUser(
                        query = query,
                        locale = sharedPreferences.preferredLocale
                            ?: LanguageConstants.DEFAULT_LOCALE,
                    ),
                    ApiNumberCodes.SERVICES_API_CODE,
                )
            }

        },
    )

    override fun addUsers(
        requestId: String,
        userList: List<String>
    ): Flow<UiState<MainResponseModel<AddUserResponse>>> = networkBoundNoCacheResource(
        fetch = {
            ApiResult.create(
                activitiesEndpoints.addUsers(
                    locale = sharedPreferences.preferredLocale ?: LanguageConstants.DEFAULT_LOCALE,
                    requestId = requestId,
                    addUsersRequest = AddUsersRequest(usersName = userList)

                ),
                ApiNumberCodes.ADD_USER__API_CODE,
            )
        },
    )

    override fun addComment(
        requestId: String,
        addCommentRequest: AddCommentRequest
    ): Flow<UiState<MainResponseModel<AddCommentResponse>>> = networkBoundNoCacheResource(
        fetch = {
            ApiResult.create(
                activitiesEndpoints.addComment(
                    requestId = requestId,
                    locale = sharedPreferences.preferredLocale ?: LanguageConstants.DEFAULT_LOCALE,
                    addCommentRequest = addCommentRequest
                ),
                ApiNumberCodes.SERVICES_API_CODE,
            )
        },
    )

    override fun addCommentWithAttachment(
        requestId: String,
        addCommentWithAttachment: AddCommentWithAttachment
    ): Flow<UiState<MainResponseModel<AddCommentResponse>>> = networkBoundNoCacheResource(
        fetch = {
            ApiResult.create(
                activitiesEndpoints.addCommentWithAttachment(
                    requestId = requestId,
                    locale = sharedPreferences.preferredLocale ?: LanguageConstants.DEFAULT_LOCALE,
                    addCommentWithAttachment = addCommentWithAttachment
                ),
                ApiNumberCodes.SERVICES_API_CODE,
            )
        },
    )


}