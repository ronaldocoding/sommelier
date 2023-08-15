package br.com.sommelier.presentation.login.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import br.com.sommelier.base.event.MutableSingleLiveEvent
import br.com.sommelier.domain.usecase.SignInUserUseCase
import br.com.sommelier.presentation.login.action.LoginAction
import br.com.sommelier.presentation.login.res.LoginStringResource
import br.com.sommelier.presentation.login.state.LoginUiEffect
import br.com.sommelier.presentation.login.state.LoginUiState
import br.com.sommelier.util.ext.asLiveData
import kotlinx.coroutines.launch
import org.apache.commons.validator.routines.EmailValidator

class LoginViewModel(private val signInUserUseCase: SignInUserUseCase) : ViewModel(), LoginAction {

    private val _uiState = MutableLiveData<LoginUiState>(LoginUiState.Initial)
    val uiState = _uiState.asLiveData()

    private val _uiEffect = MutableSingleLiveEvent<LoginUiEffect>()
    val uiEffect = _uiEffect.asSingleLiveEvent()

    override fun sendAction(action: LoginAction.Action) {
        viewModelScope.launch {
            when (action) {
                is LoginAction.Action.OnTypeEmailField -> {
                    handleOnTypeEmailField(action)
                }
                is LoginAction.Action.OnTypePasswordField -> {
                    handleOnTypePasswordField(action)
                }
                is LoginAction.Action.OnClickLoginButton -> {
                    handleOnClickLoginButton()
                }
                is LoginAction.Action.OnTryToLogin -> {
                    handleTryToLogin()
                }
                is LoginAction.Action.OnClickSignUpButton -> {
                    handleOnClickSignUpButton()
                }
                is LoginAction.Action.OnClickForgotPasswordButton -> {
                    handleOnClickForgotPasswordButton()
                }
            }
        }
    }

    private fun handleOnTypeEmailField(action: LoginAction.Action.OnTypeEmailField) {
        val state = checkNotNull(_uiState.value)
        val uiModel = state.uiModel
        val newEmailUiState = uiModel.emailUiState.copy(text = action.email)
        val newUiModel = uiModel.copy(
            emailUiState = newEmailUiState
        )
        val newUiState = LoginUiState.Resume(uiModel = newUiModel)
        _uiState.value = newUiState
    }

    private fun handleOnTypePasswordField(action: LoginAction.Action.OnTypePasswordField) {
        val state = checkNotNull(_uiState.value)
        val uiModel = state.uiModel
        val newPasswordUiState = uiModel.passwordUiState.copy(text = action.password)
        val newUiModel = uiModel.copy(
            passwordUiState = newPasswordUiState
        )
        val newUiState = LoginUiState.Resume(uiModel = newUiModel)
        _uiState.value = newUiState
    }

    private fun handleOnClickLoginButton() {
        val state = checkNotNull(_uiState.value)
        val uiModel = state.uiModel
        val emailSupportingMessage = getEmailSupportingMessage(uiModel.emailUiState.text)
        val passwordSupportingMessage = getPasswordSupportingMessage(uiModel.passwordUiState.text)
        val newEmailUiState = uiModel.emailUiState.copy(
            errorSupportingMessage = emailSupportingMessage,
            isError = emailSupportingMessage != LoginStringResource.Empty
        )
        val newPasswordUiState = uiModel.passwordUiState.copy(
            errorSupportingMessage = passwordSupportingMessage,
            isError = passwordSupportingMessage != LoginStringResource.Empty
        )
        val newUiModel = uiModel.copy(
            emailUiState = newEmailUiState,
            passwordUiState = newPasswordUiState
        )
        val newUiState = LoginUiState.Resume(uiModel = newUiModel)
        if (newEmailUiState.isError || newPasswordUiState.isError) {
            _uiState.value = newUiState
        } else {
            _uiState.value = LoginUiState.Loading(uiModel = uiModel)
            _uiEffect.value = LoginUiEffect.ShowLoading
        }
    }

    private fun getEmailSupportingMessage(email: String): LoginStringResource {
        if (email.isBlank()) {
            return LoginStringResource.BlankEmail
        }
        if (EmailValidator.getInstance().isValid(email).not()) {
            return LoginStringResource.InvalidEmail
        }
        return LoginStringResource.Empty
    }

    private fun getPasswordSupportingMessage(password: String): LoginStringResource {
        if (password.isBlank()) {
            return LoginStringResource.BlankPassword
        }
        return LoginStringResource.Empty
    }

    private suspend fun handleTryToLogin() {
        val state = checkNotNull(_uiState.value)
        val uiModel = state.uiModel
        signInUserUseCase(
            SignInUserUseCase.Params(
                userEmail = uiModel.emailUiState.text,
                userPassword = uiModel.passwordUiState.text
            )
        ).fold(
            ifLeft = {
                val newUiState = LoginUiState.Error(uiModel = uiModel)
                _uiState.value = newUiState
                _uiEffect.value = LoginUiEffect.ShowSnackbarError
            },
            ifRight = {
                _uiEffect.value = LoginUiEffect.OpenHomeScreen
            }
        )
    }

    private fun handleOnClickSignUpButton() {
        _uiEffect.value = LoginUiEffect.OpenRegisterScreen
    }

    private fun handleOnClickForgotPasswordButton() {
        _uiEffect.value = LoginUiEffect.OpenForgotPasswordScreen
    }
}
