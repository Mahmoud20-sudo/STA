package sa.sauditourism.employee.modules.account

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
import sa.sauditourism.employee.base.BaseUnitTest
import sa.sauditourism.employee.managers.network.helpers.ApiNumberCodes
import sa.sauditourism.employee.managers.network.helpers.NetworkError
import sa.sauditourism.employee.managers.network.models.MainResponseModel
import sa.sauditourism.employee.managers.network.models.UiState
import sa.sauditourism.employee.managers.realtime.RealTimeDatabase
import sa.sauditourism.employee.modules.account.domain.PaySlipsRepository
import sa.sauditourism.employee.modules.account.model.PaySlipsResponseModel

@ExperimentalCoroutinesApi
@RunWith(org.mockito.junit.MockitoJUnitRunner::class)
class PaySlipsViewModelTest: BaseUnitTest() {

    @Mock
    private lateinit var repo: PaySlipsRepository

    @Mock
    private lateinit var realTimeDatabase: RealTimeDatabase

    private lateinit var mockedTabResponse: MainResponseModel<PaySlipsResponseModel>
    private lateinit var mockedTabResponseError: NetworkError

    @Before
    fun setUp() {
        mockedTabResponse = getMockedResponse()
        mockedTabResponseError = getMockedResponseForError()
    }

    @Test
    fun `getPaySlips_when_repo_returns_success`() = runTest {
        Mockito.`when`(
            repo.getPaySlips("2024-01-01"),
        ).thenReturn(flowOf(UiState.Success(mockedTabResponse)))

        val viewModel =
            PaySlipsViewModel(repo,  realTimeDatabase)
        viewModel.getPaySlips()

        Thread.sleep(1000)

        val result = viewModel.paySlipsFlow.first()

        assertTrue(result is UiState.Success)
    }

    @Test
    fun `getPaySlips_when_repo_returns_fails`() = runTest {
        Mockito.`when`(
            repo.getPaySlips("2024-01-01"),
        ).thenReturn(flowOf(UiState.Error(getMockedResponseForError())))
        val viewModel =
            PaySlipsViewModel(repo,  realTimeDatabase)
        viewModel.getPaySlips()

        Thread.sleep(1000)

        val result = viewModel.paySlipsFlow.first()

        assertTrue(
            (result as? UiState.Error)?.networkError != null,
        )
    }

    private fun getMockedResponse(): MainResponseModel<PaySlipsResponseModel> = MainResponseModel(
            data = PaySlipsResponseModel(payslips = emptyList()),
            code = 200,
            message = null
        )

    private fun getMockedResponseForError(): NetworkError = NetworkError(code = 401, message = "Test Failed", apiNumber = ApiNumberCodes.PAYSLIPS_API_CODE, errorCode = null, exception = null)

}