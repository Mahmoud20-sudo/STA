package sa.sauditourism.employee.modules.login.domain

import kotlinx.coroutines.flow.Flow
import sa.sauditourism.employee.managers.network.helpers.ApiNumberCodes
import sa.sauditourism.employee.managers.network.helpers.ApiResult
import sa.sauditourism.employee.managers.network.models.MainResponseModel
import sa.sauditourism.employee.managers.network.models.UiState
import sa.sauditourism.employee.managers.network.networkBoundNoCacheResource
import sa.sauditourism.employee.modules.login.datasource.ProfileEndPoints
import javax.inject.Inject

class ProfileRepositoryImpl @Inject constructor(
    private val profileEndPoints: ProfileEndPoints
):ProfileRepository {
    override fun getUserData(): Flow<UiState<MainResponseModel<ProfileModel>>>
    = networkBoundNoCacheResource(
    fetch = {
        ApiResult.create(
            profileEndPoints.getProfileData(),
            ApiNumberCodes.PROFILE_DATA_API_CODE,
        )
    },
    )

}