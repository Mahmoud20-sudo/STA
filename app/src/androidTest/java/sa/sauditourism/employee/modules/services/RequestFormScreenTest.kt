package sa.sauditourism.employee.modules.services

import android.annotation.SuppressLint
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithText
import androidx.navigation.NavHostController
import androidx.test.ext.junit.runners.AndroidJUnit4
import io.mockk.every
import io.mockk.impl.annotations.RelaxedMockK
import io.mockk.junit4.MockKRule
import io.mockk.mockk
import io.mockk.mockkObject
import io.mockk.unmockkAll
import org.junit.After
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import sa.sauditourism.employee.EmployeeApplication.Companion.sharedPreferencesManager
import sa.sauditourism.employee.constants.FirebaseConstants
import sa.sauditourism.employee.constants.LanguageConstants
import sa.sauditourism.employee.extensions.localizeString
import sa.sauditourism.employee.modules.services.form.RequestFormScreen
import sa.sauditourism.employee.modules.services.model.Filed
import sa.sauditourism.employee.modules.services.model.FormModel
import sa.sauditourism.employee.modules.services.model.RequestsType
import sa.sauditourism.employee.modules.services.model.ServicesModel

@RunWith(AndroidJUnit4::class)
class RequestFormScreenTest {

    @get:Rule(order = 0)
    val composeTestRule = createComposeRule()

    @get:Rule(order = 1)
    val mockkRule = MockKRule(this)

    @RelaxedMockK
    private var mockNavController: NavHostController = mockk(relaxed = true)

    @SuppressLint("CheckResult")
    @Test
    fun requestFormScreen_displaysUiElements() {
        // Mock SharedPreferencesManager and services
        val formModel = FormModel(
            listOf(
                Filed(
                    title = "Filed Title",
                    component = FirebaseConstants.RADIO_BUTTON,
                    dropDownCount = 1,
                    id = "1",
                    note = LanguageConstants.NOTE.localizeString(),
                    required = false,
                    isDropDownUserSearch = false
                )
            ), LanguageConstants.NOTE.localizeString()
        )
        val requestsType = RequestsType(
            id = "1",
            title = "Request Type Title",
            subTitle = "Request Type Subtitle",
            comingSoon = false,
            image = ""
        )
        val service = ServicesModel(
            id = "1",
            title = "Service Title",
            comingSoon = false,
            image = "",
            requestsTypes = listOf(requestsType) // Mock requestsTypes as needed
        )

        mockkObject(sharedPreferencesManager)
        every { sharedPreferencesManager.services } returns listOf(service)

        // Set content
        composeTestRule.setContent {
            RequestFormScreen(navController = mockNavController, title = "test")
        }

        // Assertions
        composeTestRule.onNodeWithText("Here goes a supporting text for the RadioButton")
            .assertIsDisplayed()
    }

    @SuppressLint("CheckResult")
    @Test
    fun requestFormScreen_displaysEmptyView_whenFormIsNull() {
        // Mock SharedPreferencesManager and services with null form
        mockkObject(sharedPreferencesManager)
        val service = ServicesModel(
            id = "1",
            title = "Service Title",
            comingSoon = false,
            image = "",
            requestsTypes = listOf(mockk()) // Mock requestsTypes as needed
        )
        every { sharedPreferencesManager.services } returns listOf()

        // Set content
        composeTestRule.setContent {
            RequestFormScreen(
                navController = mockNavController,
                title = "test"
            ) // Use a different ID to trigger null form
        }

        // Assertions
        composeTestRule.onNodeWithText(LanguageConstants.NO_RESULT_FOUND.localizeString())
            .assertIsDisplayed()
        composeTestRule.onNodeWithText(LanguageConstants.NO_RESULT_FOUND_MESSAGE.localizeString())
            .assertIsDisplayed()
    }

    @After
    fun afterTests() {
        unmockkAll()
    }
}