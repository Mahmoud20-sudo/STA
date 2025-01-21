package sa.sauditourism.employee.modules

import androidx.compose.foundation.layout.Column
import androidx.compose.runtime.Composable
import sa.sauditourism.employee.components.CheckBoxComponent
import sa.sauditourism.employee.components.FilterCheckboxState

@Composable
fun CheckboxTest() {
   Column {
      CheckBoxComponent(onCheckedChange = {})
      CheckBoxComponent(title = "Checkbox", onCheckedChange = {})
      CheckBoxComponent(title = "Checkbox",
         subtitle = "Here goes a supporting text for the checkbox",
         onCheckedChange = {})

      CheckBoxComponent(onCheckedChange = {}, checked = FilterCheckboxState.Checked)
      CheckBoxComponent(
         title = "Checkbox",
         onCheckedChange = {},
         checked = FilterCheckboxState.Checked
      )
      CheckBoxComponent(
         title = "Checkbox",
         subtitle = "Here goes a supporting text for the checkbox",
         onCheckedChange = {}, checked = FilterCheckboxState.Checked
      )
      CheckBoxComponent(enabled = false, onCheckedChange = {})
      CheckBoxComponent(
         enabled = false,
         title = "Checkbox",
         onCheckedChange = {},
         checked = FilterCheckboxState.Checked
      )
      CheckBoxComponent(
         title = "Checkbox",
         subtitle = "Here goes a supporting text for the checkbox",
         onCheckedChange = {}, checked = FilterCheckboxState.Unchecked, enabled = false
      )
   }
}