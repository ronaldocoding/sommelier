package br.com.sommelier.di

import br.com.sommelier.data.repository.UserRepositoryImpl
import br.com.sommelier.domain.repository.UserRepository
import br.com.sommelier.util.FirestoreCollections.USERS
import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import org.koin.dsl.module

object SommelierModule {

    private val dataModule = module {
        single { provideFirestore() }
        single { provideUsersCollection(get()) }
        factory<UserRepository> { UserRepositoryImpl(get()) }
    }

    private fun provideUsersCollection(firestore: FirebaseFirestore): CollectionReference {
        return firestore.collection(USERS)
    }

    private fun provideFirestore(): FirebaseFirestore {
        return Firebase.firestore
    }

    fun getModules() = listOf(dataModule)
}