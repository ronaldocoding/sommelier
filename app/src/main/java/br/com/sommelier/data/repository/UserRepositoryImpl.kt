package br.com.sommelier.data.repository

import arrow.core.Either
import arrow.core.left
import arrow.core.right
import br.com.sommelier.data.model.UserData
import br.com.sommelier.domain.mapper.toData
import br.com.sommelier.domain.mapper.toDomain
import br.com.sommelier.domain.model.UserDomain
import br.com.sommelier.domain.repository.UserRepository
import br.com.sommelier.util.AddDocumentProblem
import br.com.sommelier.util.DeleteDocumentProblem
import br.com.sommelier.util.FirestoreCollections.UID_FIELD
import br.com.sommelier.util.GetDocumentProblem
import br.com.sommelier.util.NotFoundDocumentProblem
import br.com.sommelier.util.Problem
import br.com.sommelier.util.UpdateDocumentProblem
import com.google.firebase.firestore.CollectionReference
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class UserRepositoryImpl(private val usersCollection: CollectionReference) : UserRepository {

    override suspend fun saveUser(user: UserDomain): Either<Problem, Unit> {
        return withContext(Dispatchers.IO) {
            val userData = user.toData()
            val task = usersCollection.add(userData)
            if (task.isSuccessful) {
                Unit.right()
            } else {
                AddDocumentProblem(
                    task.exception?.message ?: "An unknown problem occurred"
                ).left()
            }
        }
    }

    override suspend fun getUser(uid: String): Either<Problem, UserDomain> {
        return withContext(Dispatchers.IO) {
            val task = usersCollection.whereEqualTo(UID_FIELD, uid).get()
            if (task.isSuccessful) {
                val userData = task.result.documents.firstOrNull()?.toObject(UserData::class.java)
                userData?.toDomain()?.right() ?: NotFoundDocumentProblem("User not found").left()
            } else {
                GetDocumentProblem(
                    task.exception?.message ?: "An unknown problem occurred"
                ).left()
            }
        }
    }

    override suspend fun updateUser(user: UserDomain): Either<Problem, Unit> {
        return withContext(Dispatchers.IO) {
            val userData = user.toData()
            val task = usersCollection.whereEqualTo(UID_FIELD, userData.uid).get()
            if (task.isSuccessful) {
                val document = task.result.documents.firstOrNull()
                if (document != null) {
                    val updateTask = document.reference.set(userData)
                    if (updateTask.isSuccessful) {
                        Unit.right()
                    } else {
                        UpdateDocumentProblem(
                            updateTask.exception?.message ?: "An unknown problem occurred"
                        ).left()
                    }
                } else {
                    NotFoundDocumentProblem("User not found").left()
                }
            } else {
                GetDocumentProblem(
                    task.exception?.message ?: "An unknown problem occurred"
                ).left()
            }
        }
    }

    override suspend fun deleteUser(uid: String): Either<Problem, Unit> {
        return withContext(Dispatchers.IO) {
            val task = usersCollection.whereEqualTo(UID_FIELD, uid).get()
            if (task.isSuccessful) {
                val document = task.result.documents.firstOrNull()
                if (document != null) {
                    val updateTask = document.reference.delete()
                    if (updateTask.isSuccessful) {
                        Unit.right()
                    } else {
                        DeleteDocumentProblem(
                            updateTask.exception?.message ?: "An unknown problem occurred"
                        ).left()
                    }
                } else {
                    NotFoundDocumentProblem("User not found").left()
                }
            } else {
                GetDocumentProblem(
                    task.exception?.message ?: "An unknown problem occurred"
                ).left()
            }
        }
    }
}