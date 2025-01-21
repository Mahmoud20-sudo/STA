package sa.sauditourism.employee.modules.services.myRequests

import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import org.junit.Assert.*
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.Mockito
import sa.sauditourism.employee.managers.network.helpers.ApiNumberCodes
import sa.sauditourism.employee.managers.network.helpers.NetworkError
import sa.sauditourism.employee.managers.network.models.MainResponseModel
import sa.sauditourism.employee.managers.network.models.UiState
import sa.sauditourism.employee.managers.realtime.RealTimeDatabase
import sa.sauditourism.employee.modules.account.domain.FiltersRepository
import sa.sauditourism.employee.modules.account.myRequests.MyRequestsViewModel
import sa.sauditourism.employee.modules.services.domain.RequestTypeRepository
import sa.sauditourism.employee.modules.account.myRequests.model.MyRequest
import sa.sauditourism.employee.modules.account.myRequests.model.MyRequestsResponse

@ExperimentalCoroutinesApi
@RunWith(org.mockito.junit.MockitoJUnitRunner::class)
class MyRequestsViewModelTest{

    @Mock
    private lateinit var repo: RequestTypeRepository
    @Mock
    private lateinit var filterRepo: FiltersRepository

    @Mock
    private lateinit var realTimeDatabase: RealTimeDatabase

    private lateinit var mockedResponse: MainResponseModel<MyRequestsResponse>
    private lateinit var mockedResponseError: NetworkError



    @Before
    fun setUp() {
        mockedResponse = getMockedResponse()
        mockedResponseError = getMockedResponseForError()

    }

    @Test
    fun `test get all My requests  when repo return success`() = runTest {
        Mockito.`when`(
            repo.getMyRequests("431", "OWNED_BY_ME"),
        ).thenReturn(flowOf(UiState.Success(mockedResponse)))
       val  viewModel = MyRequestsViewModel(repo,filterRepo, realTimeDatabase)
        viewModel.getMyRequest()

        Thread.sleep(1000)

        val result = viewModel.myRequestsFlow.first()

        assertTrue(result is UiState.Success)
    }

    @Test
    fun `test get  My requests  when repo return failed`() = runTest {
        Mockito.`when`(
            repo.getMyRequests("431", "OWNED_BY_ME"),
        ).thenReturn(flowOf(UiState.Error(getMockedResponseForError())))

        val  viewModel = MyRequestsViewModel(repo, filterRepo, realTimeDatabase)
        viewModel.getMyRequest()

        Thread.sleep(1000)

        val result = viewModel.myRequestsFlow.first()

        assertTrue(
            (result as? UiState.Error)?.networkError != null,
        )
    }



}



private fun getMockedResponse(): MainResponseModel<MyRequestsResponse> =
        MainResponseModel(
            data = MyRequestsResponse(requestsList = listOf(MyRequest("1","Title1","http://icon.com","closed","10:140","20/10/2012"))),
            code = 200,
            message = "success"
    )


private fun getMockedResponseForError(): NetworkError = NetworkError(code = 401, message = "Test Failed", apiNumber = ApiNumberCodes.REQUESTS_TYPES_API_CODE, errorCode = null, exception = null)
