package br.com.sommelier.presentation.account.state

sealed class AccountUiEffect {
    data class ShowLoading(val cause: AccountLoadingCause) : AccountUiEffect()
    object PopBackStack : AccountUiEffect()
    object OpenEditAccountScreen : AccountUiEffect()
    object OpenResetPasswordScreen : AccountUiEffect()
    object ShowDeleteAccountConfirmationDialog : AccountUiEffect()
    object ShowErrorDeleteAccountSnackbar : AccountUiEffect()
    object ShowLogoutConfirmationDialog : AccountUiEffect()
    object ShowErrorLogoutSnackbar : AccountUiEffect()
    object OpenLoginScreen : AccountUiEffect()
}

sealed class AccountLoadingCause {
    object FetchAccountData : AccountLoadingCause()
    object Logout : AccountLoadingCause()
    object DeleteAccount : AccountLoadingCause()
}
