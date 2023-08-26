package br.com.sommelier.presentation.account.viewmodel

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import arrow.core.Either
import br.com.sommelier.base.result.Failure
import br.com.sommelier.base.result.GenericProblem
import br.com.sommelier.base.result.Success
import br.com.sommelier.domain.model.UserDomain
import br.com.sommelier.domain.model.UserFirebase
import br.com.sommelier.domain.usecase.DeleteUserUseCase
import br.com.sommelier.domain.usecase.GetCurrentUserUseCase
import br.com.sommelier.domain.usecase.GetUserDocumentUseCase
import br.com.sommelier.domain.usecase.SignOutUserUseCase
import br.com.sommelier.presentation.account.action.AccountAction
import br.com.sommelier.presentation.account.model.AccountUiModel
import br.com.sommelier.presentation.account.res.AccountStringResource
import br.com.sommelier.presentation.account.state.AccountLoadingCause
import br.com.sommelier.presentation.account.state.AccountUiEffect
import br.com.sommelier.presentation.account.state.AccountUiState
import br.com.sommelier.testrule.CoroutineTestRule
import br.com.sommelier.util.ext.getOrAwaitValue
import io.mockk.coEvery
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

    private val getCurrentUserUseCase = mockk<GetCurrentUserUseCase>()

    private val getUserDocumentUseCase = mockk<GetUserDocumentUseCase>()

    private val logOutUserUseCase = mockk<SignOutUserUseCase>()

    private val deleteUserUseCase = mockk<DeleteUserUseCase>()

    private val dummyUserFirebase = UserFirebase(
        email = "email@domain.com",
        uid = "uid"
    )

    private val dummyUserDomain = UserDomain(
        name = "name",
        email = "email@domain.com",
        uid = dummyUserFirebase.uid
    )

    private lateinit var viewModel: AccountViewModel

    @Before
    fun setup() {
        viewModel = AccountViewModel(
            getCurrentUserUseCase = getCurrentUserUseCase,
            getUserDocumentUseCase = getUserDocumentUseCase,
            deleteUserUseCase = deleteUserUseCase,
            logOutUserUseCase = logOutUserUseCase
        )
    }

    @Test
    fun `GIVEN OnInitial action was sent WHEN sendAction was called THEN assert that the emitted uiState and uiEffect were the expected`() {
        val action = AccountAction.Action.OnInitial

        viewModel.sendAction(action)

        val expectedUiState = AccountUiState.Loading(AccountUiModel().copy(isLoading = true))
        val actualUiState = viewModel.uiState.getOrAwaitValue()

        val expectedUiEffect = AccountUiEffect.ShowLoading(AccountLoadingCause.FetchAccountData)
        val actualUiEffect = viewModel.uiEffect.getOrAwaitValue()

        assertUiState(expectedUiState, actualUiState)
        assertEquals(expectedUiEffect, actualUiEffect)
        assertEquals(
            expectedUiEffect.loadingCause,
            (actualUiEffect as AccountUiEffect.ShowLoading).loadingCause
        )
    }

    @Test
    fun `GIVEN OnFetchAccountData action was sent and both use cases as success WHEN sendAction was called THEN assert that the emitted uiState was Resume`() =
        coroutineTestRule.runBlockingTest {
            val action = AccountAction.Action.OnTryToFetchAccountData

            val getCurrentUserUseCaseResult: Either<Failure, Success<UserFirebase>> = Either.Right(
                Success(dummyUserFirebase)
            )
            coEvery { getCurrentUserUseCase(any()) } returns getCurrentUserUseCaseResult

            val getUserDocumentUseCaseResult: Either<Failure, Success<UserDomain>> = Either.Right(
                Success(dummyUserDomain)
            )
            coEvery {
                getUserDocumentUseCase(GetUserDocumentUseCase.Params(userUid = dummyUserDomain.uid))
            } returns getUserDocumentUseCaseResult

            viewModel.sendAction(action)

            val expectedUiState = AccountUiState.Resume(
                AccountUiModel().copy(
                    name = dummyUserDomain.name,
                    email = dummyUserDomain.email
                )
            )
            val actualUiState = viewModel.uiState.getOrAwaitValue()

            assertUiState(expectedUiState, actualUiState)
        }

    @Test
    fun `GIVEN OnFetchAccountData action was sent and getCurrentUser use case as success but getUserDocument use case as failure WHEN sendAction was called THEN assert that the emitted uiState was Resume`() =
        coroutineTestRule.runBlockingTest {
            val action = AccountAction.Action.OnTryToFetchAccountData

            val getCurrentUserUseCaseResult: Either<Failure, Success<UserFirebase>> = Either.Right(
                Success(dummyUserFirebase)
            )
            coEvery { getCurrentUserUseCase(any()) } returns getCurrentUserUseCaseResult

            val getUserDocumentUseCaseResult: Either<Failure, Success<UserDomain>> = Either.Left(
                Failure(GenericProblem("Generic problem occurred"))
            )
            coEvery {
                getUserDocumentUseCase(GetUserDocumentUseCase.Params(userUid = dummyUserDomain.uid))
            } returns getUserDocumentUseCaseResult

            viewModel.sendAction(action)

            val expectedUiState = AccountUiState.Error
            val actualUiState = viewModel.uiState.getOrAwaitValue()

            assertUiState(expectedUiState, actualUiState)
        }

    @Test
    fun `GIVEN OnFetchAccountData action was sent and getCurrentUser use case as failure WHEN sendAction was called THEN assert that the emitted uiState was Error`() =
        coroutineTestRule.runBlockingTest {
            val action = AccountAction.Action.OnTryToFetchAccountData

            val getCurrentUserUseCaseResult: Either<Failure, Success<UserFirebase>> = Either.Left(
                Failure(GenericProblem("Generic problem occurred"))
            )
            coEvery { getCurrentUserUseCase(any()) } returns getCurrentUserUseCaseResult

            viewModel.sendAction(action)

            val expectedUiState = AccountUiState.Error
            val actualUiState = viewModel.uiState.getOrAwaitValue()

            assertUiState(expectedUiState, actualUiState)
        }

    @Test
    fun `GIVEN OnClickTryToFetchAccountDataAgainButton action was sent WHEN sendAction was called THEN assert that the emitted uiState was Loading`() {
        val action = AccountAction.Action.OnClickTryToFetchAccountDataAgainButton

        viewModel.sendAction(action)

        val expectedUiState = AccountUiState.Loading(AccountUiModel().copy(isLoading = true))
        val actualUiState = viewModel.uiState.getOrAwaitValue()

        val expectedUiEffect = AccountUiEffect.ShowLoading(AccountLoadingCause.FetchAccountData)
        val actualUiEffect = viewModel.uiEffect.getOrAwaitValue()

        assertUiState(expectedUiState, actualUiState)
        assertEquals(expectedUiEffect, actualUiEffect)
        assertEquals(
            expectedUiEffect.loadingCause,
            (actualUiEffect as AccountUiEffect.ShowLoading).loadingCause
        )
    }

    @Test
    fun `GIVEN OnClickBackButton action was sent WHEN sendAction was called THEN assert that the emitted uiEffect was PopBackStack`() {
        val action = AccountAction.Action.OnClickBackButton

        viewModel.sendAction(action)

        val expectedUiEffect = AccountUiEffect.PopBackStack
        val actualUiEffect = viewModel.uiEffect.getOrAwaitValue()

        assertEquals(expectedUiEffect, actualUiEffect)
    }

    @Test
    fun `GIVEN OnClickEditButton action was sent WHEN sendAction was called THEN assert that the emitted uiEffect was OpenEditAccountScreen`() {
        val action = AccountAction.Action.OnClickEditButton

        viewModel.sendAction(action)

        val expectedUiEffect = AccountUiEffect.OpenEditAccountScreen
        val actualUiEffect = viewModel.uiEffect.getOrAwaitValue()

        assertEquals(expectedUiEffect, actualUiEffect)
    }

    @Test
    fun `GIVEN OnClickPasswordResetButton action was sent WHEN sendAction was called THEN assert that the emitted uiState was the expected`() {
        val action = AccountAction.Action.OnClickPasswordResetButton

        viewModel.sendAction(action)

        val expectedUiState = AccountUiState.Resume(
            AccountUiModel().copy(
                isPasswordResetDialogVisible = true
            )
        )
        val actualUiState = viewModel.uiState.getOrAwaitValue()

        assertUiState(expectedUiState, actualUiState)
    }

    @Test
    fun `GIVEN OnClickPasswordResetConfirmationButton action was sent WHEN sendAction was called THEN assert that the emitted uiState and uiEffect were the expected`() {
        val action = AccountAction.Action.OnClickPasswordResetConfirmationButton

        viewModel.sendAction(action)

        val expectedUiState = AccountUiState.Loading(AccountUiModel().copy(isLoading = true))
        val actualUiState = viewModel.uiState.getOrAwaitValue()

        val expectedUiEffect = AccountUiEffect.ShowLoading(AccountLoadingCause.PasswordReset)
        val actualUiEffect = viewModel.uiEffect.getOrAwaitValue()

        assertUiState(expectedUiState, actualUiState)
        assertEquals(expectedUiEffect, actualUiEffect)
    }

    @Test
    fun `GIVEN OnDismissPasswordResetDialog action was sent WHEN sendAction was called THEN assert that the emitted uiState was the expected`() {
        val action = AccountAction.Action.OnDismissPasswordResetDialog

        viewModel.sendAction(action)

        val expectedUiState = AccountUiState.Resume(
            AccountUiModel().copy(
                isPasswordResetDialogVisible = false
            )
        )
        val actualUiState = viewModel.uiState.getOrAwaitValue()

        assertUiState(expectedUiState, actualUiState)
    }

    @Test
    fun `GIVEN OnTryToPasswordReset action was sent and logOutUserUseCase as success WHEN sendAction was called THEN assert that the emitted uiEffect is OpenPasswordResetScreen`() =
        coroutineTestRule.runBlockingTest {
            val action = AccountAction.Action.OnTryToPasswordReset

            val logOutUserUseCaseResult: Either<Failure, Success<Unit>> = Either.Right(
                Success(Unit)
            )
            coEvery { logOutUserUseCase(any()) } returns logOutUserUseCaseResult

            viewModel.sendAction(action)

            val expectedUiEffect = AccountUiEffect.OpenPasswordResetScreen
            val actualUiEffect = viewModel.uiEffect.getOrAwaitValue()

            assertEquals(expectedUiEffect, actualUiEffect)
        }

    @Test
    fun `GIVEN OnTryToPasswordReset action was sent and logOutUserUseCase as failure WHEN sendAction was called THEN assert that the emitted uiEffect is OpenLoginScreen`() =
        coroutineTestRule.runBlockingTest {
            val action = AccountAction.Action.OnTryToPasswordReset

            val logOutUserUseCaseResult: Either<Failure, Success<Unit>> = Either.Left(
                Failure(GenericProblem("Generic problem occurred"))
            )
            coEvery { logOutUserUseCase(any()) } returns logOutUserUseCaseResult

            viewModel.sendAction(action)

            val expectedUiState = AccountUiState.Resume(
                AccountUiModel().copy(
                    snackbarUiState = AccountUiModel().snackbarUiState.copy(
                        message = AccountStringResource.ErrorPasswordReset
                    )
                )
            )
            val actualUiState = viewModel.uiState.getOrAwaitValue()

            val expectedUiEffect = AccountUiEffect.ShowSnackbarError
            val actualUiEffect = viewModel.uiEffect.getOrAwaitValue()

            assertUiState(expectedUiState, actualUiState)
            assertEquals(expectedUiEffect, actualUiEffect)
        }

    @Test
    fun `GIVEN OnClickDeleteAccountButton action was sent WHEN sendAction was called THEN assert that the emitted uiState was the expected`() {
        val action = AccountAction.Action.OnClickDeleteAccountButton

        viewModel.sendAction(action)

        val expectedUiState = AccountUiState.Resume(
            AccountUiModel().copy(
                isDeleteAccountDialogVisible = true
            )
        )
        val actualUiState = viewModel.uiState.getOrAwaitValue()

        assertUiState(expectedUiState, actualUiState)
    }

    @Test
    fun `GIVEN OnClickDeleteAccountConfirmationButton action was sent WHEN sendAction was called THEN assert that the emitted uiState and uiEffect were the expected`() {
        val action = AccountAction.Action.OnClickDeleteAccountConfirmationButton

        viewModel.sendAction(action)

        val expectedUiState = AccountUiState.Loading(AccountUiModel().copy(isLoading = true))
        val actualUiState = viewModel.uiState.getOrAwaitValue()

        val expectedUiEffect = AccountUiEffect.ShowLoading(AccountLoadingCause.DeleteAccount)
        val actualUiEffect = viewModel.uiEffect.getOrAwaitValue()

        assertUiState(expectedUiState, actualUiState)
        assertEquals(expectedUiEffect, actualUiEffect)
        assertEquals(
            expectedUiEffect.loadingCause,
            (actualUiEffect as AccountUiEffect.ShowLoading).loadingCause
        )
    }

    @Test
    fun `GIVEN OnDismissDeleteAccountDialog action was sent WHEN sendAction was called THEN assert that the emitted uiState was the expected`() {
        val action = AccountAction.Action.OnDismissDeleteAccountDialog

        viewModel.sendAction(action)

        val expectedUiState = AccountUiState.Resume(
            AccountUiModel().copy(
                isDeleteAccountDialogVisible = false
            )
        )
        val actualUiState = viewModel.uiState.getOrAwaitValue()

        assertUiState(expectedUiState, actualUiState)
    }

    @Test
    fun `GIVEN OnTryToDeleteAccount action was sent and both use cases as success WHEN sendAction was called THEN assert that the emitted uiEffect was the expected`() =
        coroutineTestRule.runBlockingTest {
            val action = AccountAction.Action.OnTryToDeleteAccount

            val getCurrentUserUseCaseResult: Either<Failure, Success<UserFirebase>> = Either.Right(
                Success(dummyUserFirebase)
            )
            coEvery { getCurrentUserUseCase(any()) } returns getCurrentUserUseCaseResult

            val deleteUserUseCaseResult: Either<Failure, Success<Unit>> = Either.Right(
                Success(Unit)
            )
            coEvery {
                deleteUserUseCase(DeleteUserUseCase.Params(userUid = dummyUserFirebase.uid))
            } returns deleteUserUseCaseResult

            viewModel.sendAction(action)

            val expectedUiEffect = AccountUiEffect.OpenLoginScreen
            val actualUiEffect = viewModel.uiEffect.getOrAwaitValue()

            assertEquals(expectedUiEffect, actualUiEffect)
        }

    @Test
    fun `GIVEN OnTryToDeleteAccount action was sent and getCurrentUseCase as failure WHEN sendAction was called THEN assert that the emitted uiState and uiEffect were the expected`() =
        coroutineTestRule.runBlockingTest {
            val action = AccountAction.Action.OnTryToDeleteAccount

            val getCurrentUserUseCaseResult: Either<Failure, Success<UserFirebase>> = Either.Left(
                Failure(GenericProblem("Generic problem occurred"))
            )
            coEvery { getCurrentUserUseCase(any()) } returns getCurrentUserUseCaseResult

            viewModel.sendAction(action)

            val expectedUiState = AccountUiState.Resume(
                AccountUiModel().copy(
                    snackbarUiState = AccountUiModel().snackbarUiState.copy(
                        message = AccountStringResource.ErrorDeleteAccount
                    )
                )
            )
            val actualUiState = viewModel.uiState.getOrAwaitValue()

            val expectedUiEffect = AccountUiEffect.ShowSnackbarError
            val actualUiEffect = viewModel.uiEffect.getOrAwaitValue()

            assertUiState(expectedUiState, actualUiState)
            assertEquals(expectedUiEffect, actualUiEffect)
        }

    @Test
    fun `GIVEN OnTryToDeleteAccount action was sent and getCurrentUseCase as success but deleteUserUseCase as failure WHEN sendAction was called THEN assert that the emitted uiState and uiEffect were the expected`() =
        coroutineTestRule.runBlockingTest {
            val action = AccountAction.Action.OnTryToDeleteAccount

            val getCurrentUserUseCaseResult: Either<Failure, Success<UserFirebase>> = Either.Right(
                Success(dummyUserFirebase)
            )
            coEvery { getCurrentUserUseCase(any()) } returns getCurrentUserUseCaseResult

            val deleteUserUseCaseResult: Either<Failure, Success<Unit>> = Either.Left(
                Failure(GenericProblem("Generic problem occurred"))
            )
            coEvery {
                deleteUserUseCase(DeleteUserUseCase.Params(userUid = dummyUserFirebase.uid))
            } returns deleteUserUseCaseResult

            viewModel.sendAction(action)

            val expectedUiState = AccountUiState.Resume(
                AccountUiModel().copy(
                    snackbarUiState = AccountUiModel().snackbarUiState.copy(
                        message = AccountStringResource.ErrorDeleteAccount
                    )
                )
            )
            val actualUiState = viewModel.uiState.getOrAwaitValue()

            val expectedUiEffect = AccountUiEffect.ShowSnackbarError
            val actualUiEffect = viewModel.uiEffect.getOrAwaitValue()

            assertUiState(expectedUiState, actualUiState)
            assertEquals(expectedUiEffect, actualUiEffect)
        }

    @Test
    fun `GIVEN OnClickLogoutButton action was sent WHEN sendAction was called THEN assert that the emitted uiState was the expected`() {
        val action = AccountAction.Action.OnClickLogoutButton

        viewModel.sendAction(action)

        val expectedUiState = AccountUiState.Resume(
            AccountUiModel().copy(
                isLogoutDialogVisible = true
            )
        )
        val actualUiState = viewModel.uiState.getOrAwaitValue()

        assertUiState(expectedUiState, actualUiState)
    }

    @Test
    fun `GIVEN OnClickLogoutConfirmationButton action was sent WHEN sendAction was called THEN assert that the emitted uiState and uiEffect were the expected`() {
        val action = AccountAction.Action.OnClickLogoutConfirmationButton

        viewModel.sendAction(action)

        val expectedUiState = AccountUiState.Loading(AccountUiModel().copy(isLoading = true))
        val actualUiState = viewModel.uiState.getOrAwaitValue()

        val expectedUiEffect = AccountUiEffect.ShowLoading(AccountLoadingCause.Logout)
        val actualUiEffect = viewModel.uiEffect.getOrAwaitValue()

        assertUiState(expectedUiState, actualUiState)
        assertEquals(expectedUiEffect, actualUiEffect)
        assertEquals(
            expectedUiEffect.loadingCause,
            (actualUiEffect as AccountUiEffect.ShowLoading).loadingCause
        )
    }

    @Test
    fun `GIVEN OnDismissLogoutDialog action was sent WHEN sendAction was called THEN assert that the emitted uiState was the expected`() {
        val action = AccountAction.Action.OnDismissLogoutDialog

        viewModel.sendAction(action)

        val expectedUiState = AccountUiState.Resume(
            AccountUiModel().copy(
                isLogoutDialogVisible = false
            )
        )
        val actualUiState = viewModel.uiState.getOrAwaitValue()

        assertUiState(expectedUiState, actualUiState)
    }

    @Test
    fun `GIVEN OnTryToLogout action was sent and logOutUserUseCase as success WHEN sendAction was called THEN assert that the emitted uiEffect was the expected`() =
        coroutineTestRule.runBlockingTest {
            val action = AccountAction.Action.OnTryToLogout

            val logOutUserUseCaseResult: Either<Failure, Success<Unit>> = Either.Right(
                Success(Unit)
            )
            coEvery { logOutUserUseCase(any()) } returns logOutUserUseCaseResult

            viewModel.sendAction(action)

            val expectedUiEffect = AccountUiEffect.OpenLoginScreen
            val actualUiEffect = viewModel.uiEffect.getOrAwaitValue()

            assertEquals(expectedUiEffect, actualUiEffect)
        }

    @Test
    fun `GIVEN OnTryToLogout action was sent and logOutUserUseCase as failure WHEN sendAction was called THEN assert that the emitted uiState and uiEffect were the expected`() =
        coroutineTestRule.runBlockingTest {
            val action = AccountAction.Action.OnTryToLogout

            val logOutUserUseCaseResult: Either<Failure, Success<Unit>> = Either.Left(
                Failure(GenericProblem("Generic problem occurred"))
            )
            coEvery { logOutUserUseCase(any()) } returns logOutUserUseCaseResult

            viewModel.sendAction(action)

            val expectedUiState = AccountUiState.Resume(
                AccountUiModel().copy(
                    snackbarUiState = AccountUiModel().snackbarUiState.copy(
                        message = AccountStringResource.ErrorLogout
                    )
                )
            )
            val actualUiState = viewModel.uiState.getOrAwaitValue()

            val expectedUiEffect = AccountUiEffect.ShowSnackbarError
            val actualUiEffect = viewModel.uiEffect.getOrAwaitValue()

            assertUiState(expectedUiState, actualUiState)
            assertEquals(expectedUiEffect, actualUiEffect)
        }

    private fun assertUiState(expectedUiState: AccountUiState, actualUiState: AccountUiState) {
        assertEquals(expectedUiState.javaClass, actualUiState.javaClass)
        assertEquals(expectedUiState.uiModel.email, actualUiState.uiModel.email)
        assertEquals(expectedUiState.uiModel.name, actualUiState.uiModel.name)
        assertEquals(expectedUiState.uiModel.isLoading, actualUiState.uiModel.isLoading)
        assertEquals(
            expectedUiState.uiModel.snackbarUiState.hostState.currentSnackbarData,
            actualUiState.uiModel.snackbarUiState.hostState.currentSnackbarData
        )
        assertEquals(
            expectedUiState.uiModel.snackbarUiState.message,
            actualUiState.uiModel.snackbarUiState.message
        )
        assertEquals(
            expectedUiState.uiModel.snackbarUiState.type,
            actualUiState.uiModel.snackbarUiState.type
        )
        assertEquals(
            expectedUiState.uiModel.isPasswordResetDialogVisible,
            actualUiState.uiModel.isPasswordResetDialogVisible
        )
        assertEquals(
            expectedUiState.uiModel.isDeleteAccountDialogVisible,
            actualUiState.uiModel.isDeleteAccountDialogVisible
        )
        assertEquals(
            expectedUiState.uiModel.isLogoutDialogVisible,
            actualUiState.uiModel.isLogoutDialogVisible
        )
    }
}
