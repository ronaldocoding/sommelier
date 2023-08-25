package br.com.sommelier.presentation.editaccount.state

sealed class EditAccountUiEffect {
    object PopBackStack : EditAccountUiEffect()
    data class ShowLoading(val loadingCause: EditAccountLoadingCause) : EditAccountUiEffect()
    object ShowSnackbarError : EditAccountUiEffect()
    object ShowSnackbarSuccess : EditAccountUiEffect()
}
