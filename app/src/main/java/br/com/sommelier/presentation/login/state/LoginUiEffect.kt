package br.com.sommelier.presentation.login.state

sealed class LoginUiEffect {
    object ShowLoading : LoginUiEffect()
    object OpenHomeScreen : LoginUiEffect()
    object OpenRegisterScreen : LoginUiEffect()
    object ShowSnackbarError : LoginUiEffect()
    object OpenForgotPasswordScreen : LoginUiEffect()
    object OpenConfirmEmailScreen : LoginUiEffect()
}
