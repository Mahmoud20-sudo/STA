package sa.sauditourism.employee.modules.home.domain

import kotlinx.coroutines.flow.Flow
import sa.sauditourism.employee.managers.network.models.MainResponseModel
import sa.sauditourism.employee.managers.network.models.UiState
import sa.sauditourism.employee.modules.home.model.AnnouncementResponse
import sa.sauditourism.employee.modules.home.model.QuickActionsResponseModel

interface HomeRepository {
    fun getAnnouncement(): Flow<UiState<MainResponseModel<AnnouncementResponse>>>
    fun getQuickActions(): Flow<UiState<MainResponseModel<QuickActionsResponseModel>>>
}