package sa.sauditourism.employee.modules.services.domain

import android.net.Uri
import kotlinx.coroutines.flow.Flow
import okhttp3.MediaType
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody
import okhttp3.RequestBody.Companion.asRequestBody
import sa.sauditourism.employee.EmployeeApplication
import sa.sauditourism.employee.constants.LanguageConstants
import sa.sauditourism.employee.extensions.getFilename
import sa.sauditourism.employee.managers.network.helpers.ApiNumberCodes
import sa.sauditourism.employee.managers.network.helpers.ApiResult
import sa.sauditourism.employee.managers.network.models.MainResponseModel
import sa.sauditourism.employee.managers.network.models.UiState
import sa.sauditourism.employee.managers.network.networkBoundNoCacheResource
import sa.sauditourism.employee.modules.services.datasource.remote.RequestDetailsEndpoints
import sa.sauditourism.employee.modules.services.datasource.remote.RequestFormEndpoints
import sa.sauditourism.employee.modules.services.model.FormModel
import sa.sauditourism.employee.modules.services.model.details.RequestDetailsModel
import sa.sauditourism.employee.modules.services.model.form.request.FormSubmitRequestModel
import sa.sauditourism.employee.modules.services.model.form.response.AttachmentUploadResponseModel
import sa.sauditourism.employee.modules.services.model.form.response.FormResponseModel
import java.io.File
import javax.inject.Inject

class RequestDetailsRepositoryImpl @Inject constructor(
    private val endPoints: RequestDetailsEndpoints
) : RequestDetailsRepository {
    override fun getRequestDetails(id: String): Flow<UiState<MainResponseModel<RequestDetailsModel>>> =
        networkBoundNoCacheResource(
            fetch = {
                ApiResult.create(
                    endPoints.getRequestDetails(
                        id = id,
                        locale = EmployeeApplication.sharedPreferencesManager.preferredLocale
                            ?: LanguageConstants.DEFAULT_LOCALE,
                    ),
                    ApiNumberCodes.REQUEST_DETAILS_API_CODE,
                )
            },
        )
}