package sa.sauditourism.employee.models

/**
 * used to fetch tabs from firebase
 */
data class TabScreenModel(
    val icon: String?,
    val id: String,
    val title: String
){
    constructor() : this("", "", "")
}
