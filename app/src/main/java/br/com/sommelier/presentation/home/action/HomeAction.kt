package br.com.sommelier.presentation.home.action

interface HomeAction {
    fun sendAction(action: Action)

    sealed class Action {
        object OnTypeSearchField : Action()
        object OnClickSearchField : Action()
        object OnClickManageAccountButton : Action()
        object OnClickAddRestaurantButton : Action()
    }
}
