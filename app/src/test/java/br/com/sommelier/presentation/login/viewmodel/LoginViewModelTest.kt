package br.com.sommelier.presentation.login.viewmodel

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import arrow.core.Either
import br.com.sommelier.base.result.Failure
import br.com.sommelier.base.result.GenericProblem
import br.com.sommelier.base.result.Success
import br.com.sommelier.domain.usecase.SignInUserUseCase
import br.com.sommelier.presentation.login.action.LoginAction
import br.com.sommelier.presentation.login.model.LoginUiModel
import br.com.sommelier.presentation.login.res.LoginStringResource
import br.com.sommelier.presentation.login.state.LoginUiEffect
import br.com.sommelier.presentation.login.state.LoginUiState
import br.com.sommelier.testrule.CoroutineTestRule
import br.com.sommelier.util.ext.getOrAwaitValue
import io.mockk.coEvery
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

    private lateinit var viewModel: LoginViewModel

    @Before
    fun setUp() {
        viewModel = LoginViewModel(useCase)
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
    fun `GIVEN OnClickLoginButton action was sent but email and password are empty WHEN sendAction was called THEN assert that emitted uiState is the expected`() =
        coroutineTestRule.runBlockingTest {
            val action = LoginAction.Action.OnClickLoginButton
            val emailErrorSupportingMessage = LoginStringResource.BlankEmail
            val passwordErrorSupportingMessage = LoginStringResource.BlankPassword

            viewModel.sendAction(action)

            val expectedUiState = LoginUiState.Resume(
                LoginUiModel().copy(
                    emailUiState = LoginUiModel().emailUiState.copy(
                        errorSupportingMessage = emailErrorSupportingMessage,
                        isError = true
                    ),
                    passwordUiState = LoginUiModel().passwordUiState.copy(
                        errorSupportingMessage = passwordErrorSupportingMessage,
                        isError = true
                    )
                )
            )
            val actualUiState = viewModel.uiState.getOrAwaitValue()

            assertUiState(expectedUiState, actualUiState)
        }

    @Test
    fun `GIVEN OnClickLoginButton was the last action sent but email is invalid and password is blank WHEN sendAction was called THEN assert that emitted uiState is the expected`() =
        coroutineTestRule.runBlockingTest {
            val action = LoginAction.Action.OnClickLoginButton
            val email = "Email"
            val errorSupportingMessage = LoginStringResource.InvalidEmail
            val passwordErrorSupportingMessage = LoginStringResource.BlankPassword

            viewModel.sendAction(LoginAction.Action.OnTypeEmailField(email))
            viewModel.sendAction(action)

            val expectedUiState = LoginUiState.Resume(
                LoginUiModel().copy(
                    emailUiState = LoginUiModel().emailUiState.copy(
                        text = email,
                        errorSupportingMessage = errorSupportingMessage,
                        isError = true
                    ),
                    passwordUiState = LoginUiModel().passwordUiState.copy(
                        errorSupportingMessage = passwordErrorSupportingMessage,
                        isError = true
                    )
                )
            )
            val actualUiState = viewModel.uiState.getOrAwaitValue()

            assertUiState(expectedUiState, actualUiState)
        }

    @Test
    fun `GIVEN OnClickLoginButton was the last action sent but password is blank WHEN sendAction was called THEN assert that emitted uiState is the expected`() =
        coroutineTestRule.runBlockingTest {
            val action = LoginAction.Action.OnClickLoginButton
            val email = "email@email.com"
            val passwordErrorSupportingMessage = LoginStringResource.BlankPassword

            viewModel.sendAction(LoginAction.Action.OnTypeEmailField(email))
            viewModel.sendAction(action)

            val expectedUiState = LoginUiState.Resume(
                LoginUiModel().copy(
                    emailUiState = LoginUiModel().emailUiState.copy(
                        text = email
                    ),
                    passwordUiState = LoginUiModel().passwordUiState.copy(
                        errorSupportingMessage = passwordErrorSupportingMessage,
                        isError = true
                    )
                )
            )
            val actualUiState = viewModel.uiState.getOrAwaitValue()

            assertUiState(expectedUiState, actualUiState)
        }

    @Test
    fun `GIVEN OnClickLoginButton was the last action sent but email is blank invalid WHEN sendAction was called THEN assert that emitted uiState is the expected`() =
        coroutineTestRule.runBlockingTest {
            val action = LoginAction.Action.OnClickLoginButton
            val emailErrorSupportingMessage = LoginStringResource.BlankEmail
            val password = "password"

            viewModel.sendAction(LoginAction.Action.OnTypePasswordField(password))
            viewModel.sendAction(action)

            val expectedUiState = LoginUiState.Resume(
                LoginUiModel().copy(
                    emailUiState = LoginUiModel().emailUiState.copy(
                        errorSupportingMessage = emailErrorSupportingMessage,
                        isError = true
                    ),
                    passwordUiState = LoginUiModel().passwordUiState.copy(
                        text = password
                    )
                )
            )
            val actualUiState = viewModel.uiState.getOrAwaitValue()

            assertUiState(expectedUiState, actualUiState)
        }

    @Test
    fun `GIVEN OnClickLoginButton was the last action sent but email is invalid WHEN sendAction was called THEN assert that emitted uiState is the expected`() =
        coroutineTestRule.runBlockingTest {
            val action = LoginAction.Action.OnClickLoginButton
            val email = "email"
            val emailErrorSupportingMessage = LoginStringResource.InvalidEmail
            val password = "password"

            viewModel.sendAction(LoginAction.Action.OnTypeEmailField(email))
            viewModel.sendAction(LoginAction.Action.OnTypePasswordField(password))
            viewModel.sendAction(action)

            val expectedUiState = LoginUiState.Resume(
                LoginUiModel().copy(
                    emailUiState = LoginUiModel().emailUiState.copy(
                        text = email,
                        errorSupportingMessage = emailErrorSupportingMessage,
                        isError = true
                    ),
                    passwordUiState = LoginUiModel().passwordUiState.copy(
                        text = password
                    )
                )
            )
            val actualUiState = viewModel.uiState.getOrAwaitValue()

            assertUiState(expectedUiState, actualUiState)
        }

    @Test
    fun `GIVEN OnClickLoginButton was the last action sent and all fields are valid WHEN sendAction was called THEN assert that emitted uiState and uiEffect are the expected`() =
        coroutineTestRule.runBlockingTest {
            val action = LoginAction.Action.OnClickLoginButton
            val email = "email@email.com"
            val password = "password"

            viewModel.sendAction(LoginAction.Action.OnTypeEmailField(email))
            viewModel.sendAction(LoginAction.Action.OnTypePasswordField(password))
            viewModel.sendAction(action)

            val expectedUiState = LoginUiState.Loading(
                LoginUiModel().copy(
                    emailUiState = LoginUiModel().emailUiState.copy(
                        text = email
                    ),
                    passwordUiState = LoginUiModel().passwordUiState.copy(
                        text = password
                    )
                )
            )
            val actualUiState = viewModel.uiState.getOrAwaitValue()

            val expectedUiEffect = LoginUiEffect.ShowLoading
            val actualUiEffect = viewModel.uiEffect.getOrAwaitValue()

            assertUiState(expectedUiState, actualUiState)
            assertEquals(expectedUiEffect, actualUiEffect)
        }

    @Test
    fun `GIVEN OnTryToLogin was the last action sent and use case as failure WHEN sendAction was called THEN assert that the emitted uiState and uiEffect are the expected`() =
        coroutineTestRule.runBlockingTest {
            val action = LoginAction.Action.OnTryToLogin
            val email = "email@email.com"
            val password = "password"

            val result: Either<Failure, Success<Unit>> = Either.Left(
                Failure(
                    GenericProblem(
                        "Error"
                    )
                )
            )
            coEvery {
                useCase(
                    SignInUserUseCase.Params(
                        email,
                        password
                    )
                )
            } returns result

            viewModel.sendAction(LoginAction.Action.OnTypeEmailField(email))
            viewModel.sendAction(LoginAction.Action.OnTypePasswordField(password))
            viewModel.sendAction(action)

            val expectedUiState = LoginUiState.Error(
                LoginUiModel().copy(
                    emailUiState = LoginUiModel().emailUiState.copy(
                        text = email
                    ),
                    passwordUiState = LoginUiModel().passwordUiState.copy(
                        text = password
                    )
                )
            )
            val actualUiState = viewModel.uiState.getOrAwaitValue()

            val expectedUiEffect = LoginUiEffect.ShowSnackbarError
            val actualUiEffect = viewModel.uiEffect.getOrAwaitValue()

            assertUiState(expectedUiState, actualUiState)
            assertEquals(expectedUiEffect, actualUiEffect)
        }

    @Test
    fun `GIVEN OnTryToLogin was the last action sent and use case as success WHEN sendAction was called THEN assert that the uiEffect is the expected`() =
        coroutineTestRule.runBlockingTest {
            val action = LoginAction.Action.OnTryToLogin
            val email = "email@email.com"
            val password = "password"

            val result: Either<Failure, Success<Unit>> = Either.Right(Success(Unit))
            coEvery {
                useCase(
                    SignInUserUseCase.Params(
                        email,
                        password
                    )
                )
            } returns result

            viewModel.sendAction(LoginAction.Action.OnTypeEmailField(email))
            viewModel.sendAction(LoginAction.Action.OnTypePasswordField(password))
            viewModel.sendAction(action)

            val expectedUiEffect = LoginUiEffect.OpenHomeScreen
            val actualUiEffect = viewModel.uiEffect.getOrAwaitValue()

            assertEquals(expectedUiEffect, actualUiEffect)
        }

    @Test
    fun `GIVEN OnClickSignUpButton action was sent WHEN sendAction was called THEN assert that emitted uiEffect must be OpenSignUpScreen`() =
        coroutineTestRule.runBlockingTest {
            val action = LoginAction.Action.OnClickSignUpButton

            viewModel.sendAction(action)

            val uiEffect = viewModel.uiEffect.getOrAwaitValue()

            assertTrue(uiEffect is LoginUiEffect.OpenRegisterScreen)
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
        assertEquals(
            expected.uiModel.snackbarUiState.type,
            actual.uiModel.snackbarUiState.type
        )
        assertEquals(
            expected.uiModel.snackbarUiState.hostState.currentSnackbarData,
            actual.uiModel.snackbarUiState.hostState.currentSnackbarData
        )
    }
}
