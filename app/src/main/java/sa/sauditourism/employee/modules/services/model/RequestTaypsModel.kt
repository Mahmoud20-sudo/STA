package sa.sauditourism.employee.modules.services.model

data class RequestTypesModel(
    val requestsTypeList:MutableList<RequestsType> = mutableListOf(),
    val filterRequestsTypeList:MutableList<RequestsType> = mutableListOf(),
)
