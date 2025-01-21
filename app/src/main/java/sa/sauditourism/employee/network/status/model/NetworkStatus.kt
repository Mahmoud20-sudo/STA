package sa.sauditourism.employee.network.status.model

sealed class NetworkStatus {
    data object Unknown: NetworkStatus()
    data object Connected: NetworkStatus()
    data object Disconnected: NetworkStatus()
}