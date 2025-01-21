package sa.sauditourism.employee.modules.services

import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.hasText
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.compose.ui.test.performScrollToNode
import androidx.compose.ui.test.performTextInput
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import androidx.test.ext.junit.runners.AndroidJUnit4
import io.mockk.every
import io.mockk.junit4.MockKRule
import io.mockk.mockk
import io.mockk.mockkObject
import io.mockk.unmockkAll
import io.mockk.verify
import org.junit.After
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import sa.sauditourism.employee.EmployeeApplication.Companion.sharedPreferencesManager
import sa.sauditourism.employee.constants.LanguageConstants
import sa.sauditourism.employee.constants.TestTagsConstants.SERVICES_LIST_TAG
import sa.sauditourism.employee.extensions.localizeString
import sa.sauditourism.employee.modules.login.Routes
import sa.sauditourism.employee.modules.services.model.FormModel
import sa.sauditourism.employee.modules.services.model.RequestsType
import sa.sauditourism.employee.modules.services.model.ServicesModel

@RunWith(AndroidJUnit4::class)
class ServicesScreenTest {

    @get:Rule(order = 0)
    val composeTestRule = createComposeRule()

    @get:Rule(order = 1)
    val mockkRule = MockKRule(this)

    @Test
    fun servicesScreen_displaysUiElements() {
        // Mock SharedPreferencesManager and services
        mockkObject(sharedPreferencesManager)
        val requestsType = RequestsType(
            id = "1",
            title = "Request Type Title",
            subTitle = "Request Type Subtitle",
            comingSoon = false,
            image = "",
            //form = FormModel(listOf(), "")
        )
        val service = ServicesModel(
            id = "1",
            title = "Service Title",
            image = "",
            comingSoon = false,
            requestsTypes = listOf(requestsType)
        )
        every { sharedPreferencesManager.services } returns listOf(service)

        // Mock NavController
        val navController = mockk<NavController>()

        // Set content
        composeTestRule.setContent {
            ServicesScreen(navController = navController)
        }

        composeTestRule.onNodeWithText(LanguageConstants.SERVICES_TAB.localizeString())
            .assertIsDisplayed()
        composeTestRule.onNodeWithText(LanguageConstants.SERVICES_CATEGORIES.localizeString())
            .assertIsDisplayed()
        composeTestRule.onNodeWithTag(SERVICES_LIST_TAG)
            .performScrollToNode(hasText("Service Title")) // <- Scroll to the node containing the appropriate text
            .assertIsDisplayed() // <- Assert that it is displayed    }

        @Test
        fun servicesScreen_displaysEmptyView_whenNoResultsFound() {
            // Mock SharedPreferencesManager and services with empty results
            mockkObject(sharedPreferencesManager)
            every { sharedPreferencesManager.services } returns emptyList()

            // Mock NavController
            val navController = mockk<NavController>()

            // Set content
            composeTestRule.setContent {
                ServicesScreen(navController = navController)
            }

            // Perform search
            composeTestRule.onNodeWithText(LanguageConstants.SERVICES_TAB.localizeString())
                .performTextInput("Nonexistent Search")

            // Assertions
            composeTestRule.onNodeWithText(LanguageConstants.NO_RESULT_FOUND.localizeString())
                .assertIsDisplayed()
            composeTestRule.onNodeWithText(LanguageConstants.NO_RESULT_FOUND_MESSAGE.localizeString())
                .assertIsDisplayed()
        }
    }

    @Test
    fun servicesScreen_navigatesToRequestsTypes_onClick() {
        // Mock SharedPreferencesManager and services
        mockkObject(sharedPreferencesManager)
        val requestsType = RequestsType(
            id = "1",
            title = "Request Type Title",
            subTitle = "Request Type Subtitle",
            comingSoon = false,
            image = "",
            //form = FormModel(listOf(), "")
        )
        val service = ServicesModel(
            id = "1",
            title = "Service Title",
            image = "",
            comingSoon = false,
            requestsTypes = listOf(requestsType)
        )
        every { sharedPreferencesManager.services } returns listOf(service)

        // Mock NavController
        val navController = mockk<NavHostController>(relaxed = true)

        // Set content
        composeTestRule.setContent {
            ServicesScreen(navController = navController)
        }

        // Perform click on service card
        composeTestRule.onNodeWithText("Service Title").performClick()

        // Verify navigation
        verify { navController.navigate("${Routes.REQUESTS_TYPES}/0") }
    }

    @Test
    fun servicesScreen_navigatesToRequestForm_onSearchClick() {
        // Mock SharedPreferencesManager and services
        mockkObject(sharedPreferencesManager)
        val requestsType = RequestsType(
            id = "1",
            title = "Request Type Title",
            subTitle = "Request Type Subtitle",
            comingSoon = false,
            image = "",
            //form = FormModel(listOf(), "")
        )
        val service = ServicesModel(
            id = "1",
            title = "Service Title",
            image = "",
            comingSoon = false,
            requestsTypes = listOf(requestsType)
        )
        every { sharedPreferencesManager.services } returns listOf(service)

        // Mock NavController
        val navController = mockk<NavHostController>(relaxed = true)

        // Set content
        composeTestRule.setContent {
            ServicesScreen(navController = navController)
        }

        // Perform search
        composeTestRule.onNodeWithText(LanguageConstants.SERVICES_TAB.localizeString())
            .performTextInput("Request Type Title")

        // Perform click on search result
        composeTestRule.onNodeWithText("Request Type Title").performClick()

        // Verify navigation
        verify { navController.navigate("${Routes.REQUESTS_FORM}/${requestsType.id}/${requestsType.title}") }
    }

    @After
    fun afterTests() {
        unmockkAll()
    }
}
