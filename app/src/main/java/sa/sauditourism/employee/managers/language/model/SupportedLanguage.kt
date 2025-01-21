package sa.sauditourism.employee.managers.language.model

import sa.sauditourism.employee.models.TabScreenModel


data class SupportedLanguage(
    val code: String,
    val evn: String,
    val flag: String,
    val id: Long,
    val value: String,
    val tabs: List<TabScreenModel>
) {
//    constructor() : this("en", "prod", "US", 1, "English")

    override fun toString(): String {
        return value
    }
}
