package br.com.sommelier.presentation.account.action

interface AccountAction {
    fun sendAction(action: Action)

    sealed class Action {
        object OnFetchAccountData : Action()
        object OnClickTryToFetchAccountDataAgainButton : Action()
        object OnTryToFetchAccountDataAgain : Action()
        object OnClickBackButton : Action()
        object OnClickEditButton : Action()
        object OnClickResetPasswordButton : Action()
        object OnClickResetPasswordConfirmationButton : Action()
        object OnTryToResetPassword : Action()
        object OnClickDeleteAccountButton : Action()
        object OnClickDeleteAccountConfirmationButton : Action()
        object OnTryToDeleteAccount : Action()
        object OnClickLogoutButton : Action()
        object OnClickLogoutConfirmationButton : Action()
        object OnTryToLogout : Action()
    }
}
