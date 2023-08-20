package br.com.sommelier.presentation.passwordreset.state

sealed class PasswordResetUiEffect {
    object PopBackStack : PasswordResetUiEffect()
    object ShowLoading : PasswordResetUiEffect()
}
