package sa.sauditourism.employee.base

import androidx.compose.runtime.Composable
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import sa.sauditourism.employee.managers.network.helpers.NetworkError
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import sa.sauditourism.employee.EmployeeApplication.Companion.sharedPreferencesManager
import sa.sauditourism.employee.extensions.launchIO
import sa.sauditourism.employee.managers.errorhandeling.ErrorHandlingManager
import sa.sauditourism.employee.managers.realtime.RealTimeDatabase
import sa.sauditourism.employee.modules.services.form.MAX_FILE_SIZE
import java.io.File
import javax.inject.Inject

/**
 * BaseViewModel class, everything common should go here
 */
abstract class BaseViewModel(val realTimeDatabase: RealTimeDatabase) : ViewModel() {

    var isRefreshing = false
    var isScrollToTop = false

    private val hasSwipedToRefreshMutable = MutableStateFlow(false)
    val hasSwipedToRefresh = hasSwipedToRefreshMutable.asStateFlow()

    private val _attachmentMaxSizeError = MutableStateFlow<Boolean?>(null)
    val attachmentMaxSizeError: StateFlow<Boolean?> = _attachmentMaxSizeError.asStateFlow()
    private var availableSize = 25.0

    init {
        availableSize = MAX_FILE_SIZE
    }

    @Inject
    lateinit var errorHandlingManager: ErrorHandlingManager

    @Composable
    fun HandleError(
        error: NetworkError?,
        isDataEmpty: Boolean,
        showBack: Boolean = true,
        withTab: Boolean = false,
        dismissCallback: () -> Unit,
        callback: () -> Unit,
    ) {
        errorHandlingManager.HandleError(
            error,
            isDataEmpty,
            showBack,
            withTab,
            dismissCallback,
            callback,
        )
    }

    fun checkFileSize(
        file: File,
        onUploadStarting:(File) -> Unit = {}
    ) = launchIO {
        val fileSizeInBytes = file.length()
        val fileSizeInKB = (fileSizeInBytes / 1024).toDouble()
        val fileSizeInMB = (fileSizeInKB / 1024)
        if (fileSizeInMB >= 25L) {
            _attachmentMaxSizeError.emit(true)
        } else {
            if (fileSizeInMB < availableSize) {
                availableSize -= fileSizeInMB
                _attachmentMaxSizeError.emit(false)
                onUploadStarting.invoke(file)
            } else
                _attachmentMaxSizeError.emit(true)
        }
    }

    open fun doLogout() = viewModelScope.launch {
        sharedPreferencesManager.accessToken = null
        sharedPreferencesManager.refreshToken = null
        sharedPreferencesManager.authCode = null
        sharedPreferencesManager.authCode = null
        sharedPreferencesManager.authState = null
    }

    open fun clearState() = launchIO {
        _attachmentMaxSizeError.emit(false)
    }

    open fun onSwipeToRefresh(){}

    open fun onLoadMore(){}
}
