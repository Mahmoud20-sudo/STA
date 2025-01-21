package sa.sauditourism.employee.modules.home.domain

import kotlinx.coroutines.flow.Flow
import sa.sauditourism.employee.EmployeeApplication
import sa.sauditourism.employee.constants.LanguageConstants
import sa.sauditourism.employee.managers.network.helpers.ApiNumberCodes
import sa.sauditourism.employee.managers.network.helpers.ApiResult
import sa.sauditourism.employee.managers.network.models.MainResponseModel
import sa.sauditourism.employee.managers.network.models.UiState
import sa.sauditourism.employee.managers.network.networkBoundNoCacheResource
import sa.sauditourism.employee.managers.sharedprefrences.SharedPreferencesManager
import sa.sauditourism.employee.modules.home.datasource.remote.HomeEndpoints
import sa.sauditourism.employee.modules.home.model.AnnouncementResponse
import sa.sauditourism.employee.modules.home.model.QuickActionsResponseModel

class HomeRepositoryImpl (
    private val homeEndpoints: HomeEndpoints,
    private val sharedPreferences: SharedPreferencesManager
) :HomeRepository{
    override fun getAnnouncement(): Flow<UiState<MainResponseModel<AnnouncementResponse>>> = networkBoundNoCacheResource(
        fetch = {
            ApiResult.create(
                homeEndpoints.getAnnouncement(
                    locale = sharedPreferences.preferredLocale ?: LanguageConstants.DEFAULT_LOCALE,
                    actEmail = EmployeeApplication.sharedPreferencesManager.accountDetails?.email.toString()
                ),
                ApiNumberCodes.GET_ANNOUNCEMENT,
            )
        },
    )

    override fun getQuickActions(): Flow<UiState<MainResponseModel<QuickActionsResponseModel>>> = networkBoundNoCacheResource(
        fetch = {
            ApiResult.create(
                homeEndpoints.getQuickActions(
                    locale = sharedPreferences.preferredLocale ?: LanguageConstants.DEFAULT_LOCALE,
                ),
                ApiNumberCodes.GET_QUICK_ACTIONS,
            )
        },
    )
}