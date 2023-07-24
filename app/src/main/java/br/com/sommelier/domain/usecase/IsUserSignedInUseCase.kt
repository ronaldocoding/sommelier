package br.com.sommelier.domain.usecase

import arrow.core.Either
import arrow.core.right
import br.com.sommelier.base.result.Problem
import br.com.sommelier.base.usecase.UseCase
import br.com.sommelier.domain.repository.AuthRepository
import kotlinx.coroutines.CoroutineDispatcher

class IsUserSignedInUseCase(
    private val authRepository: AuthRepository,
    coroutineDispatcher: CoroutineDispatcher
) : UseCase<UseCase.None, Boolean>(coroutineDispatcher) {

    override suspend fun execute(parameters: None): Either<Problem, Boolean> {
        return authRepository.isUserSignedIn().right()
    }
}
