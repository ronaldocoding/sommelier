package br.com.sommelier.di

import br.com.sommelier.data.repository.UserRepositoryImpl
import br.com.sommelier.domain.repository.UserRepository
import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.FirebaseFirestore
import io.mockk.every
import io.mockk.mockk
import io.mockk.mockkStatic
import io.mockk.unmockkStatic
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.koin.core.KoinApplication
import org.koin.core.context.startKoin
import org.koin.core.context.stopKoin
import org.koin.test.KoinTest
import org.koin.test.check.checkModules
import org.koin.test.get

class SommelierModuleTest : KoinTest {

    private val firestoreMock = mockk<FirebaseFirestore>()
    private val collectionMock = mockk<CollectionReference>()
    private lateinit var koin: KoinApplication

    @Before
    fun setUp() {
        mockkStatic(FirebaseFirestore::class)
        every { FirebaseFirestore.getInstance() } returns firestoreMock
        every { firestoreMock.collection(any()) } returns collectionMock
        koin = startKoin {
            modules(SommelierModule.getModules())
        }
    }

    @Test
    fun `GIVEN a SommelierModule WHEN call getModules THEN all definitions must exist`() {
        koin.checkModules()
    }

    @Test
    fun `GIVEN a SommelierModule WHEN resolve UserRepository THEN it should return UserRepositoryImpl with the correct dependencies`() {
        koin.checkModules {
            val userRepository: UserRepository = get()
            assert(userRepository is UserRepositoryImpl)
        }
    }

    @After
    fun tearDown() {
        unmockkStatic(FirebaseFirestore::class)
        stopKoin()
    }
}