package sa.sauditourism.employee.managers.language

import androidx.compose.ui.text.intl.Locale
import androidx.lifecycle.MutableLiveData
import sa.sauditourism.employee.EmployeeApplication
import sa.sauditourism.employee.constants.LanguageConstants
import sa.sauditourism.employee.di.RealTimeDatabaseModule
import sa.sauditourism.employee.di.SharedPreferencesModule
import sa.sauditourism.employee.extensions.getStringResourceByName
import sa.sauditourism.employee.managers.language.model.SupportedLanguage
import sa.sauditourism.employee.managers.sharedprefrences.SharedPreferencesManager
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import sa.sauditourism.employee.managers.realtime.RealTimeDatabase

@Module
@InstallIn(SingletonComponent::class)
object LanguageManager {

    private var realTimeDatabaseManager = RealTimeDatabaseModule.provideRealTimeDatabaseManager()

    private var localizationData = HashMap<String, String>()
    val localizationChanged = MutableLiveData<Boolean?>()
    var supportedLanguages = ArrayList<SupportedLanguage>()

    // get translated string based on the text key, as a fallback option we look into the strings
    // file then if failed, we return the key itself
    fun getTranslationByKey(
        key: String,
        args: Any? = null,
        params: Any? = null,
        before: Boolean = false,
    ): String {
        val retVal = if (localizationData[key] != null) {
            if (args != null) {
                localizationData[key]!!.replace("%1\$s", args.toString()).replace("\\n", "\n")
            } else {
                localizationData[key]!!.replace("\\n", "\n")
            }
        } else {
            try {
                EmployeeApplication.instance.currentActivity!!.getStringResourceByName(
                    key,
                    args,
                ) ?: key
            } catch (e: Exception) {
                EmployeeApplication.instance.applicationContext.getStringResourceByName(
                    key,
                    args,
                ) ?: key
            }
        }
        return if (params != null) {
            if (before) {
                "$params $retVal"
            } else {
                "$retVal $params "
            }
        } else {
            retVal
        }
    }

    // set all localized data to show the strings faster on UI
    fun insertAllData(
        lang: String,
        languagesAndKeys: HashMap<String, String>,
    ) {
        setApplicationLanguage(lang)
        localizationData = languagesAndKeys
    }

    fun handleLocalization(
        realTimeDbManager: RealTimeDatabase? = null,
        languageFetched: MutableStateFlow<Boolean?>,
    ) {
        if (realTimeDbManager != null) {
            realTimeDatabaseManager = realTimeDbManager
        }
        realTimeDatabaseManager.languagesAndKeys.clear()
        if (realTimeDatabaseManager.supportedLanguagesList.isEmpty()) {
            realTimeDatabaseManager.fetchSupportedLanguages(languageFetched)
            return
        }
        handleSupportedLanguages(languageFetched)
    }

    // handle supported languages by parsing the response that we get from firebase
    fun handleSupportedLanguages(languageFetched: MutableStateFlow<Boolean?>) {
        // get all supported languages
        if(supportedLanguages.isEmpty()) {
            supportedLanguages = realTimeDatabaseManager.supportedLanguagesList
        }

        // get preferred language from shared preferences
        var locale =
            SharedPreferencesModule.provideSharedPreferencesManager(EmployeeApplication.instance.applicationContext)
                .preferredLocale
        if (locale.isNullOrEmpty()) {
            locale = Locale.current.language
        }

        // if preferred language is not supported revert back to default locale
        val filteredSupportedLanguage = supportedLanguages.filter { item -> item.code == locale }
        if (filteredSupportedLanguage.isEmpty()) {
            locale = LanguageConstants.DEFAULT_LOCALE
        }
        realTimeDatabaseManager.fetchLocalization(locale, languageFetched)
    }

    // save application language to shared preferences
    fun setApplicationLanguage(lang: String) {
        SharedPreferencesManager(EmployeeApplication.instance.applicationContext).preferredLocale =
            lang
    }

    // change language based on the language code
    fun changeLanguage(langCode: String) {
        val languageFetched: MutableStateFlow<Boolean?> = MutableStateFlow(false)
        setApplicationLanguage(langCode)
        CoroutineScope(Dispatchers.Main).launch {
            languageFetched.collect {
                localizationChanged.value = it == true
            }
        }
        handleLocalization(languageFetched = languageFetched)
    }

    // this method is only used for testing to avoid letting the variable public
    fun getLocalizationData(): HashMap<String, String> {
        return localizationData
    }
}
