package sa.sauditourism.employee.configuration

import sa.sauditourism.employee.BuildConfig


object AppConfig {

    val isDebugBuild = BuildConfig.DEBUG
    val appFlavor = BuildConfig.FLAVOR

    const val FIREBASE_EMAIL = "employee-app@sta.gov.sa"
    const val FIREBASE_PASS = "EmployeeApp"
}
