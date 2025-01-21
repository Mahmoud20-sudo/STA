package sa.sauditourism.employee.managers.network

import android.content.Context
import android.net.ConnectivityManager
import android.net.Network
import android.net.NetworkCapabilities
import android.net.NetworkRequest
import androidx.lifecycle.LiveData

/**
 * Class responsible to check for network availability and send it as livedata
 */
class NetworkStatusLiveData(context: Context) : LiveData<Boolean>() {

    private val connectivityManager =
        context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager

    private var availableNetworks: MutableSet<Network> = mutableSetOf()

    private var isNetworkCallbackRegistered = false

    /**
     * Listen to network availability
     */
    private val networkCallback = object : ConnectivityManager.NetworkCallback() {
        override fun onAvailable(network: Network) {
            availableNetworks.add(network)
            checkAndPost()
        }

        override fun onLost(network: Network) {
            availableNetworks.remove(network)
            checkAndPost()
        }
    }

    /**
     * Post network availability boolean
     */
    private fun checkAndPost() {
        val isConnected = availableNetworks.isNotEmpty()
        if (previousState != null && previousState != isConnected) {
            postValue(isConnected)
        }
        previousState = isConnected
    }

    private var previousState: Boolean? = null

    override fun onActive() {
        super.onActive()
        try {
            if (isNetworkCallbackRegistered && availableNetworks.size > 0) {
                connectivityManager.unregisterNetworkCallback(networkCallback)
                isNetworkCallbackRegistered = false
            }
        } catch (ignored: Exception) {
        }
        isNetworkCallbackRegistered = try {
            availableNetworks.clear()
            val networkRequest = NetworkRequest.Builder()
                .addCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET)
                .build()
            connectivityManager.registerNetworkCallback(networkRequest, networkCallback)
            true
        } catch (ex: Exception) {
            //network is already registered
            false
        }
        try {
            // Get the initial state
            val activeNetwork = connectivityManager.activeNetwork
            val capabilities = connectivityManager.getNetworkCapabilities(activeNetwork)
            val isConnected =
                capabilities != null && capabilities.hasCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET)
            previousState = isConnected
        } catch (ignored: Exception) {
            //network is already registered
        }
    }

    override fun onInactive() {
        super.onInactive()
        try {
            if (isNetworkCallbackRegistered && availableNetworks.size > 0) {
                connectivityManager.unregisterNetworkCallback(networkCallback)
            }
        } catch (ignored: Exception) {
            //network is already unregistered
        } finally {
            isNetworkCallbackRegistered = false
        }
    }
}
