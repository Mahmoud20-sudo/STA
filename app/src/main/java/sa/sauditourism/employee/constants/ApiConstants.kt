package sa.sauditourism.employee.constants

object ApiConstants {
    const val REQUEST_TYPE_ID = "request_type_id"
    const val FIELD_TYPE_ID = "field_id"
    const val REQUEST_ID = "request_id"
    const val USER_ID = "user_id"
    const val MEETING_ROOM_ID = "meeting_room_id"
    const val ACT_EMAIL = "act_email"

    const val GET_SERVICES = "services/"
    const val GET_REQUEST_FROM = "services/request-types/{$REQUEST_TYPE_ID}/request-fields/"
    const val SUBMIT_FORM = "services/submit-form/"
    const val GET_DROP_DOWN_VALUES = "services/request-types/{$REQUEST_TYPE_ID}/request-fields/{$FIELD_TYPE_ID}/dropdown-values/"

    const val UPLOAD_ATTACHMENTS = "services/request-types/{$REQUEST_TYPE_ID}/add-attachments/"
    const val REQUESTS_TYPES = "services/{serviceId}/request-types/"
    const val MY_REQUESTS = "services/my-requests/"
    const val REQUESTS_TYPES_SEARCH = "request-types/search"
    const val ACCOUNT_DETAILS= "services/user-profile/"

    const val REQUEST_COMMENTS = "services/my-requests/{requestId}/comments/"
    const val ADD_COMMENT = "services/my-requests/{requestId}/create-comment/"
    const val ADD_COMMENT_WITH_ATTACHMENT = "services/my-requests/{requestId}/create-comment-with-attachment/"
    const val SEARCH_USER = "services/users-search/"
    const val SEARCH_PARTICIPANTS = "services/search-users/"
    const val ADD_USER = "services/my-requests/{${REQUEST_ID}}/participants-action/"
    const val REQUEST_DETAILS = "services/my-requests/{${REQUEST_ID}}"

    const val PAY_SLIPS = "services/payslips-data/{$USER_ID}/"

    const val SERVICES_FILTERS = "services/services-filters/"

    const val PARTICIPANTS_FILTERS = "services/my-requests/{$REQUEST_ID}/participants/"

    const val PARTICIPANT_ACTION = "services/my-requests/{$REQUEST_ID}/participants-action/"

    const val DELETE_PROFILE_PIC = "services/remove-profile-picture/{$USER_ID}/"

    const val UPDATE_PROFILE_PIC = "services/update-profile-picture/{$USER_ID}/"
    const val GET_ANNOUNCEMENT="services/my-day-announcements"

    const val MY_DAY_MEETINGS = "services/my-day-meetings"

    const val MY_DAY_ACTIONS = " my-day-actions"


    // for meeting rooms
    const val CHECK_AVAILABLE_MEETING_ROOMS = "meeting-rooms/available-meeting-rooms"
    const val BOOK_MEETING_ROOM = "meeting-rooms/book-meeting-room/{$MEETING_ROOM_ID}/"


}

