package sa.sauditourism.employee.modules.maintenance

import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.assertIsNotDisplayed
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithTag
import androidx.navigation.compose.rememberNavController
import org.junit.Rule
import org.junit.Test
import sa.sauditourism.employee.constants.TestTagsConstants.BACK_BUTTON_TAG

class MaintenanceScreenTests {

    @get:Rule
    val composeTestRule = createComposeRule()

    @Test
    fun maintenance_view_with_back_button_ui_test() {
        composeTestRule.setContent {
            val navController = rememberNavController()
            MaintenancePage(navController)
        }

        composeTestRule.onNodeWithTag(BACK_BUTTON_TAG).assertIsDisplayed()
    }

    @Test
    fun maintenance_view_without_back_button_ui_test() {
        composeTestRule.setContent {
            val navController = rememberNavController()
            MaintenancePage(navController, shouldIncludeBackButton = false)
        }

        composeTestRule.onNodeWithTag(BACK_BUTTON_TAG).assertIsNotDisplayed()
    }
}