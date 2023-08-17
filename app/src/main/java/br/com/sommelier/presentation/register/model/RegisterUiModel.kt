package br.com.sommelier.presentation.register.model

import androidx.compose.material3.SnackbarHostState
import br.com.sommelier.presentation.register.res.RegisterStringResource
import br.com.sommelier.ui.component.SommelierSnackbarType
import br.com.sommelier.util.emptyString

data class NameUiState(
    val text: String = emptyString(),
    val errorSupportingMessage: RegisterStringResource = RegisterStringResource.Empty,
    val isError: Boolean = false
)

data class EmailUiState(
    val text: String = emptyString(),
    val errorSupportingMessage: RegisterStringResource = RegisterStringResource.Empty,
    val isError: Boolean = false
)

data class PasswordUiState(
    val text: String = emptyString(),
    val errorSupportingMessage: RegisterStringResource = RegisterStringResource.Empty,
    val isError: Boolean = false
)

data class PasswordConfirmationUiState(
    val text: String = emptyString(),
    val errorSupportingMessage: RegisterStringResource = RegisterStringResource.Empty,
    val isError: Boolean = false
)

data class SnackbarUiState(
    val hostState: SnackbarHostState = SnackbarHostState(),
    val type: SommelierSnackbarType = SommelierSnackbarType.Error
)

data class RegisterUiModel(
    val nameUiState: NameUiState = NameUiState(),
    val emailUiState: EmailUiState = EmailUiState(),
    val passwordUiState: PasswordUiState = PasswordUiState(),
    val passwordConfirmationUiState: PasswordConfirmationUiState = PasswordConfirmationUiState(),
    val snackbarUiState: SnackbarUiState = SnackbarUiState()
)
