package sa.sauditourism.employee.modules.onboarding.model

import com.google.gson.annotations.SerializedName

data class OnBoarding(
    @SerializedName("version")
    val version: String,
    @SerializedName("list")
    val list: List<OnBoardingModel>,
)

data class OnBoardingModel(
    @SerializedName("description")
    val description: String,
    @SerializedName("imageUrl")
    val image: String,
    @SerializedName("title")
    val title: String
)
