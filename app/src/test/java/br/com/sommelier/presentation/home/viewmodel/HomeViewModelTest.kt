package br.com.sommelier.presentation.home.viewmodel

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import br.com.sommelier.presentation.home.action.HomeAction
import br.com.sommelier.presentation.home.model.HomeUiModel
import br.com.sommelier.presentation.home.state.HomeEffect
import br.com.sommelier.presentation.home.state.HomeUiState
import br.com.sommelier.testrule.CoroutineTestRule
import br.com.sommelier.util.ext.getOrAwaitValue
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Rule
import org.junit.Test

class HomeViewModelTest {

    @get:Rule
    val instantTaskExecutorRule = InstantTaskExecutorRule()

    @get:Rule
    val coroutineTestRule = CoroutineTestRule()

    private lateinit var viewModel: HomeViewModel

    @Before
    fun setup() {
        viewModel = HomeViewModel()
    }

    @Test
    fun `GIVEN OnTypeSearchField action sent WHEN sendAction was called THEN assert that the uiState is the expected`() =
        coroutineTestRule.runBlockingTest {
            val query = "query"
            val action = HomeAction.Action.OnTypeSearchField(query)

            viewModel.sendAction(action)

            val expectedUiState = HomeUiState.Resume(
                HomeUiModel().copy(
                    searchFieldUiState = HomeUiModel().searchFieldUiState.copy(query = query)
                )
            )
            val actualUiState = viewModel.uiState.getOrAwaitValue()

            assertUiState(actualUiState, expectedUiState)
        }

    private fun assertUiState(expected: HomeUiState, actual: HomeUiState) {
        assertEquals(expected.javaClass, actual.javaClass)
        assertEquals(
            expected.uiModel.searchFieldUiState.query,
            actual.uiModel.searchFieldUiState.query
        )
    }

    @Test
    fun `GIVEN OnSearch action sent WHEN sendAction was called THEN assert that the uiEffect is the expected`() =
        coroutineTestRule.runBlockingTest {
            val action = HomeAction.Action.OnSearch

            viewModel.sendAction(action)

            val expectedUiEffect = HomeEffect.GetRestaurants
            val actualUiEffect = viewModel.uiEffect.getOrAwaitValue()

            assertEquals(expectedUiEffect, actualUiEffect)
        }

    @Test
    fun `OnClickManageAccountButton action sent WHEN sendAction was called THEN assert that the uiEffect is the expected`() =
        coroutineTestRule.runBlockingTest {
            val action = HomeAction.Action.OnClickManageAccountButton

            viewModel.sendAction(action)

            val expectedUiEffect = HomeEffect.OpenManageAccount
            val actualUiEffect = viewModel.uiEffect.getOrAwaitValue()

            assertEquals(expectedUiEffect, actualUiEffect)
        }

    @Test
    fun `OnClickAddRestaurantButton action sent WHEN sendAction was called THEN assert that the uiEffect is the expected`() =
        coroutineTestRule.runBlockingTest {
            val action = HomeAction.Action.OnClickAddRestaurantButton

            viewModel.sendAction(action)

            val expectedUiEffect = HomeEffect.OpenAddRestaurant
            val actualUiEffect = viewModel.uiEffect.getOrAwaitValue()

            assertEquals(expectedUiEffect, actualUiEffect)
        }
}
