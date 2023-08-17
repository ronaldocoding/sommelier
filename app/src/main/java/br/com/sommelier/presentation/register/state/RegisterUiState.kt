package br.com.sommelier.presentation.register.state

import br.com.sommelier.presentation.register.model.RegisterUiModel

sealed class RegisterUiState(open val uiModel: RegisterUiModel) {
    object Initial : RegisterUiState(uiModel = RegisterUiModel())
    data class Resume(override val uiModel: RegisterUiModel) : RegisterUiState(uiModel = uiModel)
    data class Loading(override val uiModel: RegisterUiModel) : RegisterUiState(uiModel = uiModel)
    data class Error(override val uiModel: RegisterUiModel) : RegisterUiState(uiModel = uiModel)
}
