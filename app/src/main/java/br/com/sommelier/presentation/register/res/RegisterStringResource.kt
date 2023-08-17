package br.com.sommelier.presentation.register.res

sealed class RegisterStringResource {
    object Empty : RegisterStringResource()
    object BlankName : RegisterStringResource()
    object BlankEmail : RegisterStringResource()
    object BlankPassword : RegisterStringResource()
    object BlankPasswordConfirmation : RegisterStringResource()
    object PasswordConfirmationNotMatch : RegisterStringResource()
    object InvalidEmail : RegisterStringResource()
    object InvalidPassword : RegisterStringResource()
    object InvalidName : RegisterStringResource()
}
