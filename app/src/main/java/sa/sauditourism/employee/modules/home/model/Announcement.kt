package sa.sauditourism.employee.modules.home.model

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize
import kotlinx.serialization.Serializable

@Serializable
@Parcelize
data class AnnouncementResponse(


    @SerializedName("announcements")
    val announcements: List<Announcement> = emptyList()
): Parcelable

@Serializable
@Parcelize
data class Announcement(
    @SerializedName("subject")
    val subject: String="",

    @SerializedName("sender")
    val sender: Sender= Sender("",""),

    @SerializedName("time")
    val time: String="",

    @SerializedName("banner_url")
    val bannerUrl: String="",

    @SerializedName("attachments")
    val attachments:List<Attachment> = emptyList()
): Parcelable

@Serializable
@Parcelize
data class Sender(
    @SerializedName("name")
    val name: String="",
    @SerializedName("email")
    val email: String=""
): Parcelable

@Serializable
@Parcelize
data class Attachment(
    @SerializedName("name")
    val name: String="",
    @SerializedName("url")
    val url: String="",
    @SerializedName("mime_type")
    val mimeType: String="",
    @SerializedName("id")
    val id: String="",
): Parcelable