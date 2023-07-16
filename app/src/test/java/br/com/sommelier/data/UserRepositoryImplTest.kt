package br.com.sommelier.data

import br.com.sommelier.data.model.UserData
import br.com.sommelier.data.repository.UserRepositoryImpl
import br.com.sommelier.domain.model.UserDomain
import br.com.sommelier.util.AddDocumentProblem
import br.com.sommelier.util.DeleteDocumentProblem
import br.com.sommelier.util.FirestoreCollections.UID_FIELD
import br.com.sommelier.util.GetDocumentProblem
import br.com.sommelier.util.NotFoundDocumentProblem
import br.com.sommelier.util.UpdateDocumentProblem
import com.google.android.gms.tasks.Task
import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.QuerySnapshot
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test

@ExperimentalCoroutinesApi
class UserRepositoryImplTest {

    private val usersCollectionMock = mockk<CollectionReference>()
    private val dummyUserDomain = UserDomain(email = "email", name = "name", uid = "uid")
    private val dummyUserData = UserData(email = "email", name = "name", uid = "uid")
    private val dummyUid = "uid"
    private lateinit var userRepository: UserRepositoryImpl

    @Before
    fun setUp() {
        userRepository = UserRepositoryImpl(usersCollectionMock)
    }

    @Test
    fun `GIVEN a successful task WHEN call saveUser THEN must return Unit`() = runTest {
        val unsuccessfulTask = mockk<Task<DocumentReference>>().apply {
            coEvery { isSuccessful } returns true
        }

        coEvery { usersCollectionMock.add(any()) } returns unsuccessfulTask

        val result = userRepository.saveUser(dummyUserDomain)

        assertTrue(result.isRight { problem ->
            problem == Unit
        })
    }

    @Test
    fun `GIVEN an unsuccessful task WHEN call saveUser THEN must return the expected Problem`() =
        runTest {
            val errorMessage = "Error writing document"
            val unsuccessfulTask = mockk<Task<DocumentReference>>().apply {
                coEvery { isSuccessful } returns false
                coEvery { exception?.message } returns errorMessage
            }

            coEvery { usersCollectionMock.add(any()) } returns unsuccessfulTask

            val result = userRepository.saveUser(dummyUserDomain)

            assertTrue(result.isLeft { problem ->
                problem is AddDocumentProblem && problem.message == errorMessage
            })
        }

    @Test
    fun `GIVEN an unsuccessful task with a null exception message task WHEN call saveUser THEN must return the expected Problem`() =
        runTest {
            val errorMessage = "An unknown problem occurred"
            val unsuccessfulTask = mockk<Task<DocumentReference>>().apply {
                coEvery { isSuccessful } returns false
                coEvery { exception?.message } returns null
            }

            coEvery { usersCollectionMock.add(any()) } returns unsuccessfulTask

            val result = userRepository.saveUser(dummyUserDomain)

            assertTrue(result.isLeft { problem ->
                problem is AddDocumentProblem && problem.message == errorMessage
            })
        }

    @Test
    fun `GIVEN a successful task WHEN call getUser THEN must return the expected userDomain`() =
        runTest {
            val successfulTask = mockk<Task<QuerySnapshot>>().apply {
                coEvery { isSuccessful } returns true
                coEvery { result.documents.isEmpty() } returns false
                coEvery {
                    result.documents.firstOrNull()?.toObject(UserData::class.java)
                } returns dummyUserData
            }

            coEvery {
                usersCollectionMock.whereEqualTo(UID_FIELD, dummyUid).get()
            } returns successfulTask

            val result = userRepository.getUser(dummyUid)

            assertTrue(result.isRight { userDomain ->
                userDomain == dummyUserDomain
            })
        }

    @Test
    fun `GIVEN a successful task but with a empty documents list WHEN call getUser THEN must return the expected Problem`() =
        runTest {
            val successfulTask = mockk<Task<QuerySnapshot>>().apply {
                coEvery { isSuccessful } returns true
                coEvery { result.documents.isEmpty() } returns true
            }

            coEvery {
                usersCollectionMock.whereEqualTo(UID_FIELD, dummyUid).get()
            } returns successfulTask

            val result = userRepository.getUser(dummyUid)

            assertTrue(result.isLeft { problem ->
                problem is NotFoundDocumentProblem && problem.message == "User not found"
            })
        }

    @Test
    fun `GIVEN an unsuccessful task WHEN call getUser THEN must return the expected Problem`() =
        runTest {
            val errorMessage = "Error getting document"
            val unsuccessfulTask = mockk<Task<QuerySnapshot>>().apply {
                coEvery { isSuccessful } returns false
                coEvery { exception?.message } returns errorMessage
            }

            coEvery {
                usersCollectionMock.whereEqualTo(UID_FIELD, dummyUid).get()
            } returns unsuccessfulTask

            val result = userRepository.getUser(dummyUid)

            assertTrue(result.isLeft { problem ->
                problem is GetDocumentProblem && problem.message == errorMessage
            })
        }

    @Test
    fun `GIVEN an unsuccessful task with a null exception message WHEN call getUser THEN must return the expected Problem`() =
        runTest {
            val errorMessage = "An unknown problem occurred"
            val unsuccessfulTask = mockk<Task<QuerySnapshot>>().apply {
                coEvery { isSuccessful } returns false
                coEvery { exception?.message } returns null
            }

            coEvery {
                usersCollectionMock.whereEqualTo(UID_FIELD, dummyUid).get()
            } returns unsuccessfulTask

            val result = userRepository.getUser(dummyUid)

            assertTrue(result.isLeft { problem ->
                problem is GetDocumentProblem && problem.message == errorMessage
            })
        }

    @Test
    fun `GIVEN an successful task and a successful updateTask WHEN call updateUser THEN must return Unit`() =
        runTest {
            val dummyDocumentSnapshot = mockk<DocumentSnapshot>().apply {
                coEvery { reference } returns mockk()
            }

            val successfulTask = mockk<Task<QuerySnapshot>>().apply {
                coEvery { isSuccessful } returns true
                coEvery { result.documents.isEmpty() } returns false
                coEvery {
                    result.documents.firstOrNull()
                } returns dummyDocumentSnapshot
            }

            val successfulUpdateTask = mockk<Task<Void>>().apply {
                coEvery { isSuccessful } returns true
            }

            coEvery {
                usersCollectionMock.whereEqualTo(UID_FIELD, dummyUserData.uid).get()
            } returns successfulTask

            coEvery {
                dummyDocumentSnapshot.reference.set(any())
            } returns successfulUpdateTask

            val result = userRepository.updateUser(dummyUserDomain)

            assertTrue(result.isRight { problem ->
                problem == Unit
            })
        }

    @Test
    fun `GIVEN an successful task but an unsuccessful updateTask WHEN call updateUser THEN must return the expected Problem`() =
        runTest {
            val dummyDocumentSnapshot = mockk<DocumentSnapshot>().apply {
                coEvery { reference } returns mockk()
            }

            val successfulTask = mockk<Task<QuerySnapshot>>().apply {
                coEvery { isSuccessful } returns true
                coEvery { result.documents.isEmpty() } returns false
                coEvery {
                    result.documents.firstOrNull()
                } returns dummyDocumentSnapshot
            }

            val errorMessage = "Error updating document"
            val unsuccessfulUpdateTask = mockk<Task<Void>>().apply {
                coEvery { isSuccessful } returns false
                coEvery { exception?.message } returns errorMessage
            }

            coEvery {
                usersCollectionMock.whereEqualTo(UID_FIELD, dummyUserData.uid).get()
            } returns successfulTask

            coEvery {
                dummyDocumentSnapshot.reference.set(any())
            } returns unsuccessfulUpdateTask

            val result = userRepository.updateUser(dummyUserDomain)

            assertTrue(result.isLeft { problem ->
                problem is UpdateDocumentProblem && problem.message == errorMessage
            })
        }

    @Test
    fun `GIVEN a successful task but with an empty documents list WHEN call updateUser THEN must return the expected Problem`() =
        runTest {
            val successfulTask = mockk<Task<QuerySnapshot>>().apply {
                coEvery { isSuccessful } returns true
                coEvery { result.documents.isEmpty() } returns true
            }

            coEvery {
                usersCollectionMock.whereEqualTo(UID_FIELD, dummyUserData.uid).get()
            } returns successfulTask

            val result = userRepository.updateUser(dummyUserDomain)

            assertTrue(result.isLeft { problem ->
                problem is NotFoundDocumentProblem && problem.message == "User not found"
            })
        }

    @Test
    fun `GIVEN an unsuccessful task WHEN call updateUser THEN must return the expected Problem`() =
        runTest {
            val errorMessage = "Error updating document"
            val unsuccessfulTask = mockk<Task<QuerySnapshot>>().apply {
                coEvery { isSuccessful } returns false
                coEvery { exception?.message } returns errorMessage
            }

            coEvery {
                usersCollectionMock.whereEqualTo(UID_FIELD, dummyUserData.uid).get()
            } returns unsuccessfulTask

            val result = userRepository.updateUser(dummyUserDomain)

            assertTrue(result.isLeft { problem ->
                problem is GetDocumentProblem && problem.message == errorMessage
            })
        }

    @Test
    fun `GIVEN an unsuccessful task with a null exception message WHEN call updateUser THEN must return the expected Problem`() =
        runTest {
            val errorMessage = "An unknown problem occurred"
            val unsuccessfulTask = mockk<Task<QuerySnapshot>>().apply {
                coEvery { isSuccessful } returns false
                coEvery { exception?.message } returns null
            }

            coEvery {
                usersCollectionMock.whereEqualTo(UID_FIELD, dummyUserData.uid).get()
            } returns unsuccessfulTask

            val result = userRepository.updateUser(dummyUserDomain)

            assertTrue(result.isLeft { problem ->
                problem is GetDocumentProblem && problem.message == errorMessage
            })
        }

    @Test
    fun `GIVEN an successful task and a successful deleteTask WHEN call deleteUser THEN must return Unit`() =
        runTest {
            val dummyDocumentSnapshot = mockk<DocumentSnapshot>().apply {
                coEvery { reference } returns mockk()
            }

            val successfulTask = mockk<Task<QuerySnapshot>>().apply {
                coEvery { isSuccessful } returns true
                coEvery { result.documents.isEmpty() } returns false
                coEvery {
                    result.documents.firstOrNull()
                } returns dummyDocumentSnapshot
            }

            val successfulDeleteTask = mockk<Task<Void>>().apply {
                coEvery { isSuccessful } returns true
            }

            coEvery {
                usersCollectionMock.whereEqualTo(UID_FIELD, dummyUid).get()
            } returns successfulTask

            coEvery {
                dummyDocumentSnapshot.reference.delete()
            } returns successfulDeleteTask

            val result = userRepository.deleteUser(dummyUid)

            assertTrue(result.isRight { problem ->
                problem == Unit
            })
        }

    @Test
    fun `GIVEN an successful task but an unsuccessful deleteTask WHEN call deleteUser THEN must return the expected Problem`() =
        runTest {
            val dummyDocumentSnapshot = mockk<DocumentSnapshot>().apply {
                coEvery { reference } returns mockk()
            }

            val successfulTask = mockk<Task<QuerySnapshot>>().apply {
                coEvery { isSuccessful } returns true
                coEvery { result.documents.isEmpty() } returns false
                coEvery {
                    result.documents.firstOrNull()
                } returns dummyDocumentSnapshot
            }

            val errorMessage = "Error deleting document"
            val unsuccessfulDeleteTask = mockk<Task<Void>>().apply {
                coEvery { isSuccessful } returns false
                coEvery { exception?.message } returns errorMessage
            }

            coEvery {
                usersCollectionMock.whereEqualTo(UID_FIELD, dummyUid).get()
            } returns successfulTask

            coEvery {
                dummyDocumentSnapshot.reference.delete()
            } returns unsuccessfulDeleteTask

            val result = userRepository.deleteUser(dummyUid)

            assertTrue(result.isLeft { problem ->
                problem is DeleteDocumentProblem && problem.message == errorMessage
            })
        }

    @Test
    fun `GIVEN a successful task but with an empty documents list WHEN call deleteUser THEN must return the expected Problem`() =
        runTest {
            val successfulTask = mockk<Task<QuerySnapshot>>().apply {
                coEvery { isSuccessful } returns true
                coEvery { result.documents.isEmpty() } returns true
            }

            coEvery {
                usersCollectionMock.whereEqualTo(UID_FIELD, dummyUid).get()
            } returns successfulTask

            val result = userRepository.deleteUser(dummyUid)

            assertTrue(result.isLeft { problem ->
                problem is NotFoundDocumentProblem && problem.message == "User not found"
            })
        }

    @Test
    fun `GIVEN an unsuccessful task WHEN call deleteUser THEN must return the expected Problem`() =
        runTest {
            val errorMessage = "Error updating document"
            val unsuccessfulTask = mockk<Task<QuerySnapshot>>().apply {
                coEvery { isSuccessful } returns false
                coEvery { exception?.message } returns errorMessage
            }

            coEvery {
                usersCollectionMock.whereEqualTo(UID_FIELD, dummyUid).get()
            } returns unsuccessfulTask

            val result = userRepository.deleteUser(dummyUid)

            assertTrue(result.isLeft { problem ->
                problem is GetDocumentProblem && problem.message == errorMessage
            })
        }

    @Test
    fun `GIVEN an unsuccessful task with a null exception message WHEN call deleteUser THEN must return the expected Problem`() =
        runTest {
            val errorMessage = "An unknown problem occurred"
            val unsuccessfulTask = mockk<Task<QuerySnapshot>>().apply {
                coEvery { isSuccessful } returns false
                coEvery { exception?.message } returns null
            }

            coEvery {
                usersCollectionMock.whereEqualTo(UID_FIELD, dummyUid).get()
            } returns unsuccessfulTask

            val result = userRepository.deleteUser(dummyUid)

            assertTrue(result.isLeft { problem ->
                problem is GetDocumentProblem && problem.message == errorMessage
            })
        }
}