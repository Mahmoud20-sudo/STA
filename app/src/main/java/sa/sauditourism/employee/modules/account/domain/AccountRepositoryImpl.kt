package sa.sauditourism.employee.modules.account.domain

import kotlinx.coroutines.flow.Flow
import sa.sauditourism.employee.EmployeeApplication
import sa.sauditourism.employee.constants.LanguageConstants
import sa.sauditourism.employee.managers.network.helpers.ApiNumberCodes
import sa.sauditourism.employee.managers.network.helpers.ApiResult
import sa.sauditourism.employee.managers.network.models.MainResponseModel
import sa.sauditourism.employee.managers.network.models.UiState
import sa.sauditourism.employee.managers.network.networkBoundNoCacheResource
import sa.sauditourism.employee.modules.account.datasource.AccountEndPoints
import sa.sauditourism.employee.modules.account.model.AccountResponse
import javax.inject.Inject

class AccountRepositoryImpl @Inject constructor(
    val api:AccountEndPoints
):AccountRepository{
    override fun getAccountDetails(userName:String): Flow<UiState<MainResponseModel<AccountResponse>>> =
        networkBoundNoCacheResource(
            fetch = {
                ApiResult.create(
                    api.getAccountDetails(
                        userName,
                        locale = EmployeeApplication.sharedPreferencesManager.preferredLocale
                            ?: LanguageConstants.DEFAULT_LOCALE,

                    ),
                    ApiNumberCodes.ACCOUNT_DETAILS_API_CODE,
                )
            },
        )
}