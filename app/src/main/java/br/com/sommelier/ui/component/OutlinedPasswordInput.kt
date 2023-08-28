package br.com.sommelier.ui.component

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import br.com.sommelier.R
import br.com.sommelier.ui.theme.ColorReference
import br.com.sommelier.ui.theme.Sizing
import br.com.sommelier.ui.theme.Spacing
import br.com.sommelier.ui.theme.Typography
import br.com.sommelier.util.emptyString

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun OutlinedPasswordInput(
    modifier: Modifier = Modifier,
    value: String = emptyString(),
    onValueChange: (String) -> Unit = {},
    label: String = emptyString(),
    placeholder: String = emptyString(),
    isError: Boolean = false,
    supportingText: (@Composable () -> Unit)? = null
) {
    var isTextFieldFocused by rememberSaveable { mutableStateOf(false) }
    var isPasswordVisible by rememberSaveable { mutableStateOf(false) }
    var passwordEye by rememberSaveable { mutableStateOf(R.drawable.ic_opened_eye) }

    val lockIconColor = if (isError) {
        ColorReference.bitterSweet
    } else {
        ColorReference.taupeGray
    }
    val eyeIconColor = if (isError) {
        ColorReference.bitterSweet
    } else {
        ColorReference.taupeGray
    }
    val containerColor = if (isError) {
        ColorReference.seaShell
    } else {
        ColorReference.antiFlashWhite
    }
    val textColor = if (isError) {
        ColorReference.bitterSweet
    } else {
        ColorReference.eerieBlack
    }
    val placeholderColor = if (isError) {
        ColorReference.bitterSweet
    } else {
        ColorReference.taupeGray
    }
    val labelColor = if (isError) {
        ColorReference.bitterSweet
    } else {
        ColorReference.taupeGray
    }

    Column(
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.Start,
        modifier = modifier
    ) {
        Text(
            text = label,
            style = Typography.bodyLarge,
            color = labelColor,
            modifier = Modifier.testTag("OutlinedPasswordInputLabel")
        )
        Spacer(modifier = Modifier.padding(Spacing.smallest))
        OutlinedTextField(
            visualTransformation = if (isPasswordVisible) {
                VisualTransformation.None
            } else {
                PasswordVisualTransformation()
            },
            value = value,
            onValueChange = onValueChange,
            modifier = Modifier
                .fillMaxWidth()
                .onFocusChanged {
                    isTextFieldFocused = it.isFocused
                }
                .testTag("OutlinedPasswordInput"),
            textStyle = Typography.bodyLarge.copy(color = textColor),
            enabled = true,
            isError = isError,
            singleLine = true,
            supportingText = supportingText,
            colors = TextFieldDefaults.outlinedTextFieldColors(
                focusedBorderColor = ColorReference.antiFlashWhite,
                unfocusedBorderColor = ColorReference.antiFlashWhite,
                containerColor = containerColor,
                errorBorderColor = ColorReference.seaShell,
                errorCursorColor = ColorReference.bitterSweet,
                errorSupportingTextColor = ColorReference.bitterSweet,
                errorLeadingIconColor = ColorReference.bitterSweet
            ),
            shape = RoundedCornerShape(Sizing.normal),
            placeholder = {
                Text(
                    text = placeholder,
                    style = Typography.bodyLarge,
                    color = placeholderColor,
                    modifier = Modifier.testTag("OutlinedPasswordInputPlaceholder")
                )
            },
            leadingIcon = {
                Icon(
                    imageVector = ImageVector.vectorResource(id = R.drawable.ic_lock),
                    contentDescription = null,
                    tint = lockIconColor,
                    modifier = Modifier.testTag("OutlinedPasswordInputLeadingIcon")
                )
            },
            trailingIcon = {
                IconButton(onClick = {
                    if (isPasswordVisible) {
                        isPasswordVisible = false
                        passwordEye = R.drawable.ic_opened_eye
                    } else {
                        isPasswordVisible = true
                        passwordEye = R.drawable.ic_closed_eye
                    }
                }) {
                    Icon(
                        imageVector = ImageVector.vectorResource(id = passwordEye),
                        contentDescription = "Visibility",
                        tint = eyeIconColor,
                        modifier = Modifier.testTag("OutlinedPasswordInputTrailingIcon")
                    )
                }
            }
        )
    }
}

@Preview(showBackground = true)
@Composable
fun OutlinedPasswordInputDefaultPreview() {
    OutlinedPasswordInput()
}

@Preview(showBackground = true)
@Composable
fun OutlinedPasswordInputPreview() {
    var text by rememberSaveable { mutableStateOf(emptyString()) }
    OutlinedPasswordInput(
        value = text,
        onValueChange = { text = it },
        placeholder = "Type your password"
    )
}

@Preview(showBackground = true)
@Composable
fun OutlinedPasswordInputErrorPreview() {
    var text by rememberSaveable { mutableStateOf(emptyString()) }
    OutlinedPasswordInput(
        value = text,
        onValueChange = { text = it },
        placeholder = "Type your password",
        isError = true,
        supportingText = {
            Text(text = "Error message", style = Typography.label)
        }
    )
}
