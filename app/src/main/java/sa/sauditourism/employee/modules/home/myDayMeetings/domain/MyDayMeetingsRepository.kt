package sa.sauditourism.employee.modules.home.myDayMeetings.domain

import kotlinx.coroutines.flow.Flow
import sa.sauditourism.employee.managers.network.models.MainResponseModel
import sa.sauditourism.employee.managers.network.models.UiState
import sa.sauditourism.employee.modules.home.myDayMeetings.model.MyDayMeetingsResponseModel

interface MyDayMeetingsRepository {
    fun getMyDayMeetings(): Flow<UiState<MainResponseModel<MyDayMeetingsResponseModel>>>
}