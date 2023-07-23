package br.com.sommelier.domain.usecase

import arrow.core.Either
import br.com.sommelier.base.result.Problem
import br.com.sommelier.base.usecase.UseCase
import br.com.sommelier.domain.model.UserDomain
import br.com.sommelier.domain.repository.AuthRepository
import br.com.sommelier.domain.repository.UserRepository
import kotlinx.coroutines.CoroutineDispatcher

class UpdateUserEmailUseCase(
    private val authRepository: AuthRepository,
    private val userRepository: UserRepository,
    coroutineDispatcher: CoroutineDispatcher
) : UseCase<UserDomain, Unit>(coroutineDispatcher) {

    override suspend fun execute(parameters: UserDomain): Either<Problem, Unit> {
        val result = authRepository.updateUserEmail(parameters.email)
        if (result.isRight()) {
            userRepository.updateUser(parameters)
        }
        return result
    }
}