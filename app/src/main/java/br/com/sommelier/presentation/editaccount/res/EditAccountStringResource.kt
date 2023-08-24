package br.com.sommelier.presentation.editaccount.res

sealed class EditAccountStringResource {
    object Empty: EditAccountStringResource()
    object InvalidName: EditAccountStringResource()
    object ErrorSnackbar: EditAccountStringResource()
    object SuccessSnackbar: EditAccountStringResource()
}
