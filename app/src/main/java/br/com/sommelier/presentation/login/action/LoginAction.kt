package br.com.sommelier.presentation.login.action

fun interface LoginAction {
    fun sendAction(action: Action)

    sealed class Action {
        data class OnTypeEmailField(val email: String) : Action()
        data class OnTypePasswordField(val password: String) : Action()
        object OnClickLoginButton : Action()
        object OnTryToLogin : Action()
        object OnClickSignUpButton : Action()
        object OnClickForgotPasswordButton : Action()
    }
}
