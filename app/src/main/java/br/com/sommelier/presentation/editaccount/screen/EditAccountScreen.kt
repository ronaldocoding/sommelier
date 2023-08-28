package br.com.sommelier.presentation.editaccount.screen

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.style.TextAlign
import br.com.sommelier.R
import br.com.sommelier.presentation.editaccount.action.EditAccountAction
import br.com.sommelier.presentation.editaccount.model.EditAccountUiModel
import br.com.sommelier.presentation.editaccount.res.EditAccountStringResource
import br.com.sommelier.presentation.editaccount.state.EditAccountLoadingCause
import br.com.sommelier.presentation.editaccount.state.EditAccountUiEffect
import br.com.sommelier.presentation.editaccount.state.EditAccountUiState
import br.com.sommelier.presentation.editaccount.viewmodel.EditAccountViewModel
import br.com.sommelier.shared.screen.GenericErrorScreen
import br.com.sommelier.shared.screen.GenericLoadingScreen
import br.com.sommelier.ui.component.ActionButton
import br.com.sommelier.ui.component.OutlinedTextInput
import br.com.sommelier.ui.component.SommelierSnackbar
import br.com.sommelier.ui.component.SommelierTopBar
import br.com.sommelier.ui.component.SommelierTopBarButton
import br.com.sommelier.ui.theme.ColorReference
import br.com.sommelier.ui.theme.SommelierTheme
import br.com.sommelier.ui.theme.Spacing
import br.com.sommelier.ui.theme.Typography
import br.com.sommelier.util.emptyString
import kotlinx.coroutines.launch
import org.koin.androidx.compose.getViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EditAccountScreen(popBackStack: () -> Unit) {
    val viewModel = getViewModel<EditAccountViewModel>()
    val uiState = checkNotNull(viewModel.uiState.observeAsState())
    val uiModel = checkNotNull(uiState.value?.uiModel)

    SommelierTheme {
        Scaffold(
            containerColor = ColorReference.white,
            topBar = if (uiModel.isLoading.not()) {
                { EditAccountTopBar(viewModel) }
            } else {
                {}
            },
            snackbarHost = {
                SnackbarHost(uiModel.snackbarUiState.hostState) {
                    SommelierSnackbar(
                        modifier = Modifier.padding(
                            start = Spacing.mediumLarge,
                            end = Spacing.mediumLarge,
                            bottom = Spacing.larger
                        ),
                        text = uiModel.snackbarUiState.message.toText(),
                        type = uiModel.snackbarUiState.type
                    )
                }
            }
        ) { innerPadding ->
            UiState(checkNotNull(uiState.value), viewModel, innerPadding, uiModel)
            UiEffect(viewModel, uiModel, popBackStack)
        }
    }
}

@Composable
private fun UiState(
    uiState: EditAccountUiState,
    viewModel: EditAccountViewModel,
    innerPadding: PaddingValues,
    uiModel: EditAccountUiModel
) {
    when (uiState) {
        is EditAccountUiState.Initial -> {
            viewModel.sendAction(EditAccountAction.Action.OnInitial)
        }

        is EditAccountUiState.Loading -> {
            EditAccountLoadingScreen()
        }

        is EditAccountUiState.Error -> {
            EditAccountErrorScreen(viewModel)
        }

        is EditAccountUiState.Resume -> {
            EditAccountResumeScreen(
                innerPadding = innerPadding,
                viewModel = viewModel,
                uiModel = uiModel
            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun EditAccountTopBar(viewModel: EditAccountViewModel) {
    SommelierTopBar(
        title = stringResource(id = R.string.edit_account_top_bar_title),
        leftButton = SommelierTopBarButton.Enabled(
            icon = Icons.Default.ArrowBack,
            contentDescription = stringResource(
                id = R.string.arrow_back_icon_description
            ),
            onClick = {
                viewModel.sendAction(EditAccountAction.Action.OnClickBackButton)
            }
        )
    )
}

@Composable
private fun EditAccountResumeScreen(
    innerPadding: PaddingValues,
    viewModel: EditAccountViewModel,
    uiModel: EditAccountUiModel
) {
    Column(
        verticalArrangement = Arrangement.SpaceBetween,
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier
            .fillMaxSize()
            .padding(innerPadding)
    ) {
        OutlinedTextInput(
            leadingIcon = ImageVector.vectorResource(id = R.drawable.ic_user),
            value = uiModel.editNameFieldUiState.name,
            onValueChange = { changedValue ->
                viewModel.sendAction(EditAccountAction.Action.OnTypeNameField(changedValue))
            },
            label = stringResource(id = R.string.name_text_field_label),
            placeholder = uiModel.editNameFieldUiState.placeholder,
            isError = uiModel.editNameFieldUiState.isError,
            supportingText = {
                Text(
                    text = uiModel.editNameFieldUiState.errorSupportingMessage.toText(),
                    style = Typography.label
                )
            },
            modifier = Modifier.padding(
                horizontal = Spacing.mediumLarge,
                vertical = Spacing.extraLarge
            )
        )
        ActionButton(
            text = stringResource(id = R.string.save_button_label),
            onClick = {
                viewModel.sendAction(EditAccountAction.Action.OnClickSaveButton)
            },
            modifier = Modifier.padding(
                horizontal = Spacing.small,
                vertical = Spacing.small
            )
        )
    }
}

@Composable
fun EditAccountLoadingScreen() {
    GenericLoadingScreen()
}

@Composable
fun EditAccountErrorScreen(viewModel: EditAccountViewModel) {
    GenericErrorScreen(
        image = {
            Image(
                painter = painterResource(id = R.drawable.ic_drink),
                contentDescription = stringResource(id = R.string.drink_icon_description)
            )
        },
        text = {
            Text(
                text = stringResource(id = R.string.account_error_message),
                style = Typography.bodyLarge.copy(color = ColorReference.quartz),
                textAlign = TextAlign.Center,
                modifier = Modifier.padding(horizontal = Spacing.larger)
            )
        },
        buttonText = stringResource(
            id = R.string.try_again_button_label
        ),
        onClickButton = {
            viewModel.sendAction(
                EditAccountAction.Action.OnClickTryToFetchAccountDataAgainButton
            )
        }
    )
}

@Composable
fun UiEffect(
    viewModel: EditAccountViewModel,
    uiModel: EditAccountUiModel,
    popBackStack: () -> Unit = {}
) {
    val lifecycleOwner = LocalLifecycleOwner.current
    val coroutineScope = rememberCoroutineScope()
    val snackbarMessage = uiModel.snackbarUiState.message.toText()
    viewModel.uiEffect.observe(lifecycleOwner) { uiEffect ->
        when (uiEffect) {
            is EditAccountUiEffect.PopBackStack -> {
                popBackStack()
            }

            is EditAccountUiEffect.ShowLoading -> {
                handleLoadingCause(viewModel, uiEffect.loadingCause)
            }

            is EditAccountUiEffect.ShowSnackbarSuccess,
            is EditAccountUiEffect.ShowSnackbarError -> {
                coroutineScope.launch {
                    uiModel.snackbarUiState.hostState.showSnackbar(
                        message = snackbarMessage
                    )
                }
            }
        }
    }
}

@Composable
fun EditAccountStringResource.toText(): String {
    return when (this) {
        is EditAccountStringResource.Empty -> {
            emptyString()
        }

        is EditAccountStringResource.BlankName -> {
            stringResource(id = R.string.blank_name_message)
        }

        is EditAccountStringResource.InvalidName -> {
            stringResource(id = R.string.invalid_name_message)
        }

        is EditAccountStringResource.ErrorSnackbar -> {
            stringResource(id = R.string.edit_account_snackbar_error_message)
        }

        is EditAccountStringResource.SuccessSnackbar -> {
            stringResource(id = R.string.edit_account_success_snackbar_message)
        }
    }
}

private fun handleLoadingCause(
    viewModel: EditAccountViewModel,
    loadingCause: EditAccountLoadingCause
) {
    when (loadingCause) {
        is EditAccountLoadingCause.FetchAccountData -> {
            viewModel.sendAction(EditAccountAction.Action.OnFetchAccountData)
        }

        is EditAccountLoadingCause.SaveAccountData -> {
            viewModel.sendAction(EditAccountAction.Action.OnTryToSave)
        }
    }
}
