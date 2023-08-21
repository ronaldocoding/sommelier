package br.com.sommelier.presentation.account.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import br.com.sommelier.base.event.MutableSingleLiveEvent
import br.com.sommelier.domain.usecase.DeleteUserUseCase
import br.com.sommelier.domain.usecase.GetUserDocumentUseCase
import br.com.sommelier.domain.usecase.SignOutUserUseCase
import br.com.sommelier.presentation.account.action.AccountAction
import br.com.sommelier.presentation.account.state.AccountUiEffect
import br.com.sommelier.presentation.account.state.AccountUiState
import br.com.sommelier.util.ext.asLiveData
import kotlinx.coroutines.launch

class AccountViewModel(
    private val getUserDocumentUseCase: GetUserDocumentUseCase,
    private val logOutUserUseCase: SignOutUserUseCase,
    private val deleteUserUseCase: DeleteUserUseCase
) : ViewModel(), AccountAction {

    private val _uiState = MutableLiveData<AccountUiState>(AccountUiState.Initial)
    val uiState = _uiState.asLiveData()

    private val _uiEffect = MutableSingleLiveEvent<AccountUiEffect>()
    val uiEffect = _uiEffect.asSingleLiveEvent()

    override fun sendAction(action: AccountAction.Action) {
        viewModelScope.launch {
            when (action) {
                is AccountAction.Action.OnClickBackButton -> {
                    handleOnClickBackButton()
                }

                is AccountAction.Action.OnClickEditButton -> {
                    handleOnClickEditButton()
                }

                is AccountAction.Action.OnClickResetPasswordButton -> {
                    handleOnClickResetPasswordButton()
                }

                else -> {
                    // do nothing
                }
            }
        }
    }

    private fun handleOnClickBackButton() {
        _uiEffect.value = AccountUiEffect.PopBackStack
    }

    private fun handleOnClickEditButton() {
        _uiEffect.value = AccountUiEffect.OpenEditAccountScreen
    }

    private fun handleOnClickResetPasswordButton() {
        _uiEffect.value = AccountUiEffect.OpenResetPasswordScreen
    }
}