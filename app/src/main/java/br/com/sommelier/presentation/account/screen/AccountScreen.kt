package br.com.sommelier.presentation.account.screen

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.CircularProgressIndicator
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
import androidx.compose.ui.tooling.preview.Preview
import br.com.sommelier.R
import br.com.sommelier.presentation.account.action.AccountAction
import br.com.sommelier.presentation.account.model.AccountUiModel
import br.com.sommelier.presentation.account.res.AccountStringResource
import br.com.sommelier.presentation.account.state.AccountLoadingCause
import br.com.sommelier.presentation.account.state.AccountUiEffect
import br.com.sommelier.presentation.account.state.AccountUiState
import br.com.sommelier.presentation.account.viewmodel.AccountViewModel
import br.com.sommelier.ui.component.ActionButton
import br.com.sommelier.ui.component.SommelierDialog
import br.com.sommelier.ui.component.SommelierSnackbar
import br.com.sommelier.ui.component.SommelierTopBar
import br.com.sommelier.ui.component.SommelierTopBarButton
import br.com.sommelier.ui.component.TextView
import br.com.sommelier.ui.theme.ColorReference
import br.com.sommelier.ui.theme.SommelierTheme
import br.com.sommelier.ui.theme.Spacing
import br.com.sommelier.ui.theme.Typography
import br.com.sommelier.util.emptyString
import kotlinx.coroutines.launch
import org.koin.androidx.compose.getViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AccountScreen(
    navigateToEditAccountScreen: () -> Unit,
    navigateToLoginScreen: () -> Unit,
    navigateToPasswordResetScreen: () -> Unit,
    popBackStack: () -> Unit
) {
    val viewModel = getViewModel<AccountViewModel>()
    val uiState = checkNotNull(viewModel.uiState.observeAsState().value)
    val uiModel = checkNotNull(uiState.uiModel)

    SommelierTheme {
        Scaffold(
            containerColor = ColorReference.white,
            topBar = if (uiModel.isLoading.not()) {
                { AccountTopBar(viewModel) }
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
            UiState(innerPadding, viewModel, uiState, uiModel)
            AccountDialog(viewModel, uiModel)
            UiEffect(
                viewModel,
                uiModel,
                navigateToEditAccountScreen,
                navigateToLoginScreen,
                navigateToPasswordResetScreen,
                popBackStack
            )
            viewModel.sendAction(AccountAction.Action.OnTryToFetchAccountData)
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun AccountTopBar(viewModel: AccountViewModel) {
    SommelierTopBar(
        title = stringResource(id = R.string.account_top_bar_title),
        leftButton = SommelierTopBarButton.Enabled(
            icon = Icons.Default.ArrowBack,
            contentDescription = stringResource(id = R.string.arrow_back_icon_description),
            onClick = {
                viewModel.sendAction(AccountAction.Action.OnClickBackButton)
            }
        ),
        rightButton = SommelierTopBarButton.Enabled(
            icon = ImageVector.vectorResource(id = R.drawable.ic_edit),
            contentDescription = stringResource(id = R.string.edit_icon_description),
            onClick = {
                viewModel.sendAction(AccountAction.Action.OnClickEditButton)
            }
        )
    )
}

@Composable
private fun UiState(
    innerPadding: PaddingValues,
    viewModel: AccountViewModel,
    uiState: AccountUiState,
    uiModel: AccountUiModel
) {
    when (uiState) {
        is AccountUiState.Initial, is AccountUiState.Resume -> {
            AccountInitialAndResumeScreen(innerPadding, uiModel, viewModel)
        }

        is AccountUiState.Error -> {
            AccountErrorScreen(viewModel)
        }

        is AccountUiState.Loading -> {
            AccountLoadingScreen()
        }
    }
}

@Composable
private fun AccountDialog(viewModel: AccountViewModel, uiModel: AccountUiModel) {
    if (uiModel.isPasswordResetDialogVisible) {
        SommelierDialog(
            title = stringResource(id = R.string.password_reset_dialog_title),
            text = stringResource(id = R.string.password_reset_dialog_description),
            confirmButtonText = stringResource(id = R.string.confirm_dialog_button_label),
            dismissButtonText = stringResource(id = R.string.cancel_dialog_button_label),
            onDismissRequest = {
                viewModel.sendAction(AccountAction.Action.OnDismissPasswordResetDialog)
            },
            onConfirmButtonClicked = {
                viewModel.sendAction(AccountAction.Action.OnClickPasswordResetConfirmationButton)
            },
            onDismissButtonClicked = {
                viewModel.sendAction(AccountAction.Action.OnDismissPasswordResetDialog)
            }
        )
    }
    if (uiModel.isDeleteAccountDialogVisible) {
        SommelierDialog(
            title = stringResource(id = R.string.delete_account_dialog_title),
            text = stringResource(id = R.string.delete_account_dialog_description),
            confirmButtonText = stringResource(id = R.string.confirm_dialog_button_label),
            dismissButtonText = stringResource(id = R.string.cancel_dialog_button_label),
            onDismissRequest = {
                viewModel.sendAction(AccountAction.Action.OnDismissDeleteAccountDialog)
            },
            onConfirmButtonClicked = {
                viewModel.sendAction(AccountAction.Action.OnClickDeleteAccountConfirmationButton)
            },
            onDismissButtonClicked = {
                viewModel.sendAction(AccountAction.Action.OnDismissDeleteAccountDialog)
            }
        )
    }
    if (uiModel.isLogoutDialogVisible) {
        SommelierDialog(
            title = stringResource(id = R.string.logout_dialog_title),
            text = stringResource(id = R.string.logout_dialog_description),
            confirmButtonText = stringResource(id = R.string.confirm_dialog_button_label),
            dismissButtonText = stringResource(id = R.string.cancel_dialog_button_label),
            onDismissRequest = {
                viewModel.sendAction(AccountAction.Action.OnDismissLogoutDialog)
            },
            onConfirmButtonClicked = {
                viewModel.sendAction(AccountAction.Action.OnClickLogoutConfirmationButton)
            },
            onDismissButtonClicked = {
                viewModel.sendAction(AccountAction.Action.OnDismissLogoutDialog)
            }
        )
    }
}

@Composable
private fun AccountInitialAndResumeScreen(
    innerPadding: PaddingValues,
    uiModel: AccountUiModel,
    viewModel: AccountViewModel
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
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(modifier = Modifier.padding(Spacing.small))
            TextView(
                value = uiModel.name,
                label = stringResource(id = R.string.name_text_field_label),
                leadingIcon = ImageVector.vectorResource(id = R.drawable.ic_user),
                leadingIconContentDescription = stringResource(id = R.string.user_icon_description),
                modifier = Modifier.padding(horizontal = Spacing.mediumLarge)
            )
            Spacer(modifier = Modifier.padding(Spacing.small))
            TextView(
                value = uiModel.email,
                label = stringResource(id = R.string.email_text_field_label),
                leadingIcon = ImageVector.vectorResource(id = R.drawable.ic_mail),
                leadingIconContentDescription = stringResource(
                    id = R.string.email_icon_description
                ),
                modifier = Modifier.padding(horizontal = Spacing.mediumLarge)
            )
        }
        Column(
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            ActionButton(
                text = stringResource(id = R.string.password_reset_button_label),
                onClick = {
                    viewModel.sendAction(AccountAction.Action.OnClickPasswordResetButton)
                },
                modifier = Modifier.padding(horizontal = Spacing.small)
            )
            Spacer(modifier = Modifier.padding(Spacing.smaller))
            ActionButton(
                text = stringResource(id = R.string.delete_account_button_label),
                backGroundColor = ColorReference.frenchFuchsia,
                onClick = {
                    viewModel.sendAction(AccountAction.Action.OnClickDeleteAccountButton)
                },
                modifier = Modifier.padding(horizontal = Spacing.small)
            )
            Spacer(modifier = Modifier.padding(Spacing.smaller))
            ActionButton(
                text = stringResource(id = R.string.logout_button_label),
                textColor = ColorReference.royalPurple,
                backGroundColor = ColorReference.white,
                isOutlined = true,
                onClick = {
                    viewModel.sendAction(AccountAction.Action.OnClickLogoutButton)
                },
                modifier = Modifier.padding(horizontal = Spacing.small)
            )
            Spacer(modifier = Modifier.padding(Spacing.small))
        }
    }
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
private fun AccountLoadingScreen() {
    Box(
        contentAlignment = Alignment.Center,
        modifier = Modifier.fillMaxSize()
    ) {
        CircularProgressIndicator(
            color = ColorReference.royalPurple
        )
    }
}

@Composable
private fun AccountErrorScreen(viewModel: AccountViewModel) {
    Column(
        verticalArrangement = Arrangement.SpaceBetween,
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier
            .fillMaxSize()
            .padding(Spacing.none)
            .verticalScroll(rememberScrollState())
    ) {
        Column(
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(modifier = Modifier.padding(Spacing.extraLarge))
            Image(
                painter = painterResource(id = R.drawable.ic_drink),
                contentDescription = stringResource(id = R.string.drink_icon_description)
            )
            Spacer(modifier = Modifier.padding(Spacing.medium))
            Text(
                text = stringResource(id = R.string.account_error_message),
                style = Typography.bodyLarge.copy(color = ColorReference.quartz),
                textAlign = TextAlign.Center,
                modifier = Modifier.padding(horizontal = Spacing.larger)
            )
        }
        ActionButton(
            text = stringResource(id = R.string.try_again_button_label),
            modifier = Modifier.padding(horizontal = Spacing.small, vertical = Spacing.small),
            onClick = {
                viewModel.sendAction(AccountAction.Action.OnClickTryToFetchAccountDataAgainButton)
            }
        )
    }
}

@Composable
private fun UiEffect(
    viewModel: AccountViewModel,
    uiModel: AccountUiModel,
    navigateToEditAccountScreen: () -> Unit = {},
    navigateToLoginScreen: () -> Unit = {},
    navigateToPasswordResetScreen: () -> Unit = {},
    popBackStack: () -> Unit = {}
) {
    val lifecycleOwner = LocalLifecycleOwner.current
    val coroutineScope = rememberCoroutineScope()
    val snackbarMessage = uiModel.snackbarUiState.message.toText()
    viewModel.uiEffect.observe(lifecycleOwner) { uiEffect ->
        when (uiEffect) {
            is AccountUiEffect.OpenEditAccountScreen -> {
                navigateToEditAccountScreen()
            }

            is AccountUiEffect.OpenLoginScreen -> {
                navigateToLoginScreen()
            }

            is AccountUiEffect.OpenPasswordResetScreen -> {
                navigateToPasswordResetScreen()
            }

            is AccountUiEffect.PopBackStack -> {
                popBackStack()
            }

            is AccountUiEffect.ShowLoading -> {
                handleLoadingCause(uiEffect, viewModel)
            }

            is AccountUiEffect.ShowSnackbarError -> {
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
fun AccountStringResource.toText(): String {
    return when (this) {
        is AccountStringResource.Empty -> {
            emptyString()
        }

        is AccountStringResource.ErrorPasswordReset -> {
            stringResource(id = R.string.password_reset_snackbar_error_message)
        }

        is AccountStringResource.ErrorDeleteAccount -> {
            stringResource(id = R.string.delete_account_snackbar_error_message)
        }

        is AccountStringResource.ErrorLogout -> {
            stringResource(id = R.string.logout_snackbar_error_message)
        }
    }
}

private fun handleLoadingCause(
    uiEffect: AccountUiEffect.ShowLoading,
    viewModel: AccountViewModel
) {
    when (uiEffect.loadingCause) {
        is AccountLoadingCause.DeleteAccount -> {
            viewModel.sendAction(AccountAction.Action.OnTryToDeleteAccount)
        }

        is AccountLoadingCause.PasswordReset -> {
            viewModel.sendAction(AccountAction.Action.OnTryToPasswordReset)
        }

        is AccountLoadingCause.FetchAccountData -> {
            viewModel.sendAction(AccountAction.Action.OnTryToFetchAccountData)
        }

        is AccountLoadingCause.Logout -> {
            viewModel.sendAction(AccountAction.Action.OnTryToLogout)
        }
    }
}
