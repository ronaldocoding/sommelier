package br.com.sommelier.ui.component

import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.test.assertHasClickAction
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.assertTextEquals
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.performClick
import androidx.compose.ui.test.performTextInput
import br.com.sommelier.R
import br.com.sommelier.util.emptyString
import org.junit.Rule
import org.junit.Test

class OutlinedTextInputTest {

    @get:Rule
    val composeTestRule = createComposeRule()

    @Test
    fun givenOutlinedTextInputDefaultProperties_whenRendered_thenAssertDefaultState() {
        composeTestRule.setContent {
            OutlinedTextInput()
        }
        composeTestRule.onNodeWithTag("OutlinedTextField")
            .assertIsDisplayed()
            .assertTextEquals(emptyString())
            .assertHasClickAction()
            .performClick()
            .performTextInput("Test")
        composeTestRule.onNodeWithTag("OutlinedTextFieldLabel").assertDoesNotExist()
        composeTestRule.onNodeWithTag("OutlinedTextFieldPlaceholder").assertDoesNotExist()
        composeTestRule.onNodeWithTag("OutlinedTextFieldLeadingIcon").assertDoesNotExist()
    }

    @Test
    fun givenOutlinedTextInputWithLabel_whenRendered_thenAssertLabelIsDisplayed() {
        composeTestRule.setContent {
            OutlinedTextInput(label = "Label")
        }
        composeTestRule.onNodeWithTag("OutlinedTextFieldLabel", useUnmergedTree = true)
            .assertIsDisplayed()
            .assertTextEquals("Label")
        composeTestRule.onNodeWithTag("OutlinedTextFieldPlaceholder").assertDoesNotExist()
        composeTestRule.onNodeWithTag("OutlinedTextFieldLeadingIcon").assertDoesNotExist()
    }

    @Test
    fun givenOutlinedTextInputWithPlaceholder_whenRendered_thenAssertPlaceholderIsDisplayed() {
        composeTestRule.setContent {
            OutlinedTextInput(placeholder = "Placeholder")
        }
        composeTestRule.onNodeWithTag("OutlinedTextFieldPlaceholder", useUnmergedTree = true)
            .assertIsDisplayed()
            .assertTextEquals("Placeholder")
        composeTestRule.onNodeWithTag("OutlinedTextFieldLabel").assertDoesNotExist()
        composeTestRule.onNodeWithTag("OutlinedTextFieldLeadingIcon").assertDoesNotExist()
    }

    @Test
    fun givenOutlinedTextInputWithLeadingIcon_whenRendered_thenAssertLeadingIconIsDisplayed() {
        composeTestRule.setContent {
            OutlinedTextInput(leadingIcon = ImageVector.vectorResource(id = R.drawable.ic_user))
        }
        composeTestRule.onNodeWithTag("OutlinedTextFieldLeadingIcon", useUnmergedTree = true)
            .assertIsDisplayed()
        composeTestRule.onNodeWithTag("OutlinedTextFieldLabel").assertDoesNotExist()
        composeTestRule.onNodeWithTag("OutlinedTextFieldPlaceholder").assertDoesNotExist()
    }
}
