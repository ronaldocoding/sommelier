package br.com.sommelier.di

import br.com.sommelier.data.repository.AuthRepositoryImpl
import br.com.sommelier.data.repository.UserRepositoryImpl
import br.com.sommelier.domain.repository.AuthRepository
import br.com.sommelier.domain.repository.UserRepository
import br.com.sommelier.domain.usecase.GetCurrentUserUseCase
import br.com.sommelier.domain.usecase.GetUserDocumentUseCase
import br.com.sommelier.domain.usecase.IsUserEmailVerifiedUseCase
import br.com.sommelier.domain.usecase.IsUserSignedInUseCase
import br.com.sommelier.domain.usecase.SendEmailVerificationUseCase
import br.com.sommelier.domain.usecase.SignInUserUseCase
import br.com.sommelier.domain.usecase.SignOutUserUseCase
import br.com.sommelier.util.FirestoreCollections.USERS
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import org.koin.dsl.module

object SommelierModule {

    private val dataModule = module {
        single { provideFirestore() }
        single { provideFirebaseAuth() }
        single { provideUsersCollection(firestore = get()) }
        factory<UserRepository> { UserRepositoryImpl(usersCollection = get()) }
        factory<AuthRepository> { AuthRepositoryImpl(firebaseAuth = get()) }
    }

    private val domainModule = module {
        factory { provideCoroutinesDispatcherIO() }
        factory { GetCurrentUserUseCase(authRepository = get(), coroutineDispatcher = get()) }
        factory { GetUserDocumentUseCase(userRepository = get(), coroutineDispatcher = get()) }
        factory { IsUserEmailVerifiedUseCase(authRepository = get(), coroutineDispatcher = get()) }
        factory { IsUserSignedInUseCase(authRepository = get(), coroutineDispatcher = get()) }
        factory {
            SendEmailVerificationUseCase(
                authRepository = get(),
                coroutineDispatcher = get()
            )
        }
        factory { SignInUserUseCase(authRepository = get(), coroutineDispatcher = get()) }
        factory { SignOutUserUseCase(authRepository = get(), coroutineDispatcher = get()) }
    }

    private fun provideFirebaseAuth(): FirebaseAuth {
        return Firebase.auth
    }

    private fun provideUsersCollection(firestore: FirebaseFirestore): CollectionReference {
        return firestore.collection(USERS)
    }

    private fun provideFirestore(): FirebaseFirestore {
        return Firebase.firestore
    }

    private fun provideCoroutinesDispatcherIO(): CoroutineDispatcher {
        return Dispatchers.IO
    }

    fun getModules() = listOf(dataModule, domainModule)
}