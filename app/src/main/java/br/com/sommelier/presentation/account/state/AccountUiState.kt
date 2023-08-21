package br.com.sommelier.presentation.account.state

import br.com.sommelier.presentation.account.model.AccountUiModel

sealed class AccountUiState(open val uiModel: AccountUiModel) {
    object Initial : AccountUiState(uiModel = AccountUiModel())
    object Error : AccountUiState(uiModel = AccountUiModel())
    data class Resume(override val uiModel: AccountUiModel) : AccountUiState(uiModel)
    data class Loading(override val uiModel: AccountUiModel) : AccountUiState(uiModel)
}
