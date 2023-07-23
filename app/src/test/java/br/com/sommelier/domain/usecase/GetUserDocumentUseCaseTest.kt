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
import org.junit.Before
import org.junit.Test
import kotlin.test.assertTrue

@ExperimentalCoroutinesApi
class GetUserDocumentUseCaseTest {

    private val userRepository = mockk<UserRepository>()

    private val coroutineDispatcher = StandardTestDispatcher()

    private lateinit var useCase: GetUserDocumentUseCase

    @Before
    fun setUp() {
        useCase = GetUserDocumentUseCase(userRepository, coroutineDispatcher)
    }

    @Test
    fun `GIVEN  a successful result WHEN execute use case THEN return the expected user`() =
        runTest(coroutineDispatcher) {
            val expectedUser = UserDomain("email", "name", "uid")
            val successfulResult: Either<Problem, UserDomain> = expectedUser.right()

            coEvery { userRepository.getUser(any()) } returns successfulResult

            val output = useCase(GetUserDocumentUseCase.Params(expectedUser.uid))

            assertTrue(output.isRight {
                it.data.email == expectedUser.email
                        && it.data.name == expectedUser.name
                        && it.data.uid == expectedUser.uid
            })
        }

    @Test
    fun `GIVEN  an unsuccessful result WHEN execute use case THEN return the expected failure result`() =
        runTest(coroutineDispatcher) {
            val errorMessage = "Generic problem occurred"

            coEvery { userRepository.getUser(any()) } returns GenericProblem(errorMessage).left()

            val output = useCase(GetUserDocumentUseCase.Params("uid"))

            assertTrue(output.isLeft {
                it.problem is GenericProblem
                        && (it.problem as GenericProblem).message == errorMessage
            })
        }

}