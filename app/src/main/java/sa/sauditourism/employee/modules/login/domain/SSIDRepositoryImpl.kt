package sa.sauditourism.employee.modules.login.domain

import android.net.Uri
import sa.sauditourism.employee.EmployeeApplication
import sa.sauditourism.employee.managers.environment.EnvironmentKeys
import sa.sauditourism.employee.managers.network.helpers.ApiResult
import sa.sauditourism.employee.managers.network.models.UiState
import sa.sauditourism.employee.managers.network.networkBoundNoCacheResource
import sa.sauditourism.employee.models.ssid.LogoutModel
import sa.sauditourism.employee.models.ssid.TokenModel
import sa.sauditourism.employee.network.remote.SSIDEndPoints
import kotlinx.coroutines.flow.Flow
import  sa.sauditourism.employee.managers.network.helpers.ApiNumberCodes
import javax.inject.Inject

class SSIDRepositoryImpl @Inject constructor(private val endPoints: SSIDEndPoints) :
    SSIDRepository {

    override fun getAccessToken(
        authCode: String,
        codeVerify: String
    ): Flow<UiState<TokenModel>> =
        networkBoundNoCacheResource(
            fetch = {
                ApiResult.create(
                    endPoints.getAccessToken(
                        authCode = authCode,
                        codeVerify = codeVerify,
                        authType = "authorization_code",
                        clientId =   EmployeeApplication.instance.environmentManager.getVariable(
                            EnvironmentKeys.OPEN_AUTH_CLIENT_ID),
                        clientSecret = EmployeeApplication.instance.environmentManager.getVariable(
                            EnvironmentKeys.OPEN_AUTH_CLIENT_SECRET),
                        scope = "openid profile email",
                        redirectUri = Uri.parse(EmployeeApplication.instance.environmentManager.getVariable(
                            EnvironmentKeys.OPEN_AUTH_TOKEN_URL))
                    ),
                    ApiNumberCodes.SSID_API_CODE,
                )
            },
        )


    override fun doLogout(): Flow<UiState<LogoutModel>> =
        networkBoundNoCacheResource(
            fetch = {
                ApiResult.create(
                    endPoints.doLogout(
                        refreshToken = EmployeeApplication.sharedPreferencesManager.refreshToken.orEmpty(),
                        clientId = EmployeeApplication.instance.environmentManager.getVariable(
                            EnvironmentKeys.OPEN_AUTH_CLIENT_ID),
                        clientSecret = EmployeeApplication.instance.environmentManager.getVariable(
                            EnvironmentKeys.OPEN_AUTH_CLIENT_SECRET),
                    ),
                    ApiNumberCodes.SSID_API_CODE,
                )
            },
        )
}
