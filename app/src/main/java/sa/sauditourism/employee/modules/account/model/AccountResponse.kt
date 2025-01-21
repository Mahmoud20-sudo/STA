package sa.sauditourism.employee.modules.account.model

import com.google.gson.annotations.SerializedName

data class AccountResponse(
    @SerializedName("user")
    val accountItem: AccountDetails
)