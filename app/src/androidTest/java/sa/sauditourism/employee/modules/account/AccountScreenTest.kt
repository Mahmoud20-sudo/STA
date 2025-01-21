package sa.sauditourism.employee.modules.account

import android.annotation.SuppressLint
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.navigation.NavController
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mockito
import org.mockito.Mockito.mock
import org.mockito.Mockito.verify
import org.mockito.junit.MockitoJUnitRunner
import sa.sauditourism.employee.BuildConfig
import sa.sauditourism.employee.constants.LanguageConstants
import sa.sauditourism.employee.constants.TestTagsConstants.ACCOUNT_SIGN_OUT_TAG
import sa.sauditourism.employee.constants.TestTagsConstants.PRIMARY_BUTTON
import sa.sauditourism.employee.extensions.localizeString
import sa.sauditourism.employee.modules.account.model.AccountDetails

@RunWith(MockitoJUnitRunner::class)
class AccountScreenTest {

    @get:Rule
    val composeTestRule = createComposeRule()

    @SuppressLint("CheckResult")
    @Test
    fun accountScreen_displaysUiElements() {
        val navController = mock(NavController::class.java)
        val onLogout = Mockito.mock<() -> Unit>()
        val onDigitalCardClick = Mockito.mock<(AccountDetails) -> Unit>()
        val onSettingsClick = Mockito.mock<() -> Unit>()

        // Set content
        composeTestRule.setContent {
            AccountScreen(
                onLogout = onLogout,
                onSettingsClick = onSettingsClick,
                onDigitalCardClick = onDigitalCardClick,
                onMyRequestClick = {},
            )
        }

        // Assertions
        composeTestRule.onNodeWithText(LanguageConstants.ACCOUNT_TAB.localizeString())
            .assertIsDisplayed()
        composeTestRule.onNodeWithText("User Name").assertIsDisplayed()
        composeTestRule.onNodeWithText(LanguageConstants.ACCOUNT_ID.localizeString() + "12345")
            .assertIsDisplayed()
        composeTestRule.onNodeWithText(LanguageConstants.ACCOUNT_SHOW_MY_BUSINESS_CARD.localizeString())
            .assertIsDisplayed()
        composeTestRule.onNodeWithText(LanguageConstants.ACCOUNT_SETTINGS.localizeString())
            .assertIsDisplayed()
        composeTestRule.onNodeWithText(LanguageConstants.ACCOUNT_SIGN_OUT.localizeString())
            .assertIsDisplayed()
        composeTestRule.onNodeWithText(LanguageConstants.ACCOUNT_VERSION.localizeString(BuildConfig.VERSION_NAME))
            .assertIsDisplayed()
    }

    @SuppressLint("CheckResult")
    @Test
    fun accountScreen_settings_callsSettingsScreen() {
        // Start the app
        // Mock dependencies
        val navController = mock(NavController::class.java)
        val onLogout = Mockito.mock<() -> Unit>()
        val onDigitalCardClick = Mockito.mock<(AccountDetails) -> Unit>()
        val onSettingsClick = Mockito.mock<() -> Unit>()

        // Set content
        composeTestRule.setContent {
            AccountScreen(
                onLogout = onLogout,
                onSettingsClick = onSettingsClick,
                onDigitalCardClick = onDigitalCardClick,
                onMyRequestClick = {},
            )
        }

        // Trigger logout action
        composeTestRule.onNodeWithText(LanguageConstants.ACCOUNT_SETTINGS.localizeString()).performClick()
        // Assuming the dialog has a button with the text "Sign Out"
        // Verify that onLogout was called
        verify(onSettingsClick).invoke()
    }

    @SuppressLint("CheckResult")
    @Test
    fun accountScreen_logout_callsOnLogout() {
        // Mock dependencies
        val navController = mock(NavController::class.java)
        val onLogout = Mockito.mock<() -> Unit>()
        val onDigitalCardClick = Mockito.mock<(AccountDetails) -> Unit>()
        val onSettingsClick = Mockito.mock<() -> Unit>()

        // Set content
        composeTestRule.setContent {
            AccountScreen(
                onLogout = onLogout,
                onSettingsClick = onSettingsClick,
                onDigitalCardClick = onDigitalCardClick,
                onMyRequestClick = {},
            )
        }

        // Trigger logout action
        composeTestRule.onNodeWithTag(ACCOUNT_SIGN_OUT_TAG).performClick()
        // Assuming the dialog has a button with the text "Sign Out"
        composeTestRule.onNodeWithTag(PRIMARY_BUTTON).performClick()
        // Verify that onLogout was called
        verify(onLogout).invoke()
    }

    @SuppressLint("CheckResult")
    @Test
    fun accountScreen_digital_callsDigitalCard() {
        // Mock dependencies
        val navController = mock(NavController::class.java)
        val onLogout = Mockito.mock<() -> Unit>()
        val onDigitalCardClick = Mockito.mock<(AccountDetails) -> Unit>()
        val onSettingsClick = Mockito.mock<() -> Unit>()

        // Set content
        composeTestRule.setContent {
            AccountScreen(
                onLogout = onLogout,
                onSettingsClick = onSettingsClick,
                onDigitalCardClick = onDigitalCardClick,
                onMyRequestClick = {},
            )
        }

        // Trigger logout action
        composeTestRule.onNodeWithText(LanguageConstants.ACCOUNT_SHOW_MY_BUSINESS_CARD.localizeString()).performClick()
        // Assuming the dialog has a button with the text "Sign Out"
        // Verify that onLogout was called
        verify(onDigitalCardClick).invoke(AccountDetails())
    }
}
