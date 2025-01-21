package sa.sauditourism.employee.modules.services.model


import com.google.gson.annotations.SerializedName

data class ServicesResponseModel(
    @SerializedName("services")
    val servicesLList: List<ServicesModel>
)