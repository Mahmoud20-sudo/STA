package sa.sauditourism.employee.modules.services.datasource.remote

import okhttp3.MultipartBody
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.Part
import retrofit2.http.Path
import retrofit2.http.Query
import sa.sauditourism.employee.constants.ApiConstants
import sa.sauditourism.employee.constants.ApiConstants.FIELD_TYPE_ID
import sa.sauditourism.employee.constants.ApiConstants.REQUEST_TYPE_ID
import sa.sauditourism.employee.managers.network.models.MainResponseModel
import sa.sauditourism.employee.modules.services.model.form.request.FormSubmitRequestModel
import sa.sauditourism.employee.modules.services.model.form.response.AttachmentUploadResponseModel
import sa.sauditourism.employee.modules.services.model.form.response.FormResponseModel
import sa.sauditourism.employee.modules.services.model.form.response.dropDown.DropDownResponseModel
import sa.sauditourism.employee.modules.services.model.form.response.submit.FormSubmitResponseModel

interface RequestFormEndpoints {

    @GET(ApiConstants.GET_REQUEST_FROM)
    suspend fun getRequestForm(
        @Path(REQUEST_TYPE_ID) id: String,
        @Query("locale") locale: String
    ): Response<MainResponseModel<FormResponseModel>>

    @POST(ApiConstants.SUBMIT_FORM)
    suspend fun submitForm(
        @Query("locale") locale: String,
        @Body formSubmitRequestModel: FormSubmitRequestModel
    ): Response<MainResponseModel<FormSubmitResponseModel>>

    @GET(ApiConstants.GET_DROP_DOWN_VALUES)
    suspend fun getDropDownValues(
        @Path(REQUEST_TYPE_ID) requestTypeId: String,
        @Path(FIELD_TYPE_ID) fieldId: String,
        @Query("query") query: String
    ): Response<MainResponseModel<DropDownResponseModel>>

    @Multipart
    @POST(ApiConstants.UPLOAD_ATTACHMENTS)
    suspend fun uploadFormAttachment(
        @Path(REQUEST_TYPE_ID) requestTypeId: String,
        @Part file: MultipartBody.Part
    ): Response<MainResponseModel<AttachmentUploadResponseModel>>
}
