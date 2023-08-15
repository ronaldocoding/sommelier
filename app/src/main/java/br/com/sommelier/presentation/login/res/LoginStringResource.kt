package br.com.sommelier.presentation.login.res

sealed class LoginStringResource {
    object Empty : LoginStringResource()
    object BlankEmail : LoginStringResource()
    object InvalidEmail : LoginStringResource()
    object BlankPassword : LoginStringResource()
}
