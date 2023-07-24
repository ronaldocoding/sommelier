package br.com.sommelier.domain.usecase

import arrow.core.left
import arrow.core.right
import br.com.sommelier.base.result.AlreadySignedOutUserProblem
import br.com.sommelier.base.usecase.UseCase
import br.com.sommelier.domain.repository.AuthRepository
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test

@ExperimentalCoroutinesApi
class SignOutUserUseCaseTest {

    private val authRepository = mockk<AuthRepository>()

    private val coroutineDispatcher = StandardTestDispatcher()

    private lateinit var signOutUserUseCase: SignOutUserUseCase

    @Before
    fun setUp() {
        signOutUserUseCase = SignOutUserUseCase(authRepository, coroutineDispatcher)
    }

    @Test
    fun `GIVEN a successful result WHEN execute use case THEN must return the expected success result`() =
        runTest(coroutineDispatcher) {
            every { authRepository.signOutUser() } returns Unit.right()

            val output = signOutUserUseCase(UseCase.None())

            assertTrue(
                output.isRight {
                    it.data == Unit
                }
            )
        }

    @Test
    fun `GIVEN an unsuccessful result WHEN execute use case THEN must return the expected failure result`() =
        runTest(coroutineDispatcher) {
            val errorMessage = "Already signed out user problem occurred"
            every {
                authRepository.signOutUser()
            } returns AlreadySignedOutUserProblem(errorMessage).left()

            val output = signOutUserUseCase(UseCase.None())

            assertTrue(
                output.isLeft() {
                    it.problem is AlreadySignedOutUserProblem &&
                        (it.problem as AlreadySignedOutUserProblem).message == errorMessage
                }
            )
        }
}
