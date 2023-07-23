package br.com.sommelier.domain.usecase

import arrow.core.Either
import arrow.core.left
import arrow.core.right
import br.com.sommelier.base.result.NullUserProblem
import br.com.sommelier.base.result.Problem
import br.com.sommelier.base.usecase.UseCase
import br.com.sommelier.domain.mapper.toDomain
import br.com.sommelier.domain.model.UserFirebase
import br.com.sommelier.domain.repository.AuthRepository
import kotlinx.coroutines.CoroutineDispatcher

class GetCurrentUserUseCase(
    private val authRepository: AuthRepository,
    coroutineDispatcher: CoroutineDispatcher
) : UseCase<UseCase.None, UserFirebase>(coroutineDispatcher) {

    override suspend fun execute(parameters: None): Either<Problem, UserFirebase> {
        return authRepository.getCurrentUser().getOrNull()?.toDomain()?.right()
            ?: NullUserProblem("Null user problem occurred").left()
    }
}