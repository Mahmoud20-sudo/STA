package sa.sauditourism.employee.modules.login.domain

import sa.sauditourism.employee.managers.network.models.UiState
import sa.sauditourism.employee.models.ssid.LogoutModel
import sa.sauditourism.employee.models.ssid.TokenModel
import kotlinx.coroutines.flow.Flow

interface SSIDRepository {
    fun getAccessToken(
        authCode: String,
        codeVerify: String
    ): Flow<UiState<TokenModel>>


    fun doLogout(
    ): Flow<UiState<LogoutModel>>


}