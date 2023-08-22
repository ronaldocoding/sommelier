package br.com.sommelier.presentation.account.state

sealed class AccountDialogType {
    object PasswordResetConfirmation : AccountDialogType()
    object DeleteAccountConfirmation : AccountDialogType()
    object LogoutConfirmation : AccountDialogType()
}

