package br.com.sommelier.presentation.editaccount.state

import br.com.sommelier.presentation.editaccount.model.EditAccountUiModel

sealed class EditAccountUiState(open val uiModel: EditAccountUiModel) {
    object Initial : EditAccountUiState(EditAccountUiModel())
    data class Resume(override val uiModel: EditAccountUiModel) : EditAccountUiState(uiModel)
    data class Loading(override val uiModel: EditAccountUiModel) : EditAccountUiState(uiModel)
    data class Error(override val uiModel: EditAccountUiModel) : EditAccountUiState(uiModel)
}
