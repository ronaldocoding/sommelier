package br.com.sommelier.domain.usecase

import arrow.core.Either
import arrow.core.left
import arrow.core.right
import br.com.sommelier.base.result.GenericProblem
import br.com.sommelier.base.result.Problem
import br.com.sommelier.base.usecase.UseCase
import br.com.sommelier.domain.repository.AuthRepository
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test

@ExperimentalCoroutinesApi
class IsUserEmailVerifiedUseCaseTest {

    private val authRepository = mockk<AuthRepository>()

    private val coroutineDispatcher = StandardTestDispatcher()

    private lateinit var useCase: IsUserEmailVerifiedUseCase

    @Before
    fun setUp() {
        useCase = IsUserEmailVerifiedUseCase(authRepository, coroutineDispatcher)
    }

    @Test
    fun `GIVEN a successful result and a user with verified email WHEN execute use case THEN must return true`() =
        runTest(coroutineDispatcher) {
            val isUserEmailVerified: Either<Problem, Boolean> = true.right()

            every { authRepository.isUserEmailVerified() } returns isUserEmailVerified

            val result = useCase(UseCase.None())

            assertTrue(
                result.isRight {
                    it.data
                }
            )
        }

    @Test
    fun `GIVEN a successful result and a user with unverified email WHEN execute use case THEN must return false`() =
        runTest(coroutineDispatcher) {
            val isUserEmailVerified: Either<Problem, Boolean> = false.right()

            every { authRepository.isUserEmailVerified() } returns isUserEmailVerified

            val result = useCase(UseCase.None())

            assertFalse(
                result.isRight {
                    it.data
                }
            )
        }

    @Test
    fun `GIVEN an unsuccessful result WHEN execute use case THEN must return the expected failure result`() =
        runTest(coroutineDispatcher) {
            val errorMessage = "Generic problem occurred"
            val problem: Either<Problem, Boolean> = GenericProblem(errorMessage).left()

            every { authRepository.isUserEmailVerified() } returns problem

            val result = useCase(UseCase.None())

            assertTrue(
                result.isLeft {
                    it.problem is GenericProblem &&
                        (it.problem as GenericProblem).message == errorMessage
                }
            )
        }
}
