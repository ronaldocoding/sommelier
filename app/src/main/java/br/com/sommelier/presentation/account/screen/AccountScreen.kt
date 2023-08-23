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
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.livedata.observeAsState
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
import br.com.sommelier.presentation.account.state.AccountDialogType
import br.com.sommelier.presentation.account.state.AccountLoadingCause
import br.com.sommelier.presentation.account.state.AccountSnackbarErrorCause
import br.com.sommelier.presentation.account.state.AccountUiEffect
import br.com.sommelier.presentation.account.state.AccountUiState
import br.com.sommelier.presentation.account.viewmodel.AccountViewModel
import br.com.sommelier.ui.component.ActionButton
import br.com.sommelier.ui.component.SommelierTopBar
import br.com.sommelier.ui.component.SommelierTopBarButton
import br.com.sommelier.ui.component.TextView
import br.com.sommelier.ui.theme.ColorReference
import br.com.sommelier.ui.theme.SommelierTheme
import br.com.sommelier.ui.theme.Spacing
import br.com.sommelier.ui.theme.Typography
import org.koin.androidx.compose.getViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Preview(showBackground = true, showSystemUi = true)
@Composable
fun AccountScreen() {
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
            }
        ) { innerPadding ->
            UiState(innerPadding, viewModel, uiState, uiModel)
            UiEffect(viewModel)
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
    Column(
        verticalArrangement = Arrangement.SpaceBetween,
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier
            .fillMaxSize()
            .padding(innerPadding)
    ) {
        Column(
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(modifier = Modifier.padding(Spacing.small))
            TextView(
                value = "Ronaldo Costa",
                label = "Name",
                leadingIcon = ImageVector.vectorResource(id = R.drawable.ic_user),
                leadingIconContentDescription = "User icon",
                modifier = Modifier.padding(horizontal = Spacing.mediumLarge)
            )
            Spacer(modifier = Modifier.padding(Spacing.small))
            TextView(
                value = "ronaldo@email.com",
                label = "Email",
                leadingIcon = ImageVector.vectorResource(id = R.drawable.ic_mail),
                leadingIconContentDescription = "Email icon",
                modifier = Modifier.padding(horizontal = Spacing.mediumLarge)
            )
        }
        Column(
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            ActionButton(
                text = "Password reset",
                onClick = { /*TODO*/ },
                modifier = Modifier.padding(horizontal = Spacing.small)
            )
            Spacer(modifier = Modifier.padding(Spacing.smaller))
            ActionButton(
                text = "Delete account",
                backGroundColor = ColorReference.frenchFuchsia,
                onClick = { /*TODO*/ },
                modifier = Modifier.padding(horizontal = Spacing.small)
            )
            Spacer(modifier = Modifier.padding(Spacing.smaller))
            ActionButton(
                text = "Logout",
                textColor = ColorReference.royalPurple,
                backGroundColor = ColorReference.white,
                isOutlined = true,
                onClick = { /*TODO*/ },
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

@Preview(showBackground = true, showSystemUi = true)
@Composable
private fun AccountErrorScreen() {
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
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier.padding(top = Spacing.small)
        ) {
            Image(
                painter = painterResource(id = R.drawable.ic_drink),
                contentDescription = stringResource(id = R.string.drink_icon_description)
            )
            Spacer(modifier = Modifier.padding(Spacing.medium))
            Text(
                text = "Something went wrong",
                style = Typography.bodyLarge.copy(color = ColorReference.quartz),
                textAlign = TextAlign.Center,
                modifier = Modifier.padding(horizontal = Spacing.larger)
            )
        }
        ActionButton(
            text = stringResource(id = R.string.try_again_button_label),
            modifier = Modifier.padding(horizontal = Spacing.small, vertical = Spacing.small),
            onClick = {
                //viewModel.sendAction(AccountAction.Action.OnClickTryToFetchAccountDataAgainButton)
            }
        )
    }
}

@Composable
private fun UiEffect(viewModel: AccountViewModel) {
    val lifecycleOwner = LocalLifecycleOwner.current
    viewModel.uiEffect.observe(lifecycleOwner) { uiEffect ->
        when (uiEffect) {
            is AccountUiEffect.OpenEditAccountScreen -> {
                // TODO: Navigate to EditAccountScreen
            }

            is AccountUiEffect.OpenLoginScreen -> {
                // TODO: Navigate to LoginScreen
            }

            is AccountUiEffect.OpenResetPasswordScreen -> {
                // TODO: Navigate to ResetPasswordScreen
            }

            is AccountUiEffect.PopBackStack -> {
                // TODO: Pop back stack
            }

            is AccountUiEffect.ShowDialog -> {
                when(uiEffect.dialogType) {
                    AccountDialogType.DeleteAccountConfirmation -> TODO()
                    AccountDialogType.LogoutConfirmation -> TODO()
                    AccountDialogType.PasswordResetConfirmation -> TODO()
                }
            }

            is AccountUiEffect.ShowLoading -> {
                when (uiEffect.loadingCause) {
                    AccountLoadingCause.DeleteAccount -> TODO()
                    AccountLoadingCause.PasswordReset -> TODO()
                    AccountLoadingCause.FetchAccountData -> TODO()
                    AccountLoadingCause.Logout -> TODO()
                }
            }

            is AccountUiEffect.ShowSnackbarError -> {
                when (uiEffect.errorCause) {
                    AccountSnackbarErrorCause.DeleteAccount -> TODO()
                    AccountSnackbarErrorCause.PasswordReset -> TODO()
                    AccountSnackbarErrorCause.Logout -> TODO()
                }
            }
        }
    }
}
