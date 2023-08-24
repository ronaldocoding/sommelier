package br.com.sommelier.presentation.editaccount.state

sealed class EditAccountLoadingCause {
    object FetchAccountData : EditAccountLoadingCause()
    object SaveAccountData : EditAccountLoadingCause()
}
