package br.com.sommelier.presentation.account.state

sealed class AccountLoadingCause {
    object FetchAccountData : AccountLoadingCause()
    object PasswordReset : AccountLoadingCause()
    object Logout : AccountLoadingCause()
    object DeleteAccount : AccountLoadingCause()
}
