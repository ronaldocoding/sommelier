package br.com.sommelier.presentation.main.state

import br.com.sommelier.presentation.main.model.MainUiModel

sealed class MainUiState(open val uiModel: MainUiModel) {
    object Loading : MainUiState(MainUiModel())
    data class Resume(override val uiModel: MainUiModel) : MainUiState(uiModel)
}
