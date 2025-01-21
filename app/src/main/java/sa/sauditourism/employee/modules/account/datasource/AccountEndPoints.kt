package sa.sauditourism.employee.modules.account.datasource

import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Query
import sa.sauditourism.employee.constants.ApiConstants
import sa.sauditourism.employee.managers.network.models.MainResponseModel
import sa.sauditourism.employee.modules.account.model.AccountResponse

 interface AccountEndPoints {
    @GET(ApiConstants.ACCOUNT_DETAILS)
    suspend fun getAccountDetails(
        @Query("userName") userName: String,
        @Query("locale") locale: String,
    ): Response<MainResponseModel<AccountResponse>>
}