package br.com.sommelier.ui.component

import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.test.assertHasClickAction
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.assertTextEquals
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.performClick
import br.com.sommelier.R
import br.com.sommelier.util.emptyString
import org.junit.Rule
import org.junit.Test

class TextViewTest {

    @get:Rule
    val composeTestRule = createComposeRule()

    @Test
    fun givenTextViewDefaultProperties_whenRendered_thenAssertDefaultState() {
        composeTestRule.setContent {
            TextView()
        }
        composeTestRule.onNodeWithTag("TextView")
            .assertIsDisplayed()
            .assertTextEquals(emptyString())
            .assertHasClickAction()
            .performClick()
        composeTestRule.onNodeWithTag("TextViewLeadingIcon")
            .assertDoesNotExist()
    }

    @Test
    fun givenTextViewWithLeadingIcon_whenRendered_thenAssertLeadingIconIsDisplayed() {
        composeTestRule.setContent {
            TextView(
                leadingIcon = ImageVector.vectorResource(id = R.drawable.ic_user)
            )
        }
        composeTestRule.onNodeWithTag("TextView")
            .assertIsDisplayed()
            .assertTextEquals(emptyString())
            .assertHasClickAction()
            .performClick()

        composeTestRule.onNodeWithTag("TextViewLeadingIcon", useUnmergedTree = true)
            .assertIsDisplayed()
    }
}
