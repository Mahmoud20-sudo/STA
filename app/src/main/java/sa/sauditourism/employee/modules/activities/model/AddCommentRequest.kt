package sa.sauditourism.employee.modules.activities.model

data class AddCommentRequest(
    val body:String,
    val public:Boolean = true
)
