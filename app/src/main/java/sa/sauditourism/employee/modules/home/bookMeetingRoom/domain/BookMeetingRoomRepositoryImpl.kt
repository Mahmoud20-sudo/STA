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
import sa.sauditourism.employee.modules.home.bookMeetingRoom.datasource.BookMeetingRoomEndPoints
import sa.sauditourism.employee.modules.home.bookMeetingRoom.model.single_meeting_room.request.BookMeetingRoomRequestModel
import sa.sauditourism.employee.modules.home.bookMeetingRoom.model.single_meeting_room.response.BookMeetingRoomResponse
import javax.inject.Inject

class BookMeetingRoomRepositoryImpl @Inject constructor(
    private val endPoints: BookMeetingRoomEndPoints,

) :
    BookMeetingRoomRepository {
    override fun bookMeeting(
        meetingId: String,
        bookMeetRequestModel: BookMeetingRoomRequestModel
    ): Flow<UiState<MainResponseModel<BookMeetingRoomResponse>>> =
        networkBoundNoCacheResource(
            fetch = {
                ApiResult.create(
                    endPoints.bookRoom(
                        meetingRoomId = meetingId,
                        bookMeetingRoomRequestModel = bookMeetRequestModel,
                        locale = EmployeeApplication.sharedPreferencesManager.preferredLocale
                            ?: LanguageConstants.DEFAULT_LOCALE,
                        actEmail =EmployeeApplication.sharedPreferencesManager.accountDetails?.email.toString()
                    ),
                    ApiNumberCodes.SUBMIT_FORM_API_CODE,
                )
            },
        )
}