package br.com.sommelier.presentation.account.state

sealed class AccountUiEffect {
    object PopBackStack : AccountUiEffect()
    object OpenEditAccountScreen : AccountUiEffect()
    object OpenResetPasswordScreen : AccountUiEffect()
    object OpenLoginScreen : AccountUiEffect()
    data class ShowLoading(val loadingCause: AccountLoadingCause) : AccountUiEffect()
    data class ShowSnackbarError(val errorCause: AccountSnackbarErrorCause) : AccountUiEffect()
    data class ShowDialog(val dialogType: AccountDialogType) : AccountUiEffect()
}
