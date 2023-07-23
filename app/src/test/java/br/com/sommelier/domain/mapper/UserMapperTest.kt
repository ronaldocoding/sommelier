package br.com.sommelier.domain.mapper

import br.com.sommelier.data.model.UserData
import br.com.sommelier.domain.mapper.toData
import br.com.sommelier.domain.mapper.toDomain
import br.com.sommelier.domain.model.UserDomain
import br.com.sommelier.domain.model.UserFirebase
import com.google.firebase.auth.FirebaseUser
import io.mockk.every
import io.mockk.mockk
import org.junit.Assert.assertEquals
import org.junit.Test

class UserMapperTest {

    @Test
    fun `GIVEN an userData WHEN toDomain is called THEN must return the expected userDomain`() {
        val userData = UserData(email = "email", name = "name", uid = "uid")

        val expectedUserDomain = UserDomain(email = "email", name = "name", uid = "uid")
        val actualUserDomain = userData.toDomain()

        assertEquals(expectedUserDomain.email, actualUserDomain.email)
        assertEquals(expectedUserDomain.name, actualUserDomain.name)
        assertEquals(expectedUserDomain.uid, actualUserDomain.uid)
    }

    @Test
    fun `GIVEN an userDomain WHEN toData is called THEN must return the expected userData`() {
        val userData = UserDomain(email = "email", name = "name", uid = "uid")

        val expectedUserData = UserData(email = "email", name = "name", uid = "uid")
        val actualUserData = userData.toData()

        assertEquals(expectedUserData.email, actualUserData.email)
        assertEquals(expectedUserData.name, actualUserData.name)
        assertEquals(expectedUserData.uid, actualUserData.uid)
    }

    @Test
    fun `GIVEN an FirebaseUser WHEN toDomain is called THEN must return the expected UserFirebase`() {
        val firebaseUser = mockk<FirebaseUser>().apply {
            every { email } returns "email"
            every { uid } returns "uid"
        }

        val expectedUserFirebase = UserFirebase(email = "email", uid = "uid")
        val actualUserFirebase = firebaseUser.toDomain()

        assertEquals(expectedUserFirebase.email, actualUserFirebase.email)
        assertEquals(expectedUserFirebase.uid, actualUserFirebase.uid)
    }
}