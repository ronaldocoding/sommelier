package br.com.sommelier.presentation.account.model

import br.com.sommelier.util.emptyString

data class AccountUiModel(
    val name: String = emptyString(),
    val email: String = emptyString(),
    val isLoading: Boolean = false,
)
