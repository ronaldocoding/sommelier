package br.com.sommelier.presentation.login.state

import br.com.sommelier.presentation.login.model.LoginUiModel

sealed class LoginUiState(open val uiModel: LoginUiModel) {
    object Initial : LoginUiState(uiModel = LoginUiModel())
    data class Resume(override val uiModel: LoginUiModel) : LoginUiState(uiModel = uiModel)
    data class Loading(override val uiModel: LoginUiModel) : LoginUiState(uiModel = uiModel)
    data class Error(override val uiModel: LoginUiModel) : LoginUiState(uiModel = uiModel)
}
