package sa.sauditourism.employee.modules.activities.model

import com.google.gson.annotations.SerializedName
import sa.sauditourism.employee.modules.services.model.form.response.Attachment

data class AddCommentResponse(

    @SerializedName("comment")
    val comment: RequestComment,
    @SerializedName("attachments")
    val attachments: List<Attachment>,

    )
