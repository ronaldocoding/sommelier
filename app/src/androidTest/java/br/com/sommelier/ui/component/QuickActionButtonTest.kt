package br.com.sommelier.ui.component

import androidx.compose.ui.test.assertHasClickAction
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.performClick
import org.junit.Rule
import org.junit.Test

class QuickActionButtonTest {

    @get:Rule
    val composeTestRule = createComposeRule()

    @Test
    fun givenQuickActionDefaultProperties_WhenRendered_ThenShouldAssertDefaultState() {
        composeTestRule.setContent {
            QuickActionButton()
        }
        composeTestRule.onNodeWithTag("QuickActionButton")
            .assertIsDisplayed()
            .assertHasClickAction()
        composeTestRule.onNodeWithTag("QuickActionButtonIcon", useUnmergedTree = true)
            .assertIsDisplayed()
    }

    @Test
    fun givenQuickActionDefaultProperties_WhenRendered_ThenPerformClick() {
        composeTestRule.setContent {
            QuickActionButton()
        }
        composeTestRule.onNodeWithTag("QuickActionButton").performClick()
    }
}
