package sa.sauditourism.employee

import android.app.Activity
import android.app.Application
import android.os.Bundle
import com.google.firebase.FirebaseApp
import com.google.firebase.crashlytics.FirebaseCrashlytics
import com.google.firebase.crashlytics.ktx.crashlytics
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import sa.sauditourism.employee.managers.environment.EnvironmentManager
import sa.sauditourism.employee.managers.sharedprefrences.SharedPreferencesManager
import dagger.hilt.android.HiltAndroidApp
import timber.log.Timber

@HiltAndroidApp
class EmployeeApplication : Application() {

    companion object {
        lateinit var instance: EmployeeApplication
        lateinit var sharedPreferencesManager: SharedPreferencesManager
    }

    var currentActivity: Activity? = null

    lateinit var environmentManager: EnvironmentManager

    private lateinit var firebaseCrashlytics: FirebaseCrashlytics

    override fun onCreate() {
        super.onCreate()

        instance = this

        FirebaseApp.initializeApp(this)
        Firebase.database.setPersistenceEnabled(true)

        // crashlytics
        firebaseCrashlytics = Firebase.crashlytics
        firebaseCrashlytics.isCrashlyticsCollectionEnabled = true

        sharedPreferencesManager = SharedPreferencesManager(applicationContext)

        environmentManager = EnvironmentManager(
            applicationContext,
            sharedPreferencesManager,
        )

        plantTimberTrees()

        // fullstory
//        FS.setReadyListener(FSSessionReadyListener())

        registerActivityLifecycleCallbacks(object : ActivityLifecycleCallbacks {
            override fun onActivityCreated(activity: Activity, savedInstanceState: Bundle?) {
                currentActivity = activity
            }

            override fun onActivityStarted(activity: Activity) {
                currentActivity = activity
            }

            override fun onActivityResumed(activity: Activity) {
                currentActivity = activity
            }

            override fun onActivityPaused(activity: Activity) {
            }

            override fun onActivityStopped(activity: Activity) {
            }

            override fun onActivitySaveInstanceState(activity: Activity, outState: Bundle) {
            }

            override fun onActivityDestroyed(activity: Activity) {
            }
        })
    }

    private fun plantTimberTrees() {
        if (BuildConfig.DEBUG) {
            Timber.plant(Timber.DebugTree())
        }
    }
}

