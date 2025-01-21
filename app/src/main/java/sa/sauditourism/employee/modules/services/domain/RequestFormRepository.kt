package sa.sauditourism.employee.modules.services.domain

import kotlinx.coroutines.flow.Flow
import sa.sauditourism.employee.managers.network.models.MainResponseModel
import sa.sauditourism.employee.managers.network.models.UiState
import sa.sauditourism.employee.modules.services.model.form.request.FormSubmitRequestModel
import sa.sauditourism.employee.modules.services.model.form.response.AttachmentUploadResponseModel
import sa.sauditourism.employee.modules.services.model.form.response.FormResponseModel
import sa.sauditourism.employee.modules.services.model.form.response.dropDown.DropDownResponseModel
import sa.sauditourism.employee.modules.services.model.form.response.submit.FormSubmitResponseModel
import java.io.File

interface RequestFormRepository {

    fun getRequestForm(id: String): Flow<UiState<MainResponseModel<FormResponseModel>>>

    fun submitForm(formSubmitRequestModel: FormSubmitRequestModel): Flow<UiState<MainResponseModel<FormSubmitResponseModel>>>//

    fun getDropDownValues(
        requestTypeId: String,
        fieldId: String,
        query: String
    ): Flow<UiState<MainResponseModel<DropDownResponseModel>>>

    fun uploadAttachment(
        requestTypeId: String,
        file: File
    ) : Flow<UiState<MainResponseModel<AttachmentUploadResponseModel>>>
}