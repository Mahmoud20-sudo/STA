package sa.sauditourism.employee.modules.services.domain

import kotlinx.coroutines.flow.Flow

import sa.sauditourism.employee.managers.network.models.MainResponseModel
import sa.sauditourism.employee.managers.network.models.UiState
import sa.sauditourism.employee.modules.services.model.ServicesModel
import sa.sauditourism.employee.modules.services.model.ServicesResponseModel

interface ServicesRepository {

    fun getServices(): Flow<UiState<MainResponseModel<ServicesResponseModel>>>

}