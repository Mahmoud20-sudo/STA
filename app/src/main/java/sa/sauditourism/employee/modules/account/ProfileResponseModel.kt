package sa.sauditourism.employee.modules.account

import com.google.errorprone.annotations.Keep
import com.google.gson.annotations.SerializedName

@Keep
data class ProfileResponseModel(
    @SerializedName("profile") var profile: Profile? = Profile(),
    @SerializedName("smartPass") var smartPass: SmartPass? = SmartPass(),
    @SerializedName("entertainerEnrolled")
    var entertainerEnrolled: Boolean? = null,
) {

    data class SmartPass(

        @SerializedName("id") var id: String? = null

    )

    data class Profile(

        @SerializedName("userId") var userId: String? = null,
        @SerializedName("firstName") var firstName: String? = null,
        @SerializedName("lastName") var lastName: String? = null,
        @SerializedName("email") var email: Email? = Email(),
        @SerializedName("isMobileNumberVerified") val isMobileVerified: Boolean? = null,
        @SerializedName("countryCode") val countryCode: String? = null,
        @SerializedName("mobileNumber") val mobileNumber: String? = null,
        @SerializedName("halaYallaUserId") val halaYallaUserId: String? = null,
        @SerializedName("gender") val gender: String? = null,
    ) {
        data class Email(

            @SerializedName("value") var value: String? = null,
            @SerializedName("isVerified") var isVerified: Boolean? = null

        )
    }
}
