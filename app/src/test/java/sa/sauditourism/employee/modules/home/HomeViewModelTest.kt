package sa.sauditourism.employee.modules.home

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
import sa.sauditourism.employee.modules.home.domain.HomeRepository
import sa.sauditourism.employee.modules.home.myDayMeetings.domain.MyDayMeetingsRepository
import sa.sauditourism.employee.modules.home.myDayMeetings.model.MyDayMeetingsResponseModel

@ExperimentalCoroutinesApi
@RunWith(MockitoJUnitRunner::class)
class HomeViewModelTest : BaseUnitTest() {

    private val homeRepository: HomeRepository = mock()
    private val repository: MyDayMeetingsRepository = mock()
    private val realTimeDatabase: RealTimeDatabase = mock()
    private val viewModel = HomeViewModel(realTimeDatabase, repository, homeRepository)
    private val response: MainResponseModel<MyDayMeetingsResponseModel> =
        MainResponseModel(data = MyDayMeetingsResponseModel(listOf()), code = 200, message = "")
    private val errorResponse: NetworkError = NetworkError(errorCode = "404")

    @Test
    fun test_successful_get_my_day_meetings() = runTest {
        Mockito.`when`(repository.getMyDayMeetings()).thenReturn(flowOf(UiState.Success(response)))
        viewModel.getMyDayMeetings()
        Thread.sleep(1000)
        val result = viewModel.meetingsData.first()
        assertTrue(result is UiState.Success)
    }

    @Test
    fun test_failed_get_my_day_meetings() = runTest {
        Mockito.`when`(repository.getMyDayMeetings())
            .thenReturn(flowOf(UiState.Error(errorResponse)))
        viewModel.getMyDayMeetings()
        Thread.sleep(1000)
        val result = viewModel.meetingsData.first()
        assertTrue(result is UiState.Error)
    }
}