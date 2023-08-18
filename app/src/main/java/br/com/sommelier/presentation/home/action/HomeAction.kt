package br.com.sommelier.presentation.home.action

interface HomeAction {
    fun sendAction(action: Action)

    sealed class Action {
        data class OnTypeSearchField(val query: String) : Action()
        object OnSearch : Action()
        object OnClickManageAccountButton : Action()
        object OnClickAddRestaurantButton : Action()
    }
}
