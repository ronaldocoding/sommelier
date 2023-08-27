package br.com.sommelier.presentation.confirmemail.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import br.com.sommelier.base.event.MutableSingleLiveEvent
import br.com.sommelier.presentation.confirmemail.action.ConfirmEmailAction
import br.com.sommelier.presentation.confirmemail.state.ConfirmEmailUiEffect
import br.com.sommelier.presentation.confirmemail.state.ConfirmEmailUiState
import br.com.sommelier.util.ext.asLiveData

class ConfirmEmailViewModel : ViewModel(), ConfirmEmailAction {

    private val _uiState = MutableLiveData<ConfirmEmailUiState>(ConfirmEmailUiState.Initial)
    val uiState = _uiState.asLiveData()

    private val _uiEffect = MutableSingleLiveEvent<ConfirmEmailUiEffect>()
    val uiEffect = _uiEffect.asSingleLiveEvent()

    override fun sendAction(action: ConfirmEmailAction.Action) {
        when (action) {
            is ConfirmEmailAction.Action.ClickBackButton,
            is ConfirmEmailAction.Action.ClickOkButton -> {
                emitPopBackStackUiEffect()
            }
        }
    }

    private fun emitPopBackStackUiEffect() {
        _uiEffect.value = ConfirmEmailUiEffect.PopBackStack
    }
}
