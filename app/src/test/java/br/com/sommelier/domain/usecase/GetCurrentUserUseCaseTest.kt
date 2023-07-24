package br.com.sommelier.domain.usecase

import arrow.core.Either
import arrow.core.right
import br.com.sommelier.base.result.NullUserProblem
import br.com.sommelier.base.result.Problem
import br.com.sommelier.base.usecase.UseCase
import br.com.sommelier.domain.repository.AuthRepository
import com.google.firebase.auth.FirebaseUser
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test

@ExperimentalCoroutinesApi
class GetCurrentUserUseCaseTest {

    private val authRepository = mockk<AuthRepository>()

    private val coroutineDispatcher = StandardTestDispatcher()

    private lateinit var useCase: GetCurrentUserUseCase

    @Before
    fun setUp() {
        useCase = GetCurrentUserUseCase(authRepository, coroutineDispatcher)
    }

    @Test
    fun `GIVEN a successful result and non-null user WHEN execute use case THEN must return the expected user`() =
        runTest(coroutineDispatcher) {
            val firebaseUser = mockk<FirebaseUser>().apply {
                every { email } returns "email"
                every { uid } returns "uid"
            }

            val successfulResult: Either<Problem, FirebaseUser> = firebaseUser.right()

            every { authRepository.getCurrentUser() } returns successfulResult

            val output = useCase(UseCase.None())

            assertTrue(
                output.isRight {
                    it.data.email == firebaseUser.email && it.data.uid == firebaseUser.uid
                }
            )
        }

    @Test
    fun `GIVEN a successful result and null user WHEN execute use case THEN must return the expected failure result`() =
        runTest(coroutineDispatcher) {
            val nullFirebaseUser = null
            val errorMessage = "Null user problem occurred"

            every { authRepository.getCurrentUser().getOrNull() } returns nullFirebaseUser

            val output = useCase(UseCase.None())

            assertTrue(
                output.isLeft {
                    it.problem is NullUserProblem &&
                        (it.problem as NullUserProblem).message == errorMessage
                }
            )
        }
}
