package br.com.sommelier.presentation.passwordreset.state

import br.com.sommelier.presentation.passwordreset.model.PasswordResetUiModel

sealed class PasswordResetUiState(open val uiModel: PasswordResetUiModel) {
    object Initial : PasswordResetUiState(PasswordResetUiModel())
    data class Resume(override val uiModel: PasswordResetUiModel) : PasswordResetUiState(uiModel)
    data class Success(override val uiModel: PasswordResetUiModel) : PasswordResetUiState(uiModel)
    data class Error(override val uiModel: PasswordResetUiModel) : PasswordResetUiState(uiModel)
    data class Loading(override val uiModel: PasswordResetUiModel) : PasswordResetUiState(uiModel)
}
