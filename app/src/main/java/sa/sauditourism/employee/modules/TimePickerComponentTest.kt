package sa.sauditourism.employee.modules

import androidx.compose.runtime.Composable
import androidx.navigation.NavController
import sa.sauditourism.employee.components.TimePickerComponent

@Composable
fun TimePickerComponentTest(navController: NavController) {
   TimePickerComponent(onConfirmRequest = { time->
      navController.popBackStack()
   }, onDismissRequest = {
      navController.popBackStack()
   })

}