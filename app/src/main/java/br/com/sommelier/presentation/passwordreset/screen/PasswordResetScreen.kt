package br.com.sommelier.presentation.passwordreset.screen

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
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import br.com.sommelier.R
import br.com.sommelier.presentation.passwordreset.action.PasswordResetAction
import br.com.sommelier.presentation.passwordreset.model.PasswordResetUiModel
import br.com.sommelier.presentation.passwordreset.res.PasswordResetStringResource
import br.com.sommelier.presentation.passwordreset.state.PasswordResetUiEffect
import br.com.sommelier.presentation.passwordreset.state.PasswordResetUiState
import br.com.sommelier.presentation.passwordreset.viewmodel.PasswordResetViewModel
import br.com.sommelier.shared.screen.GenericErrorScreen
import br.com.sommelier.shared.screen.GenericLoadingScreen
import br.com.sommelier.ui.component.ActionButton
import br.com.sommelier.ui.component.OutlinedTextInput
import br.com.sommelier.ui.component.SommelierTopBar
import br.com.sommelier.ui.component.SommelierTopBarButton
import br.com.sommelier.ui.theme.ColorReference
import br.com.sommelier.ui.theme.SommelierTheme
import br.com.sommelier.ui.theme.Spacing
import br.com.sommelier.ui.theme.Typography
import br.com.sommelier.util.emptyString
import org.koin.androidx.compose.getViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PasswordResetScreen(popBackStack: () -> Unit) {
    val viewModel = getViewModel<PasswordResetViewModel>()
    val uiState = checkNotNull(viewModel.uiState.observeAsState())
    val uiModel = checkNotNull(uiState.value?.uiModel)

    SommelierTheme {
        Scaffold(
            containerColor = ColorReference.white,
            topBar = if (uiModel.isLoading.not()) {
                { PasswordResetScreenTopBar(uiModel, uiState, viewModel) }
            } else {
                {}
            }
        ) { innerPadding ->
            UiState(innerPadding, uiModel, viewModel)
            UiEffect(viewModel, popBackStack)
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun PasswordResetScreenTopBar(
    uiModel: PasswordResetUiModel,
    uiState: State<PasswordResetUiState?>,
    viewModel: PasswordResetViewModel
) {
    SommelierTopBar(
        title = stringResource(id = R.string.password_reset_top_bar_title),
        leftButton = if (uiModel.isBackButtonEnabled) {
            SommelierTopBarButton.Enabled(
                icon = Icons.Filled.ArrowBack,
                contentDescription = stringResource(
                    id = R.string.arrow_back_icon_description
                ),
                onClick = {
                    when (uiState.value) {
                        is PasswordResetUiState.Initial,
                        is PasswordResetUiState.Resume -> {
                            viewModel.sendAction(
                                PasswordResetAction.Action.OnClickMainBackButton
                            )
                        }

                        else -> {
                            viewModel.sendAction(
                                PasswordResetAction.Action.OnClickSecondaryBackButton
                            )
                        }
                    }
                }
            )
        } else {
            SommelierTopBarButton.Disabled
        }
    )
}

@Composable
private fun UiState(
    innerPadding: PaddingValues,
    uiModel: PasswordResetUiModel,
    viewModel: PasswordResetViewModel
) {
    when (checkNotNull(viewModel.uiState.value)) {
        is PasswordResetUiState.Initial, is PasswordResetUiState.Resume -> {
            PasswordResetInitialScreen(innerPadding, uiModel, viewModel)
        }

        is PasswordResetUiState.Loading -> {
            PasswordResetLoadingScreen()
        }

        is PasswordResetUiState.Success -> {
            PasswordResetSuccessScreen(innerPadding, viewModel)
        }

        is PasswordResetUiState.Error -> {
            PasswordResetErrorScreen(viewModel)
        }
    }
}

@Composable
private fun UiEffect(viewModel: PasswordResetViewModel, popBackStack: () -> Unit = {}) {
    val localLifecycleOwner = LocalLifecycleOwner.current
    viewModel.uiEffect.observe(localLifecycleOwner) { effect ->
        when (effect) {
            is PasswordResetUiEffect.ShowLoading -> {
                viewModel.sendAction(PasswordResetAction.Action.OnTryToSendPasswordResetEmail)
            }

            is PasswordResetUiEffect.PopBackStack -> {
                popBackStack()
            }
        }
    }
}

@Composable
private fun PasswordResetInitialScreen(
    innerPadding: PaddingValues,
    uiModel: PasswordResetUiModel,
    viewModel: PasswordResetViewModel
) {
    Column(
        verticalArrangement = Arrangement.SpaceBetween,
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier
            .fillMaxSize()
            .padding(innerPadding)
            .verticalScroll(rememberScrollState())
    ) {
        Column(
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier.padding(top = Spacing.small)
        ) {
            Image(
                painter = painterResource(id = R.drawable.ic_repair),
                contentDescription = stringResource(id = R.string.repair_icon_description)
            )
            Spacer(modifier = Modifier.padding(Spacing.medium))
            Text(
                text = stringResource(id = R.string.password_reset_description),
                style = Typography.bodyLarge.copy(color = ColorReference.quartz),
                textAlign = TextAlign.Center,
                modifier = Modifier.padding(horizontal = Spacing.larger)
            )
        }
        OutlinedTextInput(
            value = uiModel.emailUiState.text,
            onValueChange = { changedValue ->
                viewModel.sendAction(
                    PasswordResetAction.Action.OnTypeEmailField(changedValue)
                )
            },
            isError = uiModel.emailUiState.isError,
            label = stringResource(id = R.string.email_text_field_label),
            placeholder = stringResource(id = R.string.email_text_field_placeholder),
            leadingIcon = ImageVector.vectorResource(id = R.drawable.ic_mail),
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email),
            modifier = Modifier.padding(horizontal = Spacing.mediumLarge),
            supportingText = {
                Text(
                    text = uiModel.emailUiState.errorSupportingMessage.toText(),
                    style = Typography.label
                )
            }
        )
        ActionButton(
            text = stringResource(id = R.string.send_button_label),
            modifier = Modifier.padding(Spacing.small),
            onClick = {
                viewModel.sendAction(PasswordResetAction.Action.OnClickSendButton)
            }
        )
    }
}

@Composable
@Preview(showBackground = true, showSystemUi = true)
fun PasswordResetLoadingScreen() {
    GenericLoadingScreen()
}

@Composable
fun PasswordResetSuccessScreen(innerPadding: PaddingValues, viewModel: PasswordResetViewModel) {
    Column(
        verticalArrangement = Arrangement.SpaceBetween,
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier
            .fillMaxSize()
            .padding(innerPadding)
            .verticalScroll(rememberScrollState())
    ) {
        Column(
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier.padding(top = Spacing.small)
        ) {
            Image(
                painter = painterResource(id = R.drawable.ic_coffee),
                contentDescription = stringResource(id = R.string.coffee_cup_icon_description)
            )
            Spacer(modifier = Modifier.padding(Spacing.medium))
            Text(
                text = stringResource(id = R.string.password_reset_success_description),
                style = Typography.bodyLarge.copy(color = ColorReference.quartz),
                textAlign = TextAlign.Center,
                modifier = Modifier.padding(horizontal = Spacing.larger)
            )
        }
        ActionButton(
            text = stringResource(id = R.string.ok_button_label),
            modifier = Modifier.padding(Spacing.small),
            onClick = {
                viewModel.sendAction(PasswordResetAction.Action.OnClickOkButton)
            }
        )
    }
}

@Composable
fun PasswordResetErrorScreen(viewModel: PasswordResetViewModel) {
    GenericErrorScreen(
        image = {
            Image(
                painter = painterResource(id = R.drawable.ic_drink),
                contentDescription = stringResource(id = R.string.drink_icon_description)
            )
        },
        text = {
            Text(
                text = stringResource(id = R.string.password_reset_error_description),
                style = Typography.bodyLarge.copy(color = ColorReference.quartz),
                textAlign = TextAlign.Center,
                modifier = Modifier.padding(horizontal = Spacing.larger)
            )
        },
        buttonText = stringResource(
            id = R.string.try_again_button_label
        ),
        onClickButton = {
            viewModel.sendAction(PasswordResetAction.Action.OnClickTryAgainButton)
        }
    )
}

@Composable
fun PasswordResetStringResource.toText(): String {
    return when (this) {
        PasswordResetStringResource.Empty -> {
            emptyString()
        }

        is PasswordResetStringResource.BlankEmail -> {
            stringResource(id = R.string.blank_email_message)
        }

        is PasswordResetStringResource.InvalidEmail -> {
            stringResource(id = R.string.invalid_email_message)
        }
    }
}
