package br.com.sommelier.domain.repository

import arrow.core.Either
import br.com.sommelier.base.result.Problem
import com.google.firebase.auth.FirebaseUser

interface AuthRepository {
    suspend fun registerUser(email: String, password: String): Either<Problem, Unit>
    suspend fun deleteUser(): Either<Problem, Unit>
    suspend fun signInUser(email: String, password: String): Either<Problem, Unit>
    suspend fun updateUserEmail(email: String): Either<Problem, Unit>
    suspend fun updateUserPassword(password: String): Either<Problem, Unit>
    suspend fun reauthenticateUser(email: String, password: String): Either<Problem, Unit>
    suspend fun sendEmailVerification(): Either<Problem, Unit>
    suspend fun sendPasswordResetEmail(email: String): Either<Problem, Unit>
    fun signOutUser(): Either<Problem, Unit>
    fun isUserSignedIn(): Boolean
    fun isUserEmailVerified(): Either<Problem, Boolean>
    fun getCurrentUser(): Either<Problem, FirebaseUser>
}