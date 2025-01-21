package sa.sauditourism.employee.modules.account.datasource

import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Path
import retrofit2.http.Query
import sa.sauditourism.employee.constants.ApiConstants
import sa.sauditourism.employee.constants.ApiConstants.USER_ID
import sa.sauditourism.employee.managers.network.models.MainResponseModel
import sa.sauditourism.employee.modules.account.model.AccountResponse
import sa.sauditourism.employee.modules.account.model.PaySlipsResponseModel

interface PaySlipsEndPoints {
    @GET(ApiConstants.PAY_SLIPS)
    suspend fun getPaySlips(
        @Path(USER_ID) userId: String,
        @Query("PaymentDate") paymentDate: String?,//2024-01-01
        @Query("locale") locale: String,
    ): Response<MainResponseModel<PaySlipsResponseModel>>
}