package br.com.sommelier.domain.usecase

import arrow.core.Either
import br.com.sommelier.base.result.Problem
import br.com.sommelier.base.usecase.UseCase
import br.com.sommelier.domain.model.UserDomain
import br.com.sommelier.domain.repository.UserRepository
import kotlinx.coroutines.CoroutineDispatcher

class GetUserDocumentUseCase(
    private val userRepository: UserRepository,
    coroutineDispatcher: CoroutineDispatcher
) : UseCase<GetUserDocumentUseCase.Params, UserDomain>(coroutineDispatcher) {

    override suspend fun execute(parameters: Params): Either<Problem, UserDomain> {
        return userRepository.getUser(parameters.userUid)
    }

    data class Params(val userUid: String)
}