package br.com.sommelier.presentation.register.viewmodel

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import arrow.core.Either
import br.com.sommelier.base.result.Failure
import br.com.sommelier.base.result.GenericProblem
import br.com.sommelier.base.result.Success
import br.com.sommelier.domain.model.UserInfo
import br.com.sommelier.domain.usecase.CreateUserUseCase
import br.com.sommelier.domain.usecase.SendEmailVerificationUseCase
import br.com.sommelier.presentation.register.action.RegisterAction
import br.com.sommelier.presentation.register.model.EmailUiState
import br.com.sommelier.presentation.register.model.NameUiState
import br.com.sommelier.presentation.register.model.PasswordConfirmationUiState
import br.com.sommelier.presentation.register.model.PasswordUiState
import br.com.sommelier.presentation.register.model.RegisterUiModel
import br.com.sommelier.presentation.register.res.RegisterStringResource
import br.com.sommelier.presentation.register.state.RegisterUiEffect
import br.com.sommelier.presentation.register.state.RegisterUiState
import br.com.sommelier.testrule.CoroutineTestRule
import br.com.sommelier.util.ext.getOrAwaitValue
import io.mockk.coEvery
import io.mockk.mockk
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import kotlin.test.assertEquals

class RegisterViewModelTest {

    @get:Rule
    val instantTaskExecutorRule = InstantTaskExecutorRule()

    @get:Rule
    val coroutineTestRule = CoroutineTestRule()

    private val createUserUseCase = mockk<CreateUserUseCase>()

    private val sendEmailVerificationUseCase = mockk<SendEmailVerificationUseCase>()

    private lateinit var viewModel: RegisterViewModel

    @Before
    fun setUp() {
        viewModel = RegisterViewModel(createUserUseCase, sendEmailVerificationUseCase)
    }

    @Test
    fun `GIVEN OnTypeNameField action was sent WHEN sendAction was called THEN assert that the emitted ui state is the expected`() {
        val name = "Name"
        val action = RegisterAction.Action.OnTypeNameField(name)

        viewModel.sendAction(action)

        val expectedUiState = RegisterUiState.Resume(
            uiModel = RegisterUiModel().copy(
                nameUiState = RegisterUiModel().nameUiState.copy(
                    text = name
                )
            )
        )
        val actualUiState = viewModel.uiState.getOrAwaitValue()

        assertUiState(expectedUiState, actualUiState)
    }

    @Test
    fun `GIVEN OnTypeEmailField action was sent WHEN sendAction was called THEN assert that the emitted ui state is the expected`() {
        val email = "Email"
        val action = RegisterAction.Action.OnTypeEmailField(email)

        viewModel.sendAction(action)

        val expectedUiState = RegisterUiState.Resume(
            uiModel = RegisterUiModel().copy(
                emailUiState = RegisterUiModel().emailUiState.copy(
                    text = email
                )
            )
        )
        val actualUiState = viewModel.uiState.getOrAwaitValue()

        assertUiState(expectedUiState, actualUiState)
    }

    @Test
    fun `GIVEN OnTypePasswordField action was sent WHEN sendAction was called THEN assert that the emitted ui state is the expected`() {
        val password = "password"
        val action = RegisterAction.Action.OnTypePasswordField(password)

        viewModel.sendAction(action)

        val expectedUiState = RegisterUiState.Resume(
            uiModel = RegisterUiModel().copy(
                passwordUiState = RegisterUiModel().passwordUiState.copy(
                    text = password
                )
            )
        )
        val actualUiState = viewModel.uiState.getOrAwaitValue()

        assertUiState(expectedUiState, actualUiState)
    }

    @Test
    fun `GIVEN OnTypePasswordConfirmationField action was sent WHEN sendAction was called THEN assert that the emitted ui state is the expected`() {
        val passwordConfirmation = "password"
        val action = RegisterAction.Action.OnTypePasswordConfirmationField(passwordConfirmation)

        viewModel.sendAction(action)

        val expectedUiState = RegisterUiState.Resume(
            uiModel = RegisterUiModel().copy(
                passwordConfirmationUiState = RegisterUiModel().passwordConfirmationUiState
                    .copy(
                        text = passwordConfirmation
                    )
            )
        )
        val actualUiState = viewModel.uiState.getOrAwaitValue()

        assertUiState(expectedUiState, actualUiState)
    }

    @Test
    fun `GIVEN OnOnClickRegisterButton action was the last action sent but name is blank WHEN sendAction was called THEN assert that the emitted ui state is the expected`() {
        val action = RegisterAction.Action.OnClickRegisterButton
        val email = "Email@email.com"
        val password = "password"
        val passwordConfirmation = "password"

        viewModel.sendAction(RegisterAction.Action.OnTypeEmailField(email))
        viewModel.sendAction(RegisterAction.Action.OnTypePasswordField(password))
        viewModel.sendAction(
            RegisterAction.Action.OnTypePasswordConfirmationField(
                passwordConfirmation
            )
        )
        viewModel.sendAction(action)

        val expectedUiState = RegisterUiState.Resume(
            uiModel = RegisterUiModel().copy(
                nameUiState = RegisterUiModel().nameUiState.copy(
                    isError = true,
                    errorSupportingMessage = RegisterStringResource.BlankName
                ),
                emailUiState = RegisterUiModel().emailUiState.copy(
                    text = email
                ),
                passwordUiState = RegisterUiModel().passwordUiState.copy(
                    text = password
                ),
                passwordConfirmationUiState = RegisterUiModel().passwordConfirmationUiState
                    .copy(
                        text = passwordConfirmation
                    )
            )
        )
        val actualUiState = viewModel.uiState.getOrAwaitValue()

        assertUiState(expectedUiState, actualUiState)
    }

    @Test
    fun `GIVEN OnOnClickRegisterButton action was the last action sent but name is invalid WHEN sendAction was called THEN assert that the emitted ui state is the expected`() {
        val action = RegisterAction.Action.OnClickRegisterButton
        val name = "Na"
        val email = "Email@email.com"
        val password = "password"
        val passwordConfirmation = "password"

        viewModel.sendAction(RegisterAction.Action.OnTypeNameField(name))
        viewModel.sendAction(RegisterAction.Action.OnTypeEmailField(email))
        viewModel.sendAction(RegisterAction.Action.OnTypePasswordField(password))
        viewModel.sendAction(
            RegisterAction.Action.OnTypePasswordConfirmationField(
                passwordConfirmation
            )
        )
        viewModel.sendAction(action)

        val expectedUiState = RegisterUiState.Resume(
            uiModel = RegisterUiModel().copy(
                nameUiState = RegisterUiModel().nameUiState.copy(
                    text = name,
                    isError = true,
                    errorSupportingMessage = RegisterStringResource.InvalidName
                ),
                emailUiState = RegisterUiModel().emailUiState.copy(
                    text = email
                ),
                passwordUiState = RegisterUiModel().passwordUiState.copy(
                    text = password
                ),
                passwordConfirmationUiState = RegisterUiModel().passwordConfirmationUiState
                    .copy(
                        text = passwordConfirmation
                    )
            )
        )
        val actualUiState = viewModel.uiState.getOrAwaitValue()

        assertUiState(expectedUiState, actualUiState)
    }

    @Test
    fun `GIVEN OnOnClickRegisterButton action was the last action sent but email is blank WHEN sendAction was called THEN assert that the emitted ui state is the expected`() {
        val action = RegisterAction.Action.OnClickRegisterButton
        val name = "Name"
        val password = "password"
        val passwordConfirmation = "password"

        viewModel.sendAction(RegisterAction.Action.OnTypeNameField(name))
        viewModel.sendAction(RegisterAction.Action.OnTypePasswordField(password))
        viewModel.sendAction(
            RegisterAction.Action.OnTypePasswordConfirmationField(
                passwordConfirmation
            )
        )
        viewModel.sendAction(action)

        val expectedUiState = RegisterUiState.Resume(
            uiModel = RegisterUiModel().copy(
                nameUiState = RegisterUiModel().nameUiState.copy(
                    text = name
                ),
                emailUiState = RegisterUiModel().emailUiState.copy(
                    isError = true,
                    errorSupportingMessage = RegisterStringResource.BlankEmail
                ),
                passwordUiState = RegisterUiModel().passwordUiState.copy(
                    text = password
                ),
                passwordConfirmationUiState = RegisterUiModel().passwordConfirmationUiState
                    .copy(
                        text = passwordConfirmation
                    )
            )
        )
        val actualUiState = viewModel.uiState.getOrAwaitValue()

        assertUiState(expectedUiState, actualUiState)
    }

    @Test
    fun `GIVEN OnOnClickRegisterButton action was the last action sent but email is invalid WHEN sendAction was called THEN assert that the emitted ui state is the expected`() {
        val action = RegisterAction.Action.OnClickRegisterButton
        val name = "Name"
        val email = "Email"
        val password = "password"
        val passwordConfirmation = "password"

        viewModel.sendAction(RegisterAction.Action.OnTypeNameField(name))
        viewModel.sendAction(RegisterAction.Action.OnTypeEmailField(email))
        viewModel.sendAction(RegisterAction.Action.OnTypePasswordField(password))
        viewModel.sendAction(
            RegisterAction.Action.OnTypePasswordConfirmationField(
                passwordConfirmation
            )
        )
        viewModel.sendAction(action)

        val expectedUiState = RegisterUiState.Resume(
            uiModel = RegisterUiModel().copy(
                nameUiState = RegisterUiModel().nameUiState.copy(
                    text = name
                ),
                emailUiState = RegisterUiModel().emailUiState.copy(
                    text = email,
                    isError = true,
                    errorSupportingMessage = RegisterStringResource.InvalidEmail
                ),
                passwordUiState = RegisterUiModel().passwordUiState.copy(
                    text = password
                ),
                passwordConfirmationUiState = RegisterUiModel().passwordConfirmationUiState
                    .copy(
                        text = passwordConfirmation
                    )
            )
        )
        val actualUiState = viewModel.uiState.getOrAwaitValue()

        assertUiState(expectedUiState, actualUiState)
    }

    @Test
    fun `GIVEN OnOnClickRegisterButton action was the last action sent but password is blank WHEN sendAction was called THEN assert that the emitted ui state is the expected`() {
        val action = RegisterAction.Action.OnClickRegisterButton
        val name = "Name"
        val email = "Email@email.com"
        val passwordConfirmation = "password"

        viewModel.sendAction(RegisterAction.Action.OnTypeNameField(name))
        viewModel.sendAction(RegisterAction.Action.OnTypeEmailField(email))
        viewModel.sendAction(
            RegisterAction.Action.OnTypePasswordConfirmationField(
                passwordConfirmation
            )
        )
        viewModel.sendAction(action)

        val expectedUiState = RegisterUiState.Resume(
            uiModel = RegisterUiModel().copy(
                nameUiState = RegisterUiModel().nameUiState.copy(
                    text = name
                ),
                emailUiState = RegisterUiModel().emailUiState.copy(
                    text = email
                ),
                passwordUiState = RegisterUiModel().passwordUiState.copy(
                    isError = true,
                    errorSupportingMessage = RegisterStringResource.BlankPassword
                ),
                passwordConfirmationUiState = RegisterUiModel().passwordConfirmationUiState
                    .copy(
                        text = passwordConfirmation,
                        isError = true,
                        errorSupportingMessage = RegisterStringResource.PasswordConfirmationNotMatch
                    )
            )
        )
        val actualUiState = viewModel.uiState.getOrAwaitValue()

        assertUiState(expectedUiState, actualUiState)
    }

    @Test
    fun `GIVEN OnOnClickRegisterButton action was the last action sent but password is invalid WHEN sendAction was called THEN assert that the emitted ui state is the expected`() {
        val action = RegisterAction.Action.OnClickRegisterButton
        val name = "Name"
        val email = "Email@email.com"
        val password = "pass"
        val passwordConfirmation = "password"

        viewModel.sendAction(RegisterAction.Action.OnTypeNameField(name))
        viewModel.sendAction(RegisterAction.Action.OnTypeEmailField(email))
        viewModel.sendAction(RegisterAction.Action.OnTypePasswordField(password))
        viewModel.sendAction(
            RegisterAction.Action.OnTypePasswordConfirmationField(
                passwordConfirmation
            )
        )
        viewModel.sendAction(action)

        val expectedUiState = RegisterUiState.Resume(
            uiModel = RegisterUiModel().copy(
                nameUiState = RegisterUiModel().nameUiState.copy(
                    text = name
                ),
                emailUiState = RegisterUiModel().emailUiState.copy(
                    text = email
                ),
                passwordUiState = RegisterUiModel().passwordUiState.copy(
                    text = password,
                    isError = true,
                    errorSupportingMessage = RegisterStringResource.InvalidPassword
                ),
                passwordConfirmationUiState = RegisterUiModel().passwordConfirmationUiState
                    .copy(
                        text = passwordConfirmation,
                        isError = true,
                        errorSupportingMessage = RegisterStringResource.PasswordConfirmationNotMatch
                    )
            )
        )
        val actualUiState = viewModel.uiState.getOrAwaitValue()

        assertUiState(expectedUiState, actualUiState)
    }

    @Test
    fun `GIVEN OnOnClickRegisterButton action was the last action sent but confirmationPassword is blank WHEN sendAction was called THEN assert that the emitted ui state is the expected`() {
        val action = RegisterAction.Action.OnClickRegisterButton
        val name = "Name"
        val email = "Email@email.com"
        val password = "password"

        viewModel.sendAction(RegisterAction.Action.OnTypeNameField(name))
        viewModel.sendAction(RegisterAction.Action.OnTypeEmailField(email))
        viewModel.sendAction(RegisterAction.Action.OnTypePasswordField(password))
        viewModel.sendAction(action)

        val expectedUiState = RegisterUiState.Resume(
            uiModel = RegisterUiModel().copy(
                nameUiState = RegisterUiModel().nameUiState.copy(
                    text = name
                ),
                emailUiState = RegisterUiModel().emailUiState.copy(
                    text = email
                ),
                passwordUiState = RegisterUiModel().passwordUiState.copy(
                    text = password
                ),
                passwordConfirmationUiState = RegisterUiModel().passwordConfirmationUiState
                    .copy(
                        isError = true,
                        errorSupportingMessage = RegisterStringResource.BlankPasswordConfirmation
                    )
            )
        )
        val actualUiState = viewModel.uiState.getOrAwaitValue()

        assertUiState(expectedUiState, actualUiState)
    }

    @Test
    fun `GIVEN OnOnClickRegisterButton action was the last action sent but passwordConfirmation not match WHEN sendAction was called THEN assert that the emitted ui state is the expected`() {
        val action = RegisterAction.Action.OnClickRegisterButton
        val name = "Name"
        val email = "Email@email.com"
        val password = "password"
        val passwordConfirmation = "pass"

        viewModel.sendAction(RegisterAction.Action.OnTypeNameField(name))
        viewModel.sendAction(RegisterAction.Action.OnTypeEmailField(email))
        viewModel.sendAction(RegisterAction.Action.OnTypePasswordField(password))
        viewModel.sendAction(
            RegisterAction.Action.OnTypePasswordConfirmationField(
                passwordConfirmation
            )
        )
        viewModel.sendAction(action)

        val expectedUiState = RegisterUiState.Resume(
            uiModel = RegisterUiModel().copy(
                nameUiState = RegisterUiModel().nameUiState.copy(
                    text = name
                ),
                emailUiState = RegisterUiModel().emailUiState.copy(
                    text = email
                ),
                passwordUiState = RegisterUiModel().passwordUiState.copy(
                    text = password
                ),
                passwordConfirmationUiState = RegisterUiModel().passwordConfirmationUiState
                    .copy(
                        text = passwordConfirmation,
                        isError = true,
                        errorSupportingMessage = RegisterStringResource.PasswordConfirmationNotMatch
                    )
            )
        )
        val actualUiState = viewModel.uiState.getOrAwaitValue()

        assertUiState(expectedUiState, actualUiState)
    }

    @Test
    fun `GIVEN OnOnClickRegisterButton action was the last action sent and all fields are valid WHEN sendAction was called THEN assert that the emitted ui state and ui effect are the expected`() {
        val action = RegisterAction.Action.OnClickRegisterButton
        val name = "Name"
        val email = "Email@email.com"
        val password = "password"
        val passwordConfirmation = "password"

        viewModel.sendAction(RegisterAction.Action.OnTypeNameField(name))
        viewModel.sendAction(RegisterAction.Action.OnTypeEmailField(email))
        viewModel.sendAction(RegisterAction.Action.OnTypePasswordField(password))
        viewModel.sendAction(
            RegisterAction.Action.OnTypePasswordConfirmationField(
                passwordConfirmation
            )
        )
        viewModel.sendAction(action)

        val expectedUiState = RegisterUiState.Loading(
            uiModel = RegisterUiModel().copy(
                nameUiState = RegisterUiModel().nameUiState.copy(
                    text = name
                ),
                emailUiState = RegisterUiModel().emailUiState.copy(
                    text = email
                ),
                passwordUiState = RegisterUiModel().passwordUiState.copy(
                    text = password
                ),
                passwordConfirmationUiState = RegisterUiModel().passwordConfirmationUiState
                    .copy(
                        text = passwordConfirmation
                    )
            )
        )
        val actualUiState = viewModel.uiState.getOrAwaitValue()

        val expectedUiEffect = RegisterUiEffect.ShowLoading
        val actualUiEffect = viewModel.uiEffect.getOrAwaitValue()

        assertUiState(expectedUiState, actualUiState)
        assertEquals(expectedUiEffect, actualUiEffect)
    }

    @Test
    fun `GIVEN OnTryToRegister action was the last action sent and use case as failure WHEN sendAction was called THEN assert that the ui state and ui effect are the expected`() =
        coroutineTestRule.runBlockingTest {
            val userInfo = UserInfo(
                name = "Name",
                email = "Email@email.com",
                password = "Password"
            )

            val result: Either<Failure, Success<Unit>> = Either.Left(
                Failure(
                    GenericProblem("Error")
                )
            )
            coEvery { createUserUseCase(userInfo) } returns result

            val action = RegisterAction.Action.OnTryToRegister

            viewModel.sendAction(RegisterAction.Action.OnTypeNameField(userInfo.name))
            viewModel.sendAction(RegisterAction.Action.OnTypeEmailField(userInfo.email))
            viewModel.sendAction(RegisterAction.Action.OnTypePasswordField(userInfo.password))
            viewModel.sendAction(
                RegisterAction.Action.OnTypePasswordConfirmationField(userInfo.password)
            )
            viewModel.sendAction(action)

            val expectedUiState = RegisterUiState.Error(
                RegisterUiModel().copy(
                    nameUiState = RegisterUiModel().nameUiState.copy(
                        text = userInfo.name
                    ),
                    emailUiState = RegisterUiModel().emailUiState.copy(
                        text = userInfo.email
                    ),
                    passwordUiState = RegisterUiModel().passwordUiState.copy(
                        text = userInfo.password
                    ),
                    passwordConfirmationUiState = RegisterUiModel().passwordConfirmationUiState
                        .copy(
                            text = userInfo.password
                        )
                )
            )
            val actualUiState = viewModel.uiState.getOrAwaitValue()

            val expectedUiEffect = RegisterUiEffect.ShowSnackbarError
            val actualUiEffect = viewModel.uiEffect.getOrAwaitValue()

            assertUiState(expectedUiState, actualUiState)
            assertEquals(expectedUiEffect, actualUiEffect)
        }

    @Test
    fun `GIVEN OnTryToRegister action was the last action sent, use case as failure and fields with error state WHEN sendAction was called THEN assert that the ui state and ui effect are the expected`() =
        coroutineTestRule.runBlockingTest {
            val userInfo = UserInfo(
                name = "Name",
                email = "Email@email.com",
                password = "Password"
            )

            val result: Either<Failure, Success<Unit>> = Either.Left(
                Failure(
                    GenericProblem("Error")
                )
            )
            coEvery { createUserUseCase(userInfo) } returns result

            val action = RegisterAction.Action.OnTryToRegister

            viewModel.sendAction(RegisterAction.Action.OnClickRegisterButton)
            viewModel.sendAction(RegisterAction.Action.OnTypeNameField(userInfo.name))
            viewModel.sendAction(RegisterAction.Action.OnTypeEmailField(userInfo.email))
            viewModel.sendAction(RegisterAction.Action.OnTypePasswordField(userInfo.password))
            viewModel.sendAction(
                RegisterAction.Action.OnTypePasswordConfirmationField(userInfo.password)
            )
            viewModel.sendAction(action)

            val expectedUiState = RegisterUiState.Error(
                RegisterUiModel().copy(
                    nameUiState = NameUiState(
                        text = userInfo.name,
                        errorSupportingMessage = RegisterStringResource.Empty,
                        isError = false
                    ),
                    emailUiState = EmailUiState(
                        text = userInfo.email,
                        errorSupportingMessage = RegisterStringResource.Empty,
                        isError = false
                    ),
                    passwordUiState = PasswordUiState(
                        text = userInfo.password,
                        errorSupportingMessage = RegisterStringResource.Empty,
                        isError = false
                    ),
                    passwordConfirmationUiState = PasswordConfirmationUiState(
                        text = userInfo.password,
                        errorSupportingMessage = RegisterStringResource.Empty,
                        isError = false
                    )
                )
            )
            val actualUiState = viewModel.uiState.getOrAwaitValue()

            val expectedUiEffect = RegisterUiEffect.ShowSnackbarError
            val actualUiEffect = viewModel.uiEffect.getOrAwaitValue()

            assertUiState(expectedUiState, actualUiState)
            assertEquals(expectedUiEffect, actualUiEffect)
        }

    @Test
    fun `GIVEN OnTryToRegister action was the last action sent and both use cases as success WHEN sendAction was called THEN assert that the ui effect is the expected`() =
        coroutineTestRule.runBlockingTest {
            val userInfo = UserInfo(
                name = "Name",
                email = "Email@email.com",
                password = "Password"
            )

            val createUserUseCaseResult: Either<Failure, Success<Unit>> = Either.Right(
                Success(Unit)
            )
            coEvery { createUserUseCase(userInfo) } returns createUserUseCaseResult

            val sendEmailVerificationUseCaseResult: Either<Failure, Success<Unit>> = Either.Right(
                Success(Unit)
            )
            coEvery {
                sendEmailVerificationUseCase(any())
            } returns sendEmailVerificationUseCaseResult

            val action = RegisterAction.Action.OnTryToRegister

            viewModel.sendAction(RegisterAction.Action.OnTypeNameField(userInfo.name))
            viewModel.sendAction(RegisterAction.Action.OnTypeEmailField(userInfo.email))
            viewModel.sendAction(RegisterAction.Action.OnTypePasswordField(userInfo.password))
            viewModel.sendAction(
                RegisterAction.Action.OnTypePasswordConfirmationField(userInfo.password)
            )
            viewModel.sendAction(action)

            val expectedUiEffect = RegisterUiEffect.OpenConfirmEmailScreen
            val actualUiEffect = viewModel.uiEffect.getOrAwaitValue()

            assertEquals(expectedUiEffect, actualUiEffect)
        }

    @Test
    fun `GIVEN OnTryToRegister action was the last action sent and sendEmailVerification use case as failure WHEN sendAction was called THEN assert that the ui effect is the expected`() =
        coroutineTestRule.runBlockingTest {
            val userInfo = UserInfo(
                name = "Name",
                email = "Email@email.com",
                password = "Password"
            )

            val createUserUseCaseResult: Either<Failure, Success<Unit>> = Either.Right(
                Success(Unit)
            )
            coEvery { createUserUseCase(userInfo) } returns createUserUseCaseResult

            val sendEmailVerificationUseCaseResult: Either<Failure, Success<Unit>> = Either.Left(
                Failure(GenericProblem("Error"))
            )
            coEvery {
                sendEmailVerificationUseCase(any())
            } returns sendEmailVerificationUseCaseResult

            val action = RegisterAction.Action.OnTryToRegister

            viewModel.sendAction(RegisterAction.Action.OnTypeNameField(userInfo.name))
            viewModel.sendAction(RegisterAction.Action.OnTypeEmailField(userInfo.email))
            viewModel.sendAction(RegisterAction.Action.OnTypePasswordField(userInfo.password))
            viewModel.sendAction(
                RegisterAction.Action.OnTypePasswordConfirmationField(userInfo.password)
            )
            viewModel.sendAction(action)

            val expectedUiEffect = RegisterUiEffect.OpenConfirmEmailScreen
            val actualUiEffect = viewModel.uiEffect.getOrAwaitValue()

            assertEquals(expectedUiEffect, actualUiEffect)
        }

    @Test
    fun `GIVEN OnClickAlreadyHaveAccountButton was sent WHEN sendAction was called THEN assert that the emitted ui effect is OpenLoginScreen`() {
        val action = RegisterAction.Action.OnClickAlreadyHaveAccountButton

        viewModel.sendAction(action)

        val expectedUiEffect = RegisterUiEffect.OpenLoginScreen
        val actualUiEffect = viewModel.uiEffect.getOrAwaitValue()

        assertEquals(expectedUiEffect, actualUiEffect)
    }

    private fun assertUiState(expected: RegisterUiState, actual: RegisterUiState) {
        assertEquals(expected.javaClass, actual.javaClass)
        assertEquals(expected.uiModel.nameUiState.text, actual.uiModel.nameUiState.text)
        assertEquals(
            expected.uiModel.nameUiState.errorSupportingMessage,
            actual.uiModel.nameUiState.errorSupportingMessage
        )
        assertEquals(
            expected.uiModel.nameUiState.isError,
            actual.uiModel.nameUiState.isError
        )
        assertEquals(expected.uiModel.emailUiState.text, actual.uiModel.emailUiState.text)
        assertEquals(
            expected.uiModel.emailUiState.errorSupportingMessage,
            actual.uiModel.emailUiState.errorSupportingMessage
        )
        assertEquals(
            expected.uiModel.emailUiState.isError,
            actual.uiModel.emailUiState.isError
        )
        assertEquals(
            expected.uiModel.passwordUiState.text,
            actual.uiModel.passwordUiState.text
        )
        assertEquals(
            expected.uiModel.passwordUiState.errorSupportingMessage,
            actual.uiModel.passwordUiState.errorSupportingMessage
        )
        assertEquals(
            expected.uiModel.passwordUiState.isError,
            actual.uiModel.passwordUiState.isError
        )
        assertEquals(
            expected.uiModel.passwordConfirmationUiState.text,
            actual.uiModel.passwordConfirmationUiState.text
        )
        assertEquals(
            expected.uiModel.passwordConfirmationUiState.errorSupportingMessage,
            actual.uiModel.passwordConfirmationUiState.errorSupportingMessage
        )
        assertEquals(
            expected.uiModel.passwordConfirmationUiState.isError,
            actual.uiModel.passwordConfirmationUiState.isError
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
