package br.com.sommelier.domain.repository

import arrow.core.Either
import br.com.sommelier.base.result.Problem
import br.com.sommelier.domain.model.UserDomain

interface UserRepository {
    suspend fun saveUser(user: UserDomain): Either<Problem, Unit>
    suspend fun getUser(uid: String): Either<Problem, UserDomain>
    suspend fun updateUser(user: UserDomain): Either<Problem, Unit>
    suspend fun deleteUser(uid: String): Either<Problem, Unit>
}
