package sa.sauditourism.employee.managers.network.helpers

/**
 * Network error data class to be sent via UiState to ViewModel
 */
data class NetworkError(
    val errorCode: String? = null,
    val code: Int? = null,
    val message: String? = null,
    val apiNumber: Int? = null,
    val exception: Throwable? = null,
) {
    fun getDisplayCode(): String = (" - $apiNumber$code")
}
