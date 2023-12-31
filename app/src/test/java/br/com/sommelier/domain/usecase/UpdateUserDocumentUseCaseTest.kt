package br.com.sommelier.domain.usecase

import arrow.core.Either
import arrow.core.left
import arrow.core.right
import br.com.sommelier.base.result.GenericProblem
import br.com.sommelier.base.result.Problem
import br.com.sommelier.domain.model.UserDomain
import br.com.sommelier.domain.repository.UserRepository
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test

@ExperimentalCoroutinesApi
class UpdateUserDocumentUseCaseTest {

    private val userRepository = mockk<UserRepository>()

    private val coroutineDispatcher = StandardTestDispatcher()

    private lateinit var useCase: UpdateUserDocumentUseCase

    private val dummyUserDomain = UserDomain("email", "name", "uid")

    @Before
    fun setUp() {
        useCase = UpdateUserDocumentUseCase(userRepository, coroutineDispatcher)
    }

    @Test
    fun `GIVEN a successful result WHEN execute use case THEN must return Unit`() =
        runTest(coroutineDispatcher) {
            val successfulResult: Either<Problem, Unit> = Unit.right()

            coEvery { userRepository.updateUser(dummyUserDomain) } returns successfulResult

            val output = useCase(dummyUserDomain)

            assertTrue(
                output.isRight {
                    it.data == Unit
                }
            )
        }

    @Test
    fun `GIVEN an unsuccessful result WHEN execute use case THEN must return failure result`() =
        runTest(coroutineDispatcher) {
            val errorMessage = "Generic problem occurred"
            val unsuccessfulResult: Either<Problem, Unit> = GenericProblem(errorMessage).left()

            coEvery { userRepository.updateUser(dummyUserDomain) } returns unsuccessfulResult

            val output = useCase(dummyUserDomain)

            assertTrue(
                output.isLeft {
                    it.problem is GenericProblem && (it.problem as GenericProblem).message == errorMessage
                }
            )
        }
}
