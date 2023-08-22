package br.com.sommelier.presentation.account.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import br.com.sommelier.base.event.MutableSingleLiveEvent
import br.com.sommelier.base.result.Success
import br.com.sommelier.base.usecase.UseCase
import br.com.sommelier.domain.model.UserFirebase
import br.com.sommelier.domain.usecase.DeleteUserUseCase
import br.com.sommelier.domain.usecase.GetCurrentUserUseCase
import br.com.sommelier.domain.usecase.GetUserDocumentUseCase
import br.com.sommelier.domain.usecase.SignOutUserUseCase
import br.com.sommelier.presentation.account.action.AccountAction
import br.com.sommelier.presentation.account.model.AccountUiModel
import br.com.sommelier.presentation.account.state.AccountDialogType
import br.com.sommelier.presentation.account.state.AccountLoadingCause
import br.com.sommelier.presentation.account.state.AccountSnackbarErrorCause
import br.com.sommelier.presentation.account.state.AccountUiEffect
import br.com.sommelier.presentation.account.state.AccountUiState
import br.com.sommelier.util.ext.asLiveData
import kotlinx.coroutines.launch

class AccountViewModel(
    private val getCurrentUserUseCase: GetCurrentUserUseCase,
    private val getUserDocumentUseCase: GetUserDocumentUseCase,
    private val deleteUserUseCase: DeleteUserUseCase,
    private val logOutUserUseCase: SignOutUserUseCase
) : ViewModel(), AccountAction {

    private val _uiState = MutableLiveData<AccountUiState>(AccountUiState.Initial)
    val uiState = _uiState.asLiveData()

    private val _uiEffect = MutableSingleLiveEvent<AccountUiEffect>()
    val uiEffect = _uiEffect.asSingleLiveEvent()

    override fun sendAction(action: AccountAction.Action) {
        viewModelScope.launch {
            when (action) {
                is AccountAction.Action.OnFetchAccountData -> {
                    handleOnFetchAccountData()
                }

                is AccountAction.Action.OnClickTryToFetchAccountDataAgainButton -> {
                    handleClickTryToFetchAccountDataAgainButton()
                }

                is AccountAction.Action.OnTryToFetchAccountDataAgain -> {
                    handleOnClickTryToFetchAccountDataAgainButton()
                }

                is AccountAction.Action.OnClickBackButton -> {
                    handleOnClickBackButton()
                }

                is AccountAction.Action.OnClickEditButton -> {
                    handleOnClickEditButton()
                }

                is AccountAction.Action.OnClickResetPasswordButton -> {
                    handleOnClickResetPasswordButton()
                }

                is AccountAction.Action.OnClickResetPasswordConfirmationButton -> {
                    handleOnClickResetPasswordConfirmationButton()
                }

                is AccountAction.Action.OnTryToResetPassword -> {
                    handleOnTryToResetPassword()
                }

                is AccountAction.Action.OnClickDeleteAccountButton -> {
                    handleOnClickDeleteAccountButton()
                }

                is AccountAction.Action.OnClickDeleteAccountConfirmationButton -> {
                    handleOnClickDeleteAccountConfirmationButton()
                }

                is AccountAction.Action.OnTryToDeleteAccount -> {
                    handleOnTryToDeleteAccount()
                }

                is AccountAction.Action.OnClickLogoutButton -> {
                    handleOnClickLogoutButton()
                }

                is AccountAction.Action.OnClickLogoutConfirmationButton -> {
                    handleOnClickLogoutConfirmationButton()
                }

                is AccountAction.Action.OnTryToLogout -> {
                    handleOnTryToLogout()
                }
            }
        }
    }

    private suspend fun handleOnFetchAccountData() {
        tryToGetCurrentUserData()
    }

    private fun handleClickTryToFetchAccountDataAgainButton() {
        emitLoadingState(AccountUiModel(), AccountLoadingCause.FetchAccountData)
    }

    private suspend fun handleOnClickTryToFetchAccountDataAgainButton() {
        tryToGetCurrentUserData()
    }

    private suspend fun tryToGetCurrentUserData() {
        getCurrentUserUseCase(UseCase.None())
            .fold(
                ifLeft = {
                    emitErrorState()
                },
                ifRight = { successResult ->
                    tryToGetUserDocumentData(successResult)
                }
            )
    }

    private suspend fun tryToGetUserDocumentData(currentUserResult: Success<UserFirebase>) {
        getUserDocumentUseCase(
            GetUserDocumentUseCase.Params(
                userUid = currentUserResult.data.uid
            )
        ).fold(
            ifLeft = {
                emitErrorState()
            },
            ifRight = { successResult ->
                val newUiModel = AccountUiModel(
                    name = successResult.data.name,
                    email = successResult.data.email
                )
                _uiState.value = AccountUiState.Resume(newUiModel)
            }
        )
    }

    private fun emitErrorState() {
        _uiState.value = AccountUiState.Error
    }

    private fun handleOnClickBackButton() {
        _uiEffect.value = AccountUiEffect.PopBackStack
    }

    private fun handleOnClickEditButton() {
        _uiEffect.value = AccountUiEffect.OpenEditAccountScreen
    }

    private fun handleOnClickResetPasswordButton() {
        _uiEffect.value = AccountUiEffect.ShowDialog(AccountDialogType.PasswordResetConfirmation)
    }

    private fun handleOnClickResetPasswordConfirmationButton() {
        emitLoadingState(checkNotNull(_uiState.value).uiModel, AccountLoadingCause.PasswordReset)
    }

    private suspend fun handleOnTryToResetPassword() {
        logOutUserUseCase(UseCase.None())
            .fold(
                ifLeft = {
                    emitResumeState()
                    _uiEffect.value = AccountUiEffect.ShowSnackbarError(
                        AccountSnackbarErrorCause.PasswordReset
                    )
                },
                ifRight = {
                    _uiEffect.value = AccountUiEffect.OpenResetPasswordScreen
                }
            )
    }

    private fun handleOnClickDeleteAccountButton() {
        _uiEffect.value = AccountUiEffect.ShowDialog(AccountDialogType.DeleteAccountConfirmation)
    }

    private fun handleOnClickDeleteAccountConfirmationButton() {
        emitLoadingState(checkNotNull(_uiState.value).uiModel, AccountLoadingCause.DeleteAccount)
    }

    private suspend fun handleOnTryToDeleteAccount() {
        getCurrentUserUseCase(UseCase.None())
            .fold(
                ifLeft = {
                    handleTryToDeleteAccountError()
                },
                ifRight = {
                    deleteUserUseCase(
                        DeleteUserUseCase.Params(
                            userUid = it.data.uid
                        )
                    ).fold(
                        ifLeft = {
                            handleTryToDeleteAccountError()
                        },
                        ifRight = {
                            _uiEffect.value = AccountUiEffect.OpenLoginScreen
                        }
                    )
                }
            )
    }

    private fun handleTryToDeleteAccountError() {
        emitResumeState()
        _uiEffect.value = AccountUiEffect.ShowSnackbarError(
            AccountSnackbarErrorCause.DeleteAccount
        )
    }

    private fun handleOnClickLogoutButton() {
        _uiEffect.value = AccountUiEffect.ShowDialog(AccountDialogType.LogoutConfirmation)
    }

    private fun handleOnClickLogoutConfirmationButton() {
        emitLoadingState(checkNotNull(_uiState.value).uiModel, AccountLoadingCause.Logout)
    }

    private suspend fun handleOnTryToLogout() {
        logOutUserUseCase(UseCase.None())
            .fold(
                ifLeft = {
                    emitResumeState()
                    _uiEffect.value = AccountUiEffect.ShowSnackbarError(
                        AccountSnackbarErrorCause.Logout
                    )
                },
                ifRight = {
                    _uiEffect.value = AccountUiEffect.OpenLoginScreen
                }
            )
    }

    private fun emitResumeState() {
        val state = checkNotNull(_uiState.value)
        val uiModel = state.uiModel
        _uiState.value = AccountUiState.Resume(uiModel)
    }

    private fun emitLoadingState(uiModel: AccountUiModel, cause: AccountLoadingCause) {
        _uiState.value = AccountUiState.Loading(uiModel)
        _uiEffect.value = AccountUiEffect.ShowLoading(cause)
    }
}
