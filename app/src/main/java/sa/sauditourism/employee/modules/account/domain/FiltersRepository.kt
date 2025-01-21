package sa.sauditourism.employee.modules.account.domain

import kotlinx.coroutines.flow.Flow
import sa.sauditourism.employee.managers.network.models.MainResponseModel
import sa.sauditourism.employee.managers.network.models.UiState
import sa.sauditourism.employee.modules.services.model.ServicesResponseModel

fun interface FiltersRepository {
    fun getFilters(): Flow<UiState<MainResponseModel<ServicesResponseModel>>>
}