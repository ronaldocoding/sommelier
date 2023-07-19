package br.com.sommelier.data.repository

import arrow.core.Either
import arrow.core.left
import arrow.core.right
import br.com.sommelier.domain.repository.AuthRepository
import br.com.sommelier.base.result.AlreadySignedOutUserProblem
import br.com.sommelier.base.result.DeleteUserProblem
import br.com.sommelier.base.result.NullResultProblem
import br.com.sommelier.base.result.NullUserProblem
import br.com.sommelier.base.result.Problem
import br.com.sommelier.base.result.ReauthenticateUserProblem
import br.com.sommelier.base.result.RegisterUserProblem
import br.com.sommelier.base.result.SendEmailVerificationProblem
import br.com.sommelier.base.result.SendPasswordResetEmailProblem
import br.com.sommelier.base.result.SignInUserProblem
import br.com.sommelier.base.result.UpdateUserEmailProblem
import br.com.sommelier.base.result.UpdateUserPasswordProblem
import com.google.firebase.auth.EmailAuthProvider
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import kotlinx.coroutines.tasks.await

class AuthRepositoryImpl(private val firebaseAuth: FirebaseAuth) : AuthRepository {

    override suspend fun registerUser(email: String, password: String): Either<Problem, Unit> {
        val task = firebaseAuth.createUserWithEmailAndPassword(email, password)
        return try {
            val result = task.await()
            if (result.user == null) {
                NullUserProblem("Null user problem occurred").left()
            } else if (task.isSuccessful) {
                Unit.right()
            } else {
                RegisterUserProblem(
                    task.exception?.message ?: "An unknown problem occurred"
                ).left()
            }
        } catch (e: Exception) {
            RegisterUserProblem(
                e.message ?: "An unknown problem occurred"
            ).left()
        }
    }

    override suspend fun deleteUser(): Either<Problem, Unit> {
        val task = firebaseAuth.currentUser?.delete()
        return try {
            task?.await()
            if (task != null) {
                if (task.isSuccessful) {
                    Unit.right()
                } else {
                    DeleteUserProblem(
                        task.exception?.message ?: "An unknown problem occurred"
                    ).left()
                }
            } else {
                NullResultProblem("Null result problem occurred").left()
            }
        } catch (e: Exception) {
            DeleteUserProblem(
                e.message ?: "An unknown problem occurred"
            ).left()
        }
    }

    override suspend fun signInUser(email: String, password: String): Either<Problem, Unit> {
        val task = firebaseAuth.signInWithEmailAndPassword(email, password)
        return try {
            val result = task.await()
            if (result.user == null) {
                NullUserProblem("Null user problem occurred").left()
            } else if (task.isSuccessful) {
                Unit.right()
            } else {
                SignInUserProblem(
                    task.exception?.message ?: "An unknown problem occurred"
                ).left()
            }
        } catch (e: Exception) {
            SignInUserProblem(
                e.message ?: "An unknown problem occurred"
            ).left()
        }
    }

    override suspend fun updateUserEmail(email: String): Either<Problem, Unit> {
        val currentUser = firebaseAuth.currentUser
        return try {
            if (currentUser != null) {
                val task = currentUser.updateEmail(email)
                task.await()
                if (task.isSuccessful) {
                    Unit.right()
                } else {
                    UpdateUserEmailProblem(
                        task.exception?.message ?: "An unknown problem occurred"
                    ).left()
                }
            } else {
                NullUserProblem("Null user problem occurred").left()
            }
        } catch (e: Exception) {
            UpdateUserEmailProblem(
                e.message ?: "An unknown problem occurred"
            ).left()
        }
    }

    override suspend fun updateUserPassword(password: String): Either<Problem, Unit> {
        val currentUser = firebaseAuth.currentUser
        return try {
            if (currentUser != null) {
                val task = currentUser.updatePassword(password)
                task.await()
                if (task.isSuccessful) {
                    Unit.right()
                } else {
                    UpdateUserPasswordProblem(
                        task.exception?.message ?: "An unknown problem occurred"
                    ).left()
                }
            } else {
                NullUserProblem("Null user problem occurred").left()
            }
        } catch (e: Exception) {
            UpdateUserPasswordProblem(
                e.message ?: "An unknown problem occurred"
            ).left()
        }
    }

    override suspend fun reauthenticateUser(
        email: String,
        password: String
    ): Either<Problem, Unit> {
        val currentUser = firebaseAuth.currentUser
        return try {
            if (currentUser != null) {
                val credential = EmailAuthProvider.getCredential(email, password)
                val task = currentUser.reauthenticate(credential)
                task.await()
                if (task.isSuccessful) {
                    Unit.right()
                } else {
                    ReauthenticateUserProblem(
                        task.exception?.message ?: "An unknown problem occurred"
                    ).left()
                }
            } else {
                NullUserProblem("Null user problem occurred").left()
            }
        } catch (e: Exception) {
            ReauthenticateUserProblem(
                e.message ?: "An unknown problem occurred"
            ).left()
        }
    }

    override suspend fun sendEmailVerification(): Either<Problem, Unit> {
        val currentUser = firebaseAuth.currentUser
        return try {
            if (currentUser != null) {
                val task = currentUser.sendEmailVerification()
                task.await()
                if (task.isSuccessful) {
                    Unit.right()
                } else {
                    SendEmailVerificationProblem(
                        task.exception?.message ?: "An unknown problem occurred"
                    ).left()
                }
            } else {
                NullUserProblem("Null user problem occurred").left()
            }
        } catch (e: Exception) {
            SendEmailVerificationProblem(
                e.message ?: "An unknown problem occurred"
            ).left()
        }
    }

    override suspend fun sendPasswordResetEmail(email: String): Either<Problem, Unit> {
        val task = firebaseAuth.sendPasswordResetEmail(email)
        return try {
            task.await()
            if (task.isSuccessful) {
                Unit.right()
            } else {
                SendPasswordResetEmailProblem(
                    task.exception?.message ?: "An unknown problem occurred"
                ).left()
            }
        } catch (e: Exception) {
            SendPasswordResetEmailProblem(
                e.message ?: "An unknown problem occurred"
            ).left()
        }
    }

    override fun signOutUser(): Either<Problem, Unit> {
        return firebaseAuth.currentUser?.let {
            firebaseAuth.signOut()
            Unit.right()
        } ?: AlreadySignedOutUserProblem("Already signed out user problem occurred").left()
    }

    override fun isUserSignedIn(): Boolean {
        return firebaseAuth.currentUser != null
    }

    override fun isUserEmailVerified(): Either<Problem, Boolean> {
        val currentUser = firebaseAuth.currentUser
        return currentUser?.isEmailVerified?.right()
            ?: NullUserProblem("Null user problem occurred").left()
    }

    override fun getCurrentUser(): Either<Problem, FirebaseUser> {
        val currentUser = firebaseAuth.currentUser
        return currentUser?.right() ?: NullUserProblem("Null user problem occurred").left()
    }
}