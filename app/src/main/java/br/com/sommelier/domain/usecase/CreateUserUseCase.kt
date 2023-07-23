package br.com.sommelier.domain.usecase

import arrow.core.Either
import br.com.sommelier.base.result.Problem
import br.com.sommelier.base.usecase.UseCase
import br.com.sommelier.domain.model.UserDomain
import br.com.sommelier.domain.model.UserInfo
import br.com.sommelier.domain.repository.AuthRepository
import br.com.sommelier.domain.repository.UserRepository
import kotlinx.coroutines.CoroutineDispatcher

class CreateUserUseCase(
    private val authRepository: AuthRepository,
    private val userRepository: UserRepository,
    coroutineDispatcher: CoroutineDispatcher
) : UseCase<UserInfo, Unit>(coroutineDispatcher) {

    override suspend fun execute(parameters: UserInfo): Either<Problem, Unit> {
        val result = authRepository.registerUser(parameters.email, parameters.password)
        if (result.isRight()) {
            userRepository.saveUser(UserDomain(parameters.name, parameters.email, parameters.uid))
        }
        return result
    }
}