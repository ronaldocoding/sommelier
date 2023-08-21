package br.com.sommelier.presentation.account.viewmodel

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import br.com.sommelier.domain.usecase.DeleteUserUseCase
import br.com.sommelier.domain.usecase.GetUserDocumentUseCase
import br.com.sommelier.domain.usecase.SignOutUserUseCase
import br.com.sommelier.presentation.account.action.AccountAction
import br.com.sommelier.presentation.account.state.AccountUiEffect
import br.com.sommelier.testrule.CoroutineTestRule
import br.com.sommelier.util.ext.getOrAwaitValue
import io.mockk.mockk
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Rule
import org.junit.Test

class AccountViewModelTest {

    @get:Rule
    val instantTaskExecutorRule = InstantTaskExecutorRule()

    @get:Rule
    val coroutineTestRule = CoroutineTestRule()

    private val getUserDocumentUseCase = mockk<GetUserDocumentUseCase>()

    private val logOutUserUseCase = mockk<SignOutUserUseCase>()

    private val deleteUserUseCase = mockk<DeleteUserUseCase>()

    private lateinit var viewModel: AccountViewModel

    @Before
    fun setup() {
        viewModel = AccountViewModel(
            getUserDocumentUseCase = getUserDocumentUseCase,
            logOutUserUseCase = logOutUserUseCase,
            deleteUserUseCase = deleteUserUseCase
        )
    }

    @Test
    fun `GIVEN OnClickBackButton action was sent WHEN sendAction was called THEN assert that the emitted uiEffect is PopBackStack`() {
        val action = AccountAction.Action.OnClickBackButton

        viewModel.sendAction(action)

        val expectedUiEffect = AccountUiEffect.PopBackStack
        val actualUiEffect = viewModel.uiEffect.getOrAwaitValue()

        assertEquals(expectedUiEffect, actualUiEffect)
    }

    @Test
    fun `GIVEN OnClickEditButton action was sent WHEN sendAction was called THEN assert that the emitted uiEffect is OpenEditAccountScreen`() {
        val action = AccountAction.Action.OnClickEditButton

        viewModel.sendAction(action)

        val expectedUiEffect = AccountUiEffect.OpenEditAccountScreen
        val actualUiEffect = viewModel.uiEffect.getOrAwaitValue()

        assertEquals(expectedUiEffect, actualUiEffect)
    }

    @Test
    fun `GIVEN OnClickResetPasswordButton action was sent WHEN sendAction was called THEN assert that the emitted uiEffect is OpenResetPasswordScreen`() {
        val action = AccountAction.Action.OnClickResetPasswordButton

        viewModel.sendAction(action)

        val expectedUiEffect = AccountUiEffect.OpenResetPasswordScreen
        val actualUiEffect = viewModel.uiEffect.getOrAwaitValue()

        assertEquals(expectedUiEffect, actualUiEffect)
    }

}
