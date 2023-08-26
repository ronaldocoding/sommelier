package br.com.sommelier.data.model

import br.com.sommelier.util.emptyString

data class UserData(
    val email: String = emptyString(),
    val name: String = emptyString(),
    val uid: String = emptyString()
)
