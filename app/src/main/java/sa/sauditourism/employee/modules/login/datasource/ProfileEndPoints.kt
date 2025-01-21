package sa.sauditourism.employee.modules.login.datasource

import retrofit2.Response
import retrofit2.http.GET
import sa.sauditourism.employee.managers.network.models.MainResponseModel
import sa.sauditourism.employee.modules.login.domain.ProfileModel

fun interface ProfileEndPoints {

    @GET("/api/v1/services/my-profile")
    suspend fun getProfileData(): Response<MainResponseModel<ProfileModel>>
}