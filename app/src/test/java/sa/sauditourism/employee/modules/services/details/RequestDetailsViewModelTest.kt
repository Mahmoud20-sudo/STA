package sa.sauditourism.employee.modules.services.details

import androidx.lifecycle.SavedStateHandle
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
import sa.sauditourism.employee.modules.services.domain.ParticipantsRepository
import sa.sauditourism.employee.modules.services.domain.RequestDetailsRepository
import sa.sauditourism.employee.modules.services.domain.RequestTypeRepository
import sa.sauditourism.employee.modules.services.model.RequestTypeResponse
import sa.sauditourism.employee.modules.services.model.details.Request
import sa.sauditourism.employee.modules.services.model.details.RequestDetailsModel
import sa.sauditourism.employee.modules.services.model.participants.ParticipantsResponseModel

@ExperimentalCoroutinesApi
@RunWith(org.mockito.junit.MockitoJUnitRunner::class)
class RequestDetailsViewModelTest : BaseUnitTest() {
    @Mock
    private lateinit var repo: RequestDetailsRepository

    @Mock
    private lateinit var getParticipantsRepository: ParticipantsRepository

    @Mock
    private lateinit var realTimeDatabase: RealTimeDatabase

    private lateinit var mockedResponse: MainResponseModel<RequestDetailsModel>
    private lateinit var mockedResponseError: NetworkError

    private lateinit var mockedParticipantsResponse: MainResponseModel<ParticipantsResponseModel>

    @Before
    fun setUp() {
        mockedResponse = getMockedResponse()
        mockedParticipantsResponse = getParticipantsMockedResponse()
        mockedResponseError = getMockedResponseForError()
    }

    @Test
    fun `test get request details when repo return success`() = runTest {
        Mockito.`when`(
            repo.getRequestDetails("121371"),
        ).thenReturn(flowOf(UiState.Success(mockedResponse)))

        val savedStateHandle = SavedStateHandle().apply {
            set("id", "121371")
        }

        val viewModel = RequestDetailsViewModel(
            savedStateHandle,
            repo,
            getParticipantsRepository,
            realTimeDatabase
        )
        viewModel.requestTypeId = savedStateHandle.get<String>("id").toString()

        viewModel.getRequestDetails()

        Thread.sleep(1000)

        val result = viewModel.detailsResultFlow.first()

        assertTrue(result is UiState.Success)
    }

    @Test
    fun `test request details when repo return failed`() = runTest {
        Mockito.`when`(
            repo.getRequestDetails("121371"),
        ).thenReturn(flowOf(UiState.Error(getMockedResponseForError())))

        val savedStateHandle = SavedStateHandle().apply {
            set("id", "121371")
        }

        val viewModel = RequestDetailsViewModel(
            savedStateHandle,
            repo,
            getParticipantsRepository,
            realTimeDatabase
        )
        viewModel.requestTypeId = savedStateHandle.get<String>("id").toString()

        viewModel.getRequestDetails()

        Thread.sleep(1000)

        val result = viewModel.detailsResultFlow.first()

        assertTrue(
            (result as? UiState.Error)?.networkError != null,
        )
    }

    @Test
    fun `test get participants  when repo return success`() = runTest {
        Mockito.`when`(
            getParticipantsRepository.getParticipant("121371"),
        ).thenReturn(flowOf(UiState.Success(mockedParticipantsResponse)))

        val savedStateHandle = SavedStateHandle().apply {
            set("id", "121371")
        }

        val viewModel = RequestDetailsViewModel(
            savedStateHandle,
            repo,
            getParticipantsRepository,
            realTimeDatabase
        )
        viewModel.requestTypeId = savedStateHandle.get<String>("id").toString()

        viewModel.getParticipates()

        Thread.sleep(1000)

        val result = viewModel.participantsFlow.first()

        assertTrue(result is UiState.Success)
    }

    @Test
    fun `test get participants when repo return failed`() = runTest {
        Mockito.`when`(
            getParticipantsRepository.getParticipant("121371"),
        ).thenReturn(flowOf(UiState.Error(getMockedResponseForError())))

        val savedStateHandle = SavedStateHandle().apply {
            set("id", "121371")
        }

        val viewModel = RequestDetailsViewModel(
            savedStateHandle,
            repo,
            getParticipantsRepository,
            realTimeDatabase
        )
        viewModel.requestTypeId = savedStateHandle.get<String>("id").toString()

        viewModel.getParticipates()

        Thread.sleep(1000)

        val result = viewModel.participantsFlow.first()

        assertTrue(
            (result as? UiState.Error)?.networkError != null,
        )
    }

    private fun getMockedResponse(): MainResponseModel<RequestDetailsModel> =
        MainResponseModel(
            data = RequestDetailsModel(request = Request()),
            code = 200,
            message = null
        )

    private fun getParticipantsMockedResponse(): MainResponseModel<ParticipantsResponseModel> =
        MainResponseModel(
            data = ParticipantsResponseModel(participants = listOf()),
            code = 200,
            message = null
        )


    private fun getMockedResponseForError(): NetworkError = NetworkError(
        code = 401,
        message = "Test Failed",
        apiNumber = ApiNumberCodes.REQUESTS_TYPES_API_CODE,
        errorCode = null,
        exception = null
    )

}