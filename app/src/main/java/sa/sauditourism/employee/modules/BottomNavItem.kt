package sa.sauditourism.employee.modules

import sa.sauditourism.employee.EmployeeApplication
import sa.sauditourism.employee.R
import sa.sauditourism.employee.constants.LanguageConstants
import sa.sauditourism.employee.constants.NavConstants
import sa.sauditourism.employee.di.SharedPreferencesModule
import sa.sauditourism.employee.managers.sharedprefrences.SharedPreferencesManager


data class BottomNavItem(var titleLangKey: String, var icon: Any?, var screenRoute: String) {

    companion object {
        fun getTabs(): List<BottomNavItem> {
            val sharedPreferencesManager: SharedPreferencesManager =
                SharedPreferencesModule.provideSharedPreferencesManager(EmployeeApplication.instance.applicationContext)
            return if (sharedPreferencesManager.tabsList.isNullOrEmpty()) {
                listOf(
                    BottomNavItem(
                        LanguageConstants.HOME_TAB,
                        R.drawable.ic_home,
                        NavConstants.TAB_ID_HOME,
                    ),
                    BottomNavItem(
                        LanguageConstants.SERVICES_TAB,
                        R.drawable.ic_services,
                        NavConstants.TAB_ID_SERVICES,
                    ),
                    BottomNavItem(
                        LanguageConstants.PEOPLE_TAB,
                        R.drawable.ic_people,
                        NavConstants.TAB_ID_PEOPLE,
                    ),
                    BottomNavItem(
                        LanguageConstants.ACCOUNT_TAB,
                        R.drawable.ic_profile,
                        NavConstants.TAB_ID_ACCOUNT,
                    ),
                )
            } else {
                sharedPreferencesManager.tabsList?.map {
                    BottomNavItem(it.title, it.icon, it.id)
                }.orEmpty()
            }
        }
    }
}
