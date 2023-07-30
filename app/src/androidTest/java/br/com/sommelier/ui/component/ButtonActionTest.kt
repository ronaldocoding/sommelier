package br.com.sommelier.ui.component

import androidx.compose.ui.test.assertHasClickAction
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.assertTextEquals
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.performClick
import br.com.sommelier.ui.theme.ColorReference
import br.com.sommelier.ui.theme.Typography
import org.junit.Rule
import org.junit.Test

class ButtonActionTest {

    @get:Rule
    val composeTestRule = createComposeRule()

    @Test
    fun givenActionButtonDefaultProperties_WhenRendered_ThenShouldAssertDefaultState() {
        composeTestRule.setContent {
            ActionButton()
        }
        composeTestRule.onNodeWithTag("ActionButton").assertIsDisplayed()
        composeTestRule.onNodeWithTag("NormalActionButton")
            .assertIsDisplayed()
            .assertHasClickAction()
        composeTestRule.onNodeWithTag("NormalActionButtonText", useUnmergedTree = true)
            .assertExists()
            .assertTextEquals("Action")
        composeTestRule.onNodeWithTag("OutlinedActionButton").assertDoesNotExist()
        composeTestRule.onNodeWithTag("OutlinedActionButtonText", useUnmergedTree = true)
            .assertDoesNotExist()
    }

    @Test
    fun givenActionButtonPersonalizedProperties_WhenRendered_ThenShouldAssertPersonalizedState() {
        val text = "Button"
        val textColor = ColorReference.royalPurple
        val textStyle = Typography.bodyMedium
        val backgroundColor = ColorReference.white
        val isOutlined = true

        composeTestRule.setContent {
            ActionButton(
                text = text,
                textColor = textColor,
                textStyle = textStyle,
                backGroundColor = backgroundColor,
                isOutlined = isOutlined
            )
        }
        composeTestRule.onNodeWithTag("ActionButton").assertIsDisplayed()
        composeTestRule.onNodeWithTag("OutlinedActionButton")
            .assertExists()
            .assertHasClickAction()
        composeTestRule.onNodeWithTag("OutlinedActionButtonText", useUnmergedTree = true)
            .assertExists()
            .assertTextEquals(text)
        composeTestRule.onNodeWithTag("NormalActionButton").assertDoesNotExist()
        composeTestRule.onNodeWithTag("NormalActionButtonText", useUnmergedTree = true)
            .assertDoesNotExist()
    }

    @Test
    fun givenActionButtonDefaultProperties_WhenRendered_ThenPerformClick() {
        composeTestRule.setContent {
            ActionButton()
        }
        composeTestRule.onNodeWithTag("ActionButton").performClick()
    }
}
