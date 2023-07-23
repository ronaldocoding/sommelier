package br.com.sommelier.domain.usecase

import arrow.core.Either
import arrow.core.left
import arrow.core.right
import br.com.sommelier.base.result.GenericProblem
import br.com.sommelier.base.result.Problem
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
class DeleteUserUseCaseTest {

    private val authRepository = mockk<AuthRepository>()

    private val userRepository = mockk<UserRepository>()

    private val coroutineDispatcher = StandardTestDispatcher()

    private lateinit var useCase: DeleteUserUseCase

    private val dummyUid = "uid"

    @Before
    fun setUp() {
        useCase = DeleteUserUseCase(authRepository, userRepository, coroutineDispatcher)
    }

    @Test
    fun `GIVEN a successful result in deleting user auth WHEN execute use case THEN assert that delete user doc was called`() =
        runTest(coroutineDispatcher) {
            val deleteUserAuthSuccessfulResult: Either<Problem, Unit> = Unit.right()

            coEvery { authRepository.deleteUser() } returns deleteUserAuthSuccessfulResult

            useCase(DeleteUserUseCase.Params(dummyUid))

            coVerify(exactly = 1) { userRepository.deleteUser(dummyUid) }
        }

    @Test
    fun `GIVEN an unsuccessful result in deleting user auth WHEN execute use case THEN assert that delete user doc was not called`() =
        runTest(coroutineDispatcher) {
            val deleteUserAuthUnsuccessfulResult: Either<Problem, Unit> =
                GenericProblem("Generic problem occurred").left()

            coEvery { authRepository.deleteUser() } returns deleteUserAuthUnsuccessfulResult

            useCase(DeleteUserUseCase.Params(dummyUid))

            coVerify(exactly = 0) { userRepository.deleteUser(dummyUid) }
        }

    @Test
    fun `GIVEN a successful result in deleting user auth and a successful result in deleting user doc WHEN execute use case THEN must return Unit`() =
        runTest(coroutineDispatcher) {
            val deleteUserAuthSuccessfulResult: Either<Problem, Unit> = Unit.right()

            val deleteUserDocSuccessfulResult: Either<Problem, Unit> = Unit.right()

            coEvery { authRepository.deleteUser() } returns deleteUserAuthSuccessfulResult

            coEvery { userRepository.deleteUser(dummyUid) } returns deleteUserDocSuccessfulResult

            val output = useCase(DeleteUserUseCase.Params(dummyUid))

            assertTrue(output.isRight {
                it.data == Unit
            })
        }

    @Test
    fun `GIVEN a successful result in deleting user auth and an unsuccessful result in deleting user doc WHEN execute use case THEN must return Unit`() =
        runTest(coroutineDispatcher) {
            val deleteUserAuthSuccessfulResult: Either<Problem, Unit> = Unit.right()

            val deleteUserDocUnsuccessfulResult: Either<Problem, Unit> = GenericProblem(
                "Generic problem occurred"
            ).left()

            coEvery { authRepository.deleteUser() } returns deleteUserAuthSuccessfulResult

            coEvery { userRepository.deleteUser(dummyUid) } returns deleteUserDocUnsuccessfulResult

            val output = useCase(DeleteUserUseCase.Params(dummyUid))

            assertTrue(output.isRight {
                it.data == Unit
            })
        }

    @Test
    fun `GIVEN an unsuccessful result in deleting user auth WHEN execute use case THEN must return the expected failure result`() =
        runTest(coroutineDispatcher) {
            val errorMessage = "Generic problem occurred"
            val deleteUserAuthUnSuccessfulResult: Either<Problem, Unit> =
                GenericProblem(errorMessage).left()

            coEvery { authRepository.deleteUser() } returns deleteUserAuthUnSuccessfulResult

            val output = useCase(DeleteUserUseCase.Params(dummyUid))

            assertTrue(output.isLeft {
                it.problem is GenericProblem &&
                        (it.problem as GenericProblem).message == errorMessage
            })
        }
}