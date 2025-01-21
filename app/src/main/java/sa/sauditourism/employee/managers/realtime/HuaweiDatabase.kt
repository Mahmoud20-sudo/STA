package sa.sauditourism.employee.managers.realtime

import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.huawei.agconnect.AGCRoutePolicy
import com.huawei.agconnect.AGConnectInstance
import com.huawei.agconnect.AGConnectOptionsBuilder
import com.huawei.agconnect.auth.AGConnectAuth
import com.huawei.agconnect.auth.AGConnectUser
import com.huawei.agconnect.cloud.database.AGConnectCloudDB
import com.huawei.agconnect.cloud.database.CloudDBZone
import com.huawei.agconnect.cloud.database.CloudDBZoneConfig
import com.huawei.agconnect.cloud.database.CloudDBZoneQuery
import sa.sauditourism.employee.BuildConfig
import sa.sauditourism.employee.EmployeeApplication
import sa.sauditourism.employee.modules.splash.model.AccountTabModel
import sa.sauditourism.employee.modules.splash.model.AppMaintenanceModel
import sa.sauditourism.employee.modules.splash.model.AppUpdateModel
import sa.sauditourism.employee.configuration.AppConfigurations
import sa.sauditourism.employee.constants.FirebaseConstants
import sa.sauditourism.employee.constants.LanguageConstants
import sa.sauditourism.employee.extensions.getHashMapFromJson
import sa.sauditourism.employee.extensions.isHmsAvailable
import sa.sauditourism.employee.huawei.AccountTabHuaweiModel
import sa.sauditourism.employee.huawei.AppMaintenanceHuawei
import sa.sauditourism.employee.huawei.AppUpdateHuawei
import sa.sauditourism.employee.managers.language.LanguageManager
import sa.sauditourism.employee.managers.language.model.AppLang
import sa.sauditourism.employee.managers.language.model.SupportedLanguage
import sa.sauditourism.employee.managers.language.model.SupportedLanguages
import sa.sauditourism.employee.models.TabScreenModel
import sa.sauditourism.employee.resources.theme.ThemeEvent
import sa.sauditourism.employee.resources.theme.ThemeViewModel
import sa.sauditourism.employee.resources.theme.model.AppTheme
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.update
import org.json.JSONObject
import sa.sauditourism.employee.modules.onboarding.model.OnBoarding
import sa.sauditourism.employee.modules.onboarding.model.OnBoardingModel
import sa.sauditourism.employee.modules.services.model.ServicesModel


object HuaweiDatabase : RealTimeDatabase() {

    private lateinit var agConnectInstance: AGConnectInstance
    private lateinit var agCloudDB: AGConnectCloudDB
    private lateinit var agConnectAuth: AGConnectAuth
    private var cloudDbZone: CloudDBZone? = null
    private var connectedUser: AGConnectUser? = null

    override fun authenticateRealTimeDb() {
        if (isConnectedToDb.value == true) {
            return
        }

        val context = EmployeeApplication.instance.applicationContext
        if (context.isHmsAvailable()) {
            AGConnectCloudDB.initialize(context)

            val agConnectOptions =
                AGConnectOptionsBuilder().setRoutePolicy(AGCRoutePolicy.SINGAPORE).build(context)
            agConnectInstance = AGConnectInstance.buildInstance(agConnectOptions)
            agConnectAuth = AGConnectAuth.getInstance(agConnectInstance)

            agConnectAuth.signInAnonymously().addOnSuccessListener {
                try {
                    connectedUser = it.user
                    agCloudDB = AGConnectCloudDB.getInstance(
                        agConnectInstance,
                        agConnectAuth
                    )
                    agCloudDB.createObjectType(ObjectTypeInfoHelper.getObjectTypeInfo())
                    val cloudDBZoneConfig = CloudDBZoneConfig(
                        LanguageConstants.HUAWEI_CLOUD_DB_ZONE,
                        CloudDBZoneConfig.CloudDBZoneSyncProperty.CLOUDDBZONE_CLOUD_CACHE,
                        CloudDBZoneConfig.CloudDBZoneAccessProperty.CLOUDDBZONE_PUBLIC
                    )
                    cloudDBZoneConfig.persistenceEnabled = true
                    agCloudDB.openCloudDBZone2(cloudDBZoneConfig, true)
                        .addOnSuccessListener { dbZone ->
                            cloudDbZone = dbZone
                            _isConnectedToDb.value = true
                        }.addOnFailureListener { exception ->
                            _isConnectedToDb.value = false
//                            exception.print()
                        }

                } catch (ex: Exception) {
                    _isConnectedToDb.value = false
//                    ex.print()
                }
            }.addOnFailureListener {
                _isConnectedToDb.value = false
//                it.print()
            }
        }
    }

    override fun fetchLocalization(locale: String, languageFetched: MutableStateFlow<Boolean?>) {
        if (cloudDbZone == null) {
            return
        }

        val root = if (BuildConfig.FLAVOR.lowercase() == "prod") {
            FirebaseConstants.REALTIME_DB_PROD_VALUE
        } else {
            FirebaseConstants.REALTIME_DB_NON_PROD_VALUE
        }
        val languageQuery = cloudDbZone!!.executeQuery(
            CloudDBZoneQuery.where(AppLang::class.java)
                .equalTo(LanguageConstants.SUPPORTED_LANGUAGE_CODE, locale)
                .equalTo(FirebaseConstants.HUAWEI_ENVIRONMENT_COLUMN_NAME, root),
            CloudDBZoneQuery.CloudDBZoneQueryPolicy.POLICY_QUERY_FROM_CLOUD_ONLY
        )
        languageQuery.addOnSuccessListener { cloudDBZoneSnapshot ->
            try {
                if (cloudDBZoneSnapshot.snapshotObjects[0] != null) {
                    languagesAndKeys =
                        cloudDBZoneSnapshot.snapshotObjects[0]!!.content.get().getHashMapFromJson()
                    LanguageManager.insertAllData(locale, languagesAndKeys)
                }
                languageFetched.update { true }
            } catch (e: Exception) {
//                e.print()
                languageFetched.update { false }
            } finally {
                cloudDBZoneSnapshot.release()
            }
        }.addOnFailureListener {
            languageFetched.update { false }
        }
    }

    override fun fetchSupportedLanguages(languageFetched: MutableStateFlow<Boolean?>) {
        if (cloudDbZone == null) {
            return
        }

        val root = if (BuildConfig.FLAVOR.lowercase() == "prod") {
            FirebaseConstants.REALTIME_DB_PROD_VALUE
        } else {
            FirebaseConstants.REALTIME_DB_NON_PROD_VALUE
        }
        val supportedLanguagesQuery = cloudDbZone!!.executeQuery(
            CloudDBZoneQuery.where(SupportedLanguages::class.java)
                .equalTo(FirebaseConstants.HUAWEI_ENVIRONMENT_COLUMN_NAME, root)
                .orderByAsc(LanguageConstants.SUPPORTED_LANGUAGE_ID),
            CloudDBZoneQuery.CloudDBZoneQueryPolicy.POLICY_QUERY_FROM_CLOUD_ONLY
        )
        val gson = Gson()

        supportedLanguagesQuery.addOnSuccessListener { cloudDBZoneSnapshot ->
            try {
                if (cloudDBZoneSnapshot.snapshotObjects[0] != null) {
                    try {
                        if (cloudDBZoneSnapshot.snapshotObjects.size() > 0) {
                            supportedLanguagesList.clear()
                        }
                        while (cloudDBZoneSnapshot.snapshotObjects.hasNext()) {
                            val supportedLanguages = cloudDBZoneSnapshot.snapshotObjects.next()
                            val tabs: List<TabScreenModel> = gson.fromJson(
                                supportedLanguages.tabs.get(),
                                object : TypeToken<ArrayList<TabScreenModel>>() {}.type
                            )

                            supportedLanguagesList.add(
                                SupportedLanguage(
                                    supportedLanguages.code,
                                    supportedLanguages.environment,
                                    supportedLanguages.flag,
                                    supportedLanguages.id?.toLong()!!,
                                    supportedLanguages.value,
                                    tabs
                                )
                            )
                        }
                        LanguageManager.handleSupportedLanguages(languageFetched)
                    } catch (e: Exception) {
//                        e.print()
                        LanguageManager.handleSupportedLanguages(languageFetched)
                    }
                }
            } catch (e: Exception) {
                LanguageManager.handleSupportedLanguages(languageFetched)
            } finally {
                cloudDBZoneSnapshot.release()
            }
        }.addOnFailureListener {
            LanguageManager.handleSupportedLanguages(languageFetched)
        }
    }



    override fun fetchAppTheme(themeViewModel: ThemeViewModel) {
        if (cloudDbZone == null) {
            return
        }
        try {
            val root = if (BuildConfig.FLAVOR.lowercase() == "prod") {
                FirebaseConstants.REALTIME_DB_PROD_VALUE
            } else {
                FirebaseConstants.REALTIME_DB_NON_PROD_VALUE
            }
            val languageQuery = cloudDbZone!!.executeQuery(
                CloudDBZoneQuery.where(AppTheme::class.java)
                    .equalTo(FirebaseConstants.HUAWEI_IS_ACTIVE_COLUMN_NAME, true)
                    .equalTo(FirebaseConstants.HUAWEI_ENVIRONMENT_COLUMN_NAME, root),
                CloudDBZoneQuery.CloudDBZoneQueryPolicy.POLICY_QUERY_FROM_CLOUD_ONLY
            )
            languageQuery.addOnSuccessListener { cloudDBZoneSnapshot ->
                try {
                    if (cloudDBZoneSnapshot.snapshotObjects[0] != null) {
                        val dbColors = cloudDBZoneSnapshot.snapshotObjects[0]
                        val appTHeme = AppTheme(
                                dbColors.darkPrimary,
                                dbColors.lightPrimary,
                                dbColors.darkSecondary,
                                dbColors.lightSecondary,
                                dbColors.environment
                            )

                        themeViewModel.onEvent(ThemeEvent.ThemeChange(appTHeme))
                    }
                } catch (e: Exception) {
//                    e.print()
                } finally {
                    cloudDBZoneSnapshot.release()
                }
            }.addOnFailureListener {
//                it.print()
            }
        } catch (ex: Exception) {
//            ex.print()
        }
    }

    override fun fetchAppUpdateModel(appUpdateModel: MutableStateFlow<AppUpdateModel?>) {
        if (cloudDbZone == null) {
            return
        }
        try {
            val root = if (BuildConfig.FLAVOR.lowercase() == "prod") {
                FirebaseConstants.REALTIME_DB_PROD_VALUE
            } else {
                FirebaseConstants.REALTIME_DB_NON_PROD_VALUE
            }
            val languageQuery = cloudDbZone!!.executeQuery(
                CloudDBZoneQuery.where(AppUpdateHuawei::class.java)
                    .equalTo(FirebaseConstants.HUAWEI_ENVIRONMENT_COLUMN_NAME, root),
                CloudDBZoneQuery.CloudDBZoneQueryPolicy.POLICY_QUERY_FROM_CLOUD_ONLY
            )
            languageQuery.addOnSuccessListener { cloudDBZoneSnapshot ->
                try {
                    if (cloudDBZoneSnapshot.snapshotObjects[0] != null) {
                        val appUpdateHuawei = cloudDBZoneSnapshot.snapshotObjects[0]
                        appUpdateModel.update {
                            AppUpdateModel(
                                appUpdateHuawei.forceUpdateEnabled,
                                appUpdateHuawei.version
                            )
                        }
                    }
                } catch (e: Exception) {
//                    e.print()
                    appUpdateModel.update { AppUpdateModel() }
                } finally {
                    cloudDBZoneSnapshot.release()
                }
            }.addOnFailureListener {
//                it.print()
                appUpdateModel.update { AppUpdateModel() }
            }
        } catch (ex: Exception) {
//            ex.print()
            appUpdateModel.update { AppUpdateModel() }
        }
    }

    override fun fetchMaintenanceIsNeeded(appMaintenanceModel: MutableStateFlow<AppMaintenanceModel?>) {
        if (cloudDbZone == null) {
            return
        }
        try {
            val root = if (BuildConfig.FLAVOR.lowercase() == "prod") {
                FirebaseConstants.REALTIME_DB_PROD_VALUE
            } else {
                FirebaseConstants.REALTIME_DB_NON_PROD_VALUE
            }
            val languageQuery = cloudDbZone!!.executeQuery(
                CloudDBZoneQuery.where(AppMaintenanceHuawei::class.java)
                    .equalTo(FirebaseConstants.HUAWEI_ENVIRONMENT_COLUMN_NAME, root),
                CloudDBZoneQuery.CloudDBZoneQueryPolicy.POLICY_QUERY_FROM_CLOUD_ONLY
            )
            languageQuery.addOnSuccessListener { cloudDBZoneSnapshot ->
                try {
                    if (cloudDBZoneSnapshot.snapshotObjects[0] != null) {
                        val appMaintenanceHuawei = cloudDBZoneSnapshot.snapshotObjects[0]
                        appMaintenanceModel.update {
                            AppMaintenanceModel(
                                appMaintenanceHuawei.enable,
                                appMaintenanceHuawei.version
                            )
                        }
                    }
                } catch (e: Exception) {
//                    e.print()
                    appMaintenanceModel.update { AppMaintenanceModel() }
                } finally {
                    cloudDBZoneSnapshot.release()
                }
            }.addOnFailureListener {
//                it.print()
                appMaintenanceModel.update { AppMaintenanceModel() }
            }
        } catch (ex: Exception) {
//            ex.print()
            appMaintenanceModel.update { AppMaintenanceModel() }
        }
    }

    override fun fetchServices(servicesModel: MutableStateFlow<List<ServicesModel>?>) {
        //todo handle services from CloudDBZoneQuery
//        if (cloudDbZone == null) {
//            servicesModel.update { emptyList() }
//            return
//        }
//        try {
//            val root = if (BuildConfig.FLAVOR.lowercase() == "prod") {
//                FirebaseConstants.REALTIME_DB_PROD_VALUE
//            } else {
//                FirebaseConstants.REALTIME_DB_NON_PROD_VALUE
//            }
//            val languageQuery = cloudDbZone!!.executeQuery(
//                CloudDBZoneQuery.where(AccountTabHuaweiModel::class.java)
//                    .equalTo(FirebaseConstants.HUAWEI_SERVICES_COLUMN_NAME, root),
//                CloudDBZoneQuery.CloudDBZoneQueryPolicy.POLICY_QUERY_FROM_CLOUD_ONLY
//            )
//            languageQuery.addOnSuccessListener { cloudDBZoneSnapshot ->
//                try {
//                    val tabModels = ArrayList<ServicesModel>()
//                    while (cloudDBZoneSnapshot.snapshotObjects.hasNext()) {
//                        val appUpdateHuawei = cloudDBZoneSnapshot.snapshotObjects.next()
//                        tabModels.add(ServicesModel(appUpdateHuawei.id, appUpdateHuawei.id))
//                    }
//                    servicesModel.update { tabModels }
//                } catch (e: Exception) {
////                    e.print()
//                    servicesModel.update { emptyList() }
//                } finally {
//                    cloudDBZoneSnapshot.release()
//                }
//            }.addOnFailureListener {
////                it.print()
//                servicesModel.update { emptyList() }
//            }
//        } catch (ex: Exception) {
////            ex.print()
//            servicesModel.update { emptyList() }
//        }
    }

    override fun fetchOnBoarding(servicesModel: MutableStateFlow<List<OnBoarding>?>) {
        //todo
    }

    override fun fetchAppConfiguration(appConfigurationsFetched: MutableStateFlow<Boolean?>) {
        if (cloudDbZone == null) {
            return
        }
        try {
            val root = if (BuildConfig.FLAVOR.lowercase() == "prod") {
                FirebaseConstants.REALTIME_DB_PROD_VALUE
            } else {
                FirebaseConstants.REALTIME_DB_NON_PROD_VALUE
            }
            val preferredLocale = EmployeeApplication.sharedPreferencesManager.preferredLocale
            val lang = if (preferredLocale == LanguageConstants.LANG_AR) {
                LanguageConstants.LANG_AR
            } else {
                LanguageConstants.DEFAULT_LOCALE
            }
            val gson = Gson()
            val languageQuery = cloudDbZone!!.executeQuery(
                CloudDBZoneQuery.where(AppConfigurations::class.java),
                CloudDBZoneQuery.CloudDBZoneQueryPolicy.POLICY_QUERY_FROM_CLOUD_ONLY
            )
            languageQuery.addOnSuccessListener { cloudDBZoneSnapshot ->
                try {
                    if (cloudDBZoneSnapshot.snapshotObjects[0] != null) {
                        val snapshot = cloudDBZoneSnapshot.snapshotObjects[0]
                        val jsonString = snapshot.content.get() as String
                        val jsonObject = JSONObject(jsonString)
                        val currentEnv: String = EmployeeApplication.sharedPreferencesManager.currentEnv ?: "prod"
                        EmployeeApplication.sharedPreferencesManager.appConfigurations = jsonObject.getJSONObject(currentEnv)
                        appConfigurationsFetched.update { true }
                    }
                } catch (e: Exception) {
//                    e.print()
                    appConfigurationsFetched.update { false }
                } finally {
                    cloudDBZoneSnapshot.release()
                }
            }.addOnFailureListener {
//                it.print()
                appConfigurationsFetched.update { false }
            }
        } catch (ex: Exception) {
//            ex.print()
            appConfigurationsFetched.update { false }
        }
    }
}
