package sa.sauditourism.employee.modules.account.domain

import kotlinx.coroutines.flow.Flow
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import sa.sauditourism.employee.EmployeeApplication
import sa.sauditourism.employee.constants.LanguageConstants
import sa.sauditourism.employee.managers.network.helpers.ApiNumberCodes
import sa.sauditourism.employee.managers.network.helpers.ApiResult
import sa.sauditourism.employee.managers.network.models.MainResponseModel
import sa.sauditourism.employee.managers.network.models.UiState
import sa.sauditourism.employee.managers.network.networkBoundNoCacheResource
import sa.sauditourism.employee.modules.account.datasource.AccountEndPoints
import sa.sauditourism.employee.modules.account.datasource.ProfileImageEndPoints
import sa.sauditourism.employee.modules.account.model.AccountResponse
import sa.sauditourism.employee.modules.account.model.DeleteProfileResponseModel
import java.io.File
import javax.inject.Inject

class ProfileImageRepositoryImpl @Inject constructor(
    val api:ProfileImageEndPoints
):ProfileImageRepository{
     override fun deleteImage(personId: String): Flow<UiState<MainResponseModel<DeleteProfileResponseModel>>> =
        networkBoundNoCacheResource(
            fetch = {
                ApiResult.create(
                    api.deleteProfileImage(
                        personId = personId,
                        locale = EmployeeApplication.sharedPreferencesManager.preferredLocale
                            ?: LanguageConstants.DEFAULT_LOCALE,
                        ),
                    ApiNumberCodes.DELETE_PROFILE_IMAGE_API_CODE,
                )
            },
        )

    override fun updateImage(
        personId: String,
        file: File
    ): Flow<UiState<MainResponseModel<DeleteProfileResponseModel>>>   = networkBoundNoCacheResource(
        fetch = {
            ApiResult.create(
                api.updateProfileImage(
                    personId = personId,
                    locale = EmployeeApplication.sharedPreferencesManager.preferredLocale
                        ?: LanguageConstants.DEFAULT_LOCALE,
                    file = MultipartBody.Part.createFormData("file", file.name, file.asRequestBody("multipart/form-data".toMediaTypeOrNull()))
                ), ApiNumberCodes.ADD_ATTACHMENT_API_CODE,)
        },
    )
}