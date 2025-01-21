package sa.sauditourism.employee.managers.realtime

import android.util.Log
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import sa.sauditourism.employee.BuildConfig
import sa.sauditourism.employee.EmployeeApplication
import sa.sauditourism.employee.modules.splash.model.AccountTabModel
import sa.sauditourism.employee.modules.splash.model.AppMaintenanceModel
import sa.sauditourism.employee.modules.splash.model.AppUpdateModel
import sa.sauditourism.employee.constants.FirebaseConstants
import sa.sauditourism.employee.constants.LanguageConstants
import sa.sauditourism.employee.managers.language.LanguageManager
import sa.sauditourism.employee.managers.language.model.SupportedLanguage
import sa.sauditourism.employee.managers.remoteconfig.RemoteConfigService
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


class FirebaseDatabase : RealTimeDatabase() {

    private val databaseReference: DatabaseReference = Firebase.database.reference

    override fun authenticateRealTimeDb() {
        _isConnectedToDb.value = true
    }

    // authenticate firebase and get locale texts
    override fun fetchLocalization(locale: String, languageFetched: MutableStateFlow<Boolean?>) {
        FirebaseAuthenticationManager.authenticateDatabase {
            if (it) {
                val root = if (BuildConfig.FLAVOR.lowercase() == "prod") {
                    FirebaseConstants.REALTIME_DB_PROD_VALUE
                } else {
                    FirebaseConstants.REALTIME_DB_NON_PROD_VALUE
                }
                val localeReference =
                    databaseReference.child(
                        "$root/${LanguageConstants.FIREBASE_LANGUAGE_DATABASE_NAME}/$locale",
                    )
                localeReference.keepSynced(true)
                localeReference.addValueEventListener(object : ValueEventListener {
                    override fun onDataChange(dataSnapshot: DataSnapshot) {
                        // This method is called when the data at the "locale" node changes.
                        // dataSnapshot contains all the children under "locale" node.
                        try {
                            if (dataSnapshot.value == null) {
                                languageFetched.update { false }
                                return
                            }

                            languagesAndKeys =
                                dataSnapshot.value as HashMap<String, String>
                            LanguageManager.insertAllData(locale, languagesAndKeys)
                            languageFetched.update { true }
                        } catch (e: Exception) {
//                                e.print()
                            languageFetched.update { false }
                        }
                    }

                    override fun onCancelled(databaseError: DatabaseError) {
                        languageFetched.update { false }
                    }
                })
            } else {
                languageFetched.update { false }
            }
        }
    }

    // authenticate firebase and get supported locales and handle localization
    override fun fetchSupportedLanguages(languageFetched: MutableStateFlow<Boolean?>) {
        FirebaseAuthenticationManager.authenticateDatabase {
            if (it) {
                val root = if (BuildConfig.FLAVOR.lowercase() == "prod") {
                    FirebaseConstants.REALTIME_DB_PROD_VALUE
                } else {
                    FirebaseConstants.REALTIME_DB_NON_PROD_VALUE
                }
                val localeReference =
                    databaseReference.child("$root/${LanguageConstants.FIREBASE_SUPPORTED_LANGUAGES_DATABASE_NAME}")
                localeReference.keepSynced(true)
                localeReference.addValueEventListener(object : ValueEventListener {
                    override fun onDataChange(dataSnapshot: DataSnapshot) {
                        try {
                            val snapshotValue =
                                dataSnapshot.value as ArrayList<HashMap<String, Any>>
                            if (snapshotValue.isNotEmpty()) {
                                supportedLanguagesList.clear()
                            }
                            snapshotValue.forEach { mapValues ->
                                supportedLanguagesList.add(
                                    SupportedLanguage(
                                        mapValues[LanguageConstants.SUPPORTED_LANGUAGE_CODE].toString(),
                                        mapValues[LanguageConstants.SUPPORTED_LANGUAGE_ENV].toString(),
                                        mapValues[LanguageConstants.SUPPORTED_LANGUAGE_FLAG].toString(),
                                        mapValues[LanguageConstants.SUPPORTED_LANGUAGE_ID] as Long,
                                        mapValues[LanguageConstants.SUPPORTED_LANGUAGE_VALUE].toString(),
                                        mapValues[LanguageConstants.SUPPORTED_LANGUAGE_TABS] as List<TabScreenModel>,
                                    ),
                                )
                            }
                            LanguageManager.handleSupportedLanguages(languageFetched)
                        } catch (e: Exception) {
//                                e.print()
                            LanguageManager.handleSupportedLanguages(languageFetched)
                        }
                    }

                    override fun onCancelled(databaseError: DatabaseError) {
                        LanguageManager.handleSupportedLanguages(languageFetched)
                    }
                })
            } else {
                LanguageManager.handleSupportedLanguages(languageFetched)
            }
        }
    }


    override fun fetchAppTheme(themeViewModel: ThemeViewModel) {
        FirebaseAuthenticationManager.authenticateDatabase {
            if (it) {
                val root = if (BuildConfig.FLAVOR.lowercase() == "prod") {
                    FirebaseConstants.REALTIME_DB_PROD_VALUE
                } else {
                    FirebaseConstants.REALTIME_DB_NON_PROD_VALUE
                }
                val localeReference =
                    databaseReference.child("$root/${FirebaseConstants.FIREBASE_APP_THEME_TABLE_NAME}")
                localeReference.keepSynced(true)
                localeReference.addValueEventListener(object : ValueEventListener {
                    override fun onDataChange(dataSnapshot: DataSnapshot) {
                        try {
                            val dataMap = dataSnapshot.value as? Map<*, *>
                            if (dataMap != null) {
                                val appTHeme =
                                    AppTheme(
                                        dataMap[FirebaseConstants.FIREBASE_DARK_PRIMARY_COLOR_COLUMN_NAME] as String,
                                        dataMap[FirebaseConstants.FIREBASE_LIGHT_PRIMARY_COLOR_COLUMN_NAME] as String,
                                        dataMap[FirebaseConstants.FIREBASE_DARK_SECONDARY_COLOR_COLUMN_NAME] as String,
                                        dataMap[FirebaseConstants.FIREBASE_LIGHT_SECONDARY_COLOR_COLUMN_NAME] as String,
                                        root
                                    )
                                themeViewModel.onEvent(ThemeEvent.ThemeChange(appTHeme))
                            }
                        } catch (e: Exception) {

                        }
                    }

                    override fun onCancelled(databaseError: DatabaseError) {
                        Log.e(
                            "FirebaseManager",
                            "AppTheme=onCancelled $databaseError}",
                        )
                    }
                })
            }
        }
    }

    override fun fetchAppUpdateModel(appUpdateModel: MutableStateFlow<AppUpdateModel?>) {
        val appUpdateVersion =
            RemoteConfigService.checkFeatureAsString(feature = FirebaseConstants.FIREBASE_APP_UPDATE_REMOTE_CONFIG)
        val forceAppUpdate =
            RemoteConfigService.checkFeature(feature = FirebaseConstants.FIREBASE_FORCE_UPDATE_REMOTE_CONFIG)
                ?: false

        appUpdateModel.value = AppUpdateModel(forceAppUpdate, appUpdateVersion)
    }

    override fun fetchMaintenanceIsNeeded(appMaintenanceModel: MutableStateFlow<AppMaintenanceModel?>) {

        val appMaintenanceVersion =
            RemoteConfigService.checkFeatureAsString(feature = FirebaseConstants.FIREBASE_APP_MAINTENANCE_REMOTE_CONFIG)
        val enableMaintenanceUpdate =
            RemoteConfigService.checkFeature(feature = FirebaseConstants.FIREBASE_ENABLE_MAINTENANCE_REMOTE_CONFIG)
                ?: false

        appMaintenanceModel.value =
            AppMaintenanceModel(enableMaintenanceUpdate, appMaintenanceVersion)
    }

    override fun fetchServices(servicesModel: MutableStateFlow<List<ServicesModel>?>) {

        FirebaseAuthenticationManager.authenticateDatabase {
            if (it) {
                val root = if (BuildConfig.FLAVOR.lowercase() == "prod") {
                    FirebaseConstants.REALTIME_DB_PROD_VALUE
                } else {
                    FirebaseConstants.REALTIME_DB_NON_PROD_VALUE
                }
                val localeReference =
                    databaseReference.child("$root/${LanguageConstants.FIREBASE_SERVICES_DATABASE_NAME}")
                localeReference.keepSynced(true)
                localeReference.addValueEventListener(object : ValueEventListener {
                    override fun onDataChange(dataSnapshot: DataSnapshot) {
                        try {
                            if (dataSnapshot.value is List<*>) {
                                servicesModel.update {
                                    dataSnapshot.value as List<ServicesModel>
                                }
                            }
                        } catch (e: Exception) {
//                                e.print()
                            servicesModel.update {
                                emptyList()
                            }
                        }
                    }

                    override fun onCancelled(databaseError: DatabaseError) {
                        servicesModel.update {
                            emptyList()
                        }
                    }
                })
            } else {
                servicesModel.update {
                    emptyList()
                }
            }
        }
    }

    override fun fetchOnBoarding(servicesModel: MutableStateFlow<List<OnBoarding>?>) {

        FirebaseAuthenticationManager.authenticateDatabase {
            if (it) {
                val root = if (BuildConfig.FLAVOR.lowercase() == "prod") {
                    FirebaseConstants.REALTIME_DB_PROD_VALUE
                } else {
                    FirebaseConstants.REALTIME_DB_NON_PROD_VALUE
                }
                val tableName =
                    if (EmployeeApplication.sharedPreferencesManager.preferredLocale == "ar") LanguageConstants.FIREBASE_ONBOARDING_AR_DATABASE_NAME else LanguageConstants.FIREBASE_ONBOARDING_DATABASE_NAME
                val localeReference =
                    databaseReference.child("$root/${tableName}")
                localeReference.keepSynced(true)
                localeReference.addValueEventListener(object : ValueEventListener {
                    override fun onDataChange(dataSnapshot: DataSnapshot) {
                        try {
                            if (dataSnapshot.value is List<*>) {
                                servicesModel.update {
                                    dataSnapshot.value as List<OnBoarding>
                                }
                            }
                        } catch (e: Exception) {
//                                e.print()
                            servicesModel.update {
                                emptyList()
                            }
                        }
                    }

                    override fun onCancelled(databaseError: DatabaseError) {
                        servicesModel.update {
                            emptyList()
                        }
                    }
                })
            } else {
                servicesModel.update {
                    emptyList()
                }
            }
        }
    }


    override fun fetchAppConfiguration(appConfigurationsFetched: MutableStateFlow<Boolean?>) {
        Log.d("FirebaseManager", "Starting authentication process...")
        FirebaseAuthenticationManager.authenticateDatabase {
            if (it) {
                val root = if (BuildConfig.FLAVOR.lowercase() == "prod") {
                    FirebaseConstants.REALTIME_DB_PROD_VALUE
                } else {
                    FirebaseConstants.REALTIME_DB_NON_PROD_VALUE
                }
                val tableName = FirebaseConstants.FIREBASE_APP_CONFIGURATIONS
                val currentEnv =
                    EmployeeApplication.sharedPreferencesManager.currentEnv?.uppercase()

                val localeReference = databaseReference.child("$root/$tableName$currentEnv")
                Log.d("FirebaseManager", "Database reference path: $root/$tableName$currentEnv")
                localeReference.keepSynced(true)
                localeReference.addListenerForSingleValueEvent(object : ValueEventListener {
                    override fun onDataChange(dataSnapshot: DataSnapshot) {
                        try {
                            if (dataSnapshot.exists()) {
                                // Convert the DataSnapshot to a Map
                                val dataMap = dataSnapshot.value as Map<*, *>

                                // Create a JSONObject from the map
                                val dataJSONObject = JSONObject(dataMap)

                                // Check if the configuration is not empty
                                if (dataJSONObject.length() > 0) {
                                    EmployeeApplication.sharedPreferencesManager.appConfigurations =
                                        dataJSONObject
                                    appConfigurationsFetched.update { true }
                                } else {
                                    appConfigurationsFetched.update { false }
                                }
                            } else {
                                appConfigurationsFetched.update { false }
                            }
                        } catch (e: Exception) {
                            Log.e("FirebaseManager", "Error processing data: ${e.message}", e)
                            appConfigurationsFetched.update { false }
                        }
                    }

                    override fun onCancelled(databaseError: DatabaseError) {
                        appConfigurationsFetched.update { false }
                        Log.e(
                            "FirebaseManager",
                            "fetchAppConfiguration onCancelled: ${databaseError.message}"
                        )
                    }
                })
            } else {
                appConfigurationsFetched.update { false }
            }
        }
    }
}
