package br.com.sommelier.data

import android.text.TextUtils
import br.com.sommelier.data.repository.AuthRepositoryImpl
import br.com.sommelier.base.result.AlreadySignedOutUserProblem
import br.com.sommelier.base.result.DeleteUserProblem
import br.com.sommelier.base.result.NullResultProblem
import br.com.sommelier.base.result.NullUserProblem
import br.com.sommelier.base.result.ReauthenticateUserProblem
import br.com.sommelier.base.result.RegisterUserProblem
import br.com.sommelier.base.result.SendEmailVerificationProblem
import br.com.sommelier.base.result.SendPasswordResetEmailProblem
import br.com.sommelier.base.result.SignInUserProblem
import br.com.sommelier.base.result.UpdateUserEmailProblem
import br.com.sommelier.base.result.UpdateUserPasswordProblem
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.EmailAuthCredential
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import io.mockk.every
import io.mockk.mockk
import io.mockk.mockkStatic
import io.mockk.unmockkStatic
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.After
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test

@ExperimentalCoroutinesApi
class AuthRepositoryImplTest {

    private val firebaseAuth = mockk<FirebaseAuth>()
    private val dummyEmail = "email"
    private val dummyPassword = "password"
    private val dummyUid = "uid"

    private lateinit var authRepositoryImpl: AuthRepositoryImpl

    @Before
    fun setUp() {
        mockkStatic(TextUtils::class)
        authRepositoryImpl = AuthRepositoryImpl(firebaseAuth)
    }

    @Test
    fun `GIVEN a successful task WHEN call registerUser THEN must return Unit`() = runTest {
        val successfulTask = mockk<Task<AuthResult>>().apply {
            every { isSuccessful } returns true
            every { result } returns mockk()
            every { isComplete } returns true
            every { exception } returns null
            every { isCanceled } returns false
            every { result.user } returns mockk()
        }

        every {
            firebaseAuth.createUserWithEmailAndPassword(
                dummyEmail,
                dummyPassword
            )
        } returns successfulTask

        val result = authRepositoryImpl.registerUser(dummyEmail, dummyPassword)

        assertTrue(result.isRight {
            it == Unit
        })
    }

    @Test
    fun `GIVEN a successful but the user is null task WHEN call registerUser THEN must the expected Problem`() =
        runTest {
            val errorMessage = "Null user problem occurred"

            val successfulTask = mockk<Task<AuthResult>>().apply {
                every { isSuccessful } returns true
                every { result } returns mockk()
                every { isComplete } returns true
                every { exception } returns null
                every { isCanceled } returns false
                every { result.user } returns null
            }

            every {
                firebaseAuth.createUserWithEmailAndPassword(
                    dummyEmail,
                    dummyPassword
                )
            } returns successfulTask

            val result = authRepositoryImpl.registerUser(dummyEmail, dummyPassword)

            assertTrue(result.isLeft {
                it is NullUserProblem && it.message == errorMessage
            })
        }

    @Test
    fun `GIVEN an unsuccessful task WHEN call registerUser THEN must return the expected Problem`() =
        runTest {
            val errorMessage = "Error registering user"
            val unsuccessfulTask = mockk<Task<AuthResult>>().apply {
                every { isSuccessful } returns false
                every { isComplete } returns true
                every { exception?.message } returns errorMessage
                every { isCanceled } returns false
            }

            every {
                firebaseAuth.createUserWithEmailAndPassword(
                    dummyEmail,
                    dummyPassword
                )
            } returns unsuccessfulTask

            val result = authRepositoryImpl.registerUser(dummyEmail, dummyPassword)

            assertTrue(result.isLeft {
                it is RegisterUserProblem && it.message == errorMessage
            })
        }

    @Test
    fun `GIVEN an unsuccessful task with a null exception message WHEN call registerUser THEN must return the expected Problem`() =
        runTest {
            val errorMessage = "An unknown problem occurred"

            val unsuccessfulTask = mockk<Task<AuthResult>>().apply {
                every { isSuccessful } returns false
                every { isComplete } returns true
                every { exception } returns null
                every { isCanceled } returns false
                every { result.user } returns mockk()
            }

            every {
                firebaseAuth.createUserWithEmailAndPassword(
                    dummyEmail,
                    dummyPassword
                )
            } returns unsuccessfulTask

            val result = authRepositoryImpl.registerUser(dummyEmail, dummyPassword)

            assertTrue(result.isLeft {
                it is RegisterUserProblem && it.message == errorMessage
            })
        }

    @Test
    fun `GIVEN a successful task WHEN call deleteUser THEN must return Unit`() = runTest {
        val successfulTask = mockk<Task<Void>>().apply {
            every { isSuccessful } returns true
            every { result } returns mockk()
            every { isComplete } returns true
            every { exception } returns null
            every { isCanceled } returns false
        }

        every {
            firebaseAuth.currentUser?.delete()
        } returns successfulTask

        val result = authRepositoryImpl.deleteUser()

        assertTrue(result.isRight {
            it == Unit
        })
    }

    @Test
    fun `GIVEN an unsuccessful task WHEN call deleteUser THEN must return the expected Problem`() =
        runTest {
            val errorMessage = "Error deleting user"
            val unsuccessfulTask = mockk<Task<Void>>().apply {
                every { isSuccessful } returns false
                every { isComplete } returns true
                every { exception?.message } returns errorMessage
                every { isCanceled } returns false
            }

            every {
                firebaseAuth.currentUser?.delete()
            } returns unsuccessfulTask

            val result = authRepositoryImpl.deleteUser()

            assertTrue(result.isLeft {
                it is DeleteUserProblem && it.message == errorMessage
            })
        }

    @Test
    fun `GIVEN an unsuccessful task with a null exception message WHEN call deleteUser THEN must return the expected Problem`() =
        runTest {
            val errorMessage = "An unknown problem occurred"

            val unsuccessfulTask = mockk<Task<Void>>().apply {
                every { isSuccessful } returns false
                every { isComplete } returns true
                every { exception } returns null
                every { isCanceled } returns false
                every { result } returns mockk()
            }

            every {
                firebaseAuth.currentUser?.delete()
            } returns unsuccessfulTask

            val result = authRepositoryImpl.deleteUser()

            assertTrue(result.isLeft {
                it is DeleteUserProblem && it.message == errorMessage
            })
        }

    @Test
    fun `GIVEN a null task WHEN call deleteUser THEN must return the expected Problem`() =
        runTest {
            val errorMessage = "Null result problem occurred"

            val nullTask = null

            every {
                firebaseAuth.currentUser?.delete()
            } returns nullTask

            val result = authRepositoryImpl.deleteUser()

            assertTrue(result.isLeft {
                it is NullResultProblem && it.message == errorMessage
            })
        }

    @Test
    fun `GIVEN a successful task WHEN call signInUser THEN must return Unit`() = runTest {
        val successfulTask = mockk<Task<AuthResult>>().apply {
            every { isSuccessful } returns true
            every { isComplete } returns true
            every { exception } returns null
            every { isCanceled } returns false
            every { result.user } returns mockk()
        }

        every {
            firebaseAuth.signInWithEmailAndPassword(
                dummyEmail,
                dummyPassword
            )
        } returns successfulTask

        val result = authRepositoryImpl.signInUser(dummyEmail, dummyPassword)

        assertTrue(result.isRight {
            it == Unit
        })
    }

    @Test
    fun `GIVEN a result with a null user WHEN call signInUser THEN must return the expected Problem`() =
        runTest {
            val errorMessage = "Null user problem occurred"

            val successfulTask = mockk<Task<AuthResult>>().apply {
                every { isComplete } returns true
                every { exception } returns null
                every { isCanceled } returns false
                every { result.user } returns null
            }

            every {
                firebaseAuth.signInWithEmailAndPassword(
                    dummyEmail,
                    dummyPassword
                )
            } returns successfulTask

            val result = authRepositoryImpl.signInUser(dummyEmail, dummyPassword)

            assertTrue(result.isLeft {
                it is NullUserProblem && it.message == errorMessage
            })
        }

    @Test
    fun `GIVEN an unsuccessful task WHEN call signInUser THEN must return the expected Problem`() =
        runTest {
            val errorMessage = "Error signing in user"
            val unsuccessfulTask = mockk<Task<AuthResult>>().apply {
                every { isSuccessful } returns false
                every { isComplete } returns true
                every { exception?.message } returns errorMessage
                every { isCanceled } returns false
            }

            every {
                firebaseAuth.signInWithEmailAndPassword(
                    dummyEmail,
                    dummyPassword
                )
            } returns unsuccessfulTask

            val result = authRepositoryImpl.signInUser(dummyEmail, dummyPassword)

            assertTrue(result.isLeft {
                it is SignInUserProblem && it.message == errorMessage
            })
        }

    @Test
    fun `GIVEN an unsuccessful task with a null exception message WHEN call signInUser THEN must return the expected Problem`() =
        runTest {
            val errorMessage = "An unknown problem occurred"

            val unsuccessfulTask = mockk<Task<AuthResult>>().apply {
                every { isSuccessful } returns false
                every { isComplete } returns true
                every { exception } returns null
                every { isCanceled } returns false
                every { result.user } returns mockk()
            }

            every {
                firebaseAuth.signInWithEmailAndPassword(
                    dummyEmail,
                    dummyPassword
                )
            } returns unsuccessfulTask

            val result = authRepositoryImpl.signInUser(dummyEmail, dummyPassword)

            assertTrue(result.isLeft {
                it is SignInUserProblem && it.message == errorMessage
            })
        }

    @Test
    fun `GIVEN a successful task WHEN call updateUserEmail THEN must return Unit`() = runTest {
        val successfulTask = mockk<Task<Void>>().apply {
            every { isSuccessful } returns true
            every { isComplete } returns true
            every { exception } returns null
            every { isCanceled } returns false
            every { result } returns mockk()
        }

        every {
            firebaseAuth.currentUser?.updateEmail(dummyEmail)
        } returns successfulTask

        val result = authRepositoryImpl.updateUserEmail(dummyEmail)

        assertTrue(result.isRight {
            it == Unit
        })
    }

    @Test
    fun `GIVEN a null user WHEN call updateUserEmail THEN must return the expected Problem`() =
        runTest {
            val errorMessage = "Null user problem occurred"

            every { firebaseAuth.currentUser } returns null

            val result = authRepositoryImpl.updateUserEmail(dummyEmail)

            assertTrue(result.isLeft {
                it is NullUserProblem && it.message == errorMessage
            })
        }

    @Test
    fun `GIVEN an unsuccessful task WHEN call updateUserEmail THEN must return the expected Problem`() =
        runTest {
            val errorMessage = "Error updating user email"
            val unsuccessfulTask = mockk<Task<Void>>().apply {
                every { isSuccessful } returns false
                every { isComplete } returns true
                every { exception?.message } returns errorMessage
                every { isCanceled } returns false
            }

            every {
                firebaseAuth.currentUser?.updateEmail(dummyEmail)
            } returns unsuccessfulTask

            val result = authRepositoryImpl.updateUserEmail(dummyEmail)

            assertTrue(result.isLeft {
                it is UpdateUserEmailProblem && it.message == errorMessage
            })
        }

    @Test
    fun `GIVEN an unsuccessful task with a null exception message WHEN call updateUserEmail THEN must return the expected Problem`() =
        runTest {
            val errorMessage = "An unknown problem occurred"

            val unsuccessfulTask = mockk<Task<Void>>().apply {
                every { isSuccessful } returns false
                every { isComplete } returns true
                every { exception } returns null
                every { isCanceled } returns false
                every { result } returns mockk()
            }

            every {
                firebaseAuth.currentUser?.updateEmail(dummyEmail)
            } returns unsuccessfulTask

            val result = authRepositoryImpl.updateUserEmail(dummyEmail)

            assertTrue(result.isLeft {
                it is UpdateUserEmailProblem && it.message == errorMessage
            })
        }

    @Test
    fun `GIVEN a successful task WHEN call updateUserPassword THEN must return Unit`() = runTest {
        val successfulTask = mockk<Task<Void>>().apply {
            every { isSuccessful } returns true
            every { isComplete } returns true
            every { exception } returns null
            every { isCanceled } returns false
            every { result } returns mockk()
        }

        every {
            firebaseAuth.currentUser?.updatePassword(dummyPassword)
        } returns successfulTask

        val result = authRepositoryImpl.updateUserPassword(dummyPassword)

        assertTrue(result.isRight {
            it == Unit
        })
    }

    @Test
    fun `GIVEN a null user WHEN call updateUserPassword THEN must return the expected Problem`() =
        runTest {
            val errorMessage = "Null user problem occurred"

            every { firebaseAuth.currentUser } returns null

            val result = authRepositoryImpl.updateUserPassword(dummyPassword)

            assertTrue(result.isLeft {
                it is NullUserProblem && it.message == errorMessage
            })
        }

    @Test
    fun `GIVEN an unsuccessful task WHEN call updateUserPassword THEN must return the expected Problem`() =
        runTest {
            val errorMessage = "Error updating user email"
            val unsuccessfulTask = mockk<Task<Void>>().apply {
                every { isSuccessful } returns false
                every { isComplete } returns true
                every { isCanceled } returns false
                every { exception?.message } returns errorMessage
                every { result } returns mockk()
            }

            every {
                firebaseAuth.currentUser?.updatePassword(dummyPassword)
            } returns unsuccessfulTask

            val result = authRepositoryImpl.updateUserPassword(dummyPassword)

            assertTrue(result.isLeft {
                it is UpdateUserPasswordProblem && it.message == errorMessage
            })
        }

    @Test
    fun `GIVEN an unsuccessful task with a null exception message WHEN call updateUserPassword THEN must return the expected Problem`() =
        runTest {
            val errorMessage = "An unknown problem occurred"

            val unsuccessfulTask = mockk<Task<Void>>().apply {
                every { isSuccessful } returns false
                every { isComplete } returns true
                every { isCanceled } returns false
                every { exception?.message } returns null
                every { result } returns mockk()
            }

            every {
                firebaseAuth.currentUser?.updatePassword(dummyPassword)
            } returns unsuccessfulTask

            val result = authRepositoryImpl.updateUserPassword(dummyPassword)

            assertTrue(result.isLeft {
                it is UpdateUserPasswordProblem && it.message == errorMessage
            })
        }

    @Test
    fun `GIVEN a successful task WHEN call reauthenticateUser THEN must return Unit`() = runTest {
        val successfulTask = mockk<Task<Void>>().apply {
            every { isSuccessful } returns true
            every { isComplete } returns true
            every { exception } returns null
            every { isCanceled } returns false
            every { result } returns mockk()
        }

        every {
            firebaseAuth.currentUser?.reauthenticate(any<EmailAuthCredential>())
        } returns successfulTask

        every { TextUtils.isEmpty(any()) } returns false

        val result = authRepositoryImpl.reauthenticateUser(dummyEmail, dummyPassword)

        assertTrue(result.isRight {
            it == Unit
        })
    }

    @Test
    fun `GIVEN a null user WHEN call reauthenticateUser THEN must return the expected Problem`() =
        runTest {
            val errorMessage = "Null user problem occurred"

            every { firebaseAuth.currentUser } returns null

            val result = authRepositoryImpl.reauthenticateUser(dummyEmail, dummyPassword)

            assertTrue(result.isLeft {
                it is NullUserProblem && it.message == errorMessage
            })
        }

    @Test
    fun `GIVEN an unsuccessful task WHEN call reauthenticateUser THEN must return the expected Problem`() =
        runTest {
            val errorMessage = "Error reauthenticating user"
            val unsuccessfulTask = mockk<Task<Void>>().apply {
                every { isSuccessful } returns false
                every { isComplete } returns true
                every { isCanceled } returns false
                every { exception?.message } returns errorMessage
                every { result } returns mockk()
            }

            every {
                firebaseAuth.currentUser?.reauthenticate(any<EmailAuthCredential>())
            } returns unsuccessfulTask

            every { TextUtils.isEmpty(any()) } returns false

            val result = authRepositoryImpl.reauthenticateUser(dummyEmail, dummyPassword)

            assertTrue(result.isLeft {
                it is ReauthenticateUserProblem && it.message == errorMessage
            })
        }

    @Test
    fun `GIVEN an unsuccessful task with a null exception message WHEN call reauthenticateUser THEN must return the expected Problem`() =
        runTest {
            val errorMessage = "An unknown problem occurred"

            val unsuccessfulTask = mockk<Task<Void>>().apply {
                every { isSuccessful } returns false
                every { isComplete } returns true
                every { isCanceled } returns false
                every { exception?.message } returns null
                every { result } returns mockk()
            }

            every {
                firebaseAuth.currentUser?.reauthenticate(any<EmailAuthCredential>())
            } returns unsuccessfulTask

            every { TextUtils.isEmpty(any()) } returns false

            val result = authRepositoryImpl.reauthenticateUser(dummyEmail, dummyPassword)

            assertTrue(result.isLeft {
                it is ReauthenticateUserProblem && it.message == errorMessage
            })
        }

    @Test
    fun `GIVEN a successful task WHEN call sendEmailVerification THEN must return Unit`() =
        runTest {
            val successfulTask = mockk<Task<Void>>().apply {
                every { isSuccessful } returns true
                every { isComplete } returns true
                every { exception } returns null
                every { isCanceled } returns false
                every { result } returns mockk()
            }

            every {
                firebaseAuth.currentUser?.sendEmailVerification()
            } returns successfulTask

            val result = authRepositoryImpl.sendEmailVerification()

            assertTrue(result.isRight {
                it == Unit
            })
        }

    @Test
    fun `GIVEN an unsuccessful task WHEN call sendEmailVerification THEN must return the expected Problem`() =
        runTest {
            val errorMessage = "Error sending email verification"
            val unsuccessfulTask = mockk<Task<Void>>().apply {
                every { isSuccessful } returns false
                every { isComplete } returns true
                every { isCanceled } returns false
                every { exception?.message } returns errorMessage
                every { result } returns mockk()
            }

            every {
                firebaseAuth.currentUser?.sendEmailVerification()
            } returns unsuccessfulTask

            val result = authRepositoryImpl.sendEmailVerification()

            assertTrue(result.isLeft {
                it is SendEmailVerificationProblem && it.message == errorMessage
            })
        }

    @Test
    fun `GIVEN an unsuccessful task with a null exception WHEN call sendEmailVerification THEN must return the expected Problem`() =
        runTest {
            val errorMessage = "An unknown problem occurred"

            val unsuccessfulTask = mockk<Task<Void>>().apply {
                every { isSuccessful } returns false
                every { isComplete } returns true
                every { isCanceled } returns false
                every { exception?.message } returns null
                every { result } returns mockk()
            }

            every { firebaseAuth.currentUser?.sendEmailVerification() } returns unsuccessfulTask

            val result = authRepositoryImpl.sendEmailVerification()

            assertTrue(result.isLeft {
                it is SendEmailVerificationProblem && it.message == errorMessage
            })
        }

    @Test
    fun `GIVEN a null user WHEN call sendEmailVerification THEN must return the expected Problem`() =
        runTest {
            val errorMessage = "Null user problem occurred"

            every { firebaseAuth.currentUser } returns null

            val result = authRepositoryImpl.sendEmailVerification()

            assertTrue(result.isLeft {
                it is NullUserProblem && it.message == errorMessage
            })
        }

    @Test
    fun `GIVEN a successful task WHEN call sendPasswordResetEmail THEN must return Unit`() =
        runTest {
            val successfulTask = mockk<Task<Void>>().apply {
                every { isSuccessful } returns true
                every { isComplete } returns true
                every { exception } returns null
                every { isCanceled } returns false
                every { result } returns mockk()
            }

            every {
                firebaseAuth.sendPasswordResetEmail(dummyEmail)
            } returns successfulTask

            val result = authRepositoryImpl.sendPasswordResetEmail(dummyEmail)

            assertTrue(result.isRight {
                it == Unit
            })
        }

    @Test
    fun `GIVEN an unsuccessful task WHEN call sendPasswordResetEmail THEN must return the expected Problem`() =
        runTest {
            val errorMessage = "Error sending password reset email"
            val unsuccessfulTask = mockk<Task<Void>>().apply {
                every { isSuccessful } returns false
                every { isComplete } returns true
                every { isCanceled } returns false
                every { exception?.message } returns errorMessage
                every { result } returns mockk()
            }

            every {
                firebaseAuth.sendPasswordResetEmail(dummyEmail)
            } returns unsuccessfulTask

            val result = authRepositoryImpl.sendPasswordResetEmail(dummyEmail)

            assertTrue(result.isLeft {
                it is SendPasswordResetEmailProblem && it.message == errorMessage
            })
        }

    @Test
    fun `GIVEN an unsuccessful task with a null exception message WHEN call sendPasswordResetEmail THEN must return the expected Problem`() =
        runTest {
            val errorMessage = "An unknown problem occurred"

            val unsuccessfulTask = mockk<Task<Void>>().apply {
                every { isSuccessful } returns false
                every { isComplete } returns true
                every { isCanceled } returns false
                every { exception?.message } returns null
                every { result } returns mockk()
            }

            every {
                firebaseAuth.sendPasswordResetEmail(dummyEmail)
            } returns unsuccessfulTask

            val result = authRepositoryImpl.sendPasswordResetEmail(dummyEmail)

            assertTrue(result.isLeft {
                it is SendPasswordResetEmailProblem && it.message == errorMessage
            })
        }

    @Test
    fun `GIVEN a non-null user WHEN call signOutUser THEN must return Unit`() {
        every { firebaseAuth.currentUser } returns mockk()
        every { firebaseAuth.signOut() } returns mockk()

        val result = authRepositoryImpl.signOutUser()

        assertTrue(result.isRight {
            it == Unit
        })
    }

    @Test
    fun `GIVEN a null user WHEN call signOutUser THEN must return the expected Problem`() {
        val errorMessage = "Already signed out user problem occurred"

        every { firebaseAuth.currentUser } returns null

        val result = authRepositoryImpl.signOutUser()

        assertTrue(result.isLeft {
            it is AlreadySignedOutUserProblem && it.message == errorMessage
        })
    }

    @Test
    fun `GIVEN a non-null user WHEN call isUserSignedIn THEN must return true`() {
        every { firebaseAuth.currentUser } returns mockk()

        val result = authRepositoryImpl.isUserSignedIn()

        assertTrue(result)
    }

    @Test
    fun `GIVEN a null user WHEN call isUserSignedIn THEN must return false`() {
        every { firebaseAuth.currentUser } returns null

        val result = authRepositoryImpl.isUserSignedIn()

        assertFalse(result)
    }

    @Test
    fun `GIVEN a non-null user with a verified email WHEN call isUserEmailVerified THEN must return true`() {
        every { firebaseAuth.currentUser } returns mockk {
            every { isEmailVerified } returns true
        }

        val result = authRepositoryImpl.isUserEmailVerified()

        assertTrue(result.isRight { isUserEmailVerified ->
            isUserEmailVerified
        })
    }

    @Test
    fun `GIVEN a non-null user with an unverified email WHEN call isUserEmailVerified THEN must return false`() {
        every { firebaseAuth.currentUser } returns mockk {
            every { isEmailVerified } returns false
        }

        val result = authRepositoryImpl.isUserEmailVerified()

        assertFalse(result.isRight { isUserEmailVerified ->
            isUserEmailVerified
        })
    }

    @Test
    fun `GIVEN a null user WHEN call isUserEmailVerified THEN must return the expected Problem`() {
        val errorMessage = "Null user problem occurred"

        every { firebaseAuth.currentUser } returns null

        val result = authRepositoryImpl.isUserEmailVerified()

        assertTrue(result.isLeft {
            it is NullUserProblem && it.message == errorMessage
        })
    }

    @Test
    fun `GIVEN a non-null user WHEN call getCurrentUser THEN must return Unit and the expected user`() {
        val expectedUser = mockk<FirebaseUser>().apply {
            every { uid } returns dummyUid
            every { email } returns dummyEmail
        }

        every { firebaseAuth.currentUser } returns expectedUser

        val result = authRepositoryImpl.getCurrentUser()

        assertTrue(result.isRight { actualUser ->
            actualUser.uid == expectedUser.uid && actualUser.email == expectedUser.email
        })
    }

    @Test
    fun `GIVEN a null user WHEN call getCurrentUser THEN must return the expected Problem`() {
        val errorMessage = "Null user problem occurred"

        every { firebaseAuth.currentUser } returns null

        val result = authRepositoryImpl.getCurrentUser()

        assertTrue(result.isLeft {
            it is NullUserProblem && it.message == errorMessage
        })
    }

    @After
    fun tearDown() {
        unmockkStatic(TextUtils::class)
    }
}