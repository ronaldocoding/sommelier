package br.com.sommelier.presentation.confirmemail.screen

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import br.com.sommelier.R
import br.com.sommelier.presentation.confirmemail.action.ConfirmEmailAction
import br.com.sommelier.presentation.confirmemail.state.ConfirmEmailUiEffect
import br.com.sommelier.presentation.confirmemail.state.ConfirmEmailUiState
import br.com.sommelier.presentation.confirmemail.viewmodel.ConfirmEmailViewModel
import br.com.sommelier.ui.component.ActionButton
import br.com.sommelier.ui.component.SommelierTopBar
import br.com.sommelier.ui.component.SommelierTopBarButton
import br.com.sommelier.ui.theme.ColorReference
import br.com.sommelier.ui.theme.SommelierTheme
import br.com.sommelier.ui.theme.Spacing
import br.com.sommelier.ui.theme.Typography
import org.koin.androidx.compose.getViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ConfirmEmailScreen(popBackStack: () -> Unit) {
    val viewModel = getViewModel<ConfirmEmailViewModel>()
    val state = checkNotNull(viewModel.uiState.value)

    SommelierTheme {
        Scaffold(
            containerColor = ColorReference.white,
            topBar = {
                SommelierTopBar(
                    title = stringResource(id = R.string.confirm_email_top_bar_title),
                    leftButton = SommelierTopBarButton.Enabled(
                        icon = Icons.Default.ArrowBack,
                        contentDescription = stringResource(
                            id = R.string.arrow_back_icon_description
                        ),
                        onClick = {
                            viewModel.sendAction(
                                ConfirmEmailAction.Action.ClickBackButton
                            )
                        }
                    )
                )
            }
        ) { innerPadding ->
            UiState(state, innerPadding, viewModel)
            UiEffect(viewModel, popBackStack)
        }
    }
}

@Composable
private fun UiState(
    state: ConfirmEmailUiState,
    innerPadding: PaddingValues,
    viewModel: ConfirmEmailViewModel
) {
    when (state) {
        is ConfirmEmailUiState.Initial -> {
            ConfirmEmailInitialScreen(innerPadding, viewModel)
        }
    }
}

@Composable
private fun ConfirmEmailInitialScreen(
    innerPadding: PaddingValues,
    viewModel: ConfirmEmailViewModel
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.SpaceBetween,
        modifier = Modifier
            .fillMaxSize()
            .padding(innerPadding)
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Spacer(modifier = Modifier.padding(Spacing.medium))
            Image(
                painter = painterResource(id = R.drawable.ic_hamburger),
                contentDescription = stringResource(
                    id = R.string.hamburger_icon_description
                )
            )
            Spacer(modifier = Modifier.padding(Spacing.medium))
            Text(
                text = stringResource(id = R.string.confirm_email_description),
                style = Typography.bodyLarge.copy(color = ColorReference.quartz),
                textAlign = TextAlign.Center,
                modifier = Modifier.padding(horizontal = Spacing.larger)
            )
        }
        ActionButton(
            text = stringResource(id = R.string.ok_button_label),
            modifier = Modifier.padding(
                horizontal = Spacing.small,
                vertical = Spacing.small
            ),
            onClick = {
                viewModel.sendAction(
                    ConfirmEmailAction.Action.ClickOkButton
                )
            }
        )
    }
}

@Composable
fun UiEffect(viewModel: ConfirmEmailViewModel, popBackStack: () -> Unit = {}) {
    val lifecycleOwner = LocalLifecycleOwner.current
    viewModel.uiEffect.observe(lifecycleOwner) { uiEffect ->
        when (uiEffect) {
            is ConfirmEmailUiEffect.PopBackStack -> {
                popBackStack()
            }
        }
    }
}
