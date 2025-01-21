package sa.sauditourism.employee.network.remote

import android.net.Uri
import sa.sauditourism.employee.EmployeeApplication
import sa.sauditourism.employee.managers.environment.EnvironmentKeys
import sa.sauditourism.employee.models.ssid.LogoutModel
import sa.sauditourism.employee.models.ssid.TokenModel
import retrofit2.Response
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Query
import retrofit2.http.Url

interface SSIDEndPoints {

    @FormUrlEncoded
    @POST
    suspend fun getAccessToken(
        @Url url: String =   EmployeeApplication.instance.environmentManager.getVariable(
            EnvironmentKeys.OPEN_AUTH_URL) +   EmployeeApplication.instance.environmentManager.getVariable(EnvironmentKeys.SSID_TOKEN_ENDPOINT),
        @Field("code") authCode: String,
        @Field("code_verifier") codeVerify: String,
        @Field("grant_type") authType: String,
        @Field("client_id") clientId: String,
        @Field("client_secret") clientSecret: String,
        @Field("scope") scope: String,
        @Field("redirect_uri") redirectUri: Uri
        ): Response<TokenModel>

    @FormUrlEncoded
    @POST("token")
    suspend fun getAccessTokenByRefreshToken(
        @Field("grant_type") authType: String,
        @Field("client_id") clientId: String,
        @Field("client_secret") clientSecret: String,
        @Field("refresh_token") refreshToken: String,
        @Field("scope") scope: String,
        @Field("token_content_type") tokenContentType: String,
        ): Response<TokenModel>


    @FormUrlEncoded
    @POST
    suspend fun doLogout(
        @Url url: String =   EmployeeApplication.instance.environmentManager.getVariable(EnvironmentKeys.BASE_URL_V2) +
                EmployeeApplication.instance.environmentManager.getVariable(EnvironmentKeys.SSID_LOGOUT_ENDPOINT),
        @Field("refresh_token") refreshToken: String,
        @Field("client_id") clientId: String,
        @Field("client_secret") clientSecret: String,
    ): Response<LogoutModel>
}
