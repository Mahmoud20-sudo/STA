package sa.sauditourism.employee.managers.locale

import android.content.Context
import androidx.core.os.ConfigurationCompat
import sa.sauditourism.employee.EmployeeApplication
import sa.sauditourism.employee.constants.LanguageConstants
import sa.sauditourism.employee.di.SharedPreferencesModule
import java.util.Locale

object LocaleChanger {

    fun wrapContext(context: Context): Context {
        val sharedPreferencesManager = SharedPreferencesModule.provideSharedPreferencesManager(EmployeeApplication.instance.applicationContext)

        if (sharedPreferencesManager.preferredLocale == null) {
            val configuration = context.resources.configuration
            val locales = ConfigurationCompat.getLocales(configuration)
            if (locales.size() > 0) {
                val locale = locales.get(0)
                sharedPreferencesManager.preferredLocale =
                    locale?.language ?: LanguageConstants.DEFAULT_LOCALE
            }
        }
        if (sharedPreferencesManager.preferredLocale == null) {
            sharedPreferencesManager.preferredLocale = LanguageConstants.DEFAULT_LOCALE
        }
        val locale = Locale(sharedPreferencesManager.preferredLocale!!)
        // as part of creating a new context that contains the new locale we also need to override the default locale.
        Locale.setDefault(locale)
        // create new configuration with the saved locale
        val newConfig = context.resources.configuration
        newConfig.setLocale(locale)
        return context.createConfigurationContext(newConfig)
    }

    fun overrideLocale(context: Context) {

        val sharedPreferencesManager =
            SharedPreferencesModule.provideSharedPreferencesManager(EmployeeApplication.instance.applicationContext)

        if (sharedPreferencesManager.preferredLocale == null) {
            val configuration = context.resources.configuration
            val locales = ConfigurationCompat.getLocales(configuration)
            if (locales.size() > 0) {
                val locale = locales.get(0)
                sharedPreferencesManager.preferredLocale =
                    locale?.language ?: LanguageConstants.DEFAULT_LOCALE
            }
        }
        if (sharedPreferencesManager.preferredLocale == null) {
            sharedPreferencesManager.preferredLocale = LanguageConstants.DEFAULT_LOCALE
        }
        val savedLocale =
            Locale(sharedPreferencesManager.preferredLocale!!)

        // as part of creating a new context that contains the new locale we also need to override the default locale.
        Locale.setDefault(savedLocale)

        // create new configuration with the saved locale
        val newConfig = context.resources.configuration
        newConfig.setLocale(savedLocale)

        // override the locale on the given context (Activity, Fragment, etc...)
        context.resources.updateConfiguration(newConfig, context.resources.displayMetrics)

        // override the locale on the application context
        if (context != context.applicationContext) {
            context.applicationContext.resources.run {
                updateConfiguration(
                    newConfig,
                    displayMetrics
                )
            }
        }
    }
}
