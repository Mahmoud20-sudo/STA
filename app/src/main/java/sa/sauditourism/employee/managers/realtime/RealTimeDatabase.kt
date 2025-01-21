package sa.sauditourism.employee.managers.realtime

import sa.sauditourism.employee.modules.splash.model.AccountTabModel
import sa.sauditourism.employee.modules.splash.model.AppMaintenanceModel
import sa.sauditourism.employee.modules.splash.model.AppUpdateModel
import sa.sauditourism.employee.managers.language.LanguageManager
import sa.sauditourism.employee.managers.language.model.SupportedLanguage
import sa.sauditourism.employee.resources.theme.ThemeViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import sa.sauditourism.employee.modules.onboarding.model.OnBoarding
import sa.sauditourism.employee.modules.onboarding.model.OnBoardingModel
import sa.sauditourism.employee.modules.services.model.ServicesModel

abstract class RealTimeDatabase {

    var languagesAndKeys = HashMap<String, String>()
    var supportedLanguagesList = ArrayList<SupportedLanguage>()

    protected val _isConnectedToDb = MutableStateFlow<Boolean?>(null)
    var isConnectedToDb: StateFlow<Boolean?> = _isConnectedToDb


    fun fetchDataOnInitialization(
        languageFetched: MutableStateFlow<Boolean?>,
        appUpdateFetched: MutableStateFlow<AppUpdateModel?>,
        appMaintenanceFetched: MutableStateFlow<AppMaintenanceModel?>,
        appConfigurationsFetched: MutableStateFlow<Boolean?>,
        servicesModel: MutableStateFlow<List<ServicesModel>?>,
        onBoardingModel: MutableStateFlow<List<OnBoarding>?>
    ) {
        LanguageManager.handleLocalization(this, languageFetched)
        fetchAppConfiguration(appConfigurationsFetched)
        fetchAppUpdateModel(appUpdateFetched)
        fetchMaintenanceIsNeeded(appMaintenanceFetched)
        fetchServices(servicesModel)
        fetchOnBoarding(onBoardingModel)
    }

    abstract fun authenticateRealTimeDb()

    abstract fun fetchLocalization(locale: String, languageFetched: MutableStateFlow<Boolean?>)

    abstract fun fetchSupportedLanguages(languageFetched: MutableStateFlow<Boolean?>)

    abstract fun fetchAppTheme(themeViewModel: ThemeViewModel)

    abstract fun fetchAppUpdateModel(appUpdateModel: MutableStateFlow<AppUpdateModel?>)

    abstract fun fetchMaintenanceIsNeeded(appMaintenanceModel: MutableStateFlow<AppMaintenanceModel?>)

    abstract fun fetchAppConfiguration(appConfigurationsFetched: MutableStateFlow<Boolean?>)

    abstract fun fetchServices(servicesModel: MutableStateFlow<List<ServicesModel>?>)

    abstract fun fetchOnBoarding(servicesModel: MutableStateFlow<List<OnBoarding>?>)


}
