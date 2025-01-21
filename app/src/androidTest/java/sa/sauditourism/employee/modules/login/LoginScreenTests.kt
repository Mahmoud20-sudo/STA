package sa.sauditourism.employee.modules.login

import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.assertIsNotDisplayed
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.navigation.compose.rememberNavController
import org.junit.Rule
import org.junit.Test
import sa.sauditourism.employee.constants.LanguageConstants.CHANGE_LANGUAGE_TITLE
import sa.sauditourism.employee.constants.LanguageConstants.LOGIN_BUTTON_TITLE
import sa.sauditourism.employee.constants.LanguageConstants.LOGIN_CHANGE_PASSWORD_TITLE
import sa.sauditourism.employee.constants.TestTagsConstants.SELECT_LOCAL_TAG
import sa.sauditourism.employee.extensions.localizeString

class LoginScreenTests {

    @get:Rule
    val composeTestRule = createComposeRule()

    @Test
    fun login_view_loading_ui_test() {
        composeTestRule.setContent {
            val navController = rememberNavController()
            LoginScreen(navController, true) {

            }
        }

        composeTestRule.onNodeWithText(LOGIN_BUTTON_TITLE.localizeString()).assertIsNotDisplayed()
        composeTestRule.onNodeWithText(LOGIN_CHANGE_PASSWORD_TITLE.localizeString()).assertIsDisplayed()
    }

    @Test
    fun login_view_finished_loading_ui_test() {
        composeTestRule.setContent {
            val navController = rememberNavController()
            LoginScreen(navController, false) {

            }
        }

        composeTestRule.onNodeWithText(LOGIN_BUTTON_TITLE.localizeString()).assertIsDisplayed()
        composeTestRule.onNodeWithText(LOGIN_CHANGE_PASSWORD_TITLE.localizeString()).assertIsDisplayed()
    }

    @Test
    fun login_view_showing_bottomsheet_ui_test() {
        composeTestRule.setContent {
            val navController = rememberNavController()
            LoginScreen(navController, false) {

            }
        }

        composeTestRule.onNodeWithTag(SELECT_LOCAL_TAG).performClick()
        composeTestRule.onNodeWithText(CHANGE_LANGUAGE_TITLE.localizeString()).assertIsDisplayed()

    }
}