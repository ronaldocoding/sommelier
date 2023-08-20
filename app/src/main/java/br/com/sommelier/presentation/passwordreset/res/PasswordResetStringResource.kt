package br.com.sommelier.presentation.passwordreset.res

sealed class PasswordResetStringResource {
    object Empty : PasswordResetStringResource()
    object BlankEmail : PasswordResetStringResource()
    object InvalidEmail : PasswordResetStringResource()
}
