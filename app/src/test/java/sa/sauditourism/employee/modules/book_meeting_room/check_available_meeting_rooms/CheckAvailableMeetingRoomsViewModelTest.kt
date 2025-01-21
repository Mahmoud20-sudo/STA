package sa.sauditourism.employee.modules.book_meeting_room.check_available_meeting_rooms

import androidx.lifecycle.SavedStateHandle
import io.mockk.*
import junit.framework.TestCase.assertEquals
import junit.framework.TestCase.assertTrue
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.*
import org.junit.After
import org.junit.Before
import org.junit.Test
import sa.sauditourism.employee.managers.network.helpers.ApiNumberCodes
import sa.sauditourism.employee.managers.network.helpers.NetworkError
import sa.sauditourism.employee.managers.network.models.MainResponseModel
import sa.sauditourism.employee.managers.network.models.UiState
import sa.sauditourism.employee.managers.realtime.RealTimeDatabase

import sa.sauditourism.employee.modules.home.bookMeetingRoom.domain.AvailableMeetingRoomsRepository
import sa.sauditourism.employee.modules.home.bookMeetingRoom.model.available_meeting_rooms.AvailableMeetingRoomsResponseModel
import sa.sauditourism.employee.modules.home.bookMeetingRoom.model.available_meeting_rooms.MeetingRoom
import sa.sauditourism.employee.modules.home.bookMeetingRoom.model.available_meeting_rooms.Room
import sa.sauditourism.employee.modules.home.bookMeetingRoom.check_available_meeting_rooms.CheckAvailableMeetingRoomsViewModel
import sa.sauditourism.employee.modules.services.model.ServicesResponseModel
import timber.log.Timber


@OptIn(ExperimentalCoroutinesApi::class)
class CheckAvailableMeetingRoomsViewModelTest {

    private lateinit var viewModel: CheckAvailableMeetingRoomsViewModel
    private lateinit var savedStateHandle: SavedStateHandle
    private lateinit var repository: AvailableMeetingRoomsRepository
    private lateinit var realTimeDatabase: RealTimeDatabase

    private val testDispatcher = UnconfinedTestDispatcher()

    private lateinit var mockedResponse: MainResponseModel<AvailableMeetingRoomsResponseModel>
    private lateinit var mockedResponseError: NetworkError

    @Before
    fun setUp() {
        Dispatchers.setMain(testDispatcher)
        mockedResponse = getMockedResponse()
        mockedResponseError = getMockedResponseForError()

        savedStateHandle = SavedStateHandle(
            mapOf(
                "date" to "2025-01-01",
                "start_time" to "10:00",
                "end_time" to "11:00"
            )
        )
        repository = mockk()
        realTimeDatabase = mockk()


        // Mock Timber to avoid logging errors during tests
        mockkObject(Timber)
        every { Timber.d(any<String>()) } just Runs

        viewModel = CheckAvailableMeetingRoomsViewModel(
            savedStateHandle,
            repository,
            realTimeDatabase
        )
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
        unmockkAll()
    }

    @Test
    fun `test getAvailableMeetingRoom emits success`() = runTest {


        coEvery {
            repository.getAvailableMeetingRooms(
              any(),any(),any()
            )
        } returns flowOf(UiState.Success(mockedResponse))

        // When
        viewModel.getAvailableMeetingRoom()
        Thread.sleep(1000)


        val state = viewModel.meetingRoomsFlow.first()
        assertTrue(state is UiState.Success)


    }

    @Test
    fun `test getAvailableMeetingRoom emits error`() = runTest {
        val mockError = NetworkError(
            code = 401,
            message = "Test Failed",
            apiNumber = ApiNumberCodes.PAYSLIPS_API_CODE,
            errorCode = null
        )

        coEvery {
            repository.getAvailableMeetingRooms(any(), any(), any())
        } returns flowOf(UiState.Error(mockError))

        viewModel.getAvailableMeetingRoom()
        Thread.sleep(1000)

        val state = viewModel.meetingRoomsFlow.first()
        assertTrue(state is UiState.Error)
        assertEquals(mockError, (state as UiState.Error).networkError)
    }

    @Test
    fun `test filterMeetings updates flow`() = runTest {
        val mockResponse = AvailableMeetingRoomsResponseModel(
            meetingRooms = listOf(
                MeetingRoom(
                    rooms = listOf(
                        Room(title = "Room A", email = "roomA@example.com"),
                        Room(title = "Room B", email = "roomB@example.com")
                    )
                )
            )
        )

        viewModel.originalMeetingRooms = mockResponse
        viewModel.clearFilter()
        viewModel.filterMeetings("Room A")

        val state = viewModel.meetingRoomsFlow.first()
        assertTrue(state is UiState.Success)

        val filteredRooms = (state as UiState.Success).data?.meetingRooms?.flatMap { it.rooms }
        assertEquals(1, filteredRooms?.size)
        assertEquals("Room A", filteredRooms?.first()?.title)
    }

    @Test
    fun `test clearFilter resets to original data`() = runTest {
        val mockResponse = AvailableMeetingRoomsResponseModel(
            meetingRooms = listOf(
                MeetingRoom(
                    rooms = listOf(Room(title = "Room A", email = "roomA@example.com"))
                )
            )
        )

        viewModel.originalMeetingRooms = mockResponse
        viewModel.clearFilter()

        val state = viewModel.meetingRoomsFlow.value
        assertTrue(state is UiState.Success)
        assertEquals(mockResponse, (state as UiState.Success).data)
    }

    private fun getMockedResponse(): MainResponseModel<AvailableMeetingRoomsResponseModel> =
        MainResponseModel(
            data = AvailableMeetingRoomsResponseModel(
                meetingRooms = listOf(MeetingRoom(rooms = listOf()))
            ),
            code = 200,
            message = null
        )

    private fun getMockedResponseForError(): NetworkError = NetworkError(
        code = 401,
        message = "Test Failed",
        apiNumber = ApiNumberCodes.SERVICES_API_CODE,
        errorCode = null,
        exception = null
    )


}
