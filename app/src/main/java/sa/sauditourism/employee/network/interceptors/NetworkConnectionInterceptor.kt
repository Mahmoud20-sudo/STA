package sa.sauditourism.employee.network.interceptors

import android.content.Context
import sa.sauditourism.employee.extensions.isNetworkAvailable
import sa.sauditourism.employee.managers.network.exceptions.NoInternetException
import okhttp3.Interceptor
import okhttp3.Request
import okhttp3.Response

class NetworkConnectionInterceptor constructor(val context: Context) : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        if (!context.isNetworkAvailable()) {
            throw NoInternetException()
            // Throwing custom exception 'NoInternetException'
        }

        val builder: Request.Builder = chain.request().newBuilder()
        return chain.proceed(builder.build())
    }
}
