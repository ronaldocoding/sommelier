package br.com.sommelier.presentation.account.state

sealed class AccountUiEffect {
    object PopBackStack : AccountUiEffect()
    object OpenEditAccountScreen : AccountUiEffect()
    object OpenResetPasswordScreen : AccountUiEffect()
    object OpenLoginScreen : AccountUiEffect()
    data class ShowLoading(val cause: AccountLoadingCause) : AccountUiEffect()
    data class ShowSnackbarError(val cause: AccountSnackbarErrorCause) : AccountUiEffect()
    data class ShowDialog(val type: AccountDialogType) : AccountUiEffect()
}
