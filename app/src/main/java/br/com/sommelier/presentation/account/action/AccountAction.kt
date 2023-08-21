package br.com.sommelier.presentation.account.action

interface AccountAction {
    fun sendAction(action: Action)

    sealed class Action {
        object OnClickBackButton : Action()
        object OnClickEditButton : Action()
        object OnClickResetPasswordButton : Action()
        object OnClickDeleteAccountButton : Action()
        object OnClickLogoutButton : Action()
    }
}
