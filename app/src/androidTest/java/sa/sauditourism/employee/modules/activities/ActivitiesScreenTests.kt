package sa.sauditourism.employee.modules.activities

import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.onNodeWithText
import androidx.lifecycle.viewModelScope
import androidx.navigation.compose.rememberNavController
import io.mockk.coEvery
import io.mockk.every
import io.mockk.junit4.MockKRule
import io.mockk.mockk
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.launch
import org.junit.Rule
import org.junit.Test
import org.mockito.kotlin.whenever
import sa.sauditourism.employee.constants.LanguageConstants
import sa.sauditourism.employee.constants.TestTagsConstants.ADD_COMMENT_ACTION_TAG
import sa.sauditourism.employee.constants.TestTagsConstants.COMMENT_BODY_TAG
import sa.sauditourism.employee.constants.TestTagsConstants.COMMENT_DATE_TAG
import sa.sauditourism.employee.constants.TestTagsConstants.COMMENT_TIME_TAG
import sa.sauditourism.employee.constants.TestTagsConstants.COMMENT_USER_NAME_TAG
import sa.sauditourism.employee.constants.TestTagsConstants.NOTE_DATE_TAG
import sa.sauditourism.employee.constants.TestTagsConstants.NOTE_ICON_TAG
import sa.sauditourism.employee.constants.TestTagsConstants.NOTE_TITLE_TAG
import sa.sauditourism.employee.extensions.localizeString
import sa.sauditourism.employee.managers.network.models.MainResponseModel
import sa.sauditourism.employee.managers.network.models.UiState
import sa.sauditourism.employee.modules.activities.domain.ActivitiesRepository
import sa.sauditourism.employee.modules.activities.model.RequestComment
import sa.sauditourism.employee.modules.activities.model.RequestCommentUser
import sa.sauditourism.employee.modules.activities.model.RequestNote

class ActivitiesScreenTests {

    @get:Rule
    val composeTestRule = createComposeRule()

    @get:Rule(order = 1)
    val mockkRule = MockKRule(this)

    @Test
    fun test_notes_ui_test() {
        composeTestRule.setContent {
            //ActivitiesScreen(rememberNavController(), "ID", arrayListOf(RequestNote(status = "Completed", date = "2024-10-27", time = "16:55:35.877+0300")), activitiesViewModel = ActivitiesViewModel(mockk(), mockk()))
        }

        composeTestRule.onNodeWithText(LanguageConstants.REQUEST_ACTIVITY.localizeString()).assertIsDisplayed()
        composeTestRule.onNodeWithTag(NOTE_ICON_TAG).assertIsDisplayed()
        composeTestRule.onNodeWithTag(NOTE_TITLE_TAG).assertIsDisplayed()
        composeTestRule.onNodeWithTag(NOTE_DATE_TAG).assertIsDisplayed()
    }

    @Test
    fun test_comments_ui_test() {
        val repository = mockk<ActivitiesRepository>()
        val viewModel = ActivitiesViewModel(repository, mockk())
        val comment = RequestComment(id = "123123", user = RequestCommentUser(
            userImage = "",
            name = "Essam Mohamed",
            displayName = "Essam"
        ), body = "Lorem ipsum Lorem ipsum Lorem ipsum Lorem ipsum Lorem ipsum Lorem ipsum Lorem ipsum Lorem ipsum Lorem ipsum", date = "2024-10-27", time = "16:55:35.877+0300")

        every { repository.getRequestComments("ID") } returns flow {
            //emit(UiState.Success(MainResponseModel(data = listOf(comment), code = 200, message = "")))
        }

        composeTestRule.setContent {
            //ActivitiesScreen(rememberNavController(), "ID", arrayListOf(RequestNote(status = "Completed", date = "2024-10-27", time = "16:55:35.877+0300")), activitiesViewModel = viewModel)
        }

        Thread.sleep(1000)

        composeTestRule.onNodeWithTag(COMMENT_USER_NAME_TAG).assertIsDisplayed()
        composeTestRule.onNodeWithTag(COMMENT_DATE_TAG).assertIsDisplayed()
        composeTestRule.onNodeWithTag(COMMENT_TIME_TAG).assertIsDisplayed()
        composeTestRule.onNodeWithTag(COMMENT_BODY_TAG).assertIsDisplayed()
        composeTestRule.onNodeWithTag(ADD_COMMENT_ACTION_TAG).assertIsDisplayed()
    }
}