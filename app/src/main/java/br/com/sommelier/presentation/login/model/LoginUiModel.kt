package br.com.sommelier.presentation.login.model

import androidx.compose.material3.SnackbarHostState
import br.com.sommelier.ui.component.SommelierSnackbarType
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

data class SnackBarUiState(
    val text: String = emptyString(),
    val hostState: SnackbarHostState = SnackbarHostState(),
    val type: SommelierSnackbarType = SommelierSnackbarType.Error
)

data class LoginUiModel(
    val emailUiState: EmailUiState = EmailUiState(),
    val passwordUiState: PasswordUiState = PasswordUiState(),
    val snackBarUiState: SnackBarUiState = SnackBarUiState()
)
