package sa.sauditourism.employee.modules.account.datasource

import okhttp3.MultipartBody
import retrofit2.Response
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.Part
import retrofit2.http.Path
import retrofit2.http.Query
import sa.sauditourism.employee.constants.ApiConstants
import sa.sauditourism.employee.constants.ApiConstants.USER_ID
import sa.sauditourism.employee.managers.network.models.MainResponseModel
import sa.sauditourism.employee.modules.account.model.AccountResponse
import sa.sauditourism.employee.modules.account.model.DeleteProfileResponseModel

interface ProfileImageEndPoints {
    @DELETE(ApiConstants.DELETE_PROFILE_PIC)
    suspend fun deleteProfileImage(
        @Path(USER_ID) personId: String,
        @Query("locale") locale: String,
    ): Response<MainResponseModel<DeleteProfileResponseModel>>


    @Multipart
    @POST(ApiConstants.UPDATE_PROFILE_PIC)
    suspend fun updateProfileImage(
        @Path(USER_ID) personId: String,
        @Query("locale") locale: String,
        @Part file: MultipartBody.Part
    ): Response<MainResponseModel<DeleteProfileResponseModel>>
}