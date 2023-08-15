package br.com.sommelier.presentation.register.action

interface RegisterAction {
    fun sendAction(action: Action)

    sealed class Action {
        object OnTypeNameField: Action()
        object OnTypeEmailField: Action()
        object OnTypePasswordField: Action()
        object OnTypeConfirmationPasswordField: Action()
        object OnClickRegisterButton: Action()
        object OnClickAlreadyHaveAccountButton: Action()
        object OnTryToRegister: Action()
    }
}