package sa.sauditourism.employee.models

import sa.sauditourism.employee.components.DialogType

data class AlertModel(
    val type: DialogType,
    val title: String? = null,
    val message: String? = null,
    val positiveButton: String? = null,
    val negativeButton: String? = null,
    val cancelable: Boolean = false,
    val onPositiveClick: () -> Unit = {},
    val onNegativeClick: () -> Unit = {},
)
