package br.com.sommelier.presentation.login.state

sealed class LoginUiEffect {
    object ShowLoading : LoginUiEffect()
    object OpenHomeScreen : LoginUiEffect()
    object OpenSignUpScreen : LoginUiEffect()
    object OpenForgotPasswordScreen : LoginUiEffect()
}