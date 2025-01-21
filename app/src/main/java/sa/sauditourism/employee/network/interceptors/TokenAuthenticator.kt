package sa.sauditourism.employee.network.interceptors

import android.util.Log
import com.google.gson.GsonBuilder
import sa.sauditourism.employee.BuildConfig
import sa.sauditourism.employee.EmployeeApplication
import sa.sauditourism.employee.EmployeeApplication.Companion.sharedPreferencesManager
import sa.sauditourism.employee.managers.environment.EnvironmentKeys
import sa.sauditourism.employee.managers.network.helpers.ApiResult
import sa.sauditourism.employee.managers.network.models.UiState
import sa.sauditourism.employee.managers.network.networkBoundNoCacheResource
import sa.sauditourism.employee.network.remote.SSIDEndPoints
import sa.sauditourism.employee.modules.login.authintication.AuthenticationManager
import kotlinx.coroutines.delay
import kotlinx.coroutines.runBlocking
import okhttp3.Authenticator
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.Response
import okhttp3.Route
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import sa.sauditourism.employee.managers.network.helpers.ApiNumberCodes
import java.io.IOException
import java.util.concurrent.atomic.AtomicBoolean
import kotlin.jvm.Throws
import kotlin.jvm.java
import kotlin.text.orEmpty

class TokenAuthenticator : Authenticator {

    // AtomicBoolean in order to avoid race condition
    private var tokenRefreshInProgress: AtomicBoolean = AtomicBoolean(false)
    private var request: Request? = null

    @Throws(IOException::class)
    override fun authenticate(route: Route?, response: Response): Request? = runBlocking {
        request = null

        // Checking if a token refresh call is already in progress or not
        // The first request will enter the if block
        // Later requests will enter the else block
        if (!tokenRefreshInProgress.get()) {
            tokenRefreshInProgress.set(true)
            // Refreshing token
            if (EmployeeApplication.sharedPreferencesManager.accessToken != null) {
                refreshToken()
            }
            request = buildRequest(response.request.newBuilder())
            tokenRefreshInProgress.set(false)
        } else {
            // Waiting for the ongoing request to finish
            // So that we don't refresh our token multiple times
            waitForRefresh(response)
        }

        println("Token Authenticator has been automatically called")

        // return null to stop retrying once responseCount returns 3 or above.
        if (responseCount(response) >= 3) {
            null
        } else request
    }

    // Refresh your token here and save them.
    private suspend fun refreshToken() {
        val builder = OkHttpClient.Builder()
        if (BuildConfig.DEBUG) {
            val interceptor = HttpLoggingInterceptor()
            interceptor.apply {
                interceptor.level =
                    HttpLoggingInterceptor.Level.BODY
            }
            builder.addInterceptor(interceptor)
        }

        val client = builder.build()
        val retrofit: Retrofit = Retrofit.Builder()
            .baseUrl("https://agile.sta.gov.sa/rest/oauth2/latest/")
            .client(client)
            .addConverterFactory(GsonConverterFactory.create(GsonBuilder().create()))
            .build()

        val ssidEndPoints = retrofit.create(SSIDEndPoints::class.java)

        networkBoundNoCacheResource(
            fetch = {
                ApiResult.create(

                    ssidEndPoints.getAccessTokenByRefreshToken(
                        authType = "refresh_token",
                        clientId = "57dffe6ca1d776aee246a59c578b3a4c",
                        clientSecret = "349bed809264a519d421eec1e874d5c9ecfcb7ef6f70f0899ce172132a20d6e2",
                        refreshToken = EmployeeApplication.sharedPreferencesManager.refreshToken.orEmpty(),
                        scope = "WRITE",
                        tokenContentType = "jwt"
                    ),
                    ApiNumberCodes.SSID_API_CODE,
                )
            },
        ).collect {
            if (it is UiState.Success<*>) {
                EmployeeApplication.sharedPreferencesManager.accessToken = it.data?.accessToken
                EmployeeApplication.sharedPreferencesManager.refreshToken = it.data?.refreshToken
                EmployeeApplication.sharedPreferencesManager.authSessionState = it.data?.authSessionState
            } else if (it is UiState.Error<*>){
                if (it.networkError?.code == 400) {
                    sharedPreferencesManager.accessToken = null
                    sharedPreferencesManager.refreshToken = null
                    sharedPreferencesManager.authCode = null
                    sharedPreferencesManager.authCode = null
                    sharedPreferencesManager.authState = null
                    AuthenticationManager.openAuthWeb()
                }
                println(it.toString())
                Log.e("RefreshToken", "Failed to refresh token")
            }
        }
    }

    // Queuing the requests with delay
    private suspend fun waitForRefresh(response: Response) {
        while (tokenRefreshInProgress.get()) {
            delay(100)
        }
        request = buildRequest(response.request.newBuilder())
    }

    private fun responseCount(response: Response?): Int {
        var result = 1
        while (response?.priorResponse != null && result <= 3) {
            result++
        }
        return result
    }

    // Build a new request with new access token
    private fun buildRequest(requestBuilder: Request.Builder): Request {
        return requestBuilder
            .header("Content-Type", "application/json")
            .header(
                "Authorization",
                "Bearer " + EmployeeApplication.sharedPreferencesManager.accessToken.orEmpty()
            )
            .build()
    }
}

