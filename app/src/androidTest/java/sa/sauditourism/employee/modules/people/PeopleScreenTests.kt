package sa.sauditourism.employee.modules.people

import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithText
import androidx.navigation.compose.rememberNavController
import org.junit.Rule
import org.junit.Test
import sa.sauditourism.employee.constants.LanguageConstants
import sa.sauditourism.employee.extensions.localizeString

class PeopleScreenTests {

    @get:Rule
    val composeTestRule = createComposeRule()

    @Test
    fun people_view_with_back_button_ui_test() {
        composeTestRule.setContent {
            val navController = rememberNavController()
            PeopleScreen(navController)
        }

        composeTestRule.onNodeWithText(LanguageConstants.PEOPLE_TAB.localizeString()).assertIsDisplayed()
        composeTestRule.onNodeWithText(LanguageConstants.COMING_SOON.localizeString()).assertIsDisplayed()
    }
}