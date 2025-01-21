package sa.sauditourism.employee.modules.home.model

import kotlin.io.encoding.Base64
import kotlin.io.encoding.ExperimentalEncodingApi

data class UserData(
    val name:String?="",
    val image:String?=""


){
    @OptIn(ExperimentalEncodingApi::class)
    val _delegatedImage: ByteArray? get() {
        val base64Image: String? = kotlin.runCatching { image?.split(",")?.get(1) }.getOrElse { image }
        return base64Image?.let { Base64.decode(it, android.util.Base64.DEFAULT) }
    }
}
