package sa.sauditourism.employee.modules.home.bookMeetingRoom.domain

import kotlinx.coroutines.flow.Flow
import sa.sauditourism.employee.EmployeeApplication
import sa.sauditourism.employee.constants.LanguageConstants
import sa.sauditourism.employee.managers.network.helpers.ApiNumberCodes
import sa.sauditourism.employee.managers.network.helpers.ApiResult
import sa.sauditourism.employee.managers.network.models.MainResponseModel
import sa.sauditourism.employee.managers.network.models.UiState
import sa.sauditourism.employee.managers.network.networkBoundNoCacheResource
import sa.sauditourism.employee.managers.sharedprefrences.SharedPreferencesManager
import sa.sauditourism.employee.modules.home.bookMeetingRoom.datasource.AvailableMeetingRoomsEndPoints
import sa.sauditourism.employee.modules.home.bookMeetingRoom.model.available_meeting_rooms.AvailableMeetingRoomsResponseModel
import javax.inject.Inject

class AvailableMeetingRoomsRepositoryImpl @Inject constructor(
    private val endPoints: AvailableMeetingRoomsEndPoints,

    ) :
    AvailableMeetingRoomsRepository {
    override fun getAvailableMeetingRooms(
        date: String,
        timeFrom: String,
        timeTo: String
    ): Flow<UiState<MainResponseModel<AvailableMeetingRoomsResponseModel>>> =
        networkBoundNoCacheResource(
            fetch = {
                ApiResult.create(
                    endPoints.getAvailableRooms(
                        locale = EmployeeApplication.sharedPreferencesManager.preferredLocale
                            ?: LanguageConstants.DEFAULT_LOCALE,
                        date = date,
                        timeFrom = timeFrom,
                        timeTo = timeTo,
                        actEmail = EmployeeApplication.sharedPreferencesManager.accountDetails?.email.toString()
                    ),
                    ApiNumberCodes.REQUEST_DETAILS_API_CODE,
                )
            },
        )

}
