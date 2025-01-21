package sa.sauditourism.employee.managers.network.models

import sa.sauditourism.employee.managers.network.helpers.NetworkError


/**
 * Generic UI State
 */
sealed class UiState<T>(
    val data: T? = null,
    val networkError: NetworkError? = null,
) {
    class Success<T>(data: T) : UiState<T>(data)
    class Loading<T>(data: T? = null) : UiState<T>(data)
    class Error<T>(networkError: NetworkError, data: T? = null) : UiState<T>(data, networkError)
}
