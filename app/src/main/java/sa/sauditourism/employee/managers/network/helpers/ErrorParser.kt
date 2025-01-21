package sa.sauditourism.employee.managers.network.helpers

import retrofit2.Response
import retrofit2.Retrofit
import java.io.IOException

/**
 * parses error response body
 */
object ErrorParser {

    fun parseError(response: Response<*>, retrofit: Retrofit): Error? {
        val converter = retrofit.responseBodyConverter<Error>(Error::class.java, arrayOfNulls(0))
        return try {
            converter.convert(response.errorBody()!!)
        } catch (e: IOException) {
            Error()
        }
    }
}