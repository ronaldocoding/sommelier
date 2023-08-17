package br.com.sommelier.domain.usecase

import arrow.core.Either
import arrow.core.left
import arrow.core.right
import br.com.sommelier.base.result.GenericProblem
import br.com.sommelier.base.result.Problem
import br.com.sommelier.base.result.RegisterUserProblem
import br.com.sommelier.domain.model.UserInfo
import br.com.sommelier.domain.repository.AuthRepository
import br.com.sommelier.domain.repository.UserRepository
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test

@ExperimentalCoroutinesApi
class CreateUserUseCaseTest {

    private val authRepository = mockk<AuthRepository>()

    private val userRepository = mockk<UserRepository>()

    private val coroutineDispatcher = StandardTestDispatcher()

    private lateinit var useCase: CreateUserUseCase

    private val dummyUserInfo = UserInfo("email", "password", "name")

    @Before
    fun setUp() {
        useCase = CreateUserUseCase(authRepository, userRepository, coroutineDispatcher)
    }

    @Test
    fun `GIVEN a successful result in register a user WHEN execute use case THEN assert that saveUser was called`() =
        runTest(coroutineDispatcher) {
            val uid = "uid"
            val successfulResult: Either<Problem, String> = uid.right()

            coEvery { authRepository.registerUser(any(), any()) } returns successfulResult

            useCase(dummyUserInfo)

            coVerify(exactly = 1) {
                userRepository.saveUser(any())
            }
        }

    @Test
    fun `GIVEN an unsuccessful result in register a user WHEN execute use case THEN assert that saveUser was not called`() =
        runTest(coroutineDispatcher) {
            val unsuccessfulResult: Either<Problem, String> =
                RegisterUserProblem("Register user problem occurred").left()

            coEvery { authRepository.registerUser(any(), any()) } returns unsuccessfulResult

            useCase(dummyUserInfo)

            coVerify(exactly = 0) {
                userRepository.saveUser(any())
            }
        }

    @Test
    fun `GIVEN a successful result in register a user and a successful result in save a user WHEN execute use case THEN must return Unit`() =
        runTest(coroutineDispatcher) {
            val uid = "uid"
            val registerSuccessfulResult: Either<Problem, String> = uid.right()

            val saveSuccessfulResult: Either<Problem, Unit> = Unit.right()

            coEvery { authRepository.registerUser(any(), any()) } returns registerSuccessfulResult

            coEvery { userRepository.saveUser(any()) } returns saveSuccessfulResult

            val output = useCase(dummyUserInfo)

            assertTrue(
                output.isRight {
                    it.data == Unit
                }
            )
        }

    @Test
    fun `GIVEN a successful result in register a user and an unsuccessful result in save a user WHEN execute use case THEN must return Unit`() =
        runTest(coroutineDispatcher) {
            val uid = "uid"
            val registerSuccessfulResult: Either<Problem, String> = uid.right()

            val saveUnsuccessfulResult: Either<Problem, Unit> =
                GenericProblem("Generic problem occurred").left()

            coEvery { authRepository.registerUser(any(), any()) } returns registerSuccessfulResult

            coEvery { userRepository.saveUser(any()) } returns saveUnsuccessfulResult

            val output = useCase(dummyUserInfo)

            assertTrue(
                output.isRight {
                    it.data == Unit
                }
            )
        }

    @Test
    fun `GIVEN an unsuccessful result in register a user WHEN execute use case THEN must return the expected failure result`() =
        runTest(coroutineDispatcher) {
            val errorMessage = "Generic problem occurred"
            val unsuccessfulResult: Either<Problem, String> = GenericProblem(errorMessage).left()

            coEvery { authRepository.registerUser(any(), any()) } returns unsuccessfulResult

            val output = useCase(dummyUserInfo)

            assertTrue(
                output.isLeft {
                    it.problem is GenericProblem &&
                        (it.problem as GenericProblem).message == errorMessage
                }
            )
        }
}
