package br.com.sommelier.base.usecase

import arrow.core.Either
import arrow.core.left
import arrow.core.right
import br.com.sommelier.base.result.Failure
import br.com.sommelier.base.result.GenericProblem
import br.com.sommelier.base.result.Problem
import br.com.sommelier.base.result.Success
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext

abstract class UseCase<in P, R>(private val coroutinesDispatcher: CoroutineDispatcher) {

    protected abstract suspend fun execute(parameters: P): Either<Problem, R>

    suspend operator fun invoke(parameters: P): Either<Failure, Success<R>> {
        return try {
            withContext(coroutinesDispatcher) {
                execute(parameters).let { result ->
                    when (result) {
                        is Either.Left -> Failure(result.value).left()
                        is Either.Right -> Success(result.value).right()
                    }
                }
            }
        } catch (e: Exception) {
            Failure(GenericProblem(e.message ?: "Generic problem occurred")).left()
        }
    }

    class None
}