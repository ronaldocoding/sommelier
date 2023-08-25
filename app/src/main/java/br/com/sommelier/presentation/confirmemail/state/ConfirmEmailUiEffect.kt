package br.com.sommelier.presentation.confirmemail.state

sealed class ConfirmEmailUiEffect {
    object PopBackStack : ConfirmEmailUiEffect()
}
