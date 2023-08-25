package br.com.sommelier.presentation.editaccount.action

interface EditAccountAction {
    fun sendAction(action: Action)

    sealed class Action {
        object OnInitial : Action()
        object OnFetchAccountData : Action()
        object OnClickTryToFetchAccountDataAgainButton : Action()
        object OnClickBackButton : Action()
        object OnClickSaveButton : Action()
        object OnTryToSave : Action()
        data class OnTypeNameField(val name: String) : Action()
    }
}
