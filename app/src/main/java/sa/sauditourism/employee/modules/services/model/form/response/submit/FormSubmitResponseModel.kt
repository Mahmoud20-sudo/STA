package sa.sauditourism.employee.modules.services.model.form.response.submit


import com.google.gson.annotations.SerializedName

data class FormSubmitResponseModel(
    @SerializedName("customerRequest")
    val customerRequest: CustomerRequest = CustomerRequest(),
    @SerializedName("errors")
    val errors: List<Error>? = emptyList()
)

data class Error(
    val errorMessage: String?
)
