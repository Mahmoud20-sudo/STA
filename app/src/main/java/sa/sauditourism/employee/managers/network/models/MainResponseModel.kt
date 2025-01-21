package sa.sauditourism.employee.managers.network.models

data class MainResponseModel<T>(
    val data: T?,
    val code: Int?,
    val message: String?,
)