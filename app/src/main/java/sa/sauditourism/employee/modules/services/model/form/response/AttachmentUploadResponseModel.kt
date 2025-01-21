package sa.sauditourism.employee.modules.services.model.form.response

import android.net.Uri
import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize
import kotlinx.serialization.KSerializer
import kotlinx.serialization.Serializable
import kotlinx.serialization.descriptors.PrimitiveKind
import kotlinx.serialization.descriptors.PrimitiveSerialDescriptor
import kotlinx.serialization.descriptors.SerialDescriptor
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.encoding.Encoder

data class AttachmentUploadResponseModel(
    @SerializedName("attachments")
    val attachments: MutableList<Attachment> = mutableListOf()
)

@Serializable
@Parcelize
data class Attachment(
    @Serializable(with = UriSerializer::class)
    var attachmentUri: Uri = Uri.EMPTY,
    @SerializedName("temporaryAttachmentId")
    val attachmentId: String = "",
    @SerializedName("fileName")
    val fileName: String = "",
    @SerializedName("content")
    val content: String,
    @SerializedName("date")
    val date: String,
    @SerializedName("id")
    val id: String,
    @SerializedName("mimeType")
    val mimeType: String,
    @SerializedName("size")
    val size: Int,
    @SerializedName("time")
    val time: String,
    @SerializedName("thumbnail")
    val thumbnail: String
): Parcelable


object UriSerializer : KSerializer<Uri> {
    override val descriptor: SerialDescriptor =
        PrimitiveSerialDescriptor("Uri", PrimitiveKind.STRING)

    override fun serialize(encoder: Encoder, value: Uri) {
        encoder.encodeString(value.toString())
    }

    override fun deserialize(decoder: Decoder): Uri {
        return Uri.parse(decoder.decodeString())
    }
}