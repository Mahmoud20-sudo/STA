package sa.sauditourism.employee.modules.account.domain

import kotlinx.coroutines.flow.Flow
import sa.sauditourism.employee.managers.network.models.MainResponseModel
import sa.sauditourism.employee.managers.network.models.UiState
import sa.sauditourism.employee.modules.account.model.AccountResponse

fun interface AccountRepository {
    fun getAccountDetails(userName:String): Flow<UiState<MainResponseModel<AccountResponse>>>
}