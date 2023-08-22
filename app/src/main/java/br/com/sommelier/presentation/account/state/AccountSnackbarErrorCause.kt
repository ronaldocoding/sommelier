package br.com.sommelier.presentation.account.state

sealed class AccountSnackbarErrorCause {
    object PasswordReset : AccountSnackbarErrorCause()
    object DeleteAccount : AccountSnackbarErrorCause()
    object Logout : AccountSnackbarErrorCause()
}
