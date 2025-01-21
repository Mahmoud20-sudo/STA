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
import sa.sauditourism.employee.modules.services.datasource.remote.RequestFormEndpoints
import sa.sauditourism.employee.modules.services.model.FormModel
import sa.sauditourism.employee.modules.services.model.form.request.FormSubmitRequestModel
import sa.sauditourism.employee.modules.services.model.form.response.AttachmentUploadResponseModel
import sa.sauditourism.employee.modules.services.model.form.response.FormResponseModel
import java.io.File
import javax.inject.Inject

class RequestFormRepositoryImpl @Inject constructor(
    private val endPoints: RequestFormEndpoints
) : RequestFormRepository {
    override fun getRequestForm(id: String): Flow<UiState<MainResponseModel<FormResponseModel>>> =
        networkBoundNoCacheResource(
            fetch = {
                ApiResult.create(
                    endPoints.getRequestForm(
                        id = id,
                        locale = EmployeeApplication.sharedPreferencesManager.preferredLocale
                            ?: LanguageConstants.DEFAULT_LOCALE,
                    ),
                    ApiNumberCodes.GET_FORM_API_CODE,
                )
            },
        )

    override fun submitForm(formSubmitRequestModel: FormSubmitRequestModel) =
        networkBoundNoCacheResource(
            fetch = {
                ApiResult.create(
                    endPoints.submitForm(
                        formSubmitRequestModel = formSubmitRequestModel,
                        locale = EmployeeApplication.sharedPreferencesManager.preferredLocale
                            ?: LanguageConstants.DEFAULT_LOCALE,
                    ),
                    ApiNumberCodes.SUBMIT_FORM_API_CODE,
                )
            },
        )

    override fun getDropDownValues(
        requestTypeId: String,
        fieldId: String,
        query: String
    ) = networkBoundNoCacheResource(
            fetch = {
                ApiResult.create(
                    endPoints.getDropDownValues(
                        requestTypeId = requestTypeId,
                        fieldId = fieldId,
                        query = query
                    ),
                    ApiNumberCodes.GET_DROPDOWN_VALUES_API_CODE,
                )
            },
        )

    override fun uploadAttachment(
        requestTypeId: String,
        file: File
    ) = networkBoundNoCacheResource(
        fetch = {
            ApiResult.create(
                endPoints.uploadFormAttachment(
                    requestTypeId = requestTypeId,
                    file = MultipartBody.Part.createFormData("file", file.name, file.asRequestBody("multipart/form-data".toMediaTypeOrNull()))
            ), ApiNumberCodes.ADD_ATTACHMENT_API_CODE,)
        },
    )
}