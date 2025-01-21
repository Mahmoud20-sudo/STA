package sa.sauditourism.employee.modules.services.domain

import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.last
import kotlinx.coroutines.flow.toList
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.Mockito.`when`
import org.mockito.junit.MockitoJUnitRunner
import retrofit2.Response
import sa.sauditourism.employee.base.BaseUnitTest
import sa.sauditourism.employee.managers.network.models.MainResponseModel
import sa.sauditourism.employee.managers.network.models.UiState
import sa.sauditourism.employee.managers.sharedprefrences.SharedPreferencesManager
import sa.sauditourism.employee.modules.services.datasource.remote.RequestTypeEndPoints
import sa.sauditourism.employee.modules.services.model.RequestTypeResponse
import sa.sauditourism.employee.modules.account.myRequests.model.MyRequest
import sa.sauditourism.employee.modules.account.myRequests.model.MyRequestsResponse

@ExperimentalCoroutinesApi
@RunWith(MockitoJUnitRunner::class)
class RequestTypeRepositoryImplTest : BaseUnitTest() {
    @Mock
    private lateinit var api: RequestTypeEndPoints

    private val sharedPreferencesManager = mockk<SharedPreferencesManager>()

    private lateinit var repository: RequestTypeRepositoryImpl

    @Before
    fun setUp() {
        repository = RequestTypeRepositoryImpl(api, sharedPreferencesManager)

    }

    @Test
    fun `test searchRequestTypesById returns successful response`() = runTest {

        val query = "search"
        val locale = "en"
        val response = Response.success(
            MainResponseModel(
                data = RequestTypeResponse(requestsTypesList = emptyList()),
                code = 200,
                message = "success"
            )
        )
        every { sharedPreferencesManager.preferredLocale } returns "en"
        `when`(api.searchWithServiceId("1",query, locale)).thenReturn(response)


        val results = repository.searchRequestTypesById("1",query).toList()

        assertTrue(results.first() is UiState.Loading)
        assertTrue(results.last() is UiState.Success)
        assertEquals(response.body(), (results.last() as UiState.Success).data)
    }
    @Test
    fun `test searchAllRequestTypes returns successful response`() = runTest {

        val query = "search"
        val locale = "en"
        val response = Response.success(
            MainResponseModel(
                data = RequestTypeResponse(requestsTypesList = emptyList()),
                code = 200,
                message = "success"
            )
        )
        every { sharedPreferencesManager.preferredLocale } returns "en"
        `when`(api.search(query, locale)).thenReturn(response)


        val results = repository.searchAllRequestTypes(query).toList()

        assertTrue(results.first() is UiState.Loading)
        assertTrue(results.last() is UiState.Success)
        assertEquals(response.body(), (results.last() as UiState.Success).data)
    }
    @Test
    fun `test searchRequestTypesById returns error response`() = runTest {

        val query = "search"
        val locale = "en"

        every { sharedPreferencesManager.preferredLocale } returns "en"
        `when`(api.searchWithServiceId("1",query, locale)).thenThrow(RuntimeException("Network Error"))


        val results = repository.searchRequestTypesById("1",query).toList()

        assertTrue(results.first() is UiState.Loading)
        assertTrue(results.last() is UiState.Error)
    }
    @Test
    fun `test searchAllRequestTypes returns error response`() = runTest {

        val query = "search"
        val locale = "en"

        every { sharedPreferencesManager.preferredLocale } returns "en"
        `when`(api.search(query, locale)).thenThrow(RuntimeException("Network Error"))


        val results = repository.searchAllRequestTypes(query).toList()

        assertTrue(results.first() is UiState.Loading)
        assertTrue(results.last() is UiState.Error)
    }

    @Test
    fun `test getMyRequests returns success response`() = runTest {
        val locale = "en"
        val response = Response.success(
            MainResponseModel(
                data = MyRequestsResponse(requestsList = listOf(MyRequest("1","Title","http://icon.com","closed","10:140","20/10/2012"))),
                code = 200,
                message = "success"
            )
        )
        every { sharedPreferencesManager.preferredLocale } returns locale
        `when`(api.getMyRequests(locale)).thenReturn(response)

        val result = repository.getMyRequests(null, null)
        assertTrue(result.first() is UiState.Loading)
        assertTrue(result.last() is UiState.Success)
        assertEquals(response.body(), (result.last() as UiState.Success).data)
    }

    @Test
    fun `test getMyRequests returns error response`() = runTest {
        val locale = "en"
        every { sharedPreferencesManager.preferredLocale } returns locale
        `when`(api.getMyRequests( locale)).thenThrow(RuntimeException("Network Error"))

        val result = repository.getMyRequests(null, null)
        assertTrue(result.first() is UiState.Loading)
        assertTrue(result.last() is UiState.Error)
    }
}