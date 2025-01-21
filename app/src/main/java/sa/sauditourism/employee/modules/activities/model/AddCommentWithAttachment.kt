package sa.sauditourism.employee.modules.activities.model

data class AddCommentWithAttachment(
    val additionalComment : AdditionalComment,
    val public:Boolean = true,
    val temporaryAttachmentIds:List<String>
)

data class AdditionalComment(
    val  body:String
)