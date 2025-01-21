package sa.sauditourism.employee.modules.account.domain

import kotlinx.coroutines.flow.Flow
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import sa.sauditourism.employee.managers.network.helpers.ApiNumberCodes
import sa.sauditourism.employee.managers.network.helpers.ApiResult
import sa.sauditourism.employee.managers.network.models.MainResponseModel
import sa.sauditourism.employee.managers.network.models.UiState
import sa.sauditourism.employee.managers.network.networkBoundNoCacheResource
import sa.sauditourism.employee.modules.account.model.AccountResponse
import sa.sauditourism.employee.modules.account.model.DeleteProfileResponseModel
import java.io.File

interface ProfileImageRepository {
    fun deleteImage(personId :String): Flow<UiState<MainResponseModel<DeleteProfileResponseModel>>>
    fun updateImage(personId :String, file: File): Flow<UiState<MainResponseModel<DeleteProfileResponseModel>>>
}