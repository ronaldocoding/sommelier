package br.com.sommelier.presentation

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.tooling.preview.Preview
import br.com.sommelier.R
import br.com.sommelier.ui.component.ActionButton
import br.com.sommelier.ui.component.ClickableText
import br.com.sommelier.ui.component.OutlinedPasswordInput
import br.com.sommelier.ui.component.OutlinedTextInput
import br.com.sommelier.ui.component.QuickActionButton
import br.com.sommelier.ui.component.SommelierDialog
import br.com.sommelier.ui.component.SommelierSnackbar
import br.com.sommelier.ui.component.SommelierSnackbarType
import br.com.sommelier.ui.component.SommelierTopBar
import br.com.sommelier.ui.component.SommelierTopBarButton
import br.com.sommelier.ui.component.TextView
import br.com.sommelier.ui.theme.ColorReference
import br.com.sommelier.ui.theme.SommelierTheme
import br.com.sommelier.ui.theme.Spacing
import br.com.sommelier.ui.theme.Typography
import br.com.sommelier.util.emptyString
import kotlinx.coroutines.launch

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            SommelierTheme {
                Content()
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun Content() {
    val snackbarHostState = remember { SnackbarHostState() }
    val scope = rememberCoroutineScope()
    var isSnackbarSuccess by rememberSaveable { mutableStateOf(true) }
    var snackbarText by rememberSaveable {
        mutableStateOf("Action successfully completed")
    }
    var showDialog by rememberSaveable { mutableStateOf(false) }
    SommelierTheme {
        Scaffold(
            topBar = {
                SommelierTopBar(
                    leftButton = SommelierTopBarButton.Enabled(
                        icon = Icons.Default.ArrowBack,
                        contentDescription = "Back",
                        onClick = {
                            showDialog = true
                        }
                    ),
                    rightButton = SommelierTopBarButton.Enabled(
                        icon = ImageVector.vectorResource(id = R.drawable.ic_edit),
                        contentDescription = "Edit"
                    )
                )
            },
            snackbarHost = {
                SnackbarHost(snackbarHostState) {
                    SommelierSnackbar(
                        modifier = Modifier.padding(Spacing.mediumLarge),
                        text = snackbarText,
                        type = if (isSnackbarSuccess) {
                            SommelierSnackbarType.Success
                        } else {
                            SommelierSnackbarType.Error
                        }
                    )
                }
            },
            floatingActionButton = {
                QuickActionButton(
                    onClick = {
                        scope.launch {
                            snackbarHostState.showSnackbar(snackbarText)
                        }
                        if (isSnackbarSuccess) {
                            isSnackbarSuccess = false
                            snackbarText = "This action could not be done. Please, try again."
                        } else {
                            isSnackbarSuccess = true
                            snackbarText = "Action successfully completed"
                        }
                    }
                )
            },
            containerColor = ColorReference.white
        ) {
            Column(
                modifier = Modifier
                    .padding(it)
                    .fillMaxHeight(),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                if (showDialog) {
                    SommelierDialog(
                        title = "Exit app",
                        text = "Are you sure you want to exit the app? This action cannot be undone.",
                        confirmButtonText = "Yes",
                        dismissButtonText = "No",
                        onConfirmButtonClicked = {
                            showDialog = false
                        },
                        onDismissButtonClicked = {
                            showDialog = false
                        },
                        onDismissRequest = {
                            showDialog = false
                        }
                    )
                }
                TextView(
                    modifier = Modifier.padding(horizontal = Spacing.mediumLarge),
                    value = "Ronaldo Costa de Freitas",
                    label = "Name",
                    leadingIcon = ImageVector.vectorResource(id = R.drawable.ic_user)
                )
                Spacer(modifier = Modifier.padding(Spacing.small))
                TextView(
                    modifier = Modifier.padding(horizontal = Spacing.mediumLarge),
                    value = "ronaldocosta.developer@gmail.com",
                    label = "Email",
                    leadingIcon = ImageVector.vectorResource(id = R.drawable.ic_mail)
                )
                Spacer(modifier = Modifier.padding(Spacing.small))

                var text by rememberSaveable { mutableStateOf(emptyString()) }
                var textInputErrorMessage by rememberSaveable { mutableStateOf(emptyString()) }
                var textInputIsError by rememberSaveable { mutableStateOf(false) }
                var textInputIsEnabled by rememberSaveable { mutableStateOf(true) }

                OutlinedTextInput(
                    modifier = Modifier.padding(horizontal = Spacing.mediumLarge),
                    value = text,
                    onValueChange = { changedValue ->
                        text = changedValue
                    },
                    placeholder = "Type your email",
                    label = "Email",
                    leadingIcon = ImageVector.vectorResource(id = R.drawable.ic_mail),
                    isError = textInputIsError,
                    supportingText = {
                        Text(text = textInputErrorMessage, style = Typography.label)
                    },
                    isEnabled = textInputIsEnabled
                )
                var password by rememberSaveable { mutableStateOf(emptyString()) }
                OutlinedPasswordInput(
                    modifier = Modifier.padding(horizontal = Spacing.mediumLarge),
                    value = password,
                    onValueChange = { changedValue ->
                        password = changedValue
                    },
                    placeholder = "Type your password",
                    label = "Password",
                    leadingIconContentDescription = "Password"
                )
                Spacer(modifier = Modifier.padding(Spacing.small))
                ActionButton(
                    text = "Login",
                    modifier = Modifier.padding(horizontal = Spacing.small),
                    onClick = {
                        if (text.isEmpty()) {
                            textInputErrorMessage = "Please, type your name"
                            textInputIsError = true
                            textInputIsEnabled = true
                        } else {
                            textInputErrorMessage = emptyString()
                            textInputIsError = false
                            textInputIsEnabled = false
                        }
                    }
                )
                Spacer(modifier = Modifier.padding(Spacing.small))
                ClickableText(
                    nonClickableText = "Don't have an account? ",
                    clickableText = "Sign up"
                )
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun SommelierPreview() {
    SommelierTheme {
        Surface(
            modifier = Modifier.fillMaxSize(),
            color = ColorReference.white
        ) {
            Content()
        }
    }
}
