package br.com.sommelier.presentation.login.model

import br.com.sommelier.util.emptyString

data class EmailUiState(
    val text: String = emptyString(),
    val errorSupportingMessage: String = emptyString(),
    val isError: Boolean = false
)

data class PasswordUiState(
    val text: String = emptyString(),
    val errorSupportingMessage: String = emptyString(),
    val isError: Boolean = false
)

data class LoginUiModel(
    val emailUiState: EmailUiState = EmailUiState(),
    val passwordUiState: PasswordUiState = PasswordUiState()
)
