package sa.sauditourism.employee.modules.account

import android.annotation.SuppressLint
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithText
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import androidx.test.ext.junit.runners.AndroidJUnit4
import io.mockk.every
import io.mockk.impl.annotations.RelaxedMockK
import io.mockk.junit4.MockKRule
import io.mockk.mockk
import io.mockk.mockkObject
import io.mockk.unmockkAll
import io.mockk.unmockkObject
import org.checkerframework.checker.units.qual.N
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import sa.sauditourism.employee.EmployeeApplication.Companion.sharedPreferencesManager
import sa.sauditourism.employee.constants.LanguageConstants
import sa.sauditourism.employee.extensions.localizeString
import sa.sauditourism.employee.managers.realtime.RealTimeDatabase
import sa.sauditourism.employee.modules.account.domain.AccountRepository
import sa.sauditourism.employee.modules.account.domain.ProfileImageRepository
import sa.sauditourism.employee.modules.account.model.AccountDetails
import sa.sauditourism.employee.modules.services.domain.RequestFormRepository

@RunWith(AndroidJUnit4::class)
class DigitalCardScreenTest {

    @get:Rule(order = 0)
    val composeTestRule = createComposeRule()

    @get:Rule(order = 1)
    val mockkRule = MockKRule(this)

    @RelaxedMockK
    private var mockNavController: NavHostController = mockk(relaxed = true)

    @Mock
    private lateinit var accountRepository: AccountRepository

    @Mock
    private lateinit var profileImageRepository: ProfileImageRepository


    @Mock
    private lateinit var realTimeDatabase: RealTimeDatabase


    @SuppressLint("CheckResult")
    @Test
    fun digitalCardScreen_displaysUiElements() {
        // Mock SharedPreferencesManager
        mockkObject(sharedPreferencesManager)
        every { sharedPreferencesManager.preferredLocale } returns "en" // or "ar" for Arabic

        val accountViewModel = AccountViewModel(
            accountRepository = accountRepository,
            profileImageRepository = profileImageRepository,
            realTimeDatabase = realTimeDatabase
        )

        // Set content
        composeTestRule.setContent {
            DigitalCardScreen(rememberNavController(), accountViewModel)
        }

        // Assertions
        composeTestRule.onNodeWithText(LanguageConstants.ACCOUNT_BUSINESS_CARD_SCREEN_TITLE.localizeString())
            .assertIsDisplayed()
        composeTestRule.onNodeWithText("Abdulazaiz AlAbdulazaiz").assertIsDisplayed()
        composeTestRule.onNodeWithText("${LanguageConstants.ACCOUNT_ID.localizeString()}0203")
            .assertIsDisplayed()
        composeTestRule.onNodeWithText("+955 569 900 500").assertIsDisplayed()
        composeTestRule.onNodeWithText("Aabdulazaiz@sta.gov.sa").assertIsDisplayed()
        composeTestRule.onNodeWithText(LanguageConstants.ACCOUNT_JOB_TITLE.localizeString())
            .assertIsDisplayed()
        composeTestRule.onNodeWithText("VP of A&E Marketing & Communications").assertIsDisplayed()
        composeTestRule.onNodeWithText(LanguageConstants.ACCOUNT_DEPARTMENT.localizeString())
            .assertIsDisplayed()
        composeTestRule.onNodeWithText("Americas & Europe (A&E) Business Unit").assertIsDisplayed()
    }

    @After
    fun afterTests() {
        unmockkAll()
    }
}