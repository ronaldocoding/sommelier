package br.com.sommelier.presentation.home.state

import br.com.sommelier.presentation.home.model.HomeUiModel

sealed class HomeUiState(open val uiModel: HomeUiModel) {
    object Initial : HomeUiState(HomeUiModel())
    data class Resume(override val uiModel: HomeUiModel) : HomeUiState(uiModel = uiModel)
}
