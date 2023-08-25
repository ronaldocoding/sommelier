package br.com.sommelier.presentation.confirmemail.viewmodel

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import br.com.sommelier.presentation.confirmemail.action.ConfirmEmailAction
import br.com.sommelier.presentation.confirmemail.state.ConfirmEmailUiEffect
import br.com.sommelier.util.ext.getOrAwaitValue
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Rule
import org.junit.Test

class ConfirmEmailViewModelTest {

    @get:Rule
    val instantTaskExecutorRule = InstantTaskExecutorRule()

    private lateinit var viewModel: ConfirmEmailViewModel

    @Before
    fun setUp() {
        viewModel = ConfirmEmailViewModel()
    }

    @Test
    fun `GIVEN OnClickBackButton action sent WHEN sendAction was called THEN assert that the emitted uiEffect was PopBackStack`() {
        val action = ConfirmEmailAction.Action.ClickBackButton

        viewModel.sendAction(action)

        val expectedUiEffect = ConfirmEmailUiEffect.PopBackStack
        val actualUiEffect = viewModel.uiEffect.getOrAwaitValue()

        assertEquals(expectedUiEffect, actualUiEffect)
    }

    @Test
    fun `GIVEN ClickOkButton action sent WHEN sendAction was called THEN assert that the emitted uiEffect was PopBackStack`() {
        val action = ConfirmEmailAction.Action.ClickOkButton

        viewModel.sendAction(action)

        val expectedUiEffect = ConfirmEmailUiEffect.PopBackStack
        val actualUiEffect = viewModel.uiEffect.getOrAwaitValue()

        assertEquals(expectedUiEffect, actualUiEffect)
    }
}