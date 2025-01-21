package sa.sauditourism.employee.modules.activities

import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.toList
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mockito.`when`
import org.mockito.junit.MockitoJUnitRunner
import org.mockito.kotlin.mock
import retrofit2.Response
import sa.sauditourism.employee.base.BaseUnitTest
import sa.sauditourism.employee.managers.network.helpers.NetworkError
import sa.sauditourism.employee.managers.network.models.MainResponseModel
import sa.sauditourism.employee.managers.network.models.UiState
import sa.sauditourism.employee.managers.sharedprefrences.SharedPreferencesManager
import sa.sauditourism.employee.modules.activities.datasource.remote.ActivitiesEndpoints
import sa.sauditourism.employee.modules.activities.domain.ActivitiesRepository
import sa.sauditourism.employee.modules.activities.domain.ActivitiesRepositoryImpl
import sa.sauditourism.employee.modules.activities.model.RequestComment

@ExperimentalCoroutinesApi
@RunWith(MockitoJUnitRunner::class)
class ActivitiesRepositoryTest : BaseUnitTest() {

    private val api: ActivitiesEndpoints = mock()
    private val sharedPreferences = mockk<SharedPreferencesManager>()
    private val repository: ActivitiesRepository = ActivitiesRepositoryImpl(api, sharedPreferences)
    private val response = Response.success(MainResponseModel(data = emptyList<RequestComment>(), code = 200, message = "success"))
    private val errorResponse: NetworkError = NetworkError(message = "Network Error")

    @Test
    fun test_successful_get_request_comments() = runTest {
//        every { sharedPreferences.preferredLocale } returns "en"
//        `when`(api.getRequestComments("ID", locale = "en")).thenReturn(response)

        val results = repository.getRequestComments("ID").toList()
        assertTrue(results.first() is UiState.Loading)
        assertTrue(results.last() is UiState.Success)
        assertEquals(response.body(), (results.last() as UiState.Success).data)
    }

    @Test
    fun test_failed_get_request_comments() = runTest {
        every { sharedPreferences.preferredLocale } returns "en"
        `when`(api.getRequestComments("ID", locale = "en")).thenThrow(RuntimeException("Network Error"))

        val results = repository.getRequestComments("ID").toList()
        assertTrue(results.first() is UiState.Loading)
        assertTrue(results.last() is UiState.Error)
    }
}