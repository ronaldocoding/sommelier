package br.com.sommelier.domain.usecase

import arrow.core.Either
import br.com.sommelier.base.result.Problem
import br.com.sommelier.base.usecase.UseCase
import br.com.sommelier.domain.repository.AuthRepository
import kotlinx.coroutines.CoroutineDispatcher

class ReauthenticateUserUseCase(
    private val authRepository: AuthRepository,
    coroutineDispatcher: CoroutineDispatcher
) : UseCase<ReauthenticateUserUseCase.Params, Unit>(coroutineDispatcher) {

    override suspend fun execute(parameters: Params): Either<Problem, Unit> {
        return authRepository.reauthenticateUser(
            parameters.userEmail,
            parameters.userPassword
        )
    }

    data class Params(val userEmail: String, val userPassword: String)
}