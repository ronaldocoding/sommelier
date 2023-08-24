package br.com.sommelier.presentation.account.action

interface AccountAction {
    fun sendAction(action: Action)

    sealed class Action {
        object OnTryToFetchAccountData : Action()
        object OnClickTryToFetchAccountDataAgainButton : Action()
        object OnClickBackButton : Action()
        object OnClickEditButton : Action()
        object OnClickPasswordResetButton : Action()
        object OnClickPasswordResetConfirmationButton : Action()
        object OnDismissPasswordResetDialog : Action()
        object OnTryToPasswordReset : Action()
        object OnClickDeleteAccountButton : Action()
        object OnClickDeleteAccountConfirmationButton : Action()
        object OnDismissDeleteAccountDialog : Action()
        object OnTryToDeleteAccount : Action()
        object OnClickLogoutButton : Action()
        object OnClickLogoutConfirmationButton : Action()
        object OnDismissLogoutDialog : Action()
        object OnTryToLogout : Action()
    }
}
