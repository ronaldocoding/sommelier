package br.com.sommelier.domain.usecase

import arrow.core.Either
import br.com.sommelier.base.result.Problem
import br.com.sommelier.base.usecase.UseCase
import br.com.sommelier.domain.repository.AuthRepository
import br.com.sommelier.domain.repository.UserRepository
import kotlinx.coroutines.CoroutineDispatcher

class DeleteUserUseCase(
    private val authRepository: AuthRepository,
    private val userRepository: UserRepository,
    coroutineDispatcher: CoroutineDispatcher
) : UseCase<DeleteUserUseCase.Params, Unit>(coroutineDispatcher) {

    override suspend fun execute(parameters: Params): Either<Problem, Unit> {
        val result = authRepository.deleteUser()
        if (result.isRight()) {
            userRepository.deleteUser(parameters.userUid)
        }
        return result
    }

    data class Params(val userUid: String)
}
