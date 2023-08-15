package br.com.sommelier.presentation.login.viewmodel

import android.content.Context
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import br.com.sommelier.domain.usecase.SignInUserUseCase
import br.com.sommelier.presentation.login.action.LoginAction
import br.com.sommelier.presentation.login.model.LoginUiModel
import br.com.sommelier.presentation.login.state.LoginUiEffect
import br.com.sommelier.presentation.login.state.LoginUiState
import br.com.sommelier.presentation.viewmodel.LoginViewModel
import br.com.sommelier.test_rule.CoroutineTestRule
import br.com.sommelier.util.emptyString
import br.com.sommelier.util.ext.getOrAwaitValue
import br.com.sommelier.util.validator.isValidEmail
import io.mockk.every
import io.mockk.mockk
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Rule
import org.junit.Test

class LoginViewModelTest {

    @get:Rule
    val instantTaskExecutorRule = InstantTaskExecutorRule()

    @get:Rule
    val coroutineTestRule = CoroutineTestRule()

    private val useCase = mockk<SignInUserUseCase>()

    private val context = mockk<Context>()

    private lateinit var viewModel: LoginViewModel

    @Before
    fun setUp() {
        viewModel = LoginViewModel(context, useCase)
    }

    @Test
    fun `GIVEN OnTypeEmailField action was sent WHEN sendAction was called THEN assert that emitted uiState is the expected`() =
        coroutineTestRule.runBlockingTest {
            val email = "Email"
            val action = LoginAction.Action.OnTypeEmailField(email)

            viewModel.sendAction(action)

            val expectedUiState = LoginUiState.Resume(
                LoginUiModel().copy(
                    emailUiState = LoginUiModel().emailUiState.copy(text = email)
                )
            )

            val actualUiState = viewModel.uiState.getOrAwaitValue()

            assertUiState(expectedUiState, actualUiState)
        }

    @Test
    fun `GIVEN OnTypePasswordField action was sent WHEN sendAction was called THEN assert that emitted uiState is the expected`() =
        coroutineTestRule.runBlockingTest {
            val password = "Password"
            val action = LoginAction.Action.OnTypePasswordField(password)

            viewModel.sendAction(action)

            val expectedUiState = LoginUiState.Resume(
                LoginUiModel().copy(
                    passwordUiState = LoginUiModel().passwordUiState.copy(text = password)
                )
            )

            val actualUiState = viewModel.uiState.getOrAwaitValue()

            assertUiState(expectedUiState, actualUiState)
        }

    @Test
    fun `GIVEN OnClickLoginButton action was sent and email is empty WHEN sendAction was called THEN assert that emitted uiState is the expected`() =
        coroutineTestRule.runBlockingTest {
            val action = LoginAction.Action.OnClickLoginButton

            val errorSupportingMessage = "Email cannot be blank"

            every { context.getString(any()) } returnsMany listOf(
                errorSupportingMessage,
                emptyString()
            )

            viewModel.sendAction(action)

            val expectedUiState = LoginUiState.Resume(
                LoginUiModel().copy(
                    emailUiState = LoginUiModel().emailUiState.copy(
                        errorSupportingMessage = errorSupportingMessage,
                        isError = true
                    )
                )
            )

            val actualUiState = viewModel.uiState.getOrAwaitValue()

            assertUiState(expectedUiState, actualUiState)
        }

    @Test
    fun `GIVEN OnClickLoginButton action was sent and email is invalid WHEN sendAction was called THEN assert that emitted uiState is the expected`() =
        coroutineTestRule.runBlockingTest {
            val action = LoginAction.Action.OnClickLoginButton

            val email = "Email"
            val errorSupportingMessage = "Invalid email"

            every { context.getString(any()) } returnsMany listOf(
                errorSupportingMessage,
                emptyString()
            )

            every { isValidEmail(email) } returns false

            viewModel.sendAction(LoginAction.Action.OnTypeEmailField(email))
            viewModel.sendAction(action)

            val expectedUiState = LoginUiState.Resume(
                LoginUiModel().copy(
                    emailUiState = LoginUiModel().emailUiState.copy(
                        text = email,
                        errorSupportingMessage = errorSupportingMessage,
                        isError = true
                    )
                )
            )

            val actualUiState = viewModel.uiState.getOrAwaitValue()

            assertUiState(expectedUiState, actualUiState)
        }

    @Test
    fun `GIVEN OnClickSignUpButton action was sent WHEN sendAction was called THEN assert that emitted uiEffect must be OpenSignUpScreen`() =
        coroutineTestRule.runBlockingTest {
            val action = LoginAction.Action.OnClickSignUpButton

            viewModel.sendAction(action)

            val uiEffect = viewModel.uiEffect.getOrAwaitValue()
            assertTrue(uiEffect is LoginUiEffect.OpenSignUpScreen)
        }

    @Test
    fun `GIVEN OnClickForgotPasswordButton action was sent WHEN sendAction was called THEN assert that emitted uiEffect must be OpenForgotPasswordScreen`() =
        coroutineTestRule.runBlockingTest {
            val action = LoginAction.Action.OnClickForgotPasswordButton

            viewModel.sendAction(action)

            val uiEffect = viewModel.uiEffect.getOrAwaitValue()
            assertTrue(uiEffect is LoginUiEffect.OpenForgotPasswordScreen)
        }

    private fun assertUiState(expected: LoginUiState, actual: LoginUiState) {
        assertEquals(expected.javaClass, actual.javaClass)
        assertEquals(expected.uiModel.emailUiState.text, actual.uiModel.emailUiState.text)
        assertEquals(
            expected.uiModel.emailUiState.errorSupportingMessage,
            actual.uiModel.emailUiState.errorSupportingMessage
        )
        assertEquals(expected.uiModel.emailUiState.isError, actual.uiModel.emailUiState.isError)
        assertEquals(expected.uiModel.passwordUiState.text, actual.uiModel.passwordUiState.text)
        assertEquals(
            expected.uiModel.passwordUiState.errorSupportingMessage,
            actual.uiModel.passwordUiState.errorSupportingMessage
        )
        assertEquals(
            expected.uiModel.passwordUiState.isError,
            actual.uiModel.passwordUiState.isError
        )
        assertEquals(expected.uiModel.snackBarUiState.text, actual.uiModel.snackBarUiState.text)
        assertEquals(
            expected.uiModel.snackBarUiState.type,
            actual.uiModel.snackBarUiState.type
        )
        assertEquals(
            expected.uiModel.snackBarUiState.hostState.currentSnackbarData,
            actual.uiModel.snackBarUiState.hostState.currentSnackbarData
        )
    }
}
