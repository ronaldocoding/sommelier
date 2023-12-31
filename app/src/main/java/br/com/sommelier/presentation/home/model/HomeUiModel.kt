package br.com.sommelier.presentation.home.model

import br.com.sommelier.util.emptyString

data class SearchFieldUiState(
    val query: String = emptyString()
)

data class HomeUiModel(
    val searchFieldUiState: SearchFieldUiState = SearchFieldUiState()
)
