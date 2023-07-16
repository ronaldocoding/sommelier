package br.com.sommelier.domain.mapper

import br.com.sommelier.data.model.UserData
import br.com.sommelier.domain.model.UserDomain

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