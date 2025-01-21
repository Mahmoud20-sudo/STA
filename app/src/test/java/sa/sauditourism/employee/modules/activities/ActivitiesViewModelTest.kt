package sa.sauditourism.employee.modules.activities

import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertTrue
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mockito
import org.mockito.junit.MockitoJUnitRunner
import org.mockito.kotlin.mock
import sa.sauditourism.employee.base.BaseUnitTest
import sa.sauditourism.employee.managers.network.helpers.NetworkError
import sa.sauditourism.employee.managers.network.models.MainResponseModel
import sa.sauditourism.employee.managers.network.models.UiState
import sa.sauditourism.employee.managers.realtime.RealTimeDatabase
import sa.sauditourism.employee.modules.activities.domain.ActivitiesRepository
import sa.sauditourism.employee.modules.activities.model.RequestComment

@ExperimentalCoroutinesApi
@RunWith(MockitoJUnitRunner::class)
class ActivitiesViewModelTest : BaseUnitTest() {

    private val repository: ActivitiesRepository = mock()
    private val realTimeDatabase: RealTimeDatabase = mock()
    private val viewModel = ActivitiesViewModel(repository, realTimeDatabase)
    private val response: MainResponseModel<List<RequestComment>> = MainResponseModel(data = listOf(), code = 200, message = "")
    private val errorResponse: NetworkError = NetworkError(errorCode = "404")

    @Test
    fun test_successful_get_request_comments() = runTest {
        //Mockito.`when`(repository.getRequestComments("ID")).thenReturn(flowOf(UiState.Success(response)))
        viewModel.getComments("ID", emptyList())
        Thread.sleep(1000)
        val result = viewModel.commentsFlow.first()
        assertTrue(result is UiState.Success)
    }

    @Test
    fun test_failed_get_request_comments() = runTest {
        Mockito.`when`(repository.getRequestComments("ID")).thenReturn(flowOf(UiState.Error(errorResponse)))
        viewModel.getComments("ID", emptyList())
        Thread.sleep(1000)
        val result = viewModel.commentsFlow.first()
        assertTrue(result is UiState.Error)
    }
}
