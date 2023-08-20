package br.com.sommelier.presentation.passwordreset.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import br.com.sommelier.base.event.MutableSingleLiveEvent
import br.com.sommelier.domain.usecase.SendPasswordResetEmailUseCase
import br.com.sommelier.presentation.passwordreset.action.PasswordResetAction
import br.com.sommelier.presentation.passwordreset.model.PasswordResetUiModel
import br.com.sommelier.presentation.passwordreset.res.PasswordResetStringResource
import br.com.sommelier.presentation.passwordreset.state.PasswordResetUiEffect
import br.com.sommelier.presentation.passwordreset.state.PasswordResetUiState
import br.com.sommelier.util.ext.asLiveData
import kotlinx.coroutines.launch
import org.apache.commons.validator.routines.EmailValidator

class PasswordResetViewModel(
    private val sendPasswordResetEmailUseCase: SendPasswordResetEmailUseCase
) : ViewModel(), PasswordResetAction {

    private val _uiState = MutableLiveData<PasswordResetUiState>(PasswordResetUiState.Initial)
    val uiState = _uiState.asLiveData()

    private val _uiEffect = MutableSingleLiveEvent<PasswordResetUiEffect>()
    val uiEffect = _uiEffect.asSingleLiveEvent()

    override fun sendAction(action: PasswordResetAction.Action) {
        viewModelScope.launch {
            when (action) {
                is PasswordResetAction.Action.OnClickMainBackButton -> {
                    handleOnClickMainBackButton()
                }

                is PasswordResetAction.Action.OnTypeEmailField -> {
                    handleOnTypeEmailField(action)
                }

                is PasswordResetAction.Action.OnClickSendButton -> {
                    handleOnClickSendButton()
                }

                is PasswordResetAction.Action.OnTryToSendPasswordResetEmail -> {
                    handleOnTryToSendPasswordResetEmail()
                }

                is PasswordResetAction.Action.OnClickSecondaryBackButton -> {
                    handleOnClickSecondaryBackButton()
                }

                is PasswordResetAction.Action.OnClickOkButton -> {
                    handleOnClickOkButton()
                }

                is PasswordResetAction.Action.OnClickTryAgainButton -> {
                    handleOnClickTryAgainButton()
                }
            }
        }
    }

    private fun handleOnClickMainBackButton() {
        _uiEffect.value = PasswordResetUiEffect.PopBackStack
    }

    private fun handleOnTypeEmailField(action: PasswordResetAction.Action.OnTypeEmailField) {
        val state = checkNotNull(_uiState.value)
        val uiModel = checkNotNull(state.uiModel)
        val newEmailUiState = uiModel.emailUiState.copy(text = action.email)
        val newUiModel = uiModel.copy(emailUiState = newEmailUiState)
        val newUiState = PasswordResetUiState.Resume(newUiModel)
        _uiState.value = newUiState
    }

    private fun handleOnClickSendButton() {
        val state = checkNotNull(_uiState.value)
        val uiModel = checkNotNull(state.uiModel)
        val errorSupportingMessage = getEmailErrorSupportingMessage(uiModel.emailUiState.text)
        val newEmailUiState = uiModel.emailUiState.copy(
            errorSupportingMessage = errorSupportingMessage,
            isError = errorSupportingMessage != PasswordResetStringResource.Empty
        )
        val newUiModel = uiModel.copy(emailUiState = newEmailUiState)
        if (newUiModel.emailUiState.isError) {
            _uiState.value = PasswordResetUiState.Resume(newUiModel)
        } else {
            _uiState.value = PasswordResetUiState.Loading(newUiModel.copy(isLoading = true))
            _uiEffect.value = PasswordResetUiEffect.ShowLoading
        }
    }

    private fun getEmailErrorSupportingMessage(email: String): PasswordResetStringResource {
        return when {
            email.isBlank() -> {
                PasswordResetStringResource.BlankEmail
            }

            EmailValidator.getInstance().isValid(email).not() -> {
                PasswordResetStringResource.InvalidEmail
            }

            else -> {
                PasswordResetStringResource.Empty
            }
        }
    }

    private suspend fun handleOnTryToSendPasswordResetEmail() {
        tryToSendPasswordResetEmail()
    }

    private suspend fun handleOnClickTryAgainButton() {
        tryToSendPasswordResetEmail()
    }

    private suspend fun tryToSendPasswordResetEmail() {
        val state = checkNotNull(_uiState.value)
        val uiModel = checkNotNull(state.uiModel).copy(isLoading = false)
        val email = uiModel.emailUiState.text
        sendPasswordResetEmailUseCase(
            SendPasswordResetEmailUseCase.Params(userEmail = email)
        ).fold(
            ifLeft = {
                val newUiModel = removePossibleRemainingErrorState(uiModel)
                _uiState.value = PasswordResetUiState.Error(newUiModel)
            },
            ifRight = {
                val newUiModel = uiModel.copy(
                    isBackButtonEnabled = false
                )
                _uiState.value = PasswordResetUiState.Success(newUiModel)
            }
        )
    }

    private fun removePossibleRemainingErrorState(
        uiModel: PasswordResetUiModel
    ): PasswordResetUiModel {
        if (uiModel.emailUiState.isError) {
            return PasswordResetUiModel(
                emailUiState = uiModel.emailUiState.copy(
                    errorSupportingMessage = PasswordResetStringResource.Empty,
                    isError = false
                )
            )
        }
        return uiModel
    }

    private fun handleOnClickSecondaryBackButton() {
        val state = checkNotNull(_uiState.value)
        val newUiState = PasswordResetUiState.Resume(state.uiModel)
        _uiState.value = newUiState
    }

    private fun handleOnClickOkButton() {
        _uiEffect.value = PasswordResetUiEffect.PopBackStack
    }
}
