package sa.sauditourism.employee.modules.services

import androidx.lifecycle.SavedStateHandle
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
import sa.sauditourism.employee.constants.FirebaseConstants.DROPDOWN_SINGLE_SELECT
import sa.sauditourism.employee.managers.network.helpers.ApiNumberCodes
import sa.sauditourism.employee.managers.network.helpers.NetworkError
import sa.sauditourism.employee.managers.network.models.MainResponseModel
import sa.sauditourism.employee.managers.network.models.UiState
import sa.sauditourism.employee.managers.realtime.RealTimeDatabase
import sa.sauditourism.employee.modules.services.domain.RequestFormRepository
import sa.sauditourism.employee.modules.services.form.RequestFormViewModel
import sa.sauditourism.employee.modules.services.model.Filed
import sa.sauditourism.employee.modules.services.model.FormModel
import sa.sauditourism.employee.modules.services.model.form.request.FormSubmitRequestModel
import sa.sauditourism.employee.modules.services.model.form.response.FormResponseModel
import sa.sauditourism.employee.modules.services.model.form.response.dropDown.DropDownResponseModel
import sa.sauditourism.employee.modules.services.model.form.response.submit.FormSubmitResponseModel
import timber.log.Timber

@ExperimentalCoroutinesApi
@RunWith(org.mockito.junit.MockitoJUnitRunner::class)
class RequestFormViewModelTest : BaseUnitTest() {

    @Mock
    private lateinit var repo: RequestFormRepository

    @Mock
    private lateinit var realTimeDatabase: RealTimeDatabase

    private lateinit var mockedFormResponse: MainResponseModel<FormResponseModel>
    private lateinit var mockedDropDownValuesResponse: MainResponseModel<DropDownResponseModel>
    private lateinit var mockedSubmitFormResponse: MainResponseModel<FormSubmitResponseModel>
    private lateinit var mockedTabResponseError: NetworkError

    @Before
    fun setUp() {
        mockedFormResponse = getFormMockedResponse()
        mockedDropDownValuesResponse = getDropDownValuesMockedResponse()
        mockedTabResponseError = getMockedResponseForError()
        mockedSubmitFormResponse = getFormSubmitMockedResponse()
    }

    @Test
    fun `getForm_when_repo_returns_success`() = runTest {
        Mockito.`when`(
            repo.getRequestForm("123"),
        ).thenReturn(flowOf(UiState.Success(mockedFormResponse)))

        val savedStateHandle = SavedStateHandle().apply {
            set("id", "123")
        }

        val viewModel =
            RequestFormViewModel(savedStateHandle, repo, realTimeDatabase)
        viewModel.requestTypeId = "123"
        viewModel.getRequestForm()

        Thread.sleep(1000)

        val result = viewModel.requestFormFlow.first()

        Timber.d("${result.data}")

        assertTrue(result is UiState.Success)
    }

    @Test
    fun `getForm_when_repo_returns_fails`() = runTest {
        Mockito.`when`(
            repo.getRequestForm("124"),
        ).thenReturn(flowOf(UiState.Error(getMockedResponseForError())))

        val savedStateHandle = SavedStateHandle().apply {
            set("id", "124")
        }

        val viewModel =
            RequestFormViewModel(savedStateHandle, repo, realTimeDatabase)

        viewModel.requestTypeId = "124"
        viewModel.getRequestForm()

        Thread.sleep(1000)

        val result = viewModel.requestFormFlow.first()

        Timber.d("${result.data}")

        assertTrue(
            (result as? UiState.Error)?.networkError != null,
        )
    }

    @Test
    fun `getDropDownValues_when_repo_returns_success`() = runTest {
        Mockito.`when`(
            repo.getDropDownValues("703", "customfield_13525", ""),
        ).thenReturn(flowOf(UiState.Success(mockedDropDownValuesResponse)))

        val savedStateHandle = SavedStateHandle().apply {
            set("id", "703")
        }

        val mockedFiled = Filed(
            component = DROPDOWN_SINGLE_SELECT,
            dropDownCount = 0,
            id = "customfield_13525",
            note = "",
            isDropDownUserSearch = false,
            required = false,
            title = "SUCCESS TEST CASE"
        )

        val viewModel =
            RequestFormViewModel(savedStateHandle, repo, realTimeDatabase)
        viewModel.requestTypeId = "703"

        viewModel.searchDropDownValues(mockedFiled, "")

        Thread.sleep(1000)

        val result = viewModel.dropdownValuesFlow.first()

        Timber.d("${result.data}")

        assertTrue(result is UiState.Success)
    }

    @Test
    fun `getDropDownValues_when_repo_returns_fails`() = runTest {
        Mockito.`when`(
            repo.getDropDownValues("703", "customfield_13525", ""),
        ).thenReturn(flowOf(UiState.Error(mockedTabResponseError)))

        val savedStateHandle = SavedStateHandle().apply {
            set("id", "703")
        }

        val mockedFiled = Filed(
            component = DROPDOWN_SINGLE_SELECT,
            dropDownCount = 0,
            id = "customfield_13525",
            note = "",
            isDropDownUserSearch = false,
            required = false,
            title = "SUCCESS TEST CASE"
        )

        val viewModel =
            RequestFormViewModel(savedStateHandle, repo, realTimeDatabase)
        viewModel.requestTypeId = "703"
        viewModel.searchDropDownValues(mockedFiled, "")

        Thread.sleep(1000)

        val result = viewModel.dropdownValuesFlow.first()

        Timber.d("${result.data}")

        assertTrue(
            (result as? UiState.Error)?.networkError != null,
        )
    }

    @Test
    fun `submitForm_when_repo_returns_success`() = runTest {
        Mockito.`when`(
            repo.submitForm(
                FormSubmitRequestModel(
                    serviceDeskId = "1",
                    requestFieldValues = emptyMap(),
                    requestTypeId = "703"
                )
            ),
        ).thenReturn(flowOf(UiState.Success(mockedSubmitFormResponse)))

        val savedStateHandle = SavedStateHandle().apply {
            set("id", "703")
            set("service_id", "1")
        }

        val viewModel =
            RequestFormViewModel(savedStateHandle, repo, realTimeDatabase)
        viewModel.requestTypeId = "703"
        viewModel.serviceDeskId = "1"

        viewModel.submitForm()

        Thread.sleep(1000)

        val result = viewModel.dropdownSubmitFlow.first()

        Timber.d("${result.data}")

        assertTrue(result is UiState.Success)
    }

    @Test
    fun `submitForm_when_repo_returns_fail`() = runTest {
        Mockito.`when`(
            repo.submitForm(
                FormSubmitRequestModel(
                    serviceDeskId = "1",
                    requestFieldValues = emptyMap(),
                    requestTypeId = "703"
                )
            ),
        ).thenReturn(flowOf(UiState.Error(mockedTabResponseError)))

        val savedStateHandle = SavedStateHandle().apply {
            set("id", "703")
            set("service_id", "1")
        }

        val viewModel =
            RequestFormViewModel(savedStateHandle, repo, realTimeDatabase)
        viewModel.requestTypeId = "703"
        viewModel.serviceDeskId = "1"

        viewModel.submitForm()

        Thread.sleep(1000)

        val result = viewModel.dropdownSubmitFlow.first()

        Timber.d("${result.data}")

        assertTrue(
            (result as? UiState.Error)?.networkError != null,
        )
    }

    private fun getFormSubmitMockedResponse(): MainResponseModel<FormSubmitResponseModel> =
        MainResponseModel(
            data = FormSubmitResponseModel(),
            code = 200,
            message = null
        )

    private fun getFormMockedResponse(): MainResponseModel<FormResponseModel> = MainResponseModel(
        data = FormResponseModel(form = FormModel()),
        code = 200,
        message = null
    )

    private fun getDropDownValuesMockedResponse(): MainResponseModel<DropDownResponseModel> =
        MainResponseModel(
            data = DropDownResponseModel(listOf(), listOf()),
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