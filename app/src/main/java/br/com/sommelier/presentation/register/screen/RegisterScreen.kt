package br.com.sommelier.presentation.register.screen

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
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
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.input.KeyboardType
import br.com.sommelier.R
import br.com.sommelier.presentation.register.action.RegisterAction
import br.com.sommelier.presentation.register.model.RegisterUiModel
import br.com.sommelier.presentation.register.res.RegisterStringResource
import br.com.sommelier.presentation.register.state.RegisterUiEffect
import br.com.sommelier.presentation.register.state.RegisterUiState
import br.com.sommelier.presentation.register.viewmodel.RegisterViewModel
import br.com.sommelier.shared.screen.GenericLoadingScreen
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
@Composable
fun RegisterScreen(navigateToLoginScreen: () -> Unit, navigateToConfirmEmailScreen: () -> Unit) {
    val viewModel = getViewModel<RegisterViewModel>()
    val uiState = checkNotNull(viewModel.uiState.observeAsState())
    val uiModel = checkNotNull(uiState.value?.uiModel)

    SommelierTheme {
        Scaffold(
            containerColor = ColorReference.white,
            snackbarHost = {
                SnackbarHost(uiModel.snackbarUiState.hostState) {
                    SommelierSnackbar(
                        modifier = Modifier.padding(
                            start = Spacing.mediumLarge,
                            end = Spacing.mediumLarge,
                            bottom = Spacing.larger
                        ),
                        text = stringResource(id = R.string.register_error_message),
                        type = uiModel.snackbarUiState.type
                    )
                }
            }
        ) {
            UiState(uiState, it, uiModel, viewModel)
            UiEffect(viewModel, uiModel, navigateToLoginScreen, navigateToConfirmEmailScreen)
        }
    }
}

@Composable
private fun UiState(
    uiState: State<RegisterUiState?>,
    it: PaddingValues,
    uiModel: RegisterUiModel,
    viewModel: RegisterViewModel
) {
    when (uiState.value) {
        is RegisterUiState.Loading -> {
            LoadingScreen()
        }

        else -> {
            Screen(it, uiModel, viewModel)
        }
    }
}

@Composable
private fun LoadingScreen() {
    GenericLoadingScreen()
}

@Composable
private fun Screen(
    it: PaddingValues,
    uiModel: RegisterUiModel,
    viewModel: RegisterViewModel
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(it)
            .verticalScroll(rememberScrollState()),
        verticalArrangement = Arrangement.SpaceBetween,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Cocktail()
        RegisterFields(uiModel, viewModel)
        RegisterButton(viewModel)
        LoginClickableText(viewModel)
    }
}

@Composable
private fun Cocktail() {
    Column(
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Image(
            painter = painterResource(id = R.drawable.ic_wine),
            contentDescription = stringResource(
                id = R.string.cocktail_icon_description
            )
        )
        Text(
            text = stringResource(id = R.string.app_name),
            style = Typography.displayLarge
        )
    }
}

@Composable
private fun RegisterFields(
    uiModel: RegisterUiModel,
    viewModel: RegisterViewModel
) {
    Column(
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        OutlinedTextInput(
            value = uiModel.nameUiState.text,
            onValueChange = { changedValue ->
                viewModel.sendAction(
                    RegisterAction.Action.OnTypeNameField(
                        changedValue
                    )
                )
            },
            leadingIcon = ImageVector.vectorResource(id = R.drawable.ic_user),
            label = stringResource(id = R.string.name_text_field_label),
            placeholder = stringResource(id = R.string.name_text_field_placeholder),
            supportingText = {
                Text(
                    text = uiModel.nameUiState.errorSupportingMessage.toText(),
                    style = Typography.label
                )
            },
            isError = uiModel.nameUiState.isError,
            modifier = Modifier.padding(horizontal = Spacing.mediumLarge)
        )
        Spacer(modifier = Modifier.padding(Spacing.extraSmaller))
        OutlinedTextInput(
            value = uiModel.emailUiState.text,
            onValueChange = { changedValue ->
                viewModel.sendAction(
                    RegisterAction.Action.OnTypeEmailField(
                        changedValue
                    )
                )
            },
            leadingIcon = ImageVector.vectorResource(id = R.drawable.ic_mail),
            label = stringResource(id = R.string.email_text_field_label),
            placeholder = stringResource(id = R.string.email_text_field_placeholder),
            supportingText = {
                Text(
                    text = uiModel.emailUiState.errorSupportingMessage.toText(),
                    style = Typography.label
                )
            },
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Email
            ),
            isError = uiModel.emailUiState.isError,
            modifier = Modifier.padding(horizontal = Spacing.mediumLarge)
        )
        Spacer(modifier = Modifier.padding(Spacing.extraSmaller))
        OutlinedPasswordInput(
            value = uiModel.passwordUiState.text,
            onValueChange = { changedValue ->
                viewModel.sendAction(
                    RegisterAction.Action.OnTypePasswordField(
                        changedValue
                    )
                )
            },
            label = stringResource(id = R.string.password_text_field_label),
            placeholder = stringResource(id = R.string.password_text_field_placeholder),
            supportingText = {
                Text(
                    text = uiModel.passwordUiState.errorSupportingMessage.toText(),
                    style = Typography.label
                )
            },
            isError = uiModel.passwordUiState.isError,
            modifier = Modifier.padding(horizontal = Spacing.mediumLarge)
        )
        Spacer(modifier = Modifier.padding(Spacing.extraSmaller))
        OutlinedPasswordInput(
            value = uiModel.passwordConfirmationUiState.text,
            onValueChange = { changedValue ->
                viewModel.sendAction(
                    RegisterAction.Action.OnTypePasswordConfirmationField(
                        changedValue
                    )
                )
            },
            label = stringResource(id = R.string.password_confirmation_text_field_label),
            placeholder = stringResource(
                id = R.string.password_confirmation_text_field_placeholder
            ),
            supportingText = {
                Text(
                    text = uiModel.passwordConfirmationUiState.errorSupportingMessage
                        .toText(),
                    style = Typography.label
                )
            },
            isError = uiModel.passwordConfirmationUiState.isError,
            modifier = Modifier.padding(horizontal = Spacing.mediumLarge)
        )
    }
}

@Composable
private fun RegisterButton(viewModel: RegisterViewModel) {
    ActionButton(
        text = stringResource(id = R.string.register_button_label),
        modifier = Modifier.padding(horizontal = Spacing.small),
        onClick = {
            viewModel.sendAction(RegisterAction.Action.OnClickRegisterButton)
        }
    )
}

@Composable
private fun LoginClickableText(viewModel: RegisterViewModel) {
    ClickableText(
        nonClickableText = stringResource(id = R.string.register_non_clickable_text),
        clickableText = stringResource(id = R.string.register_clickable_text),
        onClick = {
            viewModel.sendAction(RegisterAction.Action.OnClickAlreadyHaveAccountButton)
        }
    )
}

@Composable
fun RegisterStringResource.toText(): String {
    return when (this) {
        is RegisterStringResource.Empty -> {
            emptyString()
        }

        is RegisterStringResource.BlankName -> {
            stringResource(id = R.string.blank_name_message)
        }

        is RegisterStringResource.InvalidName -> {
            stringResource(id = R.string.invalid_name_message)
        }

        is RegisterStringResource.BlankEmail -> {
            stringResource(id = R.string.blank_email_message)
        }

        is RegisterStringResource.InvalidEmail -> {
            stringResource(id = R.string.invalid_email_message)
        }

        is RegisterStringResource.BlankPassword -> {
            stringResource(id = R.string.blank_password_message)
        }

        is RegisterStringResource.InvalidPassword -> {
            stringResource(id = R.string.invalid_password_message)
        }

        is RegisterStringResource.BlankPasswordConfirmation -> {
            stringResource(id = R.string.blank_password_confirmation_message)
        }

        is RegisterStringResource.PasswordConfirmationNotMatch -> {
            stringResource(id = R.string.password_confirmation_not_match_message)
        }
    }
}

@Composable
private fun UiEffect(
    viewModel: RegisterViewModel,
    uiModel: RegisterUiModel,
    navigateToLoginScreen: () -> Unit = {},
    navigateToConfirmEmailScreen: () -> Unit = {}
) {
    val localLifecycleOwner = LocalLifecycleOwner.current
    val coroutineScope = rememberCoroutineScope()
    val snackbarErrorMessage = stringResource(id = R.string.register_error_message)
    viewModel.uiEffect.observe(localLifecycleOwner) { uiEffect ->
        when (uiEffect) {
            is RegisterUiEffect.ShowLoading -> {
                viewModel.sendAction(RegisterAction.Action.OnTryToRegister)
            }

            is RegisterUiEffect.OpenLoginScreen -> {
                navigateToLoginScreen()
            }

            is RegisterUiEffect.OpenConfirmEmailScreen -> {
                navigateToConfirmEmailScreen()
            }

            is RegisterUiEffect.ShowSnackbarError -> {
                coroutineScope.launch {
                    uiModel.snackbarUiState.hostState.showSnackbar(
                        message = snackbarErrorMessage
                    )
                }
            }
        }
    }
}
