package br.com.sommelier.presentation.login.screen

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import br.com.sommelier.R
import br.com.sommelier.presentation.login.action.LoginAction
import br.com.sommelier.presentation.login.model.LoginUiModel
import br.com.sommelier.presentation.login.res.LoginStringResource
import br.com.sommelier.presentation.login.state.LoginUiEffect
import br.com.sommelier.presentation.login.state.LoginUiState
import br.com.sommelier.presentation.login.viewmodel.LoginViewModel
import br.com.sommelier.ui.component.ActionButton
import br.com.sommelier.ui.component.ClickableText
import br.com.sommelier.ui.component.OutlinedPasswordInput
import br.com.sommelier.ui.component.OutlinedTextInput
import br.com.sommelier.ui.component.SommelierSnackbar
import br.com.sommelier.ui.theme.ColorReference
import br.com.sommelier.ui.theme.SommelierTheme
import br.com.sommelier.ui.theme.Spacing
import br.com.sommelier.ui.theme.Typography
import br.com.sommelier.util.emptyString
import kotlinx.coroutines.launch
import org.koin.androidx.compose.getViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Preview(showBackground = true, showSystemUi = true)
@Composable
fun LoginScreen() {
    val viewModel = getViewModel<LoginViewModel>()
    val uiState = checkNotNull(viewModel.uiState.observeAsState())
    val uiModel = checkNotNull(uiState.value?.uiModel)

    SommelierTheme {
        Scaffold(
            containerColor = Color.White,
            snackbarHost = {
                SnackbarHost(uiModel.snackBarUiState.hostState) {
                    SommelierSnackbar(
                        modifier = Modifier.padding(
                            start = Spacing.mediumLarge,
                            end = Spacing.mediumLarge,
                            bottom = Spacing.larger
                        ),
                        text = stringResource(id = R.string.login_error_message),
                        type = uiModel.snackBarUiState.type
                    )
                }
            }
        ) {
            UiState(uiState, it, uiModel, viewModel)
            UiEffect(viewModel = viewModel)
        }
    }
}

@Composable
private fun UiState(
    uiState: State<LoginUiState?>,
    it: PaddingValues,
    uiModel: LoginUiModel,
    viewModel: LoginViewModel
) {
    when (uiState.value) {
        is LoginUiState.Loading -> {
            LoadingScreen()
        }

        else -> {
            Screen(it, uiModel, viewModel)
        }
    }
}

@Composable
private fun LoadingScreen() {
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        CircularProgressIndicator(
            color = ColorReference.royalPurple
        )
    }
}

@Composable
private fun Screen(
    it: PaddingValues,
    uiModel: LoginUiModel,
    viewModel: LoginViewModel
) {
    Column(
        modifier = Modifier
            .padding(it)
            .fillMaxSize()
            .verticalScroll(rememberScrollState()),
        verticalArrangement = Arrangement.SpaceBetween,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Cocktail()
        LoginFields(uiModel, viewModel)
        LoginButton(viewModel)
        LoginClickableTexts(viewModel)
    }
}

@Composable
private fun Cocktail() {
    Column(
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Spacer(modifier = Modifier.padding(Spacing.large))
        Image(
            painter = painterResource(id = R.drawable.ic_cocktail),
            contentDescription = stringResource(id = R.string.cocktail_icon_description)
        )
        Spacer(modifier = Modifier.padding(Spacing.extraSmaller))
        Text(
            text = stringResource(id = R.string.app_name),
            style = Typography.displayLarge
        )
    }
}

@Composable
private fun LoginFields(
    uiModel: LoginUiModel,
    viewModel: LoginViewModel
) {
    Column(
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        OutlinedTextInput(
            value = uiModel.emailUiState.text,
            onValueChange = { changedValue ->
                viewModel.sendAction(
                    LoginAction.Action.OnTypeEmailField(
                        changedValue
                    )
                )
            },
            leadingIcon = ImageVector.vectorResource(id = R.drawable.ic_mail),
            label = stringResource(id = R.string.email_text_field_label),
            placeholder = stringResource(id = R.string.email_text_field_placeholder),
            supportingText = {
                Text(
                    uiModel.emailUiState.errorSupportingMessage.toText(),
                    style = Typography.label
                )
            },
            isError = uiModel.emailUiState.isError,
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Email
            ),
            modifier = Modifier.padding(horizontal = Spacing.mediumLarge)
        )
        Spacer(modifier = Modifier.padding(Spacing.extraSmaller))
        OutlinedPasswordInput(
            value = uiModel.passwordUiState.text,
            onValueChange = { changedValue ->
                viewModel.sendAction(
                    LoginAction.Action.OnTypePasswordField(
                        changedValue
                    )
                )
            },
            label = stringResource(id = R.string.password_text_field_label),
            placeholder = stringResource(id = R.string.password_text_field_placeholder),
            supportingText = {
                Text(
                    uiModel.passwordUiState.errorSupportingMessage.toText(),
                    style = Typography.label
                )
            },
            isError = uiModel.passwordUiState.isError,
            modifier = Modifier.padding(horizontal = Spacing.mediumLarge)
        )
    }
}

@Composable
private fun LoginButton(viewModel: LoginViewModel) {
    ActionButton(
        text = stringResource(id = R.string.login_button_label),
        modifier = Modifier.padding(horizontal = Spacing.small),
        onClick = {
            viewModel.sendAction(LoginAction.Action.OnClickLoginButton)
        }
    )
}

@Composable
private fun LoginClickableTexts(viewModel: LoginViewModel) {
    Column(
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier.padding(bottom = Spacing.large)
    ) {
        ClickableText(
            nonClickableText = stringResource(id = R.string.login_non_clickable_text),
            clickableText = stringResource(id = R.string.login_first_clickable_text),
            onClick = {
                viewModel.sendAction(LoginAction.Action.OnClickSignUpButton)
            }
        )
        ClickableText(
            clickableText = stringResource(id = R.string.login_second_clickable_text),
            onClick = {
                viewModel.sendAction(
                    LoginAction.Action.OnClickForgotPasswordButton
                )
            }
        )
    }
}

@Composable
private fun UiEffect(viewModel: LoginViewModel) {
    val localLifecycleOwner = LocalLifecycleOwner.current
    val coroutineScope = rememberCoroutineScope()
    val snackbarErrorMessage = stringResource(id = R.string.login_error_message)
    viewModel.uiEffect.observe(localLifecycleOwner) { effect ->
        when (effect) {
            is LoginUiEffect.ShowLoading -> {
                viewModel.sendAction(LoginAction.Action.OnTryToLogin)
            }

            is LoginUiEffect.OpenHomeScreen -> {
                // TODO: Open Home Screen
            }

            is LoginUiEffect.OpenSignUpScreen -> {
                // TODO: Open Sign Up Screen
            }

            is LoginUiEffect.ShowSnackbarError -> {
                coroutineScope.launch {
                    viewModel.uiState.value?.uiModel?.snackBarUiState?.hostState?.showSnackbar(
                        message = snackbarErrorMessage
                    )
                }
            }

            is LoginUiEffect.OpenForgotPasswordScreen -> {
                // TODO: Open Forgot Password Screen
            }
        }
    }
}

@Composable
fun LoginStringResource.toText(): String {
    return when (this) {
        LoginStringResource.Empty -> emptyString()
        LoginStringResource.BlankEmail -> stringResource(id = R.string.blank_email_message)
        LoginStringResource.BlankPassword -> stringResource(id = R.string.blank_password_message)
        LoginStringResource.InvalidEmail -> stringResource(id = R.string.invalid_email_message)
    }
}
