package sa.sauditourism.employee.managers.remoteconfig

import android.content.Context
import com.google.firebase.ktx.Firebase
import com.google.firebase.remoteconfig.FirebaseRemoteConfig
import com.google.firebase.remoteconfig.ktx.remoteConfig
import com.google.firebase.remoteconfig.ktx.remoteConfigSettings
import com.huawei.agconnect.remoteconfig.AGConnectConfig
import sa.sauditourism.employee.EmployeeApplication
import sa.sauditourism.employee.R
import sa.sauditourism.employee.extensions.isHmsAvailable

object RemoteConfigService {

    private val remoteConfig: FirebaseRemoteConfig by lazy { Firebase.remoteConfig }
    private val huaweiConfig: AGConnectConfig by lazy { AGConnectConfig.getInstance() }

    init {
        val configSettings = remoteConfigSettings {
            minimumFetchIntervalInSeconds = 0
        }
        remoteConfig.setConfigSettingsAsync(configSettings)
        remoteConfig.setDefaultsAsync(R.xml.remote_config_defaults)
    }

    var fetchRemoteConfigResult: ((Boolean) -> Unit)? = null

    fun fetchRemoteConfig(
        context: Context,

    ) {
        if (context.isHmsAvailable()) {
            huaweiConfig.fetch(0).addOnSuccessListener { configValues ->
                huaweiConfig.apply(configValues)
                fetchRemoteConfigResult?.invoke(true)
            }.addOnFailureListener {
                fetchRemoteConfigResult?.invoke(false)
            }
        } else {
            remoteConfig.fetchAndActivate()
                .addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        fetchRemoteConfigResult?.invoke(true)
                    } else {
                        fetchRemoteConfigResult?.invoke(false)
                    }
                }
        }
    }

    fun checkFeature(
        context: Context = EmployeeApplication.instance.applicationContext,
        feature: String,
        isChinaCountry: Boolean = false
    ): Boolean? {
        return if (context.isHmsAvailable() || isChinaCountry) {
            val exists = huaweiConfig.getValueAsString(feature)
            if (exists.isEmpty()) {
                null
            } else {
                huaweiConfig.getValueAsBoolean(feature)
            }
        } else {
            val exists = remoteConfig.getString(feature)
            if (exists.isEmpty()) {
                null
            } else {
                remoteConfig.getValue(feature).asBoolean()
            }
        }
    }

    fun checkFeatureAsString(
        context: Context = EmployeeApplication.instance.applicationContext,
        feature: String,
        isChinaCountry: Boolean = false
    ): String {
        return if (context.isHmsAvailable() || isChinaCountry) {
            huaweiConfig.getValueAsString(feature)
        } else {
            remoteConfig.getValue(feature).asString()
        }
    }
}
