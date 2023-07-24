package br.com.sommelier.domain.usecase

import arrow.core.Either
import br.com.sommelier.base.result.Problem
import br.com.sommelier.base.usecase.UseCase
import br.com.sommelier.domain.repository.AuthRepository
import kotlinx.coroutines.CoroutineDispatcher

class SendPasswordResetEmailUseCase(
    private val authRepository: AuthRepository,
    coroutineDispatcher: CoroutineDispatcher
) : UseCase<SendPasswordResetEmailUseCase.Params, Unit>(coroutineDispatcher) {

    override suspend fun execute(parameters: Params): Either<Problem, Unit> {
        return authRepository.sendPasswordResetEmail(parameters.userEmail)
    }

    data class Params(val userEmail: String)
}
