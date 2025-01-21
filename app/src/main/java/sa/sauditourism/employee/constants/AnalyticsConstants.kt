package sa.sauditourism.employee.constants

/**
 * Add analytics constants, events and params name
 */
object AnalyticsConstants {

    const val NOT_FOUND = "n/a"
    const val TARGET_ENVIRONMENT = "target.environmentId"
    const val EVENT_CONST = "event"
    const val LANGUAGE_CONST = "language"
    const val ACTION_DATA_SINGLE_EVENT = "event102"
    const val ACTION_DATA_MULTIPLE_EVENTS = "event102,event103"
    const val ACTION_DATA_ENVIRONMENT_PREFIX = "VisitSaudi-"


    enum class AdobeAnalyticsKeys(val value: String) {
        pageName("&&screen.name"),
        siteSection("&&screen.section"),
        previousPage("&&screen.nameprevious"),
        currentTab("&&screen.tab"),
        previousTab("&&screen.tabprevious"),
        packageName("&&tour.packageName"),
        packageID("&&screen.page.packageID"),
        loginStatus("user.loginStatus"),
        ecid("user.ecid"),
        deviceType("&&user.deviceType"),
        customerID("user.id"),
        errorMessage("&&screen.errorMessage"),
        destinationName("&&tour.destinationName"),
        visitorLocalTime("&&screen.localTime"),
        language("language"),
        userAge("&&user.age"),
        userGender("&&user.gender"),
        userInterest("&&user.interest"),
        userTravelling("&&user.travelling"),
        notice("&&screen.notice"),
        eventName("&&tour.eventName"),
        tripName("&&tour.tripName"),
        hotelName("&&tour.hotelName"),
    }

    enum class AdobeTargetKeys(val value: String) {
        loginStatus("loginStatus"),
        events("&&events"),
        environment("environment"),
    }


    enum class AnalyticsKeys(val value: String) {
        user_id("user_id"),
        user_language("user_language"),
        visa_applied("visa_applied"),
        type_of_visa_applied("type_of_visa_applied"),
        event_tickets_bought("event_tickets_bought"),
        allow_location("allow_location"),
        events("events"),
        tours_booked("tours_booked"),
        tours("tours"),
        device_type("device_type"),
        selected_tab("selected_tab"),
        event_category("event_category")
    }

    enum class AnalyticsEventsKeys(val value: String) {
        TAB_BAR("tab_bar"),
        CTA_CLICK("cta_click"),
        LEARN_MORE("learn_more"),
        DESTINATION_CITY("destination_city"),
        EXPLORE("explore"),
        HEADER_NAVIGATION("header_navigation"),
        STORIES("stories"),
        VIEW_MORE("view_more"),
        VIEW_ALL("view_all"),
        ADD_TO_FAVORITE("add_to_favorite"),
        REMOVE_FROM_FAVORITE("remove_from_favorite"),
        BOOK_NOW("book_now"),
        GET_TICKETS("get_tickets"),
        SHOP_NOW("shop_now"),
        CARDS("cards"),
        EXPLORE_SAUDI_MAP("explore_saudi_map"),
        VISIT_WEBSITE("visit_website"),
        SEARCH("search"),
        NO_RESULT_FOUND("no_result_found"),
        DONE("done"),
        CLEAR_ALL("clear_all"),
        SUBMIT("submit"),
        GET_DIRECTION("get_direction"),
        SHOW_ON_SAUDI_MAP("show_on_saudi_map"),
        OPEN_MENU("open_menu"),
        GET_THIS_DEAL("get_this_deal"),
        TICKETS("tickets"),
        OFFER_HISTORY("offer_history"),
        BROWSE_YOUR_CATEGORY("browse_your_category"),
        EXPLORE_NOW("explore_now"),
        ALLOW_LOCATION_ACCESS("allow_location_access"),
        ALLOW_LOCATION("allow_location"),
        APPLY_NOW("apply_now"),
        SEARCH_FOR_EMBASSY("search_for_embassy"),
        SEARCH_FOR_OFFICE("search_for_office"),
        PLANNER_TAB("planner_tab"),
        EXCHANGE_RATE("exchange_rate"),
        FIND_ON_GOOGLE_MAP("find_on_google_map"),
        CALL_US("call_us"),
        TRANSPORT_TYPE("transport_type"),
        SHARE("share"),
        LISTEN("listen"),
        DISCOVER_NUSUK_WEBSITE("discover_nusuk_website"),
        APPLY_FILTER("apply_filter"),
        SAVE("save"),
        RELOAD("reload"),
        UPDATE_NOW("update_now"),
        TRY_AGAIN("try_again"),
        RETRY("retry"),
        NOTIFY_ME("notify_me"),
        ERROR("error"),
        JOIN_VISIT_SAUDI("join_visit_saudi"),
        HELP_SUPPORT("help_support"),
        EDIT_YOUR_PROFILE("edit_your_profile"),
        SAUDI_SMART_PASS("saudi_smart_pass"),
        SIGN_OUT("sign_out"),
        DELETE_ACCOUNT("delete_account"),
        CONTACT_US("contact_us"),
        SELECT_SETTINGS("select_settings"),
        REDEEM("redeem"),
        SCAN_QR("scan_qr"),
        READ_MORE("read_more"),
        SCREEN_VIEW("screen_view");
    }


    object EventParams {
        const val SCREEN_CUSTOM = "screen_custom"
        const val USER_ID = "user_id"
        const val VISA_APPLIED = "visa_applied"
        const val TYPE_OF_VISA_APPLIED = "type_of_visa_applied"
        const val TICKETS_BOUGHT = "tickets_bought"
        const val ALLOW_LOCATION = "allow_location"
        const val USER_LANGUAGE = "user_language"
        const val DEVICE_TYPE = "device_type"
        const val CARD_NAME = "card_name"
        const val LOCATION = "location"
        const val CURRENCY = "currency"
        const val PRICE = "price"
        const val CATEGORY = "category"
        const val SECTION_NAME = "section_name"
        const val EVENT_CATEGORY = "event_category"
        const val SEARCH_RESULT_NUMBER = "search_result_number"
        const val SERVICES_TERM = "services_term"
        const val FILTERED_TERM = "filtered_term"
        const val SELECTED_TAB = "selected_tab"
        const val NAVIGATION = "navigation"
        const val CATEGORY_2 = "category_2"
        const val SECTION_NAME_2 = "section_name_2"
        const val NAVIGATION_2 = "navigation_2"
        const val CURRENCY_2 = "currency_2"
        const val PRICE_2 = "price_2"
        const val LOCATION_2 = "location_2"
        const val SEARCH_RESULT_NUMBER_2 = "search_result_number_2"
        const val FILTERED_TERM_2 = "filtered_term_2"
        const val TICKET = "ticket"
        const val SELECTED_CATEGORY = "selected_category"
        const val CALL_TYPE = "call_type"
        const val TRANSPORT_TYPE = "transport_type"
        const val HOW_TO_SAY = "how_to_say"
        const val SOUNDS_LIKE = "sounds_like"
        const val CATEGORIES = "categories"
        const val LOCATIONS = "locations"
        const val STARS = "stars"
        const val PRICE_RANGE = "price_range"
        const val DATE = "date"
        const val ADMISSION = "admission"
        const val SEASON_FESTIVALS = "season_festivals"
        const val ZONES = "zones"
        const val SAVE_TERM = "save_term"
        const val ERROR_TYPE = "error_type"
        const val PERMISSION = "permission"
        const val LANGUAGE = "language"
        const val SELECTED_SETTING = "selected_setting"
        const val CTA_NAME = "cta_name"
        const val SCREEN_CLASS = "screen_class"
        const val SCREEN_NAME = "screen_name"
        const val SUB_SCREEN_NAME = "sub_screen_name"

    }

    enum class ScreenAnalyticsKeys(val value: String) {
        TAB_SCREEN("tab_screen"),
        DETAILS_SCREEN("details_screen"),
        VIEW_ALL_SCREEN("view_all_screen"),
        WEB_VIEW_SCREEN("web_view_screen"),
        SPLASH_SCREEN("splash_screen"),
    }
}
