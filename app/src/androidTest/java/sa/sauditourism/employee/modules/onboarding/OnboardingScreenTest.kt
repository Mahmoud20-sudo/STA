package sa.sauditourism.employee.modules.onboarding

import android.annotation.SuppressLint
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.ComposeContentTestRule
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.onRoot
import androidx.compose.ui.test.performClick
import androidx.compose.ui.test.performGesture
import androidx.compose.ui.test.performTouchInput
import androidx.test.espresso.action.ViewActions.slowSwipeLeft
import androidx.test.espresso.action.ViewActions.swipeLeft
import androidx.test.espresso.action.ViewActions.swipeRight
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.Mockito.mock
import org.mockito.Mockito.verify
import org.mockito.MockitoAnnotations
import org.mockito.junit.MockitoJUnitRunner
import sa.sauditourism.employee.constants.LanguageConstants
import sa.sauditourism.employee.extensions.localizeString
import sa.sauditourism.employee.modules.onboarding.model.OnBoarding
import sa.sauditourism.employee.modules.onboarding.model.OnBoardingModel
import sa.sauditourism.employee.resources.theme.EmployeeTheme
import java.util.Timer
import kotlin.concurrent.schedule

@RunWith(MockitoJUnitRunner::class)
class OnboardingScreenTest {

    @get:Rule
    val composeTestRule = createComposeRule()

    @SuppressLint("CheckResult")
    @Test
    fun myTest() {
        // Start the app
        val onBoardingModel = OnBoarding(
            version = "1.0",
            list = listOf(
                OnBoardingModel(
                    title = "Title 1",
                    description = "Description 1",
                    image = "https://www.google.com/logos/doodles/2024/rise-of-the-half-moon-6753651837110583.5-s.png" // Replace with actual image resource ID
                ),
                OnBoardingModel(
                    title = "Title 2",
                    description = "Description 2",
                    image = "https://www.google.com/logos/doodles/2024/rise-of-the-half-moon-6753651837110583.5-s.png" // Replace with actual image resource ID
                )
            )
        )

        val onOnboardingFinished = Mockito.mock<(String?) -> Unit>()

        // Set content
        composeTestRule.setContent {
            OnboardingScreen(onBoarding = onBoardingModel, onOnboardingFinished = onOnboardingFinished)
        }

        // Assertions
        composeTestRule.onNodeWithText("Title 1").assertIsDisplayed()
        composeTestRule.onNodeWithText("Description 1").assertIsDisplayed()
        composeTestRule.onNodeWithText(LanguageConstants.SKIP.localizeString()).assertIsDisplayed()
        composeTestRule.onNodeWithText(LanguageConstants.NEXT.localizeString()).assertIsDisplayed()

        // Simulate swipe to next page (implementation detail: you might need to adjust this based on how swiping is handled in your actual UI)
        // For example, if swiping is triggered by performing a specific gesture:
        composeTestRule.onNodeWithText(LanguageConstants.NEXT.localizeString()).performClick()

        // Assertions for the second page
        composeTestRule.onNodeWithText("Title 2").assertIsDisplayed()
        composeTestRule.onNodeWithText("Description 2").assertIsDisplayed()

        composeTestRule.onNodeWithText(LanguageConstants.BACK.localizeString()).assertIsDisplayed()
        composeTestRule.onNodeWithText(LanguageConstants.DONE_BUTTON_TITLE.localizeString()).assertIsDisplayed()

        // Simulate clicking the "Done" button
        composeTestRule.onNodeWithText(LanguageConstants.DONE_BUTTON_TITLE.localizeString()).performClick()

        // Verify that onOnboardingFinished was called
        verify(onOnboardingFinished).invoke("1.0")
    }
}
