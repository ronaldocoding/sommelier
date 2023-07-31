package br.com.sommelier.ui.component

import androidx.compose.ui.test.assertHasClickAction
import androidx.compose.ui.test.assertHasNoClickAction
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.assertTextEquals
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.performClick
import org.junit.Rule
import org.junit.Test

class ClickableTextTest {

    @get:Rule
    val composeTestRule = createComposeRule()

    @Test
    fun givenClickableTextDefaultProperties_whenRendered_thenShouldAssertDefaultState() {
        composeTestRule.setContent {
            ClickableText()
        }
        composeTestRule.onNodeWithTag("ClickableText")
            .assertIsDisplayed()
            .assertTextEquals("Click here")
            .assertHasClickAction()
            .performClick()
        composeTestRule.onNodeWithTag("NonClickableText").assertDoesNotExist()
        composeTestRule.onNodeWithTag("ClickableTextSpacer").assertDoesNotExist()
    }

    @Test
    fun givenClickableTextWithNonClickableText_whenRendered_thenShouldAssertExpectedState() {
        composeTestRule.setContent {
            ClickableText(
                clickableText = "Sign up",
                nonClickableText = "Don't have an account yet?"
            )
        }
        composeTestRule.onNodeWithTag("ClickableText")
            .assertIsDisplayed()
            .assertTextEquals("Sign up")
            .assertHasClickAction()
            .performClick()
        composeTestRule.onNodeWithTag("NonClickableText")
            .assertIsDisplayed()
            .assertTextEquals("Don't have an account yet?")
            .assertHasNoClickAction()
        composeTestRule.onNodeWithTag("ClickableTextSpacer").assertExists()
    }
}
