package br.com.sommelier.presentation.account.res

sealed class AccountStringResource {
    object Empty : AccountStringResource()
    object SuccessLogout : AccountStringResource()
    object ErrorLogout : AccountStringResource()
    object SuccessDeleteAccount : AccountStringResource()
    object ErrorDeleteAccount : AccountStringResource()
}
