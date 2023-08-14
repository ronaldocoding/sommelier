package br.com.sommelier.presentation.login.screen

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import br.com.sommelier.R
import br.com.sommelier.presentation.login.action.LoginAction
import br.com.sommelier.presentation.viewmodel.LoginViewModel
import br.com.sommelier.ui.component.ActionButton
import br.com.sommelier.ui.component.ClickableText
import br.com.sommelier.ui.component.OutlinedPasswordInput
import br.com.sommelier.ui.component.OutlinedTextInput
import br.com.sommelier.ui.theme.SommelierTheme
import br.com.sommelier.ui.theme.Spacing
import br.com.sommelier.ui.theme.Typography
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
            containerColor = Color.White
        ) {
            Column(
                modifier = Modifier
                    .padding(it)
                    .fillMaxSize()
                    .verticalScroll(rememberScrollState()),
                verticalArrangement = Arrangement.SpaceBetween,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
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
                                text = uiModel.emailUiState.errorSupportingMessage,
                                style = Typography.label
                            )
                        },
                        isError = uiModel.emailUiState.isError,
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email),
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
                                text = uiModel.passwordUiState.errorSupportingMessage,
                                style = Typography.label
                            )
                        },
                        isError = uiModel.passwordUiState.isError,
                        modifier = Modifier.padding(horizontal = Spacing.mediumLarge)
                    )
                }
                ActionButton(
                    text = stringResource(id = R.string.login_button_label),
                    modifier = Modifier.padding(horizontal = Spacing.small),
                    onClick = {
                        viewModel.sendAction(LoginAction.Action.OnClickLoginButton)
                    }
                )
                Column(
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.CenterHorizontally,
                    modifier = Modifier.padding(bottom = Spacing.mediumLarge)
                ) {
                    ClickableText(
                        nonClickableText = stringResource(id = R.string.login_non_clickable_text),
                        clickableText = stringResource(id = R.string.login_first_clickable_text),
                    )
                    ClickableText(
                        clickableText = stringResource(id = R.string.login_second_clickable_text)
                    )
                }
            }
        }
    }
}