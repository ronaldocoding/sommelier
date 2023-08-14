package br.com.sommelier.presentation.viewmodel

import android.annotation.SuppressLint
import android.content.Context
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import br.com.sommelier.R
import br.com.sommelier.domain.usecase.SignInUserUseCase
import br.com.sommelier.presentation.login.action.LoginAction
import br.com.sommelier.presentation.login.model.EmailUiState
import br.com.sommelier.presentation.login.model.LoginUiModel
import br.com.sommelier.presentation.login.model.PasswordUiState
import br.com.sommelier.presentation.login.state.LoginUiState
import br.com.sommelier.util.emptyString
import br.com.sommelier.util.ext.asLiveData
import br.com.sommelier.util.validator.isValidEmail
import br.com.sommelier.util.validator.isValidPassword
import kotlinx.coroutines.launch

@SuppressLint("StaticFieldLeak")
class LoginViewModel(
    private val context: Context,
    private val signInUserUseCase: SignInUserUseCase
) : ViewModel(), LoginAction {

    private val _uiState = MutableLiveData<LoginUiState>(LoginUiState.Initial)
    val uiState = _uiState.asLiveData()

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
            }
        }
    }

    private fun handleOnTypeEmailField(action: LoginAction.Action.OnTypeEmailField) {
        val state = checkNotNull(_uiState.value)
        val uiModel = state.uiModel
        val newUiModel = uiModel.copy(
            emailUiState = EmailUiState(
                text = action.email,
                errorSupportingMessage = emptyString(),
                isError = false
            )
        )
        val newUiState = LoginUiState.Resume(uiModel = newUiModel)
        _uiState.value = newUiState
    }

    private fun handleOnTypePasswordField(action: LoginAction.Action.OnTypePasswordField) {
        val state = checkNotNull(_uiState.value)
        val uiModel = state.uiModel
        val newUiModel = uiModel.copy(
            passwordUiState = PasswordUiState(
                text = action.password,
                errorSupportingMessage = emptyString(),
                isError = false
            )
        )
        val newUiState = LoginUiState.Resume(uiModel = newUiModel)
        _uiState.value = newUiState
    }

    private suspend fun handleOnClickLoginButton() {
        val state = checkNotNull(_uiState.value)
        val uiModel = state.uiModel
        val emailSupportingMessage = getEmailSupportingMessage(uiModel.emailUiState.text)
        val passwordSupportingMessage = getPasswordSupportingMessage(uiModel.passwordUiState.text)
        val newEmailUiState = uiModel.emailUiState.copy(
            errorSupportingMessage = emailSupportingMessage,
            isError = emailSupportingMessage.isNotBlank()
        )
        val newPasswordUiState = uiModel.passwordUiState.copy(
            errorSupportingMessage = passwordSupportingMessage,
            isError = passwordSupportingMessage.isNotBlank()
        )
        val newUiModel = uiModel.copy(
            emailUiState = newEmailUiState,
            passwordUiState = newPasswordUiState
        )
        val newUiState = LoginUiState.Resume(uiModel = newUiModel)
        if (newEmailUiState.isError || newPasswordUiState.isError) {
            _uiState.value = newUiState
        } else {
            tryToLogin(uiModel)
        }
    }

    private suspend fun tryToLogin(uiModel: LoginUiModel) {
        _uiState.value = LoginUiState.Loading(uiModel = uiModel)
        signInUserUseCase(
            SignInUserUseCase.Params(
                userEmail = uiModel.emailUiState.text,
                userPassword = uiModel.passwordUiState.text
            )
        ).fold(
            ifLeft = {
                val newSnackBarUiState = uiModel.snackBarUiState.copy(
                    text = context.getString(R.string.login_error_message)
                )
                val newUiModel = uiModel.copy(
                    snackBarUiState = newSnackBarUiState
                )
                val newUiState = LoginUiState.Error(uiModel = newUiModel)
                _uiState.value = newUiState
                (_uiState.value as LoginUiState.Error).uiModel.snackBarUiState.hostState
                    .showSnackbar(newSnackBarUiState.text)
            },
            ifRight = {
                // TODO: Navigate to Home screen
            }
        )
    }

    private fun getEmailSupportingMessage(email: String): String {
        if (email.isBlank()) {
            return context.getString(R.string.blank_email_message)
        }
        if (isValidEmail(context, email).not()) {
            return context.getString(R.string.invalid_email_message)
        }
        return emptyString()
    }

    private fun getPasswordSupportingMessage(password: String): String {
        if (password.isBlank()) {
            return context.getString(R.string.blank_password_message)
        }
        if (isValidPassword(password).not()) {
            return context.getString(R.string.invalid_password_message)
        }
        return emptyString()
    }
}