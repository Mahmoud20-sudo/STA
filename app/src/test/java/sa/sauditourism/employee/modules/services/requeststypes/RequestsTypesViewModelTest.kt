package sa.sauditourism.employee.modules.services.requeststypes

import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.Mockito
import sa.sauditourism.employee.base.BaseUnitTest
import sa.sauditourism.employee.managers.network.helpers.ApiNumberCodes
import sa.sauditourism.employee.managers.network.helpers.NetworkError
import sa.sauditourism.employee.managers.network.models.MainResponseModel
import sa.sauditourism.employee.managers.network.models.UiState
import sa.sauditourism.employee.managers.realtime.RealTimeDatabase
import sa.sauditourism.employee.modules.services.domain.RequestTypeRepository
import sa.sauditourism.employee.modules.services.model.RequestTypeResponse

@ExperimentalCoroutinesApi
@RunWith(org.mockito.junit.MockitoJUnitRunner::class)
class RequestsTypesViewModelTest: BaseUnitTest(){
    @Mock
    private lateinit var repo: RequestTypeRepository

    @Mock
    private lateinit var realTimeDatabase: RealTimeDatabase

    private lateinit var mockedResponse: MainResponseModel<RequestTypeResponse>
    private lateinit var mockedResponseError: NetworkError

    @Before
    fun setUp() {
        mockedResponse = getMockedResponse()
        mockedResponseError = getMockedResponseForError()
    }
    @Test
    fun `test get all request Type when repo return success`() = runTest {
        Mockito.`when`(
            repo.searchRequestTypesById("1",""),
        ).thenReturn(flowOf(UiState.Success(mockedResponse)))

        val viewModel =
            RequestsTypesViewModel(repo,  realTimeDatabase)
        viewModel.searchRequestsTypes("1","")

        Thread.sleep(1000)

        val result = viewModel.requestsTypes.first()

        assertTrue(result is UiState.Success)
    }

    @Test
    fun `test get all request Type when repo return failed`() = runTest {
        Mockito.`when`(
            repo.searchRequestTypesById("1",""),
        ).thenReturn(flowOf(UiState.Error(getMockedResponseForError())))

        val viewModel =
            RequestsTypesViewModel(repo,  realTimeDatabase)
        viewModel.searchRequestsTypes("1","")

        Thread.sleep(1000)

        val result = viewModel.requestsTypes.first()

        assertTrue(
            (result as? UiState.Error)?.networkError != null,
        )
    }

    private fun getMockedResponse(): MainResponseModel<RequestTypeResponse> =
        MainResponseModel(
        data = RequestTypeResponse( requestsTypesList= emptyList()),
        code = 200,
        message = null
    )


    private fun getMockedResponseForError(): NetworkError = NetworkError(code = 401, message = "Test Failed", apiNumber = ApiNumberCodes.REQUESTS_TYPES_API_CODE, errorCode = null, exception = null)

}