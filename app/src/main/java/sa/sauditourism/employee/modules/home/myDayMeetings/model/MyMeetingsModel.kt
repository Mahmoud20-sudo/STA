package sa.sauditourism.employee.modules.home.myDayMeetings.model

data class MyMeetingsModel(
    val month: String = "",
    val day: String = "",
    val meetingsList: List<MeetingItem> = emptyList(),
)

data class MeetingItem(
    val title: String = "",
    val time: String = "",
)