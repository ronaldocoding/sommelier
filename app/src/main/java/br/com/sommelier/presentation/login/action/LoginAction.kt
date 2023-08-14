package br.com.sommelier.presentation.login.action

interface LoginAction {
    fun sendAction(action: Action)

    sealed class Action {
        data class OnTypeEmailField(val email: String) : Action()
        data class OnTypePasswordField(val password: String) : Action()
        object OnClickLoginButton : Action()
        object TryToLogin : Action()
        object onClickSignUpButton : Action()
        object onClickForgotPasswordButton : Action()
    }
}
