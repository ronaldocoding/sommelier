package br.com.sommelier.ui.component

import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.assertTextEquals
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithTag
import org.junit.Rule
import org.junit.Test

class SommelierSnackbarTest {

    @get:Rule
    val composeTestRule = createComposeRule()

    @Test
    fun givenSommelierSnackbarDefaultProperties_whenRendered_thenAssertDefaultState() {
        composeTestRule.setContent {
            SommelierSnackbar()
        }
        composeTestRule.onNodeWithTag("SommelierSnackbar")
            .assertIsDisplayed()
        composeTestRule.onNodeWithTag("SommelierSnackbarText", useUnmergedTree = true)
            .assertIsDisplayed()
            .assertTextEquals("Snackbar")
        composeTestRule.onNodeWithTag("SommelierSnackbarSuccessIcon", useUnmergedTree = true)
            .assertDoesNotExist()
        composeTestRule.onNodeWithTag("SommelierSnackbarErrorIcon", useUnmergedTree = true)
            .assertDoesNotExist()
    }

    @Test
    fun givenSommelierSnackbarWithSuccessType_whenRendered_thenAssertSuccessState() {
        val text = "Success"
        composeTestRule.setContent {
            SommelierSnackbar(
                text = text,
                type = SommelierSnackbarType.Success
            )
        }
        composeTestRule.onNodeWithTag("SommelierSnackbar")
            .assertIsDisplayed()
        composeTestRule.onNodeWithTag("SommelierSnackbarText", useUnmergedTree = true)
            .assertIsDisplayed()
            .assertTextEquals(text)
        composeTestRule.onNodeWithTag("SommelierSnackbarSuccessIcon", useUnmergedTree = true)
            .assertIsDisplayed()
        composeTestRule.onNodeWithTag("SommelierSnackbarErrorIcon", useUnmergedTree = true)
            .assertDoesNotExist()
    }

    @Test
    fun givenSommelierSnackbarWithErrorType_whenRendered_thenAssertErrorState() {
        val text = "Error"
        composeTestRule.setContent {
            SommelierSnackbar(
                text = text,
                type = SommelierSnackbarType.Error
            )
        }
        composeTestRule.onNodeWithTag("SommelierSnackbar")
            .assertIsDisplayed()
        composeTestRule.onNodeWithTag("SommelierSnackbarText", useUnmergedTree = true)
            .assertIsDisplayed()
            .assertTextEquals(text)
        composeTestRule.onNodeWithTag("SommelierSnackbarSuccessIcon", useUnmergedTree = true)
            .assertDoesNotExist()
        composeTestRule.onNodeWithTag("SommelierSnackbarErrorIcon", useUnmergedTree = true)
            .assertIsDisplayed()
    }
}
