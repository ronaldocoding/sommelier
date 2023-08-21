package br.com.sommelier.presentation.account.state

sealed class AccountUiEffect {
    object PopBackStack : AccountUiEffect()
    object OpenEditAccountScreen : AccountUiEffect()
    object OpenResetPasswordScreen : AccountUiEffect()
    object ShowLogoutConfirmationDialog : AccountUiEffect()
    object ShowSuccessLogoutSnackbar : AccountUiEffect()
    object ShowErrorLogoutSnackbar : AccountUiEffect()
    object ShowDeleteAccountConfirmationDialog : AccountUiEffect()
    object ShowSuccessDeleteAccountSnackbar : AccountUiEffect()
    object ShowErrorDeleteAccountSnackbar : AccountUiEffect()
}
