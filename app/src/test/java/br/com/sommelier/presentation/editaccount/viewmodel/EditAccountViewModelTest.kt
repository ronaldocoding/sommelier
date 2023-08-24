package br.com.sommelier.presentation.editaccount.viewmodel

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import arrow.core.Either
import br.com.sommelier.base.result.Failure
import br.com.sommelier.base.result.GenericProblem
import br.com.sommelier.base.result.Success
import br.com.sommelier.domain.model.UserDomain
import br.com.sommelier.domain.model.UserFirebase
import br.com.sommelier.domain.usecase.GetCurrentUserUseCase
import br.com.sommelier.domain.usecase.GetUserDocumentUseCase
import br.com.sommelier.domain.usecase.UpdateUserDocumentUseCase
import br.com.sommelier.presentation.editaccount.action.EditAccountAction
import br.com.sommelier.presentation.editaccount.model.EditAccountUiModel
import br.com.sommelier.presentation.editaccount.res.EditAccountStringResource
import br.com.sommelier.presentation.editaccount.state.EditAccountLoadingCause
import br.com.sommelier.presentation.editaccount.state.EditAccountUiEffect
import br.com.sommelier.presentation.editaccount.state.EditAccountUiState
import br.com.sommelier.testrule.CoroutineTestRule
import br.com.sommelier.ui.component.SommelierSnackbarType
import br.com.sommelier.util.ext.getOrAwaitValue
import io.mockk.coEvery
import io.mockk.mockk
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Rule
import org.junit.Test

class EditAccountViewModelTest {

    @get:Rule
    val instantTaskExecutorRule = InstantTaskExecutorRule()

    @get:Rule
    val coroutineTestRule = CoroutineTestRule()

    private val getCurrentUserUseCase = mockk<GetCurrentUserUseCase>()

    private val getUserDocumentUseCase = mockk<GetUserDocumentUseCase>()

    private val updateUserDocumentUseCase = mockk<UpdateUserDocumentUseCase>()

    private val dummyUserFirebase = UserFirebase(
        email = "email@domain.com",
        uid = "uid"
    )

    private val dummyUserDomain = UserDomain(
        name = "name",
        email = "email@domain.com",
        uid = dummyUserFirebase.uid
    )

    private lateinit var viewModel: EditAccountViewModel

    @Before
    fun setUp() {
        viewModel = EditAccountViewModel(
            getCurrentUserUseCase,
            getUserDocumentUseCase,
            updateUserDocumentUseCase
        )
    }

    @Test
    fun `GIVEN OnInitial action was sent WHEN sendAction was called THEN assert that the emitted uiEffect is ShowLoading with FetchAccountData loadingCause`() {
        val action = EditAccountAction.Action.OnInitial

        viewModel.sendAction(action)

        val expectedUiState = EditAccountUiState.Loading(
            EditAccountUiModel().copy(isLoading = true)
        )
        val actualUiState = viewModel.uiState.getOrAwaitValue()

        val expectedUiEffect = EditAccountUiEffect.ShowLoading(
            EditAccountLoadingCause.FetchAccountData
        )
        val actualUiEffect = viewModel.uiEffect.getOrAwaitValue()

        assertUiState(expectedUiState, actualUiState)
        assertEquals(expectedUiEffect, actualUiEffect)
    }

    @Test
    fun `GIVEN OnFetchAccountData action was sent and both use cases as success WHEN sendAction was called THEN assert that the emitted uiState was the expected`() =
        coroutineTestRule.runBlockingTest {
            val action = EditAccountAction.Action.OnFetchAccountData

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

            val expectedUiState = EditAccountUiState.Resume(
                EditAccountUiModel().copy(
                    editNameFieldUiState = EditAccountUiModel().editNameFieldUiState.copy(
                        name = dummyUserDomain.name
                    ),
                )
            )
            val actualUiState = viewModel.uiState.getOrAwaitValue()

            assertUiState(expectedUiState, actualUiState)
        }

    @Test
    fun `GIVEN OnFetchAccountData action was sent and getCurrentUserUseCase as failure WHEN sendAction was called THEN assert that the emitted uiState was the expected`() =
        coroutineTestRule.runBlockingTest {
            val action = EditAccountAction.Action.OnFetchAccountData

            val getCurrentUserUseCaseResult: Either<Failure, Success<UserFirebase>> = Either.Left(
                Failure(GenericProblem("Generic problem occurred"))
            )
            coEvery { getCurrentUserUseCase(any()) } returns getCurrentUserUseCaseResult

            viewModel.sendAction(action)

            val expectedUiState = EditAccountUiState.Error(EditAccountUiModel())
            val actualUiState = viewModel.uiState.getOrAwaitValue()

            assertUiState(expectedUiState, actualUiState)
        }

    @Test
    fun `GIVEN OnFetchAccountData action was sent and getCurrentUserUseCase as success but getUserDocumentUseCase as failure WHEN sendAction was called THEN assert that the emitted uiState was the expected`() =
        coroutineTestRule.runBlockingTest {
            val action = EditAccountAction.Action.OnFetchAccountData

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

            val expectedUiState = EditAccountUiState.Error(EditAccountUiModel())
            val actualUiState = viewModel.uiState.getOrAwaitValue()

            assertUiState(expectedUiState, actualUiState)
        }

    @Test
    fun `GIVEN OnClickTryToFetchAccountDataAgainButton action was sent WHEN sendAction was called THEN assert that the emitted uiState and uiEffect were the expected`() {
        val action = EditAccountAction.Action.OnClickTryToFetchAccountDataAgainButton

        viewModel.sendAction(action)

        val expectedUiState = EditAccountUiState.Loading(
            EditAccountUiModel().copy(isLoading = true)
        )
        val actualUiState = viewModel.uiState.getOrAwaitValue()

        val expectedUiEffect = EditAccountUiEffect.ShowLoading(
            EditAccountLoadingCause.FetchAccountData
        )
        val actualUiEffect = viewModel.uiEffect.getOrAwaitValue()

        assertUiState(expectedUiState, actualUiState)
        assertEquals(expectedUiEffect, actualUiEffect)
    }

    @Test
    fun `GIVEN OnClickBackButton action was sent WHEN sendAction was called THEN assert that the emitted uiEffect is PopBackStack`() {
        val action = EditAccountAction.Action.OnClickBackButton

        viewModel.sendAction(action)

        val expectedUiEffect = EditAccountUiEffect.PopBackStack
        val actualUiEffect = viewModel.uiEffect.getOrAwaitValue()

        assertEquals(expectedUiEffect, actualUiEffect)
    }

    @Test
    fun `GIVEN OnClickSaveButton was the last action sent and name field was valid WHEN sendAction was called THEN assert that the emitted uiState and uiEffect were the expected`() {
        val action = EditAccountAction.Action.OnClickSaveButton
        val name = "Ronaldo"

        viewModel.sendAction(EditAccountAction.Action.OnTypeNameField(name))
        viewModel.sendAction(action)

        val expectedUiState = EditAccountUiState.Loading(
            EditAccountUiModel().copy(
                editNameFieldUiState = EditAccountUiModel().editNameFieldUiState.copy(
                    name = name
                ),
                isLoading = true
            )
        )
        val actualUiState = viewModel.uiState.getOrAwaitValue()

        val expectedUiEffect =
            EditAccountUiEffect.ShowLoading(EditAccountLoadingCause.SaveAccountData)
        val actualUiEffect = viewModel.uiEffect.getOrAwaitValue()

        assertUiState(expectedUiState, actualUiState)
        assertEquals(expectedUiEffect, actualUiEffect)
    }

    @Test
    fun `GIVEN OnClickSaveButton action was sent but name field was blank WHEN sendAction was called THEN assert that the emitted uiState was the expected`() {
        val action = EditAccountAction.Action.OnClickSaveButton

        viewModel.sendAction(action)

        val expectedUiState = EditAccountUiState.Resume(
            EditAccountUiModel().copy(
                editNameFieldUiState = EditAccountUiModel().editNameFieldUiState.copy(
                    errorSupportingMessage = EditAccountStringResource.BlankName,
                    isError = true,
                ),
            )
        )
        val actualUiState = viewModel.uiState.getOrAwaitValue()

        assertUiState(expectedUiState, actualUiState)
    }

    @Test
    fun `GIVEN OnClickSaveButton was the last action sent but name field was invalid WHEN sendAction was called THEN assert that the emitted uiState was the expected`() {
        val name = "na"
        val action = EditAccountAction.Action.OnClickSaveButton

        viewModel.sendAction(EditAccountAction.Action.OnTypeNameField(name))
        viewModel.sendAction(action)

        val expectedUiState = EditAccountUiState.Resume(
            EditAccountUiModel().copy(
                editNameFieldUiState = EditAccountUiModel().editNameFieldUiState.copy(
                    name = name,
                    errorSupportingMessage = EditAccountStringResource.InvalidName,
                    isError = true,
                ),
            )
        )
        val actualUiState = viewModel.uiState.getOrAwaitValue()

        assertUiState(expectedUiState, actualUiState)
    }

    @Test
    fun `GIVEN OnTryToSave action was sent and all use cases as success WHEN sendAction was called THEN assert that the emitted uiState and uiEffect were the expected`() =
        coroutineTestRule.runBlockingTest {
            val action = EditAccountAction.Action.OnTryToSave

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

            val updateUserDocumentUseCaseResult: Either<Failure, Success<Unit>> = Either.Right(
                Success(Unit)
            )
            coEvery {
                updateUserDocumentUseCase(dummyUserDomain)
            } returns updateUserDocumentUseCaseResult

            viewModel.sendAction(action)

            val expectedUiState = EditAccountUiState.Resume(
                EditAccountUiModel().copy(
                    editNameFieldUiState = EditAccountUiModel().editNameFieldUiState.copy(
                        name = dummyUserDomain.name
                    ),
                    snackbarUiState = EditAccountUiModel().snackbarUiState.copy(
                        message = EditAccountStringResource.SuccessSnackbar,
                        type = SommelierSnackbarType.Success
                    )
                )
            )
            val actualUiState = viewModel.uiState.getOrAwaitValue()

            val expectedUiEffect = EditAccountUiEffect.ShowSnackbarSuccess
            val actualUiEffect = viewModel.uiEffect.getOrAwaitValue()

            assertUiState(expectedUiState, actualUiState)
            assertEquals(expectedUiEffect, actualUiEffect)
        }

    @Test
    fun `GIVEN OnTryToSave action was sent and getCurrentUserUseCase as failure WHEN sendAction was called THEN assert that the emitted uiState and uiEffect were the expected`() =
        coroutineTestRule.runBlockingTest {
            val action = EditAccountAction.Action.OnTryToSave

            val getCurrentUserUseCaseResult: Either<Failure, Success<UserFirebase>> = Either.Left(
                Failure(GenericProblem("Generic problem occurred"))
            )
            coEvery { getCurrentUserUseCase(any()) } returns getCurrentUserUseCaseResult

            viewModel.sendAction(action)

            val expectedUiState = EditAccountUiState.Resume(EditAccountUiModel())
            val actualUiState = viewModel.uiState.getOrAwaitValue()

            val expectedUiEffect = EditAccountUiEffect.ShowSnackbarError
            val actualUiEffect = viewModel.uiEffect.getOrAwaitValue()

            assertUiState(expectedUiState, actualUiState)
            assertEquals(expectedUiEffect, actualUiEffect)
        }

    @Test
    fun `GIVEN OnTryToSave action was sent and getCurrentUserUseCase as success but getUserDocument as failure WHEN sendAction was called THEN assert that the emitted uiState and uiEffect were the expected`() =
        coroutineTestRule.runBlockingTest {
            val action = EditAccountAction.Action.OnTryToSave

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

            val expectedUiState = EditAccountUiState.Resume(EditAccountUiModel())
            val actualUiState = viewModel.uiState.getOrAwaitValue()

            val expectedUiEffect = EditAccountUiEffect.ShowSnackbarError
            val actualUiEffect = viewModel.uiEffect.getOrAwaitValue()

            assertUiState(expectedUiState, actualUiState)
            assertEquals(expectedUiEffect, actualUiEffect)
        }

    @Test
    fun `GIVEN OnTryToSave action was sent and all use cases as success except updateUserDocument WHEN sendAction was called THEN assert that the emitted uiState and uiEffect were the expected`() =
        coroutineTestRule.runBlockingTest {
            val action = EditAccountAction.Action.OnTryToSave

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

            val updateUserDocumentUseCaseResult: Either<Failure, Success<Unit>> = Either.Left(
                Failure(GenericProblem("Generic problem occurred"))
            )
            coEvery {
                updateUserDocumentUseCase(dummyUserDomain)
            } returns updateUserDocumentUseCaseResult

            viewModel.sendAction(action)

            val expectedUiState = EditAccountUiState.Resume(EditAccountUiModel())
            val actualUiState = viewModel.uiState.getOrAwaitValue()

            val expectedUiEffect = EditAccountUiEffect.ShowSnackbarError
            val actualUiEffect = viewModel.uiEffect.getOrAwaitValue()

            assertUiState(expectedUiState, actualUiState)
            assertEquals(expectedUiEffect, actualUiEffect)
        }

    @Test
    fun `OnTypeNameField action was sent WHEN sendAction was called THEN assert that the emitted uiState was the expected`() {
        val name = "name"
        val action = EditAccountAction.Action.OnTypeNameField(name)

        viewModel.sendAction(action)

        val expectedUiState = EditAccountUiState.Resume(
            EditAccountUiModel().copy(
                editNameFieldUiState = EditAccountUiModel().editNameFieldUiState.copy(
                    name = name
                ),
            )
        )
        val actualUiState = viewModel.uiState.getOrAwaitValue()

        assertUiState(expectedUiState, actualUiState)
    }

    private fun assertUiState(expected: EditAccountUiState, actual: EditAccountUiState) {
        assertEquals(expected.javaClass, actual.javaClass)
        assertEquals(
            expected.uiModel.editNameFieldUiState.name,
            actual.uiModel.editNameFieldUiState.name
        )
        assertEquals(
            expected.uiModel.editNameFieldUiState.errorSupportingMessage,
            actual.uiModel.editNameFieldUiState.errorSupportingMessage
        )
        assertEquals(
            expected.uiModel.editNameFieldUiState.isError,
            actual.uiModel.editNameFieldUiState.isError
        )
        assertEquals(
            expected.uiModel.snackbarUiState.hostState.currentSnackbarData,
            actual.uiModel.snackbarUiState.hostState.currentSnackbarData
        )
        assertEquals(
            expected.uiModel.snackbarUiState.message,
            actual.uiModel.snackbarUiState.message
        )
        assertEquals(
            expected.uiModel.snackbarUiState.type,
            actual.uiModel.snackbarUiState.type
        )
        assertEquals(expected.uiModel.isLoading, actual.uiModel.isLoading)
        assertEquals(expected.uiModel.isSaveButtonEnabled, actual.uiModel.isSaveButtonEnabled)
    }
}