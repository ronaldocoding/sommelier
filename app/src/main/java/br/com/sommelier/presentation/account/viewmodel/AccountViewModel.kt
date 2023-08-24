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
import br.com.sommelier.presentation.account.res.AccountStringResource
import br.com.sommelier.presentation.account.state.AccountLoadingCause
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
                is AccountAction.Action.OnTryToFetchAccountData -> {
                    handleOnFetchAccountData()
                }

                is AccountAction.Action.OnClickTryToFetchAccountDataAgainButton -> {
                    handleClickTryToFetchAccountDataAgainButton()
                }

                is AccountAction.Action.OnClickBackButton -> {
                    handleOnClickBackButton()
                }

                is AccountAction.Action.OnClickEditButton -> {
                    handleOnClickEditButton()
                }

                is AccountAction.Action.OnClickPasswordResetButton -> {
                    handleOnClickResetPasswordButton()
                }

                is AccountAction.Action.OnClickPasswordResetConfirmationButton -> {
                    handleOnClickResetPasswordConfirmationButton()
                }

                is AccountAction.Action.OnDismissPasswordResetDialog -> {
                    handleOnDismissPasswordResetDialog()
                }

                is AccountAction.Action.OnTryToPasswordReset -> {
                    handleOnTryToResetPassword()
                }

                is AccountAction.Action.OnClickDeleteAccountButton -> {
                    handleOnClickDeleteAccountButton()
                }

                is AccountAction.Action.OnClickDeleteAccountConfirmationButton -> {
                    handleOnClickDeleteAccountConfirmationButton()
                }

                is AccountAction.Action.OnDismissDeleteAccountDialog -> {
                    handleOnDismissDeleteAccountDialog()
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

                is AccountAction.Action.OnDismissLogoutDialog -> {
                    handleOnDismissLogoutDialog()
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
        val state = checkNotNull(_uiState.value)
        val newUiState = state.uiModel.copy(
            isPasswordResetDialogVisible = true
        )
        _uiState.value = AccountUiState.Resume(newUiState)
    }

    private fun handleOnClickResetPasswordConfirmationButton() {
        emitLoadingState(checkNotNull(_uiState.value).uiModel, AccountLoadingCause.PasswordReset)
    }

    private fun handleOnDismissPasswordResetDialog() {
        val state = checkNotNull(_uiState.value)
        val uiModel = state.uiModel.copy(
            isPasswordResetDialogVisible = false
        )
        _uiState.value = AccountUiState.Resume(uiModel)
    }

    private suspend fun handleOnTryToResetPassword() {
        logOutUserUseCase(UseCase.None())
            .fold(
                ifLeft = {
                    val state = checkNotNull(_uiState.value)
                    val uiModel = state.uiModel.copy(
                        snackbarUiState = AccountUiModel().snackbarUiState.copy(
                            message = AccountStringResource.ErrorPasswordReset
                        )
                    )
                    _uiState.value = AccountUiState.Resume(uiModel)
                    _uiEffect.value = AccountUiEffect.ShowSnackbarError
                },
                ifRight = {
                    _uiEffect.value = AccountUiEffect.OpenPasswordResetScreen
                }
            )
    }

    private fun handleOnClickDeleteAccountButton() {
        val state = checkNotNull(_uiState.value)
        val newUiState = state.uiModel.copy(
            isDeleteAccountDialogVisible = true
        )
        _uiState.value = AccountUiState.Resume(newUiState)
    }

    private fun handleOnClickDeleteAccountConfirmationButton() {
        emitLoadingState(checkNotNull(_uiState.value).uiModel, AccountLoadingCause.DeleteAccount)
    }

    private fun handleOnDismissDeleteAccountDialog() {
        val state = checkNotNull(_uiState.value)
        val uiModel = state.uiModel.copy(
            isDeleteAccountDialogVisible = false
        )
        _uiState.value = AccountUiState.Resume(uiModel)
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
        val state = checkNotNull(_uiState.value)
        val uiModel = state.uiModel.copy(
            snackbarUiState = AccountUiModel().snackbarUiState.copy(
                message = AccountStringResource.ErrorDeleteAccount
            )
        )
        _uiState.value = AccountUiState.Resume(uiModel)
        _uiEffect.value = AccountUiEffect.ShowSnackbarError
    }

    private fun handleOnClickLogoutButton() {
        val state = checkNotNull(_uiState.value)
        val newUiState = state.uiModel.copy(
            isLogoutDialogVisible = true
        )
        _uiState.value = AccountUiState.Resume(newUiState)
    }

    private fun handleOnClickLogoutConfirmationButton() {
        emitLoadingState(checkNotNull(_uiState.value).uiModel, AccountLoadingCause.Logout)
    }

    private fun handleOnDismissLogoutDialog() {
        val state = checkNotNull(_uiState.value)
        val uiModel = state.uiModel.copy(
            isLogoutDialogVisible = false
        )
        _uiState.value = AccountUiState.Resume(uiModel)
    }

    private suspend fun handleOnTryToLogout() {
        logOutUserUseCase(UseCase.None())
            .fold(
                ifLeft = {
                    val state = checkNotNull(_uiState.value)
                    val uiModel = state.uiModel.copy(
                        snackbarUiState = AccountUiModel().snackbarUiState.copy(
                            message = AccountStringResource.ErrorLogout
                        )
                    )
                    _uiState.value = AccountUiState.Resume(uiModel)
                    _uiEffect.value = AccountUiEffect.ShowSnackbarError
                },
                ifRight = {
                    _uiEffect.value = AccountUiEffect.OpenLoginScreen
                }
            )
    }

    private fun emitLoadingState(uiModel: AccountUiModel, cause: AccountLoadingCause) {
        _uiState.value = AccountUiState.Loading(uiModel.copy(isLoading = true))
        _uiEffect.value = AccountUiEffect.ShowLoading(cause)
    }
}
