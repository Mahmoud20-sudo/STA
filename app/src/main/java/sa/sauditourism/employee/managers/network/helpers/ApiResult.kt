package sa.sauditourism.employee.managers.network.helpers

import retrofit2.Response
import java.net.HttpURLConnection

/**
 * Generic class for holding success response, error response and loading status
 */
data class ApiResult<out T>(
    val status: Status,
    val code: Int? = null,
    val data: T?,
    val error: Throwable?,
    val message: String?,
    val apiNumber: Int? = null,
) {

    enum class Status {
        SUCCESS,
        ERROR,
        LOADING,
    }

    companion object {
        /**
         * Method to be used in repositories while fetching data from BE
         */
        fun <T> create(response: Response<T>, apiNumberCode: Int?): ApiResult<T> {
            return if (response.isSuccessful) {
                val body = response.body()
                if (body == null || response.code() == HttpURLConnection.HTTP_NO_CONTENT) {
                    empty()
                } else {
                    success(
                        data = body,
                        response.code(),
                    )
                }
            } else {
                response.errorBody()?.let {
                    try {
                        // TODO parse response from server if present in error body: Error.class is the error model
//                        val errorObj = Gson().fromJson<Error<T>>(
//                            it.charStream(),
//                            Error::class.java,
//                        )
//                        errorObj.statusCode = response.code()
//                        return errorObj
                        return error(
                            message = response.message(),
                            code = response.code(),
                            error = null,
                            apiNumber = apiNumberCode,
                        )
                    } catch (e: Exception) {

                        // No Need to handle as we are already returning Error with response message below
                    }
                }

                error(
                    message = response.message(),
                    code = response.code(),
                    error = null,
                    apiNumber = apiNumberCode,
                )
            }
        }

        fun <T> success(data: T?, code: Int? = null): ApiResult<T> {
            return ApiResult(Status.SUCCESS, code, data, null, null)
        }

        fun empty(code: Int? = null): ApiResult<Nothing> {
            return ApiResult(Status.SUCCESS, code, null, null, null)
        }

        fun <T> error(
            message: String,
            code: Int? = null,
            apiNumber: Int? = null,
            error: Throwable?,
        ): ApiResult<T> {
            return ApiResult(Status.ERROR, code, null, error, message, apiNumber)
        }

        fun <T> loading(data: T? = null): ApiResult<T> {
            return ApiResult(Status.LOADING, null, data, null, null)
        }
    }

    override fun toString(): String {
        return "Result(status=$status, data=$data, error=$error, message=$message)"
    }
}
