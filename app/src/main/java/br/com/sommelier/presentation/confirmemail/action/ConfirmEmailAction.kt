package br.com.sommelier.presentation.confirmemail.action

fun interface ConfirmEmailAction {
    fun sendAction(action: Action)

    sealed class Action {
        object ClickBackButton : Action()
        object ClickOkButton : Action()
    }
}
