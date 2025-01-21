package sa.sauditourism.employee.modules.services.model.details


import com.google.gson.annotations.SerializedName
import com.google.gson.internal.LinkedTreeMap
import sa.sauditourism.employee.EmployeeApplication.Companion.sharedPreferencesManager
import sa.sauditourism.employee.components.DropDownModel
import sa.sauditourism.employee.constants.DateConstants
import sa.sauditourism.employee.modules.services.model.form.response.Attachment
import java.time.LocalDateTime
import java.time.OffsetDateTime
import java.time.ZoneId
import java.time.ZoneOffset
import java.time.ZonedDateTime
import java.time.format.DateTimeFormatter
import java.util.Calendar
import java.util.Locale

@Suppress("UNCHECKED_CAST")
data class Field(
    @SerializedName("component")
    val component: String?,
    @SerializedName("dropDownUserSelectedValue")
    val dropDownUserSelectedValue: Any,
    @SerializedName("id")
    val id: String,
    @SerializedName("isDropDownUserSearch")
    val isDropDownUserSearch: Boolean,
    @SerializedName("note")
    val note: String,
    @SerializedName("dropDownCount")
    val dropDownCount: Int,
    @SerializedName("required")
    val required: Boolean,
    @SerializedName("selectedValue")
    var selectedValue: Any?,
    @SerializedName("title")
    val title: String,
    @SerializedName("attachment")
    val attachment: List<Attachment>? = emptyList(),
    @SerializedName("attachmentCount")
    val attachmentCount: Int = 0
) {
    companion object {
        fun from(field: Field) = object {
            val selectedValue =
                if (field.isDropDownUserSearch) field.dropDownUserSelectedValue else field.selectedValue
            val map: LinkedTreeMap<String, String> = selectedValue as LinkedTreeMap<String, String>
            val id by map
            val label by map

            val key by map
            val displayName by map

            val data =
                DropDownModel(
                    stringId = if (!field.isDropDownUserSearch) id.toString() else key.toString(),
                    label =
                    if (!field.isDropDownUserSearch) label.toString() else displayName.toString()
                )
        }.data

        fun fromMultiSelections(field: Field) = object {
            val selectedValue =
                if (field.isDropDownUserSearch) field.dropDownUserSelectedValue else field.selectedValue
            val maps: List<LinkedTreeMap<String, Any>> =
                selectedValue as List<LinkedTreeMap<String, Any>>
            val data = arrayListOf<DropDownModel>().apply {
                maps.forEach { map ->
                    val key by map
                    val displayName by map

                    val id by map
                    val label by map

                    add(
                        DropDownModel(
                            stringId = if (!field.isDropDownUserSearch) id.toString() else key.toString(),
                            label =
                            if (!field.isDropDownUserSearch) label.toString() else displayName.toString()
                        )
                    )
                }
            }
        }.data

        fun fromMultiDropDowns(field: Field) = object {
            val map: LinkedTreeMap<String, Any> = field.selectedValue as LinkedTreeMap<String, Any>
            val id by map
            val label by map
            val child by map

            val data = listOf(
                DropDownModel(stringId = id.toString(), label = label.toString()),
                DropDownModel(
                    stringId = (child as LinkedTreeMap<*, *>)["id"].toString(),
                    label = (child as LinkedTreeMap<*, *>)["label"].toString()
                )
            )
        }.data

        fun fromDate(dateString: String) = object {
            var formatter: DateTimeFormatter =
                DateTimeFormatter.ofPattern("yyyy-MM-dd")

            val timeFormatter = DateTimeFormatter.ofPattern(
                "hh:mm a", Locale(
                    sharedPreferencesManager.preferredLocale ?: "en"
                )
            )
            val dateFormatter = DateTimeFormatter.ofPattern(
                "dd ${DateConstants.MMM} yyyy",
                Locale(sharedPreferencesManager.preferredLocale ?: "en")
            )

            val dateTime = ZonedDateTime.parse(dateString, formatter)
            val eventDateString = dateTime.format(dateFormatter)

            val formattedTime = dateTime.format(timeFormatter)
                .replace(" ", "\u00A0")

            var data: String = "$eventDateString $formattedTime"
        }.data


        fun fromDateTime(dateString: String) = object {
            var formatter: DateTimeFormatter =
                DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss[.SSS]Z")

            val timeFormatter = DateTimeFormatter.ofPattern(
                "hh:mm a", Locale(
                    sharedPreferencesManager.preferredLocale ?: "en"
                )
            )
            val dateFormatter = DateTimeFormatter.ofPattern(
                "dd ${DateConstants.MMM} yyyy",
                Locale(sharedPreferencesManager.preferredLocale ?: "en")
            )

            val dateTime = ZonedDateTime.parse(dateString, formatter)
            val eventDateString = dateTime.format(dateFormatter)

            val formattedTime = dateTime.format(timeFormatter)
                .replace(" ", "\u00A0")

            var data: String = "$eventDateString $formattedTime"
        }.data
    }
}