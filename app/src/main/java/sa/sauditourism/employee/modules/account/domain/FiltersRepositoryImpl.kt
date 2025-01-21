package sa.sauditourism.employee.modules.account.domain

import kotlinx.coroutines.flow.Flow
import sa.sauditourism.employee.EmployeeApplication
import sa.sauditourism.employee.constants.LanguageConstants
import sa.sauditourism.employee.managers.network.helpers.ApiNumberCodes
import sa.sauditourism.employee.managers.network.helpers.ApiResult
import sa.sauditourism.employee.managers.network.models.MainResponseModel
import sa.sauditourism.employee.managers.network.models.UiState
import sa.sauditourism.employee.managers.network.networkBoundNoCacheResource
import sa.sauditourism.employee.modules.account.datasource.FiltersEndPoints
import sa.sauditourism.employee.modules.services.model.ServicesResponseModel
import javax.inject.Inject

class FiltersRepositoryImpl @Inject constructor(
    val api: FiltersEndPoints
): FiltersRepository{
    override fun getFilters(): Flow<UiState<MainResponseModel<ServicesResponseModel>>> =
        networkBoundNoCacheResource(
            fetch = {
                ApiResult.create(
                    api.getFilters(
                        locale = EmployeeApplication.sharedPreferencesManager.preferredLocale
                            ?: LanguageConstants.DEFAULT_LOCALE,

                        ),
                    ApiNumberCodes.ACCOUNT_DETAILS_API_CODE,
                )
            },
        )
}