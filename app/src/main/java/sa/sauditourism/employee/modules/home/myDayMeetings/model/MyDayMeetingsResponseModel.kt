package sa.sauditourism.employee.modules.home.myDayMeetings.model


import com.google.gson.annotations.SerializedName

data class MyDayMeetingsResponseModel(
    @SerializedName("meetings")
    val meetings: List<Meeting?>?,
) {
    data class Meeting(
        @SerializedName("subject")
        val subject: String?,
        @SerializedName("start")
        val start: String?,
        @SerializedName("end")
        val end: String?,
        @SerializedName("organizer")
        val organizer: Organizer?,
        @SerializedName("location")
        val location: Any?,
    ) {
        data class Organizer(
            @SerializedName("name")
            val name: String?,
            @SerializedName("email")
            val email: String?,
        )
    }
}