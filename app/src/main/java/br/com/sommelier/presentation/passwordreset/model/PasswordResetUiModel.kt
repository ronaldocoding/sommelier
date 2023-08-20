package br.com.sommelier.presentation.passwordreset.model

import br.com.sommelier.presentation.passwordreset.res.PasswordResetStringResource
import br.com.sommelier.util.emptyString

data class EmailUiState(
    val text: String = emptyString(),
    val errorSupportingMessage: PasswordResetStringResource = PasswordResetStringResource.Empty,
    val isError: Boolean = false
)

data class PasswordResetUiModel(
    val emailUiState: EmailUiState = EmailUiState(),
    val isBackButtonEnabled: Boolean = true,
    val isLoading: Boolean = false
)
