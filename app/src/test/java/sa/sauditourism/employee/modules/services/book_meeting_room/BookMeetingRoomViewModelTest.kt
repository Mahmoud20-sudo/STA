package sa.sauditourism.employee.modules.services.book_meeting_room

import io.mockk.mockk
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test
import sa.sauditourism.employee.managers.realtime.RealTimeDatabase
import sa.sauditourism.employee.modules.home.bookMeetingRoom.BookMeetingRoomViewModel

@OptIn(ExperimentalCoroutinesApi::class)
class BookMeetingRoomViewModelTest {

    private lateinit var viewModel: BookMeetingRoomViewModel
    private val realTimeDatabase = mockk<RealTimeDatabase>()

    @Before
    fun setup() {
        viewModel = BookMeetingRoomViewModel(realTimeDatabase)
    }

    @Test
    fun `updateStartTime updates startTime and enables button`() = runTest {
        // Act
        viewModel.updateStartTime("10:00:00.000+0000")

        // Assert
        assertEquals("10:00:00.000+0000", viewModel.startTime.value)
        assertEquals(false, viewModel.isEnabled.value) // Initially not enabled
    }

    @Test
    fun `updateEndTime updates endTime and enables button`() = runTest {
        // Act
        viewModel.updateEndTime("12:00:00.000+0000")

        // Assert
        assertEquals("12:00:00.000+0000", viewModel.endTime.value)
        assertEquals(false, viewModel.isEnabled.value) // Initially not enabled
    }

    @Test
    fun `updateSelectedDate updates selectedDate and enables button`() = runTest {
        // Act
        viewModel.updateSelectedDate("2024-01-01")

        // Assert
        assertEquals("2024-01-01", viewModel.selectedDate.value)
        assertEquals(false, viewModel.isEnabled.value) // Initially not enabled
    }

    @Test
    fun `checkTimeValidity returns false for valid times`() = runTest {
        // Arrange
        viewModel.updateSelectedDate("2024-01-01")
        viewModel.updateStartTime("10:00:00.000+0000")
        viewModel.updateEndTime("11:00:00.000+0000")

        // Act
        val isInvalid = viewModel.checkTimeValidity()

        // Assert
        assertEquals(false, isInvalid)
        assertEquals(false, viewModel.showError.value)
    }

    @Test
    fun `checkTimeValidity returns true for invalid times`() = runTest {
        // Arrange
        viewModel.updateSelectedDate("2024-01-01")
        viewModel.updateStartTime("12:00:00.000+0000")
        viewModel.updateEndTime("10:00:00.000+0000")

        // Act
        val isInvalid = viewModel.checkTimeValidity()

        // Assert
        assertEquals(true, isInvalid)
        assertEquals(true, viewModel.showError.value)
    }

    @Test
    fun `checkTimeValidity shows error when any input is missing`() = runTest {
        // Arrange
        viewModel.updateStartTime("")
        viewModel.updateEndTime("10:00:00.000+0000")
        viewModel.updateSelectedDate("2024-01-01")

        // Act
        val isInvalid = viewModel.checkTimeValidity()

        // Assert
        assertEquals(true, isInvalid)
        assertEquals(true, viewModel.showError.value)
    }

    @Test
    fun `hideError sets showError to false`() = runTest {
        // Arrange
        viewModel.showError.value = true

        // Act
        viewModel.hideError()

        // Assert
        assertEquals(false, viewModel.showError.value)
    }
}
