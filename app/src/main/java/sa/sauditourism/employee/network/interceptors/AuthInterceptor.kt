package sa.sauditourism.employee.network.interceptors

import android.content.Context
import android.util.Log
import com.google.gson.GsonBuilder
import kotlinx.coroutines.delay
import kotlinx.coroutines.runBlocking
import sa.sauditourism.employee.extensions.isNetworkAvailable
import sa.sauditourism.employee.managers.network.exceptions.NoInternetException
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.Response
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import sa.sauditourism.employee.BuildConfig
import sa.sauditourism.employee.EmployeeApplication
import sa.sauditourism.employee.EmployeeApplication.Companion.sharedPreferencesManager
import sa.sauditourism.employee.managers.network.helpers.ApiNumberCodes
import sa.sauditourism.employee.managers.network.helpers.ApiResult
import sa.sauditourism.employee.managers.network.models.UiState
import sa.sauditourism.employee.managers.network.networkBoundNoCacheResource
import sa.sauditourism.employee.modules.login.authintication.AuthenticationManager
import sa.sauditourism.employee.network.remote.SSIDEndPoints
import java.util.concurrent.atomic.AtomicBoolean

class AuthInterceptor constructor() : Interceptor {
    // AtomicBoolean in order to avoid race condition
    private var tokenRefreshInProgress: AtomicBoolean = AtomicBoolean(false)
    private lateinit var request: Request

    override fun intercept(chain: Interceptor.Chain): Response = runBlocking {
        request = chain.request()
//        val originalRequest = chain.request()
        val response = chain.proceed(request)

        // Checking if a token refresh call is already in progress or not
        // The first request will enter the if block
        // Later requests will enter the else block

        if (!tokenRefreshInProgress.get()) {
            tokenRefreshInProgress.set(true)
            // Refreshing token
            if (EmployeeApplication.sharedPreferencesManager.accessToken != null) {
                refreshToken()
            }
            request = buildRequest(request.newBuilder())
            tokenRefreshInProgress.set(false)
        } else {
            // Waiting for the ongoing request to finish
            // So that we don't refresh our token multiple times
            waitForRefresh(response)
        }

        // return null to stop retrying once responseCount returns 3 or above.
        if (responseCount(response) >= 3) {
            chain.proceed(request)
        } else response
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
                        clientId = "08f0ff13b3ce287fabd06d6f4c074356",
                        clientSecret = "110fde651f016a0d79ab92361b78767b6f07f43325b241b5663d115e9857edf5",
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
                EmployeeApplication.sharedPreferencesManager.authSessionState =
                    it.data?.authSessionState
            } else if (it is UiState.Error<*>) {
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
