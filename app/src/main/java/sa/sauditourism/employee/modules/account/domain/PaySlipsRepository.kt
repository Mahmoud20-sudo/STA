package sa.sauditourism.employee.modules.account.domain

import kotlinx.coroutines.flow.Flow
import sa.sauditourism.employee.managers.network.models.MainResponseModel
import sa.sauditourism.employee.managers.network.models.UiState
import sa.sauditourism.employee.modules.account.model.PaySlipsResponseModel

fun interface PaySlipsRepository {
    fun getPaySlips(date: String?): Flow<UiState<MainResponseModel<PaySlipsResponseModel>>>
}