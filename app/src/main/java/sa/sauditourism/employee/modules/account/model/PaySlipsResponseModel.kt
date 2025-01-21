package sa.sauditourism.employee.modules.account.model


import com.google.gson.annotations.SerializedName

data class PaySlipsResponseModel(
    @SerializedName("payslips")
    val payslips: List<Payslip>
)