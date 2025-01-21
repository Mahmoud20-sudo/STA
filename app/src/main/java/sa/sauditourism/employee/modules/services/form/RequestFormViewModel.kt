package sa.sauditourism.employee.modules.services.form

import android.net.Uri
import androidx.compose.runtime.mutableStateMapOf
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.DefaultLifecycleObserver
import androidx.lifecycle.SavedStateHandle
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import sa.sauditourism.employee.base.BaseViewModel
import sa.sauditourism.employee.components.DropDownModel
import sa.sauditourism.employee.constants.FirebaseConstants
import sa.sauditourism.employee.constants.FirebaseConstants.DROPDOWN_MULTI_SELECT
import sa.sauditourism.employee.constants.FirebaseConstants.DROPDOWN_SINGLE_SELECT
import sa.sauditourism.employee.constants.FirebaseConstants.MULTI_DROPDOWNS
import sa.sauditourism.employee.extensions.asyncIO
import sa.sauditourism.employee.extensions.launchIO
import sa.sauditourism.employee.managers.network.helpers.ApiNumberCodes
import sa.sauditourism.employee.managers.network.helpers.NetworkError
import sa.sauditourism.employee.managers.network.models.UiState
import sa.sauditourism.employee.managers.realtime.RealTimeDatabase
import sa.sauditourism.employee.modules.services.domain.RequestFormRepository
import sa.sauditourism.employee.modules.services.model.Filed
import sa.sauditourism.employee.modules.services.model.form.request.FormSubmitRequestModel
import sa.sauditourism.employee.modules.services.model.form.request.SubmitRequestBody
import sa.sauditourism.employee.modules.services.model.form.response.Attachment
import sa.sauditourism.employee.modules.services.model.form.response.FormResponseModel
import sa.sauditourism.employee.modules.services.model.form.response.dropDown.DropDownResponseModel
import sa.sauditourism.employee.modules.services.model.form.response.dropDown.FieldResponseModel
import sa.sauditourism.employee.modules.services.model.form.response.dropDown.FieldUserResponseModel
import sa.sauditourism.employee.modules.services.model.form.response.submit.FormSubmitResponseModel
import timber.log.Timber
import java.io.File
import javax.inject.Inject
import androidx.compose.runtime.State
import androidx.compose.ui.util.fastForEachIndexed
import sa.sauditourism.employee.modules.login.domain.ProfileModel

const val MAX_FILE_SIZE = 25.0

@HiltViewModel
class RequestFormViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    private val requestFormRepository: RequestFormRepository,
    realTimeDatabase: RealTimeDatabase
) : BaseViewModel(realTimeDatabase), DefaultLifecycleObserver {

    private val _requestFormFlow = MutableStateFlow<UiState<FormResponseModel>>(UiState.Loading())
    val requestFormFlow: StateFlow<UiState<FormResponseModel>> = _requestFormFlow.asStateFlow()

    private val _dropdownValuesFlow =
        MutableStateFlow<UiState<DropDownResponseModel>>(UiState.Loading())
    val dropdownValuesFlow: StateFlow<UiState<DropDownResponseModel>> =
        _dropdownValuesFlow.asStateFlow()

    var requestTypeId: String = savedStateHandle.get<String>("id") ?: ""
    var serviceDeskId: String = savedStateHandle.get<String>("service_id") ?: ""

    private var _dropDownValues: MutableSharedFlow<List<DropDownModel>> = MutableStateFlow(listOf())
    val dropDownValues: SharedFlow<List<DropDownModel>> = _dropDownValues.asSharedFlow()

    private var _attachmentsValues: MutableSharedFlow<UiState<List<Attachment>>> =
        MutableStateFlow(UiState.Loading())
    val attachmentsValues: SharedFlow<UiState<List<Attachment>>> = _attachmentsValues.asSharedFlow()

    private val _attachmentsMap =
        mutableStateOf<HashMap<String, MutableList<Attachment>>>(hashMapOf())
    val attachmentsMap: State<HashMap<String, MutableList<Attachment>>> = _attachmentsMap

    private var attachments = mutableListOf<Attachment>()

    val selectedDropDownMap = mutableMapOf<String?, String>()
    private val _formAllFieldsMap = MutableStateFlow(mutableStateMapOf<Filed, Any?>())
    val formAllFieldsMap: StateFlow<Map<Filed, Any?>> = _formAllFieldsMap.asStateFlow()

    init {
        attachments = mutableListOf()
    }

    private val _dropdownSubmitFlow =
        MutableStateFlow<UiState<FormSubmitResponseModel>>(UiState.Loading())
    val dropdownSubmitFlow: StateFlow<UiState<FormSubmitResponseModel>> =
        _dropdownSubmitFlow.asStateFlow()

    fun submitField(field: Filed, value: Any?) {
        _formAllFieldsMap.value[field] = value
    }

    fun getRequestForm() = launchIO {
        requestFormRepository.getRequestForm(requestTypeId).collect collect@{
            when (it) {
                is UiState.Loading -> {// todo show loading
                    _requestFormFlow.emit(UiState.Loading())
                    Timber.d("${ApiNumberCodes.GET_FORM_API_CODE}: UiState.Loading")
                }

                is UiState.Success<*> -> {
                    Timber.d("${ApiNumberCodes.GET_FORM_API_CODE}: UiState.Success")
                    val responseModel = it.data?.data as FormResponseModel

                    responseModel.form?.let { form ->
                        val hasDropDown = form.fields.count { field ->
                            field.component == DROPDOWN_SINGLE_SELECT || field.component == DROPDOWN_MULTI_SELECT || field.component == MULTI_DROPDOWNS
                        } > 0

                        if (!hasDropDown) {
                            _requestFormFlow.emit(UiState.Success(responseModel))
                            return@collect
                        }

                        val onDropDownApiResponse: suspend (Boolean) -> Unit = { success ->
                            if (success)
                                _requestFormFlow.emit(UiState.Success(responseModel))
                            else
                                _requestFormFlow.emit(UiState.Error(NetworkError()))
                        }

                        val job = launchIO launchIo@{
                            form.fields.forEach { field ->
                                submitField(field.apply { initDropDowns() }, null)
                                when (field.component) {
                                    DROPDOWN_SINGLE_SELECT, DROPDOWN_MULTI_SELECT, MULTI_DROPDOWNS -> {
                                        if (!field.isDropDownUserSearch) {
                                            val result = searchDropDownValues(field, "")
                                            result.await()
                                            if (field.dropDownValues.isNullOrEmpty()) {
                                                onDropDownApiResponse.invoke(false)
                                                return@launchIo
                                            }
                                        }
                                    }
                                }
                            }
                            onDropDownApiResponse.invoke(true)
                        }
                        job.join()

                    } ?: _requestFormFlow.emit(UiState.Error(NetworkError()))
                }

                is UiState.Error -> {
                    it.networkError?.let { error ->
                        _requestFormFlow.emit(UiState.Error(error))
                        Timber.d("${ApiNumberCodes.GET_FORM_API_CODE}: ${error.message}")
                    }
                }
            }
        }
    }


    fun searchDropDownValues(
        field: Filed,
        query: String
    ) = asyncIO {
        val dropDownList = arrayListOf<DropDownModel>()

        field.dropDownValues?.clear()

        requestFormRepository.getDropDownValues(
            requestTypeId = requestTypeId,
            fieldId = field.id,
            query = query
        ).collect {
            when (it) {
                is UiState.Loading -> {
                    _dropdownValuesFlow.emit(UiState.Loading())
                    Timber.d("${ApiNumberCodes.GET_FORM_API_CODE}: UiState.Loading")
                }

                is UiState.Success<*> -> {

                    it.data?.data?.let { response ->
                        _dropdownValuesFlow.emit(UiState.Success(response))
                    }

                    if (field.isDropDownUserSearch) {
                        it.data?.data?.fieldUserResponseModel?.onEach { user ->
                            dropDownList.add(FieldUserResponseModel.ModelMapper.from(user))
                        }
                    } else {
                        it.data?.data?.fieldResponseModel?.onEach { dropdown ->
                            dropDownList.add(FieldResponseModel.ModelMapper.from(dropdown))
                        }
                    }

                    dropDownList.onEach { item ->
                        if (field.dropDownValues?.contains(item) == false)
                            field.dropDownValues?.add(item)
                    }

                    Timber.d("${ApiNumberCodes.GET_DROPDOWN_VALUES_API_CODE}: UiState.Success")
                }

                is UiState.Error -> {
                    it.networkError?.let { error ->
                        _dropdownValuesFlow.emit(UiState.Error(error))
                        Timber.d("${ApiNumberCodes.GET_DROPDOWN_VALUES_API_CODE}: ${error.message}")
                    }
                }
            }
        }
    }

    fun uploadAttachment(
        requestTypeId: String,
        file: File,
        fileUri: Uri,
        field: Filed
    ) = launchIO {
        requestFormRepository.uploadAttachment(
            requestTypeId = requestTypeId,
            file = file
        ).collect {
            when (it) {
                is UiState.Loading -> {
                    _attachmentsValues.emit(UiState.Loading())
                    Timber.d("${ApiNumberCodes.ADD_ATTACHMENT_API_CODE}: UiState.Loading")
                }

                is UiState.Success<*> -> {
                    it.data?.data?.let { response ->
                        if (it.data.code != 200 || response.attachments.isEmpty()) {
                            _attachmentsValues.emit(UiState.Error(NetworkError(message = it.data.message)))
                            return@collect
                        }

                        attachments.add(
                            response.attachments.first().apply { attachmentUri = fileUri })
                        submitField(field, attachments)
                        _attachmentsValues.emit(UiState.Success(attachments))
                        addAttachment(field.id, response.attachments.first())
                        Timber.d("${ApiNumberCodes.ADD_ATTACHMENT_API_CODE}: UiState.Success")
                    }
                }

                is UiState.Error -> {
                    it.networkError?.let { error ->
                        _attachmentsValues.emit(UiState.Error(error))
                        Timber.d("${ApiNumberCodes.ADD_ATTACHMENT_API_CODE}: ${error.message}")
                    }
                }
            }
        }
    }

    fun submitForm() = launchIO {
        val mappedValues: MutableMap<String, Any> = mutableMapOf()
        formAllFieldsMap.value.keys.forEach { field ->
            val value = formAllFieldsMap.value[field] ?: return@forEach
            when (field.component) {
                FirebaseConstants.ATTACHMENT -> {
                    val attachmentIds = arrayListOf<String>()
                    attachmentsMap.value[field.id]?.onEach { attachmentIds.add(it.attachmentId) }
                    mappedValues[field.id] = attachmentIds
                }

                FirebaseConstants.DROPDOWN_SINGLE_SELECT -> {
                    val dropDownModel = value as DropDownModel
                    val submitRequestBody: Any = if (!field.isDropDownUserSearch) SubmitRequestBody(
                        id = dropDownModel.stringId!!,
                        value = dropDownModel.label
                    )
                    else ProfileModel(name = dropDownModel.name!!)
                    mappedValues[field.id] = submitRequestBody
                }

                FirebaseConstants.DROPDOWN_MULTI_SELECT -> {
                    val dropDownModel = value as List<*>
                    val selectedValues = arrayListOf<Any?>()

                    dropDownModel.forEach {
                        val submitRequestBody: Any =
                            if (!field.isDropDownUserSearch) SubmitRequestBody(
                                id = (it as DropDownModel).stringId!!,
                                value = it.label
                            )
                            else ProfileModel(name = (it as DropDownModel).name!!)

                        selectedValues.add(submitRequestBody)
                    }
                    mappedValues[field.id] = selectedValues
                }

                FirebaseConstants.MULTI_DROPDOWNS -> {
                    val dropDownModel = value as List<*>
                    val submitRequestBodies = mutableMapOf<String, Any>()
                    dropDownModel.fastForEachIndexed { i, any ->
                        if (i == 0) {
                            submitRequestBodies["id"] = (any as DropDownModel).stringId!!
                            submitRequestBodies["value"] = any.label
                        } else {
                            val submitRequestBody = SubmitRequestBody(
                                id = (any as DropDownModel).stringId!!,
                                value = any.label
                            )
                            submitRequestBodies["child"] = submitRequestBody
                        }
                    }
                    mappedValues[field.id] = submitRequestBodies
                }

                FirebaseConstants.TEXTFIELD_NUMERIC -> {
                    mappedValues[field.id] = value.toString()
                }

                else -> {
                    mappedValues[field.id] = value
                }
            }
        }
        val formSubmitRequestModel: FormSubmitRequestModel =
            FormSubmitRequestModel(
                requestTypeId = requestTypeId,
                serviceDeskId = serviceDeskId,
                requestFieldValues = mappedValues
            )

        requestFormRepository.submitForm(formSubmitRequestModel).collect {
            when (it) {
                is UiState.Loading -> {// todo show loading
                    _dropdownSubmitFlow.emit(UiState.Loading())
                    Timber.d("${ApiNumberCodes.SUBMIT_FORM_API_CODE}: UiState.Loading")
                }

                is UiState.Success<*> -> {
                    Timber.d("${ApiNumberCodes.SUBMIT_FORM_API_CODE}: UiState.Success")
                    it.data?.data?.let { response ->
                        _dropdownSubmitFlow.emit(
                            if (it.data.code == 200) UiState.Success(response) else UiState.Error(
                                NetworkError(
                                    apiNumber = ApiNumberCodes.SUBMIT_FORM_API_CODE,
                                    message = kotlin.runCatching { it.data.data.errors?.get(0)?.errorMessage }
                                        .getOrElse { _ -> it.data.message }
                                )
                            ))
                    }
                }

                is UiState.Error -> {
                    it.networkError?.also { error ->
                        _dropdownSubmitFlow.emit(UiState.Error(error))
                        Timber.d("${ApiNumberCodes.SUBMIT_FORM_API_CODE}: ${error.message}")
                    } ?: run {
                        _dropdownSubmitFlow.emit(UiState.Error(NetworkError()))
                    }
                }
            }
        }
    }

    fun addAttachment(sectionId: String, attachment: Attachment) {
        val attachments = _attachmentsMap.value[sectionId]?.toMutableList() ?: mutableListOf()
        attachments.add(attachment)
        _attachmentsMap.value[sectionId] = attachments
    }

    fun removeAttachment(fieldId: String, attachmentUri: Uri) {
        val currentMap = _attachmentsMap.value
        val attachments = _attachmentsMap.value[fieldId]?.toMutableList()
        if (attachments != null) {
            val attachment = attachments.find {
                it.attachmentUri == attachmentUri
            }
            attachments.remove(attachment)
            val updatedMap = HashMap(currentMap)
            if (attachments.isEmpty()) {
                updatedMap.remove(fieldId)
            } else {
                updatedMap[fieldId] = attachments
            }
            _attachmentsMap.value = updatedMap
        }
    }
}
