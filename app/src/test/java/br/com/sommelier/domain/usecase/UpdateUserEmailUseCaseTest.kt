package br.com.sommelier.domain.usecase

import arrow.core.Either
import arrow.core.left
import arrow.core.right
import br.com.sommelier.base.result.GenericProblem
import br.com.sommelier.base.result.Problem
import br.com.sommelier.domain.model.UserDomain
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
class UpdateUserEmailUseCaseTest {

    private val authRepository = mockk<AuthRepository>()

    private val userRepository = mockk<UserRepository>()

    private val coroutineDispatcher = StandardTestDispatcher()

    private lateinit var useCase: UpdateUserEmailUseCase

    private val dummyUserDomain = UserDomain("email", "name", "uid")

    @Before
    fun setUp() {
        useCase = UpdateUserEmailUseCase(authRepository, userRepository, coroutineDispatcher)
    }

    @Test
    fun `GIVEN a successful result in updating user email WHEN execute use case THEN assert that updateUser was called`() =
        runTest(coroutineDispatcher) {
            val updateUserEmailResult: Either<Problem, Unit> = Unit.right()

            coEvery {
                authRepository.updateUserEmail(dummyUserDomain.email)
            } returns updateUserEmailResult

            useCase(dummyUserDomain)

            coVerify(exactly = 1) {
                userRepository.updateUser(dummyUserDomain)
            }
        }

    @Test
    fun `GIVEN a unsuccessful result in updating user email WHEN execute use case THEN assert that updateUser was not called`() =
        runTest(coroutineDispatcher) {
            val updateUserEmailResult: Either<Problem, Unit> =
                GenericProblem("Generic problem occurred").left()

            coEvery {
                authRepository.updateUserEmail(dummyUserDomain.email)
            } returns updateUserEmailResult

            useCase(dummyUserDomain)

            coVerify(exactly = 0) {
                userRepository.updateUser(dummyUserDomain)
            }
        }

    @Test
    fun `GIVEN a successful result in updating user email and a successful result in updating user doc WHEN execute use case THEN must return Unit`() =
        runTest(coroutineDispatcher) {
            val updateUserEmailResult: Either<Problem, Unit> = Unit.right()

            val updateUserResult: Either<Problem, Unit> = Unit.right()

            coEvery {
                authRepository.updateUserEmail(dummyUserDomain.email)
            } returns updateUserEmailResult

            coEvery {
                userRepository.updateUser(dummyUserDomain)
            } returns updateUserResult

            val output = useCase(dummyUserDomain)

            assertTrue(output.isRight {
                it.data == Unit
            })
        }

    @Test
    fun `GIVEN a successful result in updating user email and an unsuccessful result in updating user doc WHEN execute use case THEN must return Unit`() =
        runTest(coroutineDispatcher) {
            val updateUserEmailResult: Either<Problem, Unit> = Unit.right()

            val updateUserResult: Either<Problem, Unit> =
                GenericProblem("Generic problem occurred").left()

            coEvery {
                authRepository.updateUserEmail(dummyUserDomain.email)
            } returns updateUserEmailResult

            coEvery {
                userRepository.updateUser(dummyUserDomain)
            } returns updateUserResult

            val output = useCase(dummyUserDomain)

            assertTrue(output.isRight {
                it.data == Unit
            })
        }

    @Test
    fun `GIVEN an unsuccessful result in updating user email WHEN execute use case THEN must return the expected failure`() =
        runTest(coroutineDispatcher) {
            val updateUserEmailResult: Either<Problem, Unit> =
                GenericProblem("Generic problem occurred").left()

            coEvery {
                authRepository.updateUserEmail(dummyUserDomain.email)
            } returns updateUserEmailResult

            val output = useCase(dummyUserDomain)

            assertTrue(output.isLeft {
                it.problem is GenericProblem &&
                        (it.problem as GenericProblem).message == "Generic problem occurred"
            })
        }
}