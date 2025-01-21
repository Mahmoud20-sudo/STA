package sa.sauditourism.employee.managers.environment

import android.content.Context
import sa.sauditourism.employee.managers.sharedprefrences.SharedPreferencesManager


class EnvironmentManager(
    val appContext: Context,
    val sharedPreferencesManager: SharedPreferencesManager,
) {

    fun getVariable(envKey: String): String {
        return sharedPreferencesManager.appConfigurations?.optString(envKey) ?: ""
    }

    fun changeEnv(newEnv: String) {
        sharedPreferencesManager.currentEnv = newEnv
    }

    companion object {
        const val ENV_QA = "qa"
        const val ENV_ACC = "acc"
        const val ENV_PROD = "prod"
        const val ENV_PRODCH = "prodch"
    }
}
