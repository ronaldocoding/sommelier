package br.com.sommelier.ui.component

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.test.assertContentDescriptionEquals
import androidx.compose.ui.test.assertHasClickAction
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.assertTextEquals
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.performClick
import androidx.compose.ui.test.performTextClearance
import androidx.compose.ui.test.performTextInput
import br.com.sommelier.util.emptyString
import org.junit.Rule
import org.junit.Test

class SearchBarTest() {

    @get:Rule
    val composeTestRule = createComposeRule()

    @Test
    fun givenSearchBarDefaultProperties_whenRendered_thenAssertDefaultState() {
        composeTestRule.setContent {
            SearchBar()
        }
        composeTestRule.onNodeWithTag("SearchBar")
            .assertIsDisplayed()
            .assertTextEquals(emptyString())
            .assertHasClickAction()
            .performClick()
        composeTestRule.onNodeWithTag("SearchBarIcon", useUnmergedTree = true)
            .assertIsDisplayed()
            .assertContentDescriptionEquals("Search Icon")
        composeTestRule.onNodeWithTag("SearchBarHint", useUnmergedTree = true)
            .assertExists()
            .assertTextEquals(emptyString())
    }

    @Test
    fun givenSearchBarWithQuery_whenRendered_thenAssertThatHintDoesNotExist() {
        val query = "Test"
        val hint = "Search a restaurant"
        composeTestRule.setContent {
            SearchBar(
                query = query,
                hint = hint
            )
        }
        composeTestRule.onNodeWithTag("SearchBarHint", useUnmergedTree = true)
            .assertDoesNotExist()
    }

    @Test
    fun givenSearchBar_whenRenderedAndPerformTextInput_thenAssertThatHintDoesNotExist() {
        val hint = "Search a restaurant"
        composeTestRule.setContent {
            var query by rememberSaveable {
                mutableStateOf(emptyString())
            }
            SearchBar(
                query = query,
                onQueryChange = { query = it },
                hint = hint
            )
        }
        composeTestRule.onNodeWithTag("SearchBar")
            .performTextInput("Test")
        composeTestRule.onNodeWithTag("SearchBarHint", useUnmergedTree = true)
            .assertDoesNotExist()
    }

    @Test
    fun givenSearchBarWithQuery_whenRenderedAndDeleteQuery_thenAssertThatHintExists() {
        val hint = "Search a restaurant"
        composeTestRule.setContent {
            var query by rememberSaveable {
                mutableStateOf("Test")
            }
            SearchBar(
                query = query,
                onQueryChange = { query = it },
                hint = hint
            )
        }
        composeTestRule.onNodeWithTag("SearchBar")
            .performTextClearance()
        composeTestRule.onNodeWithTag("SearchBarHint", useUnmergedTree = true)
            .assertExists()
            .assertTextEquals(hint)
    }
}