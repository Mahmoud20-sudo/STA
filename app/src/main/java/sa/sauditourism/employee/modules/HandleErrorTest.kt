package sa.sauditourism.employee.modules

import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.NavHostController
import sa.sauditourism.employee.di.ManagersModule
import sa.sauditourism.employee.managers.errorhandeling.ErrorHandlingManager
import sa.sauditourism.employee.managers.network.helpers.ErrorCodes
import sa.sauditourism.employee.managers.network.helpers.NetworkError

@Preview
@Composable
fun HandleErrorTest(navController: NavHostController) {

    val errorHandlingManager: ErrorHandlingManager = ManagersModule.provideErrorHandlingManager()

    errorHandlingManager.HandleError(
        error = NetworkError(
            errorCode = ErrorCodes.NO_CONNECTION,
            code = 401,
            message = "Message",
            apiNumber = null,
            exception = null
        ),
        isDataEmpty = true,
        showBack = true,
        withTab = false,
        dismissCallback = {
            navController.popBackStack()
        },
        callback = {
            navController.popBackStack()
        }

    )
}