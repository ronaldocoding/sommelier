package br.com.sommelier.ui.component

import androidx.compose.ui.test.assertContentDescriptionEquals
import androidx.compose.ui.test.assertHasClickAction
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.assertTextEquals
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.performClick
import androidx.compose.ui.test.performTextInput
import br.com.sommelier.util.emptyString
import org.junit.Rule
import org.junit.Test

class OutlinedPasswordInputTest {

    @get:Rule
    val composeTestRule = createComposeRule()

    @Test
    fun givenOutlinedPasswordInputDefaultProperties_whenRendered_thenAssertDefaultState() {
        composeTestRule.setContent {
            OutlinedPasswordInput()
        }
        composeTestRule.onNodeWithTag("OutlinedPasswordInput")
            .assertIsDisplayed()
            .assertTextEquals(emptyString())
            .assertHasClickAction()
            .performClick()
            .performTextInput("Test")
        composeTestRule.onNodeWithTag("OutlinedPasswordInputLabel", useUnmergedTree = true)
            .assertExists()
            .assertTextEquals(emptyString())
        composeTestRule.onNodeWithTag("OutlinedPasswordInputPlaceholder", useUnmergedTree = true)
            .assertExists()
            .assertTextEquals(emptyString())
        composeTestRule.onNodeWithTag("OutlinedPasswordInputLeadingIcon", useUnmergedTree = true)
            .assertIsDisplayed()
        composeTestRule.onNodeWithTag("OutlinedPasswordInputTrailingIcon", useUnmergedTree = true)
            .assertIsDisplayed()
            .assertContentDescriptionEquals("Visibility")
    }
}
