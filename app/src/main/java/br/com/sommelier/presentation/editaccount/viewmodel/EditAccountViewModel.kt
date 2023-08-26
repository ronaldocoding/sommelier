package br.com.sommelier.presentation.editaccount.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import br.com.sommelier.base.event.MutableSingleLiveEvent
import br.com.sommelier.base.result.Success
import br.com.sommelier.base.usecase.UseCase
import br.com.sommelier.domain.model.UserDomain
import br.com.sommelier.domain.model.UserFirebase
import br.com.sommelier.domain.usecase.GetCurrentUserUseCase
import br.com.sommelier.domain.usecase.GetUserDocumentUseCase
import br.com.sommelier.domain.usecase.UpdateUserDocumentUseCase
import br.com.sommelier.presentation.editaccount.action.EditAccountAction
import br.com.sommelier.presentation.editaccount.res.EditAccountStringResource
import br.com.sommelier.presentation.editaccount.state.EditAccountLoadingCause
import br.com.sommelier.presentation.editaccount.state.EditAccountUiEffect
import br.com.sommelier.presentation.editaccount.state.EditAccountUiState
import br.com.sommelier.ui.component.SommelierSnackbarType
import br.com.sommelier.util.emptyString
import br.com.sommelier.util.ext.asLiveData
import br.com.sommelier.util.validator.isValidName
import kotlinx.coroutines.launch

class EditAccountViewModel(
    private val getCurrentUserUseCase: GetCurrentUserUseCase,
    private val getUserDocumentUseCase: GetUserDocumentUseCase,
    private val updateUserDocumentUseCase: UpdateUserDocumentUseCase
) : ViewModel(), EditAccountAction {

    private val _uiState = MutableLiveData<EditAccountUiState>(EditAccountUiState.Initial)
    val uiState = _uiState.asLiveData()

    private val _uiEffect = MutableSingleLiveEvent<EditAccountUiEffect>()
    val uiEffect = _uiEffect.asSingleLiveEvent()

    override fun sendAction(action: EditAccountAction.Action) {
        viewModelScope.launch {
            when (action) {
                is EditAccountAction.Action.OnInitial -> {
                    handleOnInitial()
                }

                is EditAccountAction.Action.OnFetchAccountData -> {
                    handleOnFetchAccountData()
                }

                is EditAccountAction.Action.OnClickTryToFetchAccountDataAgainButton -> {
                    handleOnClickTryToFetchAccountDataAgainButton()
                }

                is EditAccountAction.Action.OnClickBackButton -> {
                    handleOnClickBackButton()
                }

                is EditAccountAction.Action.OnClickSaveButton -> {
                    handleOnClickSaveButton()
                }

                is EditAccountAction.Action.OnTryToSave -> {
                    handleOnTryToSave()
                }

                is EditAccountAction.Action.OnTypeNameField -> {
                    handleOnTypeNameField(action)
                }
            }
        }
    }

    private fun handleOnInitial() {
        handleFetchAccountDataLoading()
    }

    private suspend fun handleOnFetchAccountData() {
        getCurrentUserUseCase(UseCase.None()).fold(
            ifLeft = {
                emitErrorState()
            },
            ifRight = { successResult ->
                tryToGetUserDocument(successResult)
            }
        )
    }

    private suspend fun tryToGetUserDocument(
        currentUserResult: Success<UserFirebase>
    ) {
        getUserDocumentUseCase(
            GetUserDocumentUseCase.Params(
                currentUserResult.data.uid
            )
        ).fold(
            ifLeft = {
                emitErrorState()
            },
            ifRight = { successResult ->
                val state = checkNotNull(_uiState.value)
                val uiModel = state.uiModel
                val newUiModel = uiModel.copy(
                    editNameFieldUiState = uiModel.editNameFieldUiState.copy(
                        placeholder = successResult.data.name,
                    ),
                    isLoading = false
                )
                _uiState.value = EditAccountUiState.Resume(newUiModel)
            }
        )
    }

    private fun handleOnClickTryToFetchAccountDataAgainButton() {
        handleFetchAccountDataLoading()
    }

    private fun handleFetchAccountDataLoading() {
        emitLoadingState()
        emitShowLoadingEffect(EditAccountLoadingCause.FetchAccountData)
    }

    private fun handleOnClickBackButton() {
        _uiEffect.emit(EditAccountUiEffect.PopBackStack)
    }

    private fun handleOnClickSaveButton() {
        val state = checkNotNull(_uiState.value)
        val uiModel = state.uiModel
        val errorSupportingMessage = getErrorSupportingMessage(uiModel.editNameFieldUiState.name)
        val newUiModel = uiModel.copy(
            editNameFieldUiState = uiModel.editNameFieldUiState.copy(
                errorSupportingMessage = errorSupportingMessage,
                isError = errorSupportingMessage != EditAccountStringResource.Empty
            )
        )
        if (newUiModel.editNameFieldUiState.isError) {
            _uiState.value = EditAccountUiState.Resume(newUiModel)
        } else {
            emitLoadingState()
            emitShowLoadingEffect(EditAccountLoadingCause.SaveAccountData)
        }
    }

    private fun getErrorSupportingMessage(name: String) =
        if (name.isBlank()) {
            EditAccountStringResource.BlankName
        } else if (isValidName(name).not()) {
            EditAccountStringResource.InvalidName
        } else {
            EditAccountStringResource.Empty
        }

    private suspend fun handleOnTryToSave() {
        getCurrentUserUseCase(UseCase.None()).fold(
            ifLeft = {
                handleErrorWhenTryingToSave()
            },
            ifRight = { currentUserResult ->
                tryToGetDataAndSave(currentUserResult)
            }
        )
    }

    private suspend fun tryToGetDataAndSave(
        currentUserResult: Success<UserFirebase>
    ) {
        getUserDocumentUseCase(
            GetUserDocumentUseCase.Params(
                currentUserResult.data.uid
            )
        ).fold(
            ifLeft = {
                handleErrorWhenTryingToSave()
            },
            ifRight = { userDocumentResult ->
                val newUserDomain = userDocumentResult.data.copy(
                    name = checkNotNull(_uiState.value).uiModel.editNameFieldUiState.name
                )
                tryToSave(newUserDomain)
            }
        )
    }

    private suspend fun tryToSave(userDomain: UserDomain) {
        updateUserDocumentUseCase(userDomain).fold(
            ifLeft = {
                handleErrorWhenTryingToSave()
            },
            ifRight = {
                handleSuccessWhenTryingToSave(userDomain)
            }
        )
    }

    private fun emitShowLoadingEffect(loadingCause: EditAccountLoadingCause) {
        _uiEffect.emit(EditAccountUiEffect.ShowLoading(loadingCause))
    }

    private fun handleSuccessWhenTryingToSave(userDomain: UserDomain) {
        val state = checkNotNull(_uiState.value)
        val uiModel = state.uiModel
        val newUiModel = uiModel.copy(
            editNameFieldUiState = uiModel.editNameFieldUiState.copy(
                name = emptyString(),
                placeholder = userDomain.name
            ),
            snackbarUiState = uiModel.snackbarUiState.copy(
                message = EditAccountStringResource.SuccessSnackbar,
                type = SommelierSnackbarType.Success
            ),
            isLoading = false
        )
        _uiState.value = EditAccountUiState.Resume(newUiModel)
        _uiEffect.value = EditAccountUiEffect.ShowSnackbarSuccess
    }

    private fun handleErrorWhenTryingToSave() {
        emitResumeState()
        emitSnackbarErrorEffect()
    }

    private fun handleOnTypeNameField(action: EditAccountAction.Action.OnTypeNameField) {
        val state = checkNotNull(_uiState.value)
        val uiModel = state.uiModel
        val newUiModel = uiModel.copy(
            editNameFieldUiState = uiModel.editNameFieldUiState.copy(
                name = action.name
            )
        )
        _uiState.value = EditAccountUiState.Resume(newUiModel)
    }

    private fun emitResumeState() {
        val state = checkNotNull(_uiState.value)
        val uiModel = state.uiModel.copy(isLoading = false)
        _uiState.value = EditAccountUiState.Resume(uiModel)
    }

    private fun emitSnackbarErrorEffect() {
        val state = checkNotNull(_uiState.value)
        val uiModel = state.uiModel
        val newUiModel = uiModel.copy(
            snackbarUiState = uiModel.snackbarUiState.copy(
                message = EditAccountStringResource.ErrorSnackbar,
                type = SommelierSnackbarType.Error
            ),
            isLoading = false
        )
        _uiState.value = EditAccountUiState.Resume(newUiModel)
        _uiEffect.emit(EditAccountUiEffect.ShowSnackbarError)
    }

    private fun emitLoadingState() {
        val state = checkNotNull(_uiState.value)
        val uiModel = state.uiModel.copy(isLoading = true)
        _uiState.value = EditAccountUiState.Loading(uiModel)
    }

    private fun emitErrorState() {
        val state = checkNotNull(_uiState.value)
        val uiModel = state.uiModel.copy(isLoading = false)
        _uiState.value = EditAccountUiState.Error(uiModel)
    }
}
