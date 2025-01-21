package sa.sauditourism.employee.managers.network

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.emitAll
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map
import sa.sauditourism.employee.managers.network.exceptions.NoInternetException
import sa.sauditourism.employee.managers.network.helpers.ApiResult
import sa.sauditourism.employee.managers.network.helpers.ErrorCodes
import sa.sauditourism.employee.managers.network.helpers.NetworkError
import sa.sauditourism.employee.managers.network.models.UiState

inline fun <ResultType, RequestType> networkBoundResource(
    crossinline query: () -> Flow<ResultType>,
    crossinline fetch: suspend () -> RequestType,
    crossinline saveFetchResult: suspend (RequestType) -> Unit,
    crossinline shouldFetch: (ResultType) -> Boolean = { true },
) = flow {
    val data = query().first()

    val resource = if (shouldFetch(data)) {
        emit(UiState.Loading(data))

        try {
            val resultType = fetch()
            if (resultType is ApiResult<*> && resultType.status == ApiResult.Status.SUCCESS) {
                saveFetchResult(resultType)
                query().map { UiState.Success(it) }
            } else if (resultType is ApiResult<*> && resultType.status == ApiResult.Status.ERROR) {
                query().map {
                    UiState.Error(
                        NetworkError(
                            null,
                            resultType.code,
                            resultType.message,
                            resultType.apiNumber,
                            resultType.error,
                        ),
                        it,
                    )
                }
            } else {
                query().map {
                    UiState.Error(
                        NetworkError(
                            ErrorCodes.UNKNOWN,
                            null,
                            "",
                            null,
                            null,
                        ),
                        it,
                    )
                }
            }
        } catch (exception: Exception) {
            if (exception is NoInternetException) {
                query().map {
                    UiState.Error(
                        NetworkError(
                            ErrorCodes.NO_CONNECTION,
                            null,
                            "",
                            null,
                            exception,
                        ),
                        it,
                    )
                }
            } else {
                query().map {
                    UiState.Error(
                        NetworkError(
                            ErrorCodes.UNKNOWN,
                            null,
                            "",
                            null,
                            exception,
                        ),
                        it,
                    )
                }
            }
        }
    } else {
        query().map {
            UiState.Success(it)
        }
    }
    emitAll(resource)
}
