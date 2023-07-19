package br.com.sommelier.base.usecase

import arrow.core.Either
import arrow.core.left
import arrow.core.right
import br.com.sommelier.base.result.GenericProblem
import br.com.sommelier.base.result.Problem
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertTrue
import org.junit.Test

@ExperimentalCoroutinesApi
class UseCaseTest {

    private val coroutinesDispatcher = StandardTestDispatcher()

    @Test
    fun `GIVEN use case completes normally WHEN get result THEN must return the expected success result`() =
        runTest(coroutinesDispatcher) {
            val successResult: Either<Problem, Boolean> = true.right()

            val useCase = givenUseCase<Boolean, Boolean> { successResult }

            val output = useCase(false)

            assertTrue(output.isRight {
                it.data
            })
        }

    @Test
    fun `GIVEN use case completes normally but with failure WHEN get result THEN must return the expected failure result`() =
        runTest(coroutinesDispatcher) {
            val failureResult: Either<Problem, Boolean> = GenericProblem("error").left()

            val useCase = givenUseCase<Boolean, Boolean> { failureResult }

            val output = useCase(false)

            assertTrue(output.isLeft {
                it.problem is GenericProblem && (it.problem as GenericProblem).message == "error"
            })
        }

    @Test
    fun `GIVEn use case throws an exception WHEN get result THEN must the return the expected failure result`() =
        runTest(coroutinesDispatcher) {
            val useCase = givenUseCase<Boolean, Boolean> { throw Exception("error") }

            val output = useCase(false)

            assertTrue(output.isLeft {
                it.problem is GenericProblem && (it.problem as GenericProblem).message == "error"
            })
        }

    @Test
    fun `GIVEn use case throws an exception with null message WHEN get result THEN must the return the expected failure result`() =
        runTest(coroutinesDispatcher) {
            val useCase = givenUseCase<Boolean, Boolean> { throw Exception() }

            val output = useCase(false)

            assertTrue(output.isLeft {
                it.problem is GenericProblem
                        && (it.problem as GenericProblem).message == "Generic problem occurred"
            })
        }

    private fun <P, R> givenUseCase(executeFunction: suspend (P) -> Either<Problem, R>): UseCase<P, R> {
        return object : UseCase<P, R>(coroutinesDispatcher) {
            override suspend fun execute(parameters: P): Either<Problem, R> {
                return executeFunction(parameters).let { result ->
                    when (result) {
                        is Problem -> result.left()
                        else -> result
                    }
                }
            }
        }
    }
}