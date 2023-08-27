package br.com.sommelier.presentation.passwordreset.viewmodel

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import arrow.core.Either
import br.com.sommelier.base.result.Failure
import br.com.sommelier.base.result.GenericProblem
import br.com.sommelier.base.result.Success
import br.com.sommelier.domain.usecase.SendPasswordResetEmailUseCase
import br.com.sommelier.presentation.passwordreset.action.PasswordResetAction
import br.com.sommelier.presentation.passwordreset.model.EmailUiState
import br.com.sommelier.presentation.passwordreset.model.PasswordResetUiModel
import br.com.sommelier.presentation.passwordreset.res.PasswordResetStringResource
import br.com.sommelier.presentation.passwordreset.state.PasswordResetUiEffect
import br.com.sommelier.presentation.passwordreset.state.PasswordResetUiState
import br.com.sommelier.testrule.CoroutineTestRule
import br.com.sommelier.util.ext.getOrAwaitValue
import io.mockk.coEvery
import io.mockk.mockk
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Rule
import org.junit.Test

class PasswordResetViewModelTest {

    @get:Rule
    val instantTaskExecutorRule = InstantTaskExecutorRule()

    @get:Rule
    val coroutineTestRule = CoroutineTestRule()

    private val sendPasswordResetEmailUseCase = mockk<SendPasswordResetEmailUseCase>()

    private lateinit var viewModel: PasswordResetViewModel

    @Before
    fun setUp() {
        viewModel = PasswordResetViewModel(sendPasswordResetEmailUseCase)
    }

    @Test
    fun `GIVEN OnClickMainBackButton action was sent WHEN sendAction was called THEN assert that the expected uiEffect is emitted`() {
        val action = PasswordResetAction.Action.OnClickMainBackButton

        viewModel.sendAction(action)

        val expectedUiEffect = PasswordResetUiEffect.PopBackStack
        val actualUiEffect = viewModel.uiEffect.getOrAwaitValue()

        assertEquals(expectedUiEffect, actualUiEffect)
    }

    @Test
    fun `GIVEN OnTypeEmailField action was sent WHEN sendAction was called THEN assert that the expected uiState is emitted`() {
        val email = "email"
        val action = PasswordResetAction.Action.OnTypeEmailField(email)

        viewModel.sendAction(action)

        val expectedUiState = PasswordResetUiState.Resume(
            PasswordResetUiModel(
                emailUiState = EmailUiState(
                    text = "email"
                )
            )
        )
        val actualUiState = viewModel.uiState.getOrAwaitValue()

        assertUiState(expectedUiState, actualUiState)
    }

    @Test
    fun `GIVEN OnClickSendButton was sent but email is blank WHEN sendAction was called THEN assert that the expected uiState is emitted`() {
        val action = PasswordResetAction.Action.OnClickSendButton

        viewModel.sendAction(action)

        val expectedUiState = PasswordResetUiState.Resume(
            PasswordResetUiModel(
                emailUiState = EmailUiState(
                    errorSupportingMessage = PasswordResetStringResource.BlankEmail,
                    isError = true
                )
            )
        )
        val actualUiState = viewModel.uiState.getOrAwaitValue()

        assertUiState(expectedUiState, actualUiState)
    }

    @Test
    fun `GIVEN OnClickSendButton was the last action sent but email is invalid WHEN sendAction was called THEN assert that the expected uiState is emitted`() {
        val email = "email"
        val action = PasswordResetAction.Action.OnClickSendButton

        viewModel.sendAction(PasswordResetAction.Action.OnTypeEmailField(email))
        viewModel.sendAction(action)

        val expectedUiState = PasswordResetUiState.Resume(
            PasswordResetUiModel(
                emailUiState = EmailUiState(
                    text = email,
                    errorSupportingMessage = PasswordResetStringResource.InvalidEmail,
                    isError = true
                )
            )
        )
        val actualUiState = viewModel.uiState.getOrAwaitValue()

        assertUiState(expectedUiState, actualUiState)
    }

    @Test
    fun `GIVEN OnClickSendButton was the last action sent and email is valid WHEN sendAction was called THEN assert that the expected uiState and uiEffect are emitted`() {
        val email = "email@email.com"
        val action = PasswordResetAction.Action.OnClickSendButton

        viewModel.sendAction(PasswordResetAction.Action.OnTypeEmailField(email))
        viewModel.sendAction(action)

        val expectedUiState = PasswordResetUiState.Loading(
            PasswordResetUiModel(
                emailUiState = EmailUiState(
                    text = email
                ),
                isLoading = true
            )
        )
        val actualUiState = viewModel.uiState.getOrAwaitValue()

        val expectedUiEffect = PasswordResetUiEffect.ShowLoading
        val actualUiEffect = viewModel.uiEffect.getOrAwaitValue()

        assertUiState(expectedUiState, actualUiState)
        assertEquals(expectedUiEffect, actualUiEffect)
    }

    @Test
    fun `GIVEN OnTryToSendPasswordResetEmail was the last action sent and sendPasswordResetEmailUseCase as success WHEN sendAction was called THEN assert that the expected uiState is emitted`() =
        coroutineTestRule.runBlockingTest {
            val email = "email"
            val action = PasswordResetAction.Action.OnTryToSendPasswordResetEmail

            val useCaseResult: Either<Failure, Success<Unit>> = Either.Right(Success(Unit))
            coEvery {
                sendPasswordResetEmailUseCase(
                    SendPasswordResetEmailUseCase.Params(userEmail = email)
                )
            } returns useCaseResult

            viewModel.sendAction(PasswordResetAction.Action.OnTypeEmailField(email))
            viewModel.sendAction(action)

            val expectedUiState = PasswordResetUiState.Success(
                PasswordResetUiModel(
                    emailUiState = EmailUiState(
                        text = email
                    ),
                    isBackButtonEnabled = false
                )
            )
            val actualUiState = viewModel.uiState.getOrAwaitValue()

            assertUiState(expectedUiState, actualUiState)
        }

    @Test
    fun `GIVEN OnTryToSendPasswordResetEmail was the last action sent and sendPasswordResetEmailUseCase as failure WHEN sendAction was called THEN assert that the expected uiState is emitted`() =
        coroutineTestRule.runBlockingTest {
            val email = "email"
            val action = PasswordResetAction.Action.OnTryToSendPasswordResetEmail

            val useCaseResult: Either<Failure, Success<Unit>> = Either.Left(
                Failure(GenericProblem("error"))
            )
            coEvery {
                sendPasswordResetEmailUseCase(
                    SendPasswordResetEmailUseCase.Params(userEmail = email)
                )
            } returns useCaseResult

            viewModel.sendAction(PasswordResetAction.Action.OnTypeEmailField(email))
            viewModel.sendAction(action)

            val expectedUiState = PasswordResetUiState.Error(
                PasswordResetUiModel(
                    emailUiState = EmailUiState(
                        text = email
                    )
                )
            )
            val actualUiState = viewModel.uiState.getOrAwaitValue()

            assertUiState(expectedUiState, actualUiState)
        }

    @Test
    fun `GIVEN OnTryToSendPasswordResetEmail was the last action sent, sendPasswordResetEmailUseCase as failure and email with error state WHEN sendAction was called THEN assert that the expected uiState is emitted`() =
        coroutineTestRule.runBlockingTest {
            val email = "email"
            val action = PasswordResetAction.Action.OnTryToSendPasswordResetEmail

            val useCaseResult: Either<Failure, Success<Unit>> = Either.Left(
                Failure(GenericProblem("error"))
            )
            coEvery {
                sendPasswordResetEmailUseCase(
                    SendPasswordResetEmailUseCase.Params(userEmail = email)
                )
            } returns useCaseResult

            viewModel.sendAction(PasswordResetAction.Action.OnClickSendButton)
            viewModel.sendAction(PasswordResetAction.Action.OnTypeEmailField(email))
            viewModel.sendAction(action)

            val expectedUiState = PasswordResetUiState.Error(
                PasswordResetUiModel(
                    emailUiState = EmailUiState(
                        text = email,
                        errorSupportingMessage = PasswordResetStringResource.Empty,
                        isError = false
                    )
                )
            )
            val actualUiState = viewModel.uiState.getOrAwaitValue()

            assertUiState(expectedUiState, actualUiState)
        }

    @Test
    fun `GIVEN OnClickTryAgainButton was the last action sent and sendPasswordResetEmailUseCase as success WHEN sendAction was called THEN assert that the expected uiState is emitted`() =
        coroutineTestRule.runBlockingTest {
            val email = "email"
            val action = PasswordResetAction.Action.OnClickTryAgainButton

            val useCaseResult: Either<Failure, Success<Unit>> = Either.Right(Success(Unit))
            coEvery {
                sendPasswordResetEmailUseCase(
                    SendPasswordResetEmailUseCase.Params(userEmail = email)
                )
            } returns useCaseResult

            viewModel.sendAction(PasswordResetAction.Action.OnTypeEmailField(email))
            viewModel.sendAction(action)

            val expectedUiState = PasswordResetUiState.Success(
                PasswordResetUiModel(
                    emailUiState = EmailUiState(
                        text = email
                    ),
                    isBackButtonEnabled = false
                )
            )
            val actualUiState = viewModel.uiState.getOrAwaitValue()

            assertUiState(expectedUiState, actualUiState)
        }

    @Test
    fun `GIVEN OnClickTryAgainButton was the last action sent and sendPasswordResetEmailUseCase as failure WHEN sendAction was called THEN assert that the expected uiState is emitted`() =
        coroutineTestRule.runBlockingTest {
            val email = "email"
            val action = PasswordResetAction.Action.OnClickTryAgainButton

            val useCaseResult: Either<Failure, Success<Unit>> = Either.Left(
                Failure(GenericProblem("error"))
            )
            coEvery {
                sendPasswordResetEmailUseCase(
                    SendPasswordResetEmailUseCase.Params(userEmail = email)
                )
            } returns useCaseResult

            viewModel.sendAction(PasswordResetAction.Action.OnTypeEmailField(email))
            viewModel.sendAction(action)

            val expectedUiState = PasswordResetUiState.Error(
                PasswordResetUiModel(
                    emailUiState = EmailUiState(
                        text = email
                    )
                )
            )
            val actualUiState = viewModel.uiState.getOrAwaitValue()

            assertUiState(expectedUiState, actualUiState)
        }

    @Test
    fun `GIVEN OnClickTryAgainButton was the last action sent, sendPasswordResetEmailUseCase as failure and email with error state WHEN sendAction was called THEN assert that the expected uiState is emitted`() {
        val email = "email"
        val action = PasswordResetAction.Action.OnClickTryAgainButton

        val useCaseResult: Either<Failure, Success<Unit>> = Either.Left(
            Failure(GenericProblem("error"))
        )
        coEvery {
            sendPasswordResetEmailUseCase(
                SendPasswordResetEmailUseCase.Params(userEmail = email)
            )
        } returns useCaseResult

        viewModel.sendAction(PasswordResetAction.Action.OnClickSendButton)
        viewModel.sendAction(PasswordResetAction.Action.OnTypeEmailField(email))
        viewModel.sendAction(action)

        val expectedUiState = PasswordResetUiState.Error(
            PasswordResetUiModel(
                emailUiState = EmailUiState(
                    text = email,
                    errorSupportingMessage = PasswordResetStringResource.Empty,
                    isError = false
                )
            )
        )
        val actualUiState = viewModel.uiState.getOrAwaitValue()

        assertUiState(expectedUiState, actualUiState)
    }

    @Test
    fun `GIVEN OnClickSecondaryBackButton was the last action sent WHEN sendAction was called THEN assert that the expected uiState is emitted`() {
        val email = "email@email.com"
        val action = PasswordResetAction.Action.OnClickSecondaryBackButton

        viewModel.sendAction(PasswordResetAction.Action.OnTypeEmailField(email))
        viewModel.sendAction(action)

        val expectedUiState = PasswordResetUiState.Resume(
            PasswordResetUiModel(
                emailUiState = EmailUiState(
                    text = email,
                    errorSupportingMessage = PasswordResetStringResource.Empty,
                    isError = false
                )
            )
        )
        val actualUiState = viewModel.uiState.getOrAwaitValue()

        assertUiState(expectedUiState, actualUiState)
    }

    @Test
    fun `GIVEN OnClickOkButton action was sent WHEN sendAction was called THEN assert that the expected uiEffect is emitted`() {
        val action = PasswordResetAction.Action.OnClickOkButton

        viewModel.sendAction(action)

        val expectedUiEffect = PasswordResetUiEffect.PopBackStack
        val actualUiEffect = viewModel.uiEffect.getOrAwaitValue()

        assertEquals(expectedUiEffect, actualUiEffect)
    }

    private fun assertUiState(
        expectedUiState: PasswordResetUiState,
        actualUiState: PasswordResetUiState
    ) {
        assertEquals(expectedUiState.javaClass, actualUiState.javaClass)
        assertEquals(
            expectedUiState.uiModel.emailUiState.text,
            actualUiState.uiModel.emailUiState.text
        )
        assertEquals(
            expectedUiState.uiModel.emailUiState.errorSupportingMessage,
            actualUiState.uiModel.emailUiState.errorSupportingMessage
        )
        assertEquals(
            expectedUiState.uiModel.emailUiState.isError,
            actualUiState.uiModel.emailUiState.isError
        )
        assertEquals(
            expectedUiState.uiModel.isBackButtonEnabled,
            actualUiState.uiModel.isBackButtonEnabled
        )
        assertEquals(
            expectedUiState.uiModel.isLoading,
            actualUiState.uiModel.isLoading
        )
    }
}
