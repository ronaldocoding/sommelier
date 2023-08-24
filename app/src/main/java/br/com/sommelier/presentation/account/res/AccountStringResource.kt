package br.com.sommelier.presentation.account.res

sealed class AccountStringResource {
    object Empty : AccountStringResource()
    object ErrorPasswordReset : AccountStringResource()
    object ErrorDeleteAccount : AccountStringResource()
    object ErrorLogout : AccountStringResource()
}
