package br.com.sommelier.presentation.main.viewmodel

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import arrow.core.Either
import br.com.sommelier.base.result.Failure
import br.com.sommelier.base.result.GenericProblem
import br.com.sommelier.base.result.Success
import br.com.sommelier.domain.usecase.IsUserEmailVerifiedUseCase
import br.com.sommelier.domain.usecase.IsUserSignedInUseCase
import br.com.sommelier.presentation.main.action.MainAction
import br.com.sommelier.presentation.main.model.MainUiModel
import br.com.sommelier.presentation.main.state.MainUiEffect
import br.com.sommelier.presentation.main.state.MainUiState
import br.com.sommelier.shared.route.SommelierRoute
import br.com.sommelier.testrule.CoroutineTestRule
import br.com.sommelier.util.ext.getOrAwaitValue
import io.mockk.coEvery
import io.mockk.mockk
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import kotlin.test.assertEquals

class MainViewModelTest {

    @get: Rule
    val instantTaskExecutorRule = InstantTaskExecutorRule()

    @get:Rule
    val coroutineTestRule = CoroutineTestRule()

    private val isUserSignedInUseCase = mockk<IsUserSignedInUseCase>()

    private val isUserEmailVerifiedUseCase = mockk<IsUserEmailVerifiedUseCase>()

    private lateinit var viewModel: MainViewModel

    @Before
    fun setUp() {
        viewModel = MainViewModel(isUserSignedInUseCase, isUserEmailVerifiedUseCase)
    }

    @Test
    fun `GIVEN OnInitial action sent WHEN sendAction was called THEN assert that the emitted uiEffect was ShowLoading`() {
        val action = MainAction.Action.OnInitial

        viewModel.sendAction(action)

        val expectedUiEffect = MainUiEffect.ShowLoading
        val actualUiEffect = viewModel.uiEffect.getOrAwaitValue()

        assertEquals(expectedUiEffect, actualUiEffect)
    }

    @Test
    fun `GIVEN OnCheckIfUserIsSignedIn action sent and user is both signed in and has a verified email WHEN sendAction was called THEN assert that the uiState was the expected`() =
        coroutineTestRule.run {
            val isUserSignedIn = true
            val isUserEmailVerified = true
            val action = MainAction.Action.OnCheckIfUserIsSignedIn

            val isSignedInUseCaseResult: Either<Failure, Success<Boolean>> = Either.Right(
                Success(isUserSignedIn)
            )
            coEvery { isUserSignedInUseCase(any()) } returns isSignedInUseCaseResult

            val isUserEmailVerifiedUseCaseResult: Either<Failure, Success<Boolean>> = Either.Right(
                Success(isUserEmailVerified)
            )
            coEvery { isUserEmailVerifiedUseCase(any()) } returns isUserEmailVerifiedUseCaseResult

            viewModel.sendAction(action)

            val expectedUiState = MainUiState.Resume(
                MainUiModel(startDestination = SommelierRoute.HOME.name)
            )
            val actualUiState = viewModel.uiState.getOrAwaitValue()

            assertEquals(
                expectedUiState.uiModel.startDestination,
                actualUiState.uiModel.startDestination
            )
        }

    @Test
    fun `GIVEN OnCheckIfUserIsSignedIn action sent and user is signed in but does not have a verified email WHEN sendAction was called THEN assert that the uiState was the expected`() =
        coroutineTestRule.run {
            val isUserSignedIn = true
            val isUserEmailVerified = false
            val action = MainAction.Action.OnCheckIfUserIsSignedIn

            val isSignedInUseCaseResult: Either<Failure, Success<Boolean>> = Either.Right(
                Success(isUserSignedIn)
            )
            coEvery { isUserSignedInUseCase(any()) } returns isSignedInUseCaseResult

            val isUserEmailVerifiedUseCaseResult: Either<Failure, Success<Boolean>> = Either.Right(
                Success(isUserEmailVerified)
            )
            coEvery { isUserEmailVerifiedUseCase(any()) } returns isUserEmailVerifiedUseCaseResult

            viewModel.sendAction(action)

            val expectedUiState = MainUiState.Resume(
                MainUiModel(startDestination = SommelierRoute.LOGIN.name)
            )
            val actualUiState = viewModel.uiState.getOrAwaitValue()

            assertEquals(
                expectedUiState.uiModel.startDestination,
                actualUiState.uiModel.startDestination
            )
        }

    @Test
    fun `GIVEN OnCheckIfUserIsSignedIn action sent and user is signed in but isUserEmailVerifiedUseCase as failure WHEN sendAction was called THEN assert that the uiState was the expected`() =
        coroutineTestRule.run {
            val isUserSignedIn = true
            val action = MainAction.Action.OnCheckIfUserIsSignedIn

            val isSignedInUseCaseResult: Either<Failure, Success<Boolean>> = Either.Right(
                Success(isUserSignedIn)
            )
            coEvery { isUserSignedInUseCase(any()) } returns isSignedInUseCaseResult

            val isUserEmailVerifiedUseCaseResult: Either<Failure, Success<Boolean>> = Either.Left(
                Failure(GenericProblem("Generic problem occurred"))
            )
            coEvery { isUserEmailVerifiedUseCase(any()) } returns isUserEmailVerifiedUseCaseResult

            viewModel.sendAction(action)

            val expectedUiState = MainUiState.Resume(
                MainUiModel(startDestination = SommelierRoute.LOGIN.name)
            )
            val actualUiState = viewModel.uiState.getOrAwaitValue()

            assertEquals(
                expectedUiState.uiModel.startDestination,
                actualUiState.uiModel.startDestination
            )
        }

    @Test
    fun `GIVEN OnCheckIfUserIsSignedIn action sent and user is not signed in WHEN sendAction was called THEN assert that the uiState was the expected`() =
        coroutineTestRule.run {
            val isUserSignedIn = false
            val action = MainAction.Action.OnCheckIfUserIsSignedIn

            val isSignedInUseCaseResult: Either<Failure, Success<Boolean>> = Either.Right(
                Success(isUserSignedIn)
            )
            coEvery { isUserSignedInUseCase(any()) } returns isSignedInUseCaseResult

            viewModel.sendAction(action)

            val expectedUiState = MainUiState.Resume(
                MainUiModel(startDestination = SommelierRoute.LOGIN.name)
            )
            val actualUiState = viewModel.uiState.getOrAwaitValue()

            assertEquals(
                expectedUiState.uiModel.startDestination,
                actualUiState.uiModel.startDestination
            )
        }

    @Test
    fun `GIVEN OnCheckIfUserIsSignedIn action sent isUserSignedInUseCase as failure WHEN sendAction was called THEN assert that the uiState was the expected`() =
        coroutineTestRule.run {
            val action = MainAction.Action.OnCheckIfUserIsSignedIn

            val isSignedInUseCaseResult: Either<Failure, Success<Boolean>> = Either.Left(
                Failure(GenericProblem("Generic problem occurred"))
            )
            coEvery { isUserSignedInUseCase(any()) } returns isSignedInUseCaseResult

            viewModel.sendAction(action)

            val expectedUiState = MainUiState.Resume(
                MainUiModel(startDestination = SommelierRoute.LOGIN.name)
            )
            val actualUiState = viewModel.uiState.getOrAwaitValue()

            assertEquals(
                expectedUiState.uiModel.startDestination,
                actualUiState.uiModel.startDestination
            )
        }
}
