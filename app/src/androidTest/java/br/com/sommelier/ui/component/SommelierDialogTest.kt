package br.com.sommelier.ui.component

import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.assertTextEquals
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithTag
import org.junit.Rule
import org.junit.Test

class SommelierDialogTest {

    @get:Rule
    val composeTestRule = createComposeRule()

    @Test
    fun givenSommelierDialogDefaultProperties_whenRendered_thenAssertDefaultState() {
        composeTestRule.setContent {
            SommelierDialog()
        }
        composeTestRule.onNodeWithTag("SommelierDialog")
            .assertIsDisplayed()
        composeTestRule.onNodeWithTag("SommelierDialogTitle", useUnmergedTree = true)
            .assertIsDisplayed()
            .assertTextEquals("Title")
        composeTestRule.onNodeWithTag("SommelierDialogText", useUnmergedTree = true)
            .assertIsDisplayed()
            .assertTextEquals("Text")
        composeTestRule.onNodeWithTag("SommelierDialogConfirmButton", useUnmergedTree = true)
            .assertIsDisplayed()
        composeTestRule.onNodeWithTag("SommelierDialogDismissButton", useUnmergedTree = true)
            .assertIsDisplayed()
        composeTestRule.onNodeWithTag("SommelierDialogConfirmButtonText", useUnmergedTree = true)
            .assertIsDisplayed()
            .assertTextEquals("Confirm")
        composeTestRule.onNodeWithTag("SommelierDialogDismissButtonText", useUnmergedTree = true)
            .assertIsDisplayed()
            .assertTextEquals("Dismiss")
    }

    @Test
    fun givenSommelierDialogProperties_whenRendered_thenAssertExpectedState() {
        val title = "Delete account"
        val text = "Are you sure you want to delete your account? This action cannot be undone."
        val confirmButtonText = "Yes"
        val dismissButtonText = "No"
        composeTestRule.setContent {
            SommelierDialog(
                title = title,
                text = text,
                confirmButtonText = confirmButtonText,
                dismissButtonText = dismissButtonText
            )
        }
        composeTestRule.onNodeWithTag("SommelierDialog")
            .assertIsDisplayed()
        composeTestRule.onNodeWithTag("SommelierDialogTitle", useUnmergedTree = true)
            .assertIsDisplayed()
            .assertTextEquals(title)
        composeTestRule.onNodeWithTag("SommelierDialogText", useUnmergedTree = true)
            .assertIsDisplayed()
            .assertTextEquals(text)
        composeTestRule.onNodeWithTag("SommelierDialogConfirmButton", useUnmergedTree = true)
            .assertIsDisplayed()
        composeTestRule.onNodeWithTag("SommelierDialogDismissButton", useUnmergedTree = true)
            .assertIsDisplayed()
        composeTestRule.onNodeWithTag("SommelierDialogConfirmButtonText", useUnmergedTree = true)
            .assertIsDisplayed()
            .assertTextEquals(confirmButtonText)
        composeTestRule.onNodeWithTag("SommelierDialogDismissButtonText", useUnmergedTree = true)
            .assertIsDisplayed()
            .assertTextEquals(dismissButtonText)
    }
}