package sa.sauditourism.employee.modules.services

import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.compose.ui.test.performTextInput
import androidx.compose.ui.test.requestFocus
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import io.mockk.every
import io.mockk.junit4.MockKRule
import io.mockk.mockk
import io.mockk.mockkObject
import io.mockk.verify
import org.junit.Rule
import org.junit.Test
import sa.sauditourism.employee.EmployeeApplication.Companion.sharedPreferencesManager
import sa.sauditourism.employee.constants.FirebaseConstants
import sa.sauditourism.employee.constants.LanguageConstants
import sa.sauditourism.employee.constants.TestTagsConstants.BASIC_TEXT_FIELD
import sa.sauditourism.employee.extensions.localizeString
import sa.sauditourism.employee.modules.login.Routes
import sa.sauditourism.employee.modules.services.model.Filed
import sa.sauditourism.employee.modules.services.model.FormModel
import sa.sauditourism.employee.modules.services.model.RequestsType
import sa.sauditourism.employee.modules.services.model.ServicesModel
import sa.sauditourism.employee.modules.services.requeststypes.RequestsTypesScreen

class RequestsTypesScreenTest {

    @get:Rule(order = 0)
    val composeTestRule = createComposeRule()

    @get:Rule(order = 1)
    val mockkRule = MockKRule(this)


    @Test
    fun requestsTypesScreen_displaysUiElements() {
        // Mock SharedPreferencesManager and services
        mockkObject(sharedPreferencesManager)
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
        val service2 = ServicesModel(
            id = "2",
            title = "Service Title 2",
            comingSoon = false,
            image = "",
            requestsTypes = listOf(requestsType) // Mock requestsTypes as needed
        )
        every { sharedPreferencesManager.services } returns listOf(service, service2)

        // Mock NavController
        val navController = mockk<NavController>()

        // Set content
        composeTestRule.setContent {
            RequestsTypesScreen(navController = navController, "1", "617")
        }

        // Assertions
        composeTestRule.onNodeWithText("Service Title").assertIsDisplayed()
        composeTestRule.onNodeWithText("Request Type Title").assertIsDisplayed()
        composeTestRule.onNodeWithText("Request Type Subtitle").assertIsDisplayed()
    }

    @Test
    fun requestsTypesScreen_displaysEmptyView_whenNoResultsFound() {
        // Mock SharedPreferencesManager and services with empty results
        mockkObject(sharedPreferencesManager)

        val service = ServicesModel(
            id = "1",
            title = "Service Title",
            comingSoon = false,
            image = "",
            requestsTypes = listOf() // Mock requestsTypes as needed
        )

        every { sharedPreferencesManager.services } returns listOf(service)

        // Mock NavController
        val navController = mockk<NavController>()

        // Set content
        composeTestRule.setContent {
            RequestsTypesScreen(navController = navController, "1", "617")
        }

        // Perform search
        composeTestRule.onNodeWithTag(BASIC_TEXT_FIELD).requestFocus()
        composeTestRule.onNodeWithTag(BASIC_TEXT_FIELD).performTextInput("Nonexistent Search")

        // Assertions
        composeTestRule.onNodeWithText(LanguageConstants.NO_RESULT_FOUND.localizeString()).assertIsDisplayed()
        composeTestRule.onNodeWithText(LanguageConstants.NO_RESULT_FOUND_MESSAGE.localizeString()).assertIsDisplayed()
    }


    @Test
    fun requestsTypesScreen_navigatesToRequestForm_onClick() {
        // Mock SharedPreferencesManager and services
        mockkObject(sharedPreferencesManager)
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
        val service2 = ServicesModel(
            id = "2",
            title = "Service Title 2",
            comingSoon = false,
            image = "",
            requestsTypes = listOf(requestsType) // Mock requestsTypes as needed
        )
        every { sharedPreferencesManager.services } returns listOf(service, service2)

        // Mock NavController
        val navController = mockk<NavHostController>(relaxed = true)

        // Set content
        composeTestRule.setContent {
            RequestsTypesScreen(navController = navController, "1", "617")
        }

        // Perform click on request type
        composeTestRule.onNodeWithText("Request Type Title").performClick()

        // Verify navigation
        verify { navController.navigate("${Routes.REQUESTS_FORM}/${requestsType.id}") }
    }
}