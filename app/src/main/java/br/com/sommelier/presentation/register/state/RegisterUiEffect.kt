package br.com.sommelier.presentation.register.state

sealed class RegisterUiEffect {
    object ShowLoading : RegisterUiEffect()
    object OpenLoginScreen : RegisterUiEffect()
    object OpenConfirmEmailScreen : RegisterUiEffect()
    object ShowSnackbarError : RegisterUiEffect()
}
