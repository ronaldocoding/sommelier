package br.com.sommelier.presentation.editaccount.state

sealed class EditAccountUiEffect {
    object PopBackStack : EditAccountUiEffect()
    object ShowSnackbarError : EditAccountUiEffect()
    object ShowSnackbarSuccess : EditAccountUiEffect()
}
