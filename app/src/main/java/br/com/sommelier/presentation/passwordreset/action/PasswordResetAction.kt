package br.com.sommelier.presentation.passwordreset.action

interface PasswordResetAction {
    fun sendAction(action: Action)

    sealed class Action {
        object OnClickMainBackButton : Action()
        object OnClickSendButton : Action()
        object OnTryToSendPasswordResetEmail : Action()
        object OnClickSecondaryBackButton : Action()
        object OnClickTryAgainButton : Action()
        object OnClickOkButton : Action()
        data class OnTypeEmailField(val email: String) : Action()
    }
}
