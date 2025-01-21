package sa.sauditourism.employee.di

import android.content.Context
import com.chuckerteam.chucker.api.ChuckerInterceptor
import kotlinx.coroutines.ExperimentalCoroutinesApi
import okhttp3.Cache
import okhttp3.Call
import okhttp3.Connection
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.Protocol
import okhttp3.Request
import okhttp3.Response
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test
import org.mockito.Mock
import org.mockito.Mockito.mock
import retrofit2.Retrofit
import sa.sauditourism.employee.EmployeeApplication
import sa.sauditourism.employee.base.BaseUnitTest
import sa.sauditourism.employee.managers.network.interceptors.NetworkConnectionInterceptor
import sa.sauditourism.employee.network.interceptors.TokenAuthenticator
import java.io.File
import java.util.concurrent.TimeUnit
import org.mockito.kotlin.whenever

@ExperimentalCoroutinesApi
class NetworkModuleTest : BaseUnitTest() {

    private lateinit var networkModule: NetworkModule

    @Mock
    private lateinit var context: Context

    @Before
    fun setup() {
        EmployeeApplication.instance = mock(EmployeeApplication::class.java)
        whenever(EmployeeApplication.instance.applicationContext).thenReturn(context)
        whenever(context.cacheDir).thenReturn(File(""))
        networkModule = NetworkModule()
    }

    @Test
    fun `test provideRetrofit should return Retrofit object with correct base URL`() {
        // Given a NetworkModule object

        val client = OkHttpClient.Builder().build()

        // When we call the provideRetrofit() method
        val retrofit: Retrofit = networkModule.provideRetrofit(client)

        // Then it should return a Retrofit object with the correct base URL
        assertEquals(TEST_BASE_URL, retrofit.baseUrl().toString())
    }

    @Test
    fun `test provideOkHttpClient should return OkHttpClient object with correct settings`() {
        // Given a NetworkModule object

        // When we call the provideOkHttpClient() method
        val headerInterceptor = networkModule.provideHeaderInterceptor()
        val networkConnectionInterceptor =
            networkModule.provideNetworkConnectionInterceptor(context)
        val tokenAuthenticator = networkModule.provideTokenInterceptor()
        val cache = networkModule.provideCache(context)
        val chuckerInterceptor = mock<ChuckerInterceptor>()

        // When we provide an OkHttpClient object
        val okHttpClient = networkModule.provideOkHttpClient(
            headerInterceptor,
            networkConnectionInterceptor,
            tokenAuthenticator,
            cache,
            chuckerInterceptor,
        )

        // Then the OkHttpClient should have the correct interceptors and timeouts
        assert(okHttpClient.connectTimeoutMillis.toLong() == NetworkModule.CONNECTION_TIMEOUT * 1000L)
        assert(okHttpClient.readTimeoutMillis.toLong() == NetworkModule.READ_TIMEOUT * 1000L)
        assert(okHttpClient.writeTimeoutMillis.toLong() == NetworkModule.WRITE_TIMEOUT * 1000L)

        assert(okHttpClient.interceptors.contains(headerInterceptor))
        assert(okHttpClient.interceptors.contains(networkConnectionInterceptor))
        assert(okHttpClient.authenticator == tokenAuthenticator)
        assert(okHttpClient.interceptors.contains(chuckerInterceptor))

        // We can also check that the cache was added to the OkHttpClient
        assert(okHttpClient.cache == cache)
    }

    @Test
    fun `test provideHeaderInterceptor should add the correct headers`() {
        // Given a NetworkModule object

        // When we call the provideHeaderInterceptor() method
        val interceptor: Interceptor = networkModule.provideHeaderInterceptor()

        // And a mocked Interceptor.Chain object with a mocked Request object
        val chain = TestInterceptorChain()

        // When we apply the Header Interceptor to the Chain object
        val response = interceptor.intercept(chain)

        // Then the Request object should have the correct headers
        assertEquals("*/*", response.request.header("Accept"))
        assertEquals("visitsaudiapp", response.request.header("useragent"))
    }

    @Test
    fun `test provideTokenInterceptor should return TokenAuthenticator object`() {
        // Given a NetworkModule object

        // When we call the provideTokenInterceptor() method
        val interceptor: TokenAuthenticator = networkModule.provideTokenInterceptor()

        // Then it should return a TokenAuthenticator object
        assertEquals(TokenAuthenticator::class.java, interceptor::class.java)
    }

    @Test
    fun `test provideNetworkConnectionInterceptor should return NetworkConnectionInterceptor object`() {
        // Given a NetworkModule object

        // When we call the provideNetworkConnectionInterceptor() method
        val interceptor: NetworkConnectionInterceptor =
            networkModule.provideNetworkConnectionInterceptor(context)

        // Then it should return a NetworkConnectionInterceptor object
        assertEquals(NetworkConnectionInterceptor::class.java, interceptor::class.java)
    }

    @Test
    fun `test provideCache should return Cache object`() {
        // Given a NetworkModule object

        // When we call the provideCache() method
        val cache: Cache = networkModule.provideCache(context)

        // Then it should return a Cache object
        assertEquals(Cache::class.java, cache::class.java)
    }

    companion object {
        const val TEST_BASE_URL = "https://qa.visitsaudi.com/bin/api/v2/"
    }
}

class TestInterceptorChain : Interceptor.Chain {

    override fun request(): Request {
        return Request.Builder().url("https://example.com").build()
    }

    override fun proceed(request: Request): Response {
        return Response.Builder()
            .request(request)
            .protocol(Protocol.HTTP_1_1)
            .code(200)
            .message("OK")
            .build()
    }

    override fun connection(): Connection? {
        return null
    }

    override fun call(): Call {
        return mock(Call::class.java)
    }

    override fun connectTimeoutMillis(): Int {
        return 0
    }

    override fun withConnectTimeout(timeout: Int, unit: TimeUnit): Interceptor.Chain {
        return this
    }

    override fun readTimeoutMillis(): Int {
        return 0
    }

    override fun withReadTimeout(timeout: Int, unit: TimeUnit): Interceptor.Chain {
        return this
    }

    override fun writeTimeoutMillis(): Int {
        return 0
    }

    override fun withWriteTimeout(timeout: Int, unit: TimeUnit): Interceptor.Chain {
        return this
    }
}
