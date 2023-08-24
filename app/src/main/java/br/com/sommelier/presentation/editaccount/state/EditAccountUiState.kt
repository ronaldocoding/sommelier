package br.com.sommelier.presentation.editaccount.state

import br.com.sommelier.presentation.account.model.AccountUiModel

sealed class EditAccountUiState(open val uiModel: AccountUiModel) {
    object Initial : EditAccountUiState(AccountUiModel())
    data class Resume(override val uiModel: AccountUiModel) : EditAccountUiState(uiModel)
    data class Loading(override val uiModel: AccountUiModel) : EditAccountUiState(uiModel)
    data class Error(override val uiModel: AccountUiModel) : EditAccountUiState(uiModel)

}
