package br.com.sommelier.di

import br.com.sommelier.util.FirestoreCollections
import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import org.koin.dsl.module

object SommelierModule {

    private val dataModule = module {
        factory { provideFirestore() }
        factory { provideUsersCollection(get()) }
    }

    private fun provideUsersCollection(firestore: FirebaseFirestore): CollectionReference {
        return firestore.collection(FirestoreCollections.USERS)
    }

    private fun provideFirestore(): FirebaseFirestore {
        return Firebase.firestore
    }

    fun getModules() = listOf(dataModule)
}