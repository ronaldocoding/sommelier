package br.com.sommelier.presentation.register.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import br.com.sommelier.base.event.MutableSingleLiveEvent
import br.com.sommelier.domain.model.UserInfo
import br.com.sommelier.domain.usecase.CreateUserUseCase
import br.com.sommelier.presentation.register.action.RegisterAction
import br.com.sommelier.presentation.register.res.RegisterStringResource
import br.com.sommelier.presentation.register.state.RegisterUiEffect
import br.com.sommelier.presentation.register.state.RegisterUiState
import br.com.sommelier.util.ext.asLiveData
import br.com.sommelier.util.validator.isValidName
import br.com.sommelier.util.validator.isValidPassword
import kotlinx.coroutines.launch
import org.apache.commons.validator.routines.EmailValidator

class RegisterViewModel(
    private val createUserUseCase: CreateUserUseCase
) : ViewModel(), RegisterAction {

    private val _uiState = MutableLiveData<RegisterUiState>(RegisterUiState.Initial)
    val uiState = _uiState.asLiveData()

    private val _uiEffect = MutableSingleLiveEvent<RegisterUiEffect>()
    val uiEffect = _uiEffect.asSingleLiveEvent()
    override fun sendAction(action: RegisterAction.Action) {
        viewModelScope.launch {
            when (action) {
                is RegisterAction.Action.OnTypeNameField -> {
                    handleOnTypeNameField(action)
                }

                is RegisterAction.Action.OnTypeEmailField -> {
                    handleOnTypeEmailField(action)
                }

                is RegisterAction.Action.OnTypePasswordField -> {
                    handleOnTypePasswordField(action)
                }

                is RegisterAction.Action.OnTypePasswordConfirmationField -> {
                    handleOnTypePasswordConfirmationField(action)
                }

                is RegisterAction.Action.OnClickAlreadyHaveAccountButton -> {
                    handleOnClickAlreadyHaveAccountButton()
                }

                is RegisterAction.Action.OnClickRegisterButton -> {
                    handleOnClickRegisterButton()
                }

                is RegisterAction.Action.OnTryToRegister -> {
                    handleOnTryToRegister()
                }
            }
        }
    }

    private fun handleOnTypeNameField(action: RegisterAction.Action.OnTypeNameField) {
        val state = checkNotNull(_uiState.value)
        val uiModel = state.uiModel
        val newNameUiState = uiModel.nameUiState.copy(text = action.name)
        val newUiModel = uiModel.copy(
            nameUiState = newNameUiState
        )
        val newUiState = RegisterUiState.Resume(uiModel = newUiModel)
        _uiState.value = newUiState
    }

    private fun handleOnTypeEmailField(action: RegisterAction.Action.OnTypeEmailField) {
        val state = checkNotNull(_uiState.value)
        val uiModel = state.uiModel
        val newEmailUiState = uiModel.emailUiState.copy(text = action.email)
        val newUiModel = uiModel.copy(
            emailUiState = newEmailUiState
        )
        val newUiState = RegisterUiState.Resume(uiModel = newUiModel)
        _uiState.value = newUiState
    }

    private fun handleOnTypePasswordField(action: RegisterAction.Action.OnTypePasswordField) {
        val state = checkNotNull(_uiState.value)
        val uiModel = state.uiModel
        val newPasswordUiState = uiModel.passwordUiState.copy(text = action.password)
        val newUiModel = uiModel.copy(
            passwordUiState = newPasswordUiState
        )
        val newUiState = RegisterUiState.Resume(uiModel = newUiModel)
        _uiState.value = newUiState
    }

    private fun handleOnTypePasswordConfirmationField(
        action: RegisterAction.Action.OnTypePasswordConfirmationField
    ) {
        val state = checkNotNull(_uiState.value)
        val uiModel = state.uiModel
        val newPasswordConfirmationUiState =
            uiModel.passwordConfirmationUiState.copy(text = action.confirmationPassword)
        val newUiModel = uiModel.copy(
            passwordConfirmationUiState = newPasswordConfirmationUiState
        )
        val newUiState = RegisterUiState.Resume(uiModel = newUiModel)
        _uiState.value = newUiState
    }

    private fun handleOnClickAlreadyHaveAccountButton() {
        _uiEffect.value = RegisterUiEffect.OpenLoginScreen
    }

    private fun handleOnClickRegisterButton() {
        val state = checkNotNull(_uiState.value)
        val uiModel = state.uiModel
        val nameErrorSupportingMessage = getNameErrorSupportingMessage(uiModel.nameUiState.text)
        val emailErrorSupportingMessage = getEmailErrorSupportingMessage(uiModel.emailUiState.text)
        val passwordErrorSupportingMessage =
            getPasswordErrorSupportingMessage(uiModel.passwordUiState.text)
        val confirmationPasswordErrorSupportingMessage =
            getConfirmationPasswordErrorSupportingMessage(
                uiModel.passwordUiState.text,
                uiModel.passwordConfirmationUiState.text
            )
        val newNameUiState = uiModel.nameUiState.copy(
            errorSupportingMessage = nameErrorSupportingMessage,
            isError = nameErrorSupportingMessage != RegisterStringResource.Empty
        )
        val newEmailUiState = uiModel.emailUiState.copy(
            errorSupportingMessage = emailErrorSupportingMessage,
            isError = emailErrorSupportingMessage != RegisterStringResource.Empty
        )
        val newPasswordUiState = uiModel.passwordUiState.copy(
            errorSupportingMessage = passwordErrorSupportingMessage,
            isError = passwordErrorSupportingMessage != RegisterStringResource.Empty
        )
        val newPasswordConfirmationUiState = uiModel.passwordConfirmationUiState.copy(
            errorSupportingMessage = confirmationPasswordErrorSupportingMessage,
            isError = confirmationPasswordErrorSupportingMessage != RegisterStringResource.Empty
        )
        val newUiModel = uiModel.copy(
            nameUiState = newNameUiState,
            emailUiState = newEmailUiState,
            passwordUiState = newPasswordUiState,
            passwordConfirmationUiState = newPasswordConfirmationUiState
        )
        if (newNameUiState.isError || newEmailUiState.isError ||
            newPasswordUiState.isError || newPasswordConfirmationUiState.isError
        ) {
            _uiState.value = RegisterUiState.Resume(uiModel = newUiModel)
        } else {
            _uiState.value = RegisterUiState.Loading(uiModel = newUiModel)
            _uiEffect.value = RegisterUiEffect.ShowLoading
        }
    }

    private fun getNameErrorSupportingMessage(name: String): RegisterStringResource {
        return if (name.isBlank()) {
            RegisterStringResource.BlankName
        } else if (isValidName(name).not()) {
            RegisterStringResource.InvalidName
        } else {
            RegisterStringResource.Empty
        }
    }

    private fun getEmailErrorSupportingMessage(email: String): RegisterStringResource {
        return if (email.isBlank()) {
            RegisterStringResource.BlankEmail
        } else if (EmailValidator.getInstance().isValid(email).not()) {
            RegisterStringResource.InvalidEmail
        } else {
            RegisterStringResource.Empty
        }
    }

    private fun getPasswordErrorSupportingMessage(password: String): RegisterStringResource {
        return if (password.isBlank()) {
            RegisterStringResource.BlankPassword
        } else if (!isValidPassword(password)) {
            RegisterStringResource.InvalidPassword
        } else {
            RegisterStringResource.Empty
        }
    }

    private fun getConfirmationPasswordErrorSupportingMessage(
        password: String,
        confirmationPassword: String
    ): RegisterStringResource {
        return if (confirmationPassword.isBlank()) {
            RegisterStringResource.BlankPasswordConfirmation
        } else if (password != confirmationPassword) {
            RegisterStringResource.PasswordConfirmationNotMatch
        } else {
            RegisterStringResource.Empty
        }
    }

    private suspend fun handleOnTryToRegister() {
        val state = checkNotNull(_uiState.value)
        val uiModel = state.uiModel
        val userInfo = UserInfo(
            name = uiModel.nameUiState.text,
            email = uiModel.emailUiState.text,
            password = uiModel.passwordUiState.text
        )
        createUserUseCase(userInfo).fold(
            ifLeft = {
                _uiState.value = RegisterUiState.Error(uiModel = uiModel)
                _uiEffect.value = RegisterUiEffect.ShowSnackbarError
            },
            ifRight = {
                _uiEffect.value = RegisterUiEffect.OpenConfirmEmailScreen
            }
        )
    }
}
