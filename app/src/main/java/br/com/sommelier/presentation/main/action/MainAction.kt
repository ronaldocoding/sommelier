package br.com.sommelier.presentation.main.action

interface MainAction {
    fun sendAction(action: Action)

    sealed class Action {
        object OnInitial : Action()
        object OnCheckIfUserIsSignedIn : Action()
    }
}
