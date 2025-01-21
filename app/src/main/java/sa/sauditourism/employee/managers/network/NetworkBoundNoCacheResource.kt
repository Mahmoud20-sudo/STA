package sa.sauditourism.employee.managers.network

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import sa.sauditourism.employee.managers.network.exceptions.NoInternetException
import sa.sauditourism.employee.managers.network.helpers.ApiResult
import sa.sauditourism.employee.managers.network.helpers.ErrorCodes
import sa.sauditourism.employee.managers.network.helpers.NetworkError
import sa.sauditourism.employee.managers.network.models.UiState

inline fun <ResultType> networkBoundNoCacheResource(
    crossinline fetch: suspend () -> ApiResult<ResultType>,
): Flow<UiState<ResultType>> = flow {
    emit(UiState.Loading(null))

    try {
        val resultType = fetch()
        if (resultType is ApiResult<*> && resultType.status == ApiResult.Status.SUCCESS) {
            val data = resultType.data as ResultType
            emit(UiState.Success(data))
        } else if (resultType is ApiResult<*> && resultType.status == ApiResult.Status.ERROR) {
            emit(
                UiState.Error(
                    NetworkError(
                        null,
                        resultType.code,
                        resultType.message,
                        resultType.apiNumber,
                        resultType.error,
                    ),
                    null,
                ),
            )
        } else {
            emit(
                UiState.Error(
                    NetworkError(
                        ErrorCodes.UNKNOWN,
                        null,
                        "",
                        null,
                        null,
                    ),
                    null,
                ),
            )
        }
    } catch (exception: Exception) {
        exception.printStackTrace()
        if (exception is NoInternetException) {
            emit(
                UiState.Error(
                    NetworkError(
                        ErrorCodes.NO_CONNECTION,
                        null,
                        "",
                        null,
                        exception,
                    ),
                    null,
                ),
            )
        } else {
            emit(
                UiState.Error(
                    NetworkError(
                        ErrorCodes.UNKNOWN,
                        null,
                        "",
                        null,
                        exception,
                    ),
                    null,
                ),
            )
        }
    }
}
