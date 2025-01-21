package sa.sauditourism.employee.modules.services.domain

import kotlinx.coroutines.flow.Flow
import sa.sauditourism.employee.EmployeeApplication
import sa.sauditourism.employee.constants.LanguageConstants
import sa.sauditourism.employee.managers.network.helpers.ApiNumberCodes
import sa.sauditourism.employee.managers.network.helpers.ApiResult
import sa.sauditourism.employee.managers.network.models.MainResponseModel
import sa.sauditourism.employee.managers.network.models.UiState
import sa.sauditourism.employee.managers.network.networkBoundNoCacheResource
import sa.sauditourism.employee.modules.services.datasource.remote.ServicesEndpoints
import sa.sauditourism.employee.modules.services.model.ServicesResponseModel
import javax.inject.Inject

class ServicesRepositoryImpl @Inject constructor(
    private val endPoints: ServicesEndpoints
) : ServicesRepository {

    override fun getServices(): Flow<UiState<MainResponseModel<ServicesResponseModel>>> =
        networkBoundNoCacheResource(
            fetch = {
                ApiResult.create(
                    endPoints.getServices(
                        locale = EmployeeApplication.sharedPreferencesManager.preferredLocale
                            ?: LanguageConstants.DEFAULT_LOCALE,
                    ),
                    ApiNumberCodes.SERVICES_API_CODE,
                )
            },
        )
}