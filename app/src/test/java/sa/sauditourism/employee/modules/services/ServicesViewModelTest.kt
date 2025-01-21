package sa.sauditourism.employee.modules.services

import android.util.Log
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import org.junit.Assert.*

import org.junit.After
import org.junit.Assert
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
import sa.sauditourism.employee.modules.services.domain.ServicesRepository
import sa.sauditourism.employee.modules.services.model.ServicesResponseModel

@ExperimentalCoroutinesApi
@RunWith(org.mockito.junit.MockitoJUnitRunner::class)
class ServicesViewModelTest: BaseUnitTest() {

    @Mock
    private lateinit var repo: ServicesRepository

    @Mock
    private lateinit var realTimeDatabase: RealTimeDatabase

    private lateinit var mockedTabResponse: MainResponseModel<ServicesResponseModel>
    private lateinit var mockedTabResponseError: NetworkError

    @Before
    fun setUp() {
        mockedTabResponse = getMockedResponse()
        mockedTabResponseError = getMockedResponseForError()
    }

    @Test
    fun `getServices_when_repo_returns_success`() = runTest {
        Mockito.`when`(
            repo.getServices(),
        ).thenReturn(flowOf(UiState.Success(mockedTabResponse)))

        val viewModel =
            ServicesViewModel(repo,  realTimeDatabase)
        viewModel.getServices()

        Thread.sleep(1000)

        val result = viewModel.servicesFlow.first()

        assertTrue(result is UiState.Success)
    }

    @Test
    fun `getServices_when_repo_returns_fails`() = runTest {
        Mockito.`when`(
            repo.getServices(),
        ).thenReturn(flowOf(UiState.Error(getMockedResponseForError())))
        val viewModel =
            ServicesViewModel(repo,  realTimeDatabase)
        viewModel.getServices()

        Thread.sleep(1000)

        val result = viewModel.servicesFlow.first()

        assertTrue(
            (result as? UiState.Error)?.networkError != null,
        )
    }

    private fun getMockedResponse(): MainResponseModel<ServicesResponseModel> = MainResponseModel(
            data = ServicesResponseModel(servicesLList = emptyList()),
            code = 200,
            message = null
        )

    private fun getMockedResponseForError(): NetworkError = NetworkError(code = 401, message = "Test Failed", apiNumber = ApiNumberCodes.SERVICES_API_CODE, errorCode = null, exception = null)

}