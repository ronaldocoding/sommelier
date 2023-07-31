package br.com.sommelier.ui.component

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.test.assertContentDescriptionEquals
import androidx.compose.ui.test.assertHasClickAction
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.assertTextEquals
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.performClick
import br.com.sommelier.R
import org.junit.Rule
import org.junit.Test

@OptIn(ExperimentalMaterial3Api::class)
class SommelierTopBarTest {

    @get:Rule
    val composeTestRule = createComposeRule()

    @Test
    fun givenSommelierTopBarDefaultProperties_WhenRendered_ThenShouldAssertDefaultState() {
        composeTestRule.setContent {
            SommelierTopBar()
        }
        composeTestRule.onNodeWithTag("SommelierTopBar")
            .assertIsDisplayed()
        composeTestRule.onNodeWithTag("SommelierTopBarTitle")
            .assertIsDisplayed()
            .assertTextEquals("Title")
        composeTestRule.onNodeWithTag("SommelierTopBarLeftButton")
            .assertDoesNotExist()
        composeTestRule.onNodeWithTag("SommelierTopBarRightButton")
            .assertDoesNotExist()
    }

    @Test
    fun givenSommelierTopBarWithLeftButton_WhenRendered_ThenShouldAssertDefaultState() {
        composeTestRule.setContent {
            SommelierTopBar(
                leftButton = SommelierTopBarButton.Enabled(
                    icon = Icons.Filled.ArrowBack,
                    contentDescription = "Back"
                )
            )
        }
        composeTestRule.onNodeWithTag("SommelierTopBar")
            .assertIsDisplayed()
        composeTestRule.onNodeWithTag("SommelierTopBarTitle")
            .assertIsDisplayed()
            .assertTextEquals("Title")
        composeTestRule.onNodeWithTag("SommelierTopBarLeftButton")
            .assertIsDisplayed()
            .assertContentDescriptionEquals("Back")
            .assertHasClickAction()
            .performClick()
        composeTestRule.onNodeWithTag("SommelierTopBarRightButton")
            .assertDoesNotExist()
    }

    @Test
    fun givenSommelierTopBarWithRightButton_WhenRendered_ThenShouldAssertDefaultState() {
        composeTestRule.setContent {
            SommelierTopBar(
                rightButton = SommelierTopBarButton.Enabled(
                    icon = ImageVector.vectorResource(id = R.drawable.ic_edit),
                    contentDescription = "Edit"
                )
            )
        }
        composeTestRule.onNodeWithTag("SommelierTopBar")
            .assertIsDisplayed()
        composeTestRule.onNodeWithTag("SommelierTopBarTitle")
            .assertIsDisplayed()
            .assertTextEquals("Title")
        composeTestRule.onNodeWithTag("SommelierTopBarRightButton")
            .assertIsDisplayed()
            .assertContentDescriptionEquals("Edit")
            .assertHasClickAction()
            .performClick()
        composeTestRule.onNodeWithTag("SommelierTopBarLeftButton")
            .assertDoesNotExist()
    }

    @Test
    fun givenSommelierTopBarWithLeftAndRightButton_WhenRendered_ThenShouldAssertDefaultState() {
        composeTestRule.setContent {
            SommelierTopBar(
                leftButton = SommelierTopBarButton.Enabled(
                    icon = Icons.Default.ArrowBack,
                    contentDescription = "Back"
                ),
                rightButton = SommelierTopBarButton.Enabled(
                    icon = ImageVector.vectorResource(id = R.drawable.ic_edit),
                    contentDescription = "Edit"
                )
            )
        }
        composeTestRule.onNodeWithTag("SommelierTopBar")
            .assertIsDisplayed()
        composeTestRule.onNodeWithTag("SommelierTopBarTitle")
            .assertIsDisplayed()
            .assertTextEquals("Title")
        composeTestRule.onNodeWithTag("SommelierTopBarLeftButton")
            .assertIsDisplayed()
            .assertContentDescriptionEquals("Back")
            .assertHasClickAction()
            .performClick()
        composeTestRule.onNodeWithTag("SommelierTopBarRightButton")
            .assertIsDisplayed()
            .assertContentDescriptionEquals("Edit")
            .assertHasClickAction()
            .performClick()
    }
}
