package sa.sauditourism.employee.modules.login.authintication

import android.net.Uri
import androidx.core.app.ActivityCompat
import sa.sauditourism.employee.EmployeeApplication
import sa.sauditourism.employee.EmployeeApplication.Companion.sharedPreferencesManager
import sa.sauditourism.employee.managers.environment.EnvironmentKeys
import sa.sauditourism.employee.managers.environment.EnvironmentKeys.OPEN_AUTH_CALLBACK_URL
import net.openid.appauth.AuthorizationRequest
import net.openid.appauth.AuthorizationService
import net.openid.appauth.AuthorizationServiceConfiguration
import net.openid.appauth.ResponseTypeValues

object AuthenticationManager {
    //REQUEST CODES
    const val AUTH_REQ = 20
    const val PROFILE_CODE = 30

    private var profileScope = EmployeeApplication.instance.environmentManager.getVariable(
        EnvironmentKeys.OPEN_AUTH_SCOPE
    )
    private fun getScopes(): Set<String> {
        val stringSet: LinkedHashSet<String> = LinkedHashSet(2)
        stringSet.add(profileScope)
        return stringSet
    }

    //Provides AuthServiceConfig (AUTH-TOKEN) Endpoints
    private fun getAuthServiceConfig(): AuthorizationServiceConfiguration {
        return AuthorizationServiceConfiguration(
            Uri.parse(EmployeeApplication.instance.environmentManager.getVariable(
                    EnvironmentKeys.OPEN_AUTH_URL
                )
            ),
            Uri.parse( EmployeeApplication.instance.environmentManager.getVariable(
                    EnvironmentKeys.OPEN_AUTH_TOKEN_URL
                )
            )
        )
    }

    //Provides Additional Params
    private fun getAdditionalParams(): HashMap<String, String> {
        val additionalParams = HashMap<String, String>()
        additionalParams["token_content_type"] = "jwt"
        return additionalParams
    }

    private fun doAuthorize() {
        val locale = sharedPreferencesManager.preferredLocale ?: "en"
        //Authorize
        val authRequestBuilder = AuthorizationRequest.Builder(
            getAuthServiceConfig(),//AuthConfig
            EmployeeApplication.instance.environmentManager.getVariable(
                EnvironmentKeys.OPEN_AUTH_CLIENT_ID
            ),//ClientID
            ResponseTypeValues.CODE,//Request Type
            Uri.parse(
                EmployeeApplication.instance.environmentManager.getVariable(
                    OPEN_AUTH_CALLBACK_URL
                )
            )
        ).setScopes(getScopes())//SCOPES
            .setAdditionalParameters(getAdditionalParams())//Additional Params
            .build()
        val authIntent =
            AuthorizationService(EmployeeApplication.instance).getAuthorizationRequestIntent(
                authRequestBuilder
            )//Auth Intent
        //Send Results to Base Activity
        ActivityCompat.startActivityForResult(
            EmployeeApplication.instance.currentActivity!!,
            authIntent,
            AUTH_REQ,
            authIntent.extras
        )
    }

    //Open The AUTH Authorizer
    fun openAuthWeb() {
//        val analyticsData = HashMap<String, String>()
//        analyticsData[AnalyticsConstants.EventParams.EVENT_CATEGORY] = "Account"
//        analyticsData[AnalyticsConstants.EventParams.SCREEN_CUSTOM] = "Account"
//
//        AnalyticsManager.trackEvent(
//            AnalyticsConstants.AnalyticsEventsKeys.JOIN_VISIT_SAUDI.value,
//            analyticsData
//        )
        doAuthorize()
    }

}
