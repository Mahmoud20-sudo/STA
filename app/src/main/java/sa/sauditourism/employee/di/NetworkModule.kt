package sa.sauditourism.employee.di

import android.content.Context
import com.chuckerteam.chucker.api.ChuckerCollector
import com.chuckerteam.chucker.api.ChuckerInterceptor
import com.google.firebase.BuildConfig
import com.google.gson.GsonBuilder
import sa.sauditourism.employee.EmployeeApplication
import sa.sauditourism.employee.managers.network.helpers.ApiResult
import sa.sauditourism.employee.managers.network.interceptors.NetworkConnectionInterceptor
import sa.sauditourism.employee.network.interceptors.TokenAuthenticator
import sa.sauditourism.employee.network.remote.SSIDEndPoints
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.Cache
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import sa.sauditourism.employee.EmployeeApplication.Companion.sharedPreferencesManager
import sa.sauditourism.employee.constants.ApiConstants
import sa.sauditourism.employee.modules.account.datasource.AccountEndPoints
import sa.sauditourism.employee.modules.services.datasource.remote.ServicesEndpoints
import sa.sauditourism.employee.managers.environment.EnvironmentKeys
import sa.sauditourism.employee.modules.account.datasource.FiltersEndPoints
import sa.sauditourism.employee.modules.account.datasource.PaySlipsEndPoints
import sa.sauditourism.employee.modules.account.datasource.ProfileImageEndPoints
import sa.sauditourism.employee.modules.activities.datasource.remote.ActivitiesEndpoints
import sa.sauditourism.employee.modules.home.bookMeetingRoom.datasource.AvailableMeetingRoomsEndPoints
import sa.sauditourism.employee.modules.home.bookMeetingRoom.datasource.BookMeetingRoomEndPoints
import sa.sauditourism.employee.modules.home.datasource.remote.HomeEndpoints
import sa.sauditourism.employee.modules.home.myDayMeetings.datasource.remote.MyMeetingsEndPoints
import sa.sauditourism.employee.modules.login.datasource.ProfileEndPoints
import sa.sauditourism.employee.modules.services.datasource.remote.ParticipantsEndpoints
import sa.sauditourism.employee.modules.services.datasource.remote.RequestDetailsEndpoints
import sa.sauditourism.employee.modules.services.datasource.remote.RequestFormEndpoints
import sa.sauditourism.employee.modules.services.datasource.remote.RequestTypeEndPoints
import java.io.File
import java.util.concurrent.TimeUnit
import javax.inject.Singleton

@InstallIn(SingletonComponent::class)
@Module
class NetworkModule {

    @Provides
    @Singleton
    fun provideRetrofit(client: OkHttpClient): Retrofit {
        return Retrofit.Builder()
            .baseUrl(EmployeeApplication.instance.environmentManager.getVariable(EnvironmentKeys.API_BASE_URL))
            //.baseUrl("https://81.208.170.16/api/v1/")
            .client(client)
            .addConverterFactory(GsonConverterFactory.create(GsonBuilder().create()))
            .build()
    }

    @Provides
    @Singleton
    fun provideOkHttpClient(
        headerInterceptor: Interceptor,
        networkConnectionInterceptor: NetworkConnectionInterceptor,
        tokenAuthenticator: TokenAuthenticator,
        cache: Cache,
        chuckerInterceptor: ChuckerInterceptor
    ): OkHttpClient {

        val okHttpClientBuilder = OkHttpClient().newBuilder()

        okHttpClientBuilder.connectTimeout(CONNECTION_TIMEOUT.toLong(), TimeUnit.SECONDS)
        okHttpClientBuilder.readTimeout(READ_TIMEOUT.toLong(), TimeUnit.SECONDS)
        okHttpClientBuilder.writeTimeout(WRITE_TIMEOUT.toLong(), TimeUnit.SECONDS)

        okHttpClientBuilder.cache(cache)

        okHttpClientBuilder.addInterceptor(headerInterceptor)
        okHttpClientBuilder.addInterceptor(networkConnectionInterceptor)
        okHttpClientBuilder.authenticator(tokenAuthenticator)

        val interceptor: HttpLoggingInterceptor = HttpLoggingInterceptor().apply {
            this.level =
                if (BuildConfig.DEBUG) HttpLoggingInterceptor.Level.BODY else HttpLoggingInterceptor.Level.BODY
        }

        okHttpClientBuilder.addInterceptor(interceptor)

        // add Chunker interceptor
        okHttpClientBuilder.addInterceptor(chuckerInterceptor)

        return okHttpClientBuilder.build()
    }

    @Provides
    @Singleton
    fun provideHeaderInterceptor(): Interceptor {
        return Interceptor {
            val requestBuilder = it.request().newBuilder()
            //hear you can add all headers you want by calling 'requestBuilder.addHeader(name ,  value)'
            requestBuilder.addHeader("Accept", "*/*")
            requestBuilder.addHeader("useragent", "emplpyeeapp")
            if (it.request().url.toString()
                    .contains("/api/v1/services/user-profile/") || it.request().url.toString()
                    .contains("/api/v1/services/remove-profile-picture/") || it.request().url.toString()
                    .contains("/api/v1/services/update-profile-picture/")
            ) {
                requestBuilder.addHeader("Authorization", "Basic bWRpbkBzdGEuZ292LnNhOjEyMzQ1Njc4")
            } else if (it.request().url.toString().contains("services/payslips-data")) {
                requestBuilder.addHeader(
                    "auth-key",
                    "Basic U1RBX01PQklMRV9JTlRFR1JBVElPTjoxMjM0NTY3OA=="
                )
            } else {
                requestBuilder.addHeader(
                    "auth-key",
                    "Bearer " + sharedPreferencesManager.accessToken
                )
            }
            requestBuilder.addHeader(
                "api-key",
                EmployeeApplication.instance.environmentManager.getVariable(EnvironmentKeys.API_KEY)
            )
            requestBuilder.addHeader(
                "locale",
                EmployeeApplication.sharedPreferencesManager.preferredLocale ?: "en"
            )

            it.proceed(requestBuilder.build())
        }
    }

    @Provides
    @Singleton
    fun provideTokenInterceptor(): TokenAuthenticator {
        return TokenAuthenticator()
    }

    @Provides
    @Singleton
    fun provideNetworkConnectionInterceptor(context: Context): NetworkConnectionInterceptor {
        return NetworkConnectionInterceptor(context)
    }

    @Provides
    @Singleton
    internal fun provideCache(context: Context): Cache {
        val httpCacheDirectory = File(context.cacheDir.absolutePath, "HttpCache")
        return Cache(httpCacheDirectory, CACHE_SIZE_BYTES)
    }

    @Provides
    @Singleton
    fun provideChuckerInterceptor(): ChuckerInterceptor {
        return ChuckerInterceptor.Builder(EmployeeApplication.instance.applicationContext)
            .collector(ChuckerCollector(EmployeeApplication.instance.applicationContext))
            .maxContentLength(250000L)
            .redactHeaders(emptySet())
            .alwaysReadResponseBody(false)
            .build()
    }

    companion object {
        const val READ_TIMEOUT = 300
        const val WRITE_TIMEOUT = 300
        const val CONNECTION_TIMEOUT = 300
        const val CACHE_SIZE_BYTES = 10 * 1024 * 1024L // 10 MB
    }

    suspend fun <T> getResponse(
        request: suspend () -> Response<T>
    ): ApiResult<T> {
        return try {
            val result = request.invoke()
            if (result.isSuccessful) {
                return ApiResult.success(result.body(), result.code())
            } else {
//result.errorBody()!!.string()
                ApiResult.error(
                    message = result.errorBody().toString(),
                    code = result.code(),
                    error = null
                )
            }
        } catch (e: Throwable) {
            ApiResult.error("Unkown Error", error = e)
        }
    }

    @Provides
    @Singleton
    fun provideSSIDEndpoints(retrofit: Retrofit): SSIDEndPoints {
        return retrofit.create(SSIDEndPoints::class.java)
    }

    @Provides
    @Singleton
    fun provideServicesEndpoints(retrofit: Retrofit): ServicesEndpoints {
        return retrofit.create(ServicesEndpoints::class.java)
    }

    @Provides
    @Singleton
    fun provideRequestTypeEndPoints(retrofit: Retrofit): RequestTypeEndPoints {
        return retrofit.create(RequestTypeEndPoints::class.java)
    }

    @Provides
    @Singleton
    fun provideAccountEndpoints(retrofit: Retrofit): AccountEndPoints {
        return retrofit.create(AccountEndPoints::class.java)
    }

    @Provides
    @Singleton
    fun provideProfileEndpoints(retrofit: Retrofit): ProfileEndPoints {
        return retrofit.create(ProfileEndPoints::class.java)
    }

    @Provides
    @Singleton
    fun provideRequestFormEndpoints(retrofit: Retrofit): RequestFormEndpoints {
        return retrofit.create(RequestFormEndpoints::class.java)
    }

    @Provides
    @Singleton
    fun provideActivitiesEndpoints(retrofit: Retrofit): ActivitiesEndpoints {
        return retrofit.create(ActivitiesEndpoints::class.java)
    }

    @Provides
    @Singleton
    fun provideRequestDetailsEndpoints(retrofit: Retrofit): RequestDetailsEndpoints {
        return retrofit.create(RequestDetailsEndpoints::class.java)
    }

    @Provides
    @Singleton
    fun providePaySlipsEndPoints(retrofit: Retrofit): PaySlipsEndPoints {
        return retrofit.create(PaySlipsEndPoints::class.java)
    }

    @Provides
    @Singleton
    fun provideFiltersEndPoints(retrofit: Retrofit): FiltersEndPoints {
        return retrofit.create(FiltersEndPoints::class.java)
    }

    @Provides
    @Singleton
    fun provideParticipantsEndpoints(retrofit: Retrofit): ParticipantsEndpoints {
        return retrofit.create(ParticipantsEndpoints::class.java)
    }

    @Provides
    @Singleton
    fun provideProfileImageEndPoints(retrofit: Retrofit): ProfileImageEndPoints {
        return retrofit.create(ProfileImageEndPoints::class.java)
    }

    @Provides
    @Singleton
    fun provideMyDayMeetingsEndpoints(retrofit: Retrofit): MyMeetingsEndPoints {
        return retrofit.create(MyMeetingsEndPoints::class.java)
    }

    @Provides
    @Singleton
    fun provideHomeEndPoints(retrofit: Retrofit): HomeEndpoints {
        return retrofit.create(HomeEndpoints::class.java)
    }

    @Provides
    @Singleton
    fun provideAvailableMeetingRoomsEndPoints(retrofit: Retrofit): AvailableMeetingRoomsEndPoints {
        return retrofit.create(AvailableMeetingRoomsEndPoints::class.java)
    }
    @Provides
    @Singleton
    fun provideBookMeetingRoomEndPoints(retrofit: Retrofit): BookMeetingRoomEndPoints {
        return retrofit.create(BookMeetingRoomEndPoints::class.java)
    }

}
