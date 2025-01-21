package sa.sauditourism.employee.modules.login.domain

import kotlinx.coroutines.flow.Flow
import sa.sauditourism.employee.managers.network.models.MainResponseModel
import sa.sauditourism.employee.managers.network.models.UiState

fun interface ProfileRepository {

    fun  getUserData():  Flow<UiState<MainResponseModel<ProfileModel>>>
}