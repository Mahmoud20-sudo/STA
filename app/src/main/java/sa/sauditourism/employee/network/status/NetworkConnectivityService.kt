package sa.sauditourism.employee.network.status

import kotlinx.coroutines.flow.Flow
import sa.sauditourism.employee.network.status.model.NetworkStatus

interface NetworkConnectivityService {
    val networkStatus: Flow<NetworkStatus>
}