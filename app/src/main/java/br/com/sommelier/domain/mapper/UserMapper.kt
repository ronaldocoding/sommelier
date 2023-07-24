package br.com.sommelier.domain.mapper

import br.com.sommelier.data.model.UserData
import br.com.sommelier.domain.model.UserDomain
import br.com.sommelier.domain.model.UserFirebase
import br.com.sommelier.util.emptyString
import com.google.firebase.auth.FirebaseUser

fun UserDomain.toData() = UserData(
    email = email,
    name = name,
    uid = uid
)

fun UserData.toDomain() = UserDomain(
    email = email,
    name = name,
    uid = uid
)

fun FirebaseUser.toDomain() = UserFirebase(
    email = email ?: emptyString(),
    uid = uid
)
