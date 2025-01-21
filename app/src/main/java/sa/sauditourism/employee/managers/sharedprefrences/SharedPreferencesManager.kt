package sa.sauditourism.employee.managers.sharedprefrences

import android.content.Context
import android.content.SharedPreferences
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import sa.sauditourism.employee.modules.splash.model.AccountTabModel
import sa.sauditourism.employee.managers.environment.EnvironmentManager
import sa.sauditourism.employee.models.TabScreenModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.buffer
import kotlinx.coroutines.flow.callbackFlow
import org.json.JSONObject
import sa.sauditourism.employee.modules.account.model.AccountDetails
import sa.sauditourism.employee.modules.account.model.AccountResponse
import sa.sauditourism.employee.modules.onboarding.model.OnBoarding
import sa.sauditourism.employee.modules.onboarding.model.OnBoardingModel
import sa.sauditourism.employee.modules.services.model.ServicesModel
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class SharedPreferencesManager @Inject constructor(@ApplicationContext context: Context) {


    companion object {
        private const val SHARED_PREFS_NAME = "VisitSaudi"
        private const val KEY_EMAIL = "Email"
        private const val KEY_DARK_THEME = "DarkModePrefs"
        public const val KEY_PREFERRED_LOCALE = "PreferredLocalePrefs"
        private const val KEY_NOTIFICATION_PREFS = "NotificationPrefs"
        private const val CAMERA_ACCESS = "camera_access"
        private const val KEY_PERSON_NUMBER_PREFS = "PersonNumber"
        private const val KEY_SKIPPED_SSP = "SkippedSsp"
        private const val KEY_SKIPPED_LOYALTY_ONBOARDING = "SkippedLoyaltyOnboarding"
        private const val KEY_PERSON_ID_PREFS = "PersonId"
        private const val KEY_ENV = "currentEnv"
        private const val KEY_TABS = "accountTab"
        private const val KEY_TABS_RESPONSES = "tabsScreensResponse"
        private const val KEY_CURRENT_LAT = "currentLat"
        private const val KEY_CURRENT_LNG = "currentLng"
        private const val KEY_CURRENT_CITY = "currentCity"
        private const val EntertainerBoarding = "EntertainerBoarding"
        private const val KEY_PROFILE_RESPONSE = "profileResponse"
        private const val KEY_APP_OPEN_COUNT = "appOpenCount"
        private const val AUTH_CODE = "authCode"
        private const val AUTH_STATE = "authState"
        private const val AUTH_SESSION_STATE = "authSessionState"
        private const val KEY_ACCOUNT_TAB_LIST = "accountTabList"
        private const val APP_CONFIGURATIONS = "appConfig"
        private const val SERVICES = "services"
        private const val ACCESS_TOKEN = "access_token"
        private const val REFRESH_TOKEN = "refresh_token"
        private const val ACCESS_TOKEN_EXPIRATION = "access_token_expiration"
        private const val ON_BOARDING = "on_boarding"
        private const val USER_NAME = "user_name"
        private const val USER_IMAGE = "user_image"
        private const val USER_AVATAR = "user_avatar"
        private const val USER_DATA = "user_data"
    }

    private val sharedPreferences =
        context.getSharedPreferences(SHARED_PREFS_NAME, Context.MODE_PRIVATE)
    var darkMode: Boolean
        get() {
            return sharedPreferences.getBoolean(
                KEY_DARK_THEME,
                false
            )
        }
        set(value) {
            sharedPreferences.edit().putBoolean(KEY_DARK_THEME, value).apply()
        }

    var email: String?
        get() = sharedPreferences.getString(KEY_EMAIL, null)
        set(value) {
            sharedPreferences.edit().putString(KEY_EMAIL, value).apply()
        }

    var userName: String?
        get() = sharedPreferences.getString(USER_NAME, null)
        set(value) {
            sharedPreferences.edit().putString(USER_NAME, value).apply()
        }

    var userAvatar: Int
        get() = sharedPreferences.getInt(USER_AVATAR, 0)
        set(value) {
            sharedPreferences.edit().putInt(USER_AVATAR, value).apply()
        }
    var userImage: String?
        get() = sharedPreferences.getString(USER_IMAGE, null)
        set(value) {
            sharedPreferences.edit().putString(USER_IMAGE, value).apply()
        }

    var preferredLocale: String?
        get() = sharedPreferences.getString(KEY_PREFERRED_LOCALE, null)
        set(value) {
            sharedPreferences.edit().putString(KEY_PREFERRED_LOCALE, value).apply()
        }

    var currentEnv: String?
        get() = sharedPreferences.getString(
            KEY_ENV,
            EnvironmentManager.ENV_ACC
            // if (BuildConfig.FLAVOR == "prod") EnvironmentManager.ENV_PROD else EnvironmentManager.ENV_ACC
        )
        set(value) {
            sharedPreferences.edit().putString(KEY_ENV, value).apply()
        }

    var authCode: String?
        get() = sharedPreferences.getString(AUTH_CODE, null)
        set(value) {
            if (value.isNullOrEmpty()) {
                sharedPreferences.edit().remove(AUTH_CODE).apply()
                return
            }
            sharedPreferences.edit().putString(AUTH_CODE, value).apply()
        }

    var appConfigurations: JSONObject?
        get() = sharedPreferences.getString(APP_CONFIGURATIONS, null)?.let { JSONObject(it) }
        set(value) {
            sharedPreferences.edit().putString(APP_CONFIGURATIONS, value.toString()).apply()
        }

    var authState: String?
        get() = sharedPreferences.getString(AUTH_STATE, null)
        set(value) {
            if (value.isNullOrEmpty()) {
                sharedPreferences.edit().remove(AUTH_STATE).apply()
                return
            }
            sharedPreferences.edit().putString(AUTH_STATE, value).apply()
        }

    var authSessionState: String?
        get() = sharedPreferences.getString(AUTH_SESSION_STATE, null)
        set(value) {
            if (value.isNullOrEmpty()) {
                sharedPreferences.edit().remove(AUTH_SESSION_STATE).apply()
                return
            }
            sharedPreferences.edit().putString(AUTH_SESSION_STATE, value).apply()
        }

    var hasSkippedSsp: Boolean
        get() = sharedPreferences.getBoolean(KEY_SKIPPED_SSP, false)
        set(value) {
            sharedPreferences.edit().putBoolean(KEY_SKIPPED_SSP, value).apply()
        }

    var hasSkippedLoyaltyOnboarding: Boolean
        get() = sharedPreferences.getBoolean(KEY_SKIPPED_LOYALTY_ONBOARDING, false)
        set(value) {
            sharedPreferences.edit().putBoolean(KEY_SKIPPED_LOYALTY_ONBOARDING, value).apply()
        }

    var currentCity: String?
        get() = sharedPreferences.getString(KEY_CURRENT_CITY, null)
        set(value) {
            sharedPreferences.edit().putString(KEY_CURRENT_CITY, value).apply()
        }

    var openAppCount: Int
        get() = sharedPreferences.getInt(KEY_APP_OPEN_COUNT, 0)
        set(value) {
            sharedPreferences.edit().putInt(KEY_APP_OPEN_COUNT, value).apply()
        }


    /**
     * to be used for phase 2
     */
    var currentLat: Float
        get() = sharedPreferences.getFloat(KEY_CURRENT_LAT, 0.0f)
        set(value) {
            sharedPreferences.edit().putFloat(KEY_CURRENT_LAT, value).apply()
        }

    /**
     * to be used for phase 2
     */
    var currentLng: Float
        get() = sharedPreferences.getFloat(KEY_CURRENT_LNG, 0.0f)
        set(value) {
            sharedPreferences.edit().putFloat(KEY_CURRENT_LNG, value).apply()
        }

    var onBoardingVersion: String?
        get() = sharedPreferences?.getString(EntertainerBoarding, null)
        set(init) {
            sharedPreferences?.edit()?.putString(EntertainerBoarding, init)?.apply()
        }

    fun clearTabsResponses() {
        /* val screens = BottomNavItem.getTabs()
         screens.forEach {
             setTabResponse(it.screenRoute, null)
         }*/
    }

//    fun saveAccountDetails(accountDetails: AccountDetails) {
//
//        val gson = Gson()
//        val json = gson.toJson(accountDetails)
//        sharedPreferences?.edit()?.putString(USER_DATA, json)?.apply()
//    }
//
//    fun getAccountDetails(): AccountDetails? {
//        val json = sharedPreferences.getString(USER_DATA, null) ?: return null
//        val gson = Gson()
//        return gson.fromJson(json, AccountDetails::class.java)
//    }

    var accountDetails: AccountDetails?
        get() {
            val gson = Gson()
            val notString = sharedPreferences.getString(USER_DATA, "").toString()
            val tabsType = object : TypeToken<AccountDetails?>() {}.type
            return gson.fromJson(notString, tabsType)
        }
        set(init) {
            val gson = Gson()
            val json = gson.toJson(init)
            sharedPreferences?.edit()?.putString(USER_DATA, json)?.apply()
        }

    fun clearAllPreferences() {
        sharedPreferences.edit().clear().apply()
    }

    var tabsList: List<TabScreenModel>?
        get() {
            val gson = Gson()
            val notString = sharedPreferences.getString(
                KEY_TABS, ""
            ).toString()
            val tabsType = object : TypeToken<List<TabScreenModel>?>() {}.type
            return gson.fromJson(notString, tabsType)
        }
        set(list) {
            if (list.isNullOrEmpty()) {
                sharedPreferences.edit().remove(KEY_TABS).apply()
                return
            }
            val gson = Gson()
            val notString = gson.toJson(list)
            sharedPreferences.edit().putString(KEY_TABS, notString).apply()
        }

    var accountTabModels: List<AccountTabModel>?
        get() {
            val gson = Gson()
            val notString = sharedPreferences.getString(
                KEY_ACCOUNT_TAB_LIST, ""
            ).toString()
            val tabsType = object : TypeToken<List<AccountTabModel>?>() {}.type
            return gson.fromJson(notString, tabsType)
        }
        set(list) {
            if (list.isNullOrEmpty()) {
                sharedPreferences.edit().remove(KEY_ACCOUNT_TAB_LIST).apply()
                return
            }
            val gson = Gson()
            val notString = gson.toJson(list)
            sharedPreferences.edit().putString(KEY_ACCOUNT_TAB_LIST, notString).apply()
        }

    var services: List<ServicesModel>?
        get() {
            val gson = Gson()
            val notString = sharedPreferences.getString(
                SERVICES, ""
            ).toString()
            val tabsType = object : TypeToken<List<ServicesModel>?>() {}.type
            return gson.fromJson(notString, tabsType)
        }
        set(list) {
            if (list.isNullOrEmpty()) {
                sharedPreferences.edit().remove(SERVICES).apply()
                return
            }
            val gson = Gson()
            val notString = gson.toJson(list)
            sharedPreferences.edit().putString(SERVICES, notString).apply()
        }


    var accessToken: String?
        get() = sharedPreferences.getString(ACCESS_TOKEN, null)
        set(value) {
            sharedPreferences.edit().putString(ACCESS_TOKEN, value).apply()
        }

    var refreshToken: String?
        get() = sharedPreferences.getString(REFRESH_TOKEN, null)
        set(value) {
            sharedPreferences.edit().putString(REFRESH_TOKEN, value).apply()
        }

//    var accessTokenExpiration: Long?
//        get() = sharedPreferences.getLong(ACCESS_TOKEN_EXPIRATION, -1).takeIf { it.toInt() != -1 }
//        set(value) {
//            sharedPreferences.edit().putLong(ACCESS_TOKEN_EXPIRATION, value ?: -1).apply()
//        }

    var onBoarding: List<OnBoarding>?
        get() {
            val gson = Gson()
            val notString = sharedPreferences.getString(
                ON_BOARDING, ""
            ).toString()
            val tabsType = object : TypeToken<List<OnBoarding>?>() {}.type
            return gson.fromJson(notString, tabsType)
        }
        set(list) {
            if (list.isNullOrEmpty()) {
                sharedPreferences.edit().remove(ON_BOARDING).apply()
                return
            }
            val gson = Gson()
            val notString = gson.toJson(list)
            sharedPreferences.edit().putString(ON_BOARDING, notString).apply()
        }

    fun getStringFlowForKey(keyForFloat: String) = callbackFlow {
        val listener = SharedPreferences.OnSharedPreferenceChangeListener { _, key ->
            if (keyForFloat == key) {
                trySend(sharedPreferences.getString(key, "0") ?: "0")
            }
        }
        sharedPreferences.registerOnSharedPreferenceChangeListener(listener)
        if (sharedPreferences.contains(keyForFloat)) {
            send(
                sharedPreferences.getString(keyForFloat, "0f") ?: "0"
            ) // if you want to emit an initial pre-existing value
        }
        awaitClose { sharedPreferences.unregisterOnSharedPreferenceChangeListener(listener) }
    }.buffer(Channel.UNLIMITED) // so trySend never fails
}
