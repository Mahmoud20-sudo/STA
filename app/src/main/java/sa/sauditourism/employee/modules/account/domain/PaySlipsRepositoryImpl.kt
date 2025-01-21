package sa.sauditourism.employee.modules.account.domain

import kotlinx.coroutines.flow.Flow
import sa.sauditourism.employee.EmployeeApplication
import sa.sauditourism.employee.constants.LanguageConstants
import sa.sauditourism.employee.managers.network.helpers.ApiNumberCodes
import sa.sauditourism.employee.managers.network.helpers.ApiResult
import sa.sauditourism.employee.managers.network.models.MainResponseModel
import sa.sauditourism.employee.managers.network.models.UiState
import sa.sauditourism.employee.managers.network.networkBoundNoCacheResource
import sa.sauditourism.employee.modules.account.datasource.PaySlipsEndPoints
import sa.sauditourism.employee.modules.account.model.PaySlipsResponseModel
import javax.inject.Inject

class PaySlipsRepositoryImpl @Inject constructor(
    val api: PaySlipsEndPoints
) : PaySlipsRepository {
    override fun getPaySlips(date: String?): Flow<UiState<MainResponseModel<PaySlipsResponseModel>>> =
        networkBoundNoCacheResource(
            fetch = {
                ApiResult.create(
                    api.getPaySlips(
                        userId = EmployeeApplication.sharedPreferencesManager.accountDetails?.id.toString(),
                        paymentDate = date,
                        locale = EmployeeApplication.sharedPreferencesManager.preferredLocale
                            ?: LanguageConstants.DEFAULT_LOCALE,

                        ),
                    ApiNumberCodes.PAYSLIPS_API_CODE,
                )
            },
        )
}