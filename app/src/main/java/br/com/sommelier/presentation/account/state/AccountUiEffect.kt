package br.com.sommelier.presentation.account.state

sealed class AccountUiEffect {
    object PopBackStack : AccountUiEffect()
    object OpenEditAccountScreen : AccountUiEffect()
    object OpenPasswordResetScreen : AccountUiEffect()
    object OpenLoginScreen : AccountUiEffect()
    object ShowSnackbarError : AccountUiEffect()
    data class ShowLoading(val loadingCause: AccountLoadingCause) : AccountUiEffect()
}
