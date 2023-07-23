package br.com.sommelier.domain.usecase

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
class IsUserSignedInUseCaseTest {

    private val authRepository = mockk<AuthRepository>()

    private val coroutineDispatcher = StandardTestDispatcher()

    private lateinit var useCase: IsUserSignedInUseCase

    @Before
    fun setUp() {
        useCase = IsUserSignedInUseCase(authRepository, coroutineDispatcher)
    }

    @Test
    fun `GIVEN a signed in user WHEN execute use case THEN must return true`() =
        runTest(coroutineDispatcher) {
            val isUserSignedIn = true

            every { authRepository.isUserSignedIn() } returns isUserSignedIn

            val result = useCase(UseCase.None())

            assertTrue(result.isRight { it.data })
        }

    @Test
    fun `GIVEN a not signed in user WHEN execute use case THEN must return true`() =
        runTest(coroutineDispatcher) {
            val isUserSignedIn = false

            every { authRepository.isUserSignedIn() } returns isUserSignedIn

            val result = useCase(UseCase.None())

            assertFalse(result.isRight { it.data })
        }
}