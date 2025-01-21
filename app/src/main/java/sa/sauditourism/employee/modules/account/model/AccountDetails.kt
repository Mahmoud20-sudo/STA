package sa.sauditourism.employee.modules.account.model

import android.os.Parcelable
import com.google.firebase.encoders.annotations.Encodable.Ignore
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize
import kotlin.io.encoding.Base64
import kotlin.io.encoding.ExperimentalEncodingApi

@OptIn(ExperimentalEncodingApi::class)
@Parcelize
data class AccountDetails(
    @SerializedName("id")
    val id: String? = null,
    @SerializedName("name")
    val name: String? = null,
    @SerializedName("image")
    var image: String? = null,
    @SerializedName("email")
    val email: String? = null,
    @SerializedName("phone")
    val phone: String? = null,
    @SerializedName("jobTitle")
    val jobTitle: String? = null,
    @SerializedName("department")
    val department: String? = null,
    @SerializedName("gender")
    val gender: String? = null,
    @SerializedName("personNumber")
    val personNumber: String? = null,
    @SerializedName("digitalCardUrl")
    val digitalCardUrl: String? = null,
    @SerializedName("firstName")
    val firstName: String? = null
) : Parcelable {

    val _delegatedImage: ByteArray?
        get() {
            val base64Image: String? =
                kotlin.runCatching { image?.split(",")?.get(1) }.getOrElse { image }
            return base64Image?.let { Base64.decode(it, android.util.Base64.DEFAULT) }
        }
}