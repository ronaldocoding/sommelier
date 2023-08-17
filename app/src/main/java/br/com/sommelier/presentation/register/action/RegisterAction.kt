package br.com.sommelier.presentation.register.action

interface RegisterAction {
    fun sendAction(action: Action)

    sealed class Action {
        data class OnTypeNameField(val name: String) : Action()
        data class OnTypeEmailField(val email: String) : Action()
        data class OnTypePasswordField(val password: String) : Action()
        data class OnTypePasswordConfirmationField(val confirmationPassword: String) : Action()
        object OnClickAlreadyHaveAccountButton : Action()
        object OnClickRegisterButton : Action()
        object OnTryToRegister : Action()
    }
}
