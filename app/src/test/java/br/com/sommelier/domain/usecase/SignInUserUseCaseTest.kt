package br.com.sommelier.domain.usecase

import arrow.core.Either
import arrow.core.left
import arrow.core.right
import br.com.sommelier.base.result.GenericProblem
import br.com.sommelier.base.result.Problem
import br.com.sommelier.domain.repository.AuthRepository
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test

@ExperimentalCoroutinesApi
class SignInUserUseCaseTest {

    private val authRepository = mockk<AuthRepository>()

    private val coroutineDispatcher = StandardTestDispatcher()

    private lateinit var useCase: SignInUserUseCase

    private val dummyEmail = "email"

    private val dummyPassword = "password"

    @Before
    fun setUp() {
        useCase = SignInUserUseCase(authRepository, coroutineDispatcher)
    }

    @Test
    fun `GIVEN a successful result WHEN call signInUser THEN must return Unit`() =
        runTest(coroutineDispatcher) {
            val successfulResult: Either<Problem, Unit> = Unit.right()

            coEvery {
                authRepository.signInUser(
                    dummyEmail,
                    dummyPassword
                )
            } returns successfulResult

            val output = useCase(SignInUserUseCase.Params(dummyEmail, dummyPassword))

            assertTrue(
                output.isRight {
                    it.data == Unit
                }
            )
        }

    @Test
    fun `GIVEN an unsuccessful result WHEN call signInUser THEN must return the expected failure`() =
        runTest(coroutineDispatcher) {
            val errorMessage = "Generic problem occurred"
            val unsuccessfulResult: Either<Problem, Unit> = GenericProblem(errorMessage).left()

            coEvery {
                authRepository.signInUser(
                    dummyEmail,
                    dummyPassword
                )
            } returns unsuccessfulResult

            val output = useCase(SignInUserUseCase.Params(dummyEmail, dummyPassword))

            assertTrue(
                output.isLeft {
                    it.problem is GenericProblem &&
                        (it.problem as GenericProblem).message == errorMessage
                }
            )
        }
}
