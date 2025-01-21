package sa.sauditourism.employee.modules.account.model


import com.google.gson.annotations.SerializedName
import java.time.format.DateTimeFormatter
import java.util.Locale

data class Payslip(
    @SerializedName("date")
    val date: String = "",
    @SerializedName("download_url")
    val downloadUrl: String? = null,
    @SerializedName("human_readable_date")
    val humanReadableDate: String = "",
    @SerializedName("id")
    val id: Long = 0L
)