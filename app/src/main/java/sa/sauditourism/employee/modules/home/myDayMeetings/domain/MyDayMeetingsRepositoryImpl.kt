package sa.sauditourism.employee.modules.home.myDayMeetings.domain

import kotlinx.coroutines.flow.Flow
import sa.sauditourism.employee.EmployeeApplication
import sa.sauditourism.employee.constants.LanguageConstants
import sa.sauditourism.employee.managers.network.helpers.ApiNumberCodes.MY_DAY_MEETINGS_API_CODE
import sa.sauditourism.employee.managers.network.helpers.ApiResult
import sa.sauditourism.employee.managers.network.models.MainResponseModel
import sa.sauditourism.employee.managers.network.models.UiState
import sa.sauditourism.employee.managers.network.networkBoundNoCacheResource
import sa.sauditourism.employee.modules.home.myDayMeetings.datasource.remote.MyMeetingsEndPoints
import sa.sauditourism.employee.modules.home.myDayMeetings.model.MyDayMeetingsResponseModel
import javax.inject.Inject

class MyDayMeetingsRepositoryImpl @Inject constructor(
    private val myMeetingsPoints: MyMeetingsEndPoints
) : MyDayMeetingsRepository {

    override fun getMyDayMeetings(): Flow<UiState<MainResponseModel<MyDayMeetingsResponseModel>>> = networkBoundNoCacheResource(
        fetch = {
            ApiResult.create(
                myMeetingsPoints.getMyDayMeetings(
                    locale = EmployeeApplication.sharedPreferencesManager.preferredLocale
                        ?: LanguageConstants.DEFAULT_LOCALE,
                    actEmail = EmployeeApplication.sharedPreferencesManager.accountDetails?.email.toString()
                ),
                MY_DAY_MEETINGS_API_CODE
            )
        }
    )
}