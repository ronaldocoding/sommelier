package br.com.sommelier.ui.component

import androidx.compose.foundation.layout.fillMaxWidth
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
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import br.com.sommelier.R
import br.com.sommelier.ui.theme.ColorReference
import br.com.sommelier.ui.theme.Typography
import br.com.sommelier.util.emptyString

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun OutlinedPasswordInput(
    modifier: Modifier = Modifier,
    value: String = emptyString(),
    valueStyle: TextStyle = Typography.bodyLarge,
    valueColor: Color = ColorReference.eerieBlack,
    onValueChange: (String) -> Unit = {},
    placeholder: String = emptyString(),
    placeholderStyle: TextStyle = Typography.bodyLarge,
    placeHolderColor: Color = ColorReference.taupeGray,
    label: String = emptyString(),
    focusedLabelStyle: TextStyle = Typography.bodyLarge,
    unfocusedLabelStyle: TextStyle = Typography.label,
    labelColor: Color = ColorReference.taupeGray,
    leadingIconContentDescription: String? = null,
    isError: Boolean = false,
    isEnabled: Boolean = true,
    singleLine: Boolean = true,
    maxLines: Int = 1,
    supportingText: (@Composable () -> Unit)? = null
) {
    var isLabelFocused by rememberSaveable { mutableStateOf(false) }
    var isTextFieldFocused by rememberSaveable { mutableStateOf(false) }
    var isPasswordVisible by rememberSaveable { mutableStateOf(false) }
    var passwordEye by rememberSaveable { mutableStateOf(R.drawable.ic_opened_eye) }

    OutlinedTextField(
        visualTransformation = if (isPasswordVisible) {
            VisualTransformation.None
        } else {
            PasswordVisualTransformation()
        },
        value = value,
        onValueChange = onValueChange,
        modifier = modifier
            .fillMaxWidth()
            .onFocusChanged {
                isTextFieldFocused = it.isFocused
            }
            .testTag("OutlinedPasswordInput"),
        textStyle = valueStyle.copy(color = valueColor),
        enabled = isEnabled,
        isError = isError,
        singleLine = singleLine,
        maxLines = maxLines,
        supportingText = supportingText,
        colors = TextFieldDefaults.outlinedTextFieldColors(
            focusedBorderColor = ColorReference.taupeGray,
            unfocusedBorderColor = ColorReference.taupeGray
        ),
        label = {
            Text(
                text = label,
                style = if (isTextFieldFocused) {
                    unfocusedLabelStyle
                } else if (!isLabelFocused && value.isEmpty()) {
                    focusedLabelStyle
                } else {
                    unfocusedLabelStyle
                },
                color = labelColor,
                modifier = Modifier
                    .onFocusChanged {
                        isLabelFocused = it.isFocused
                    }
                    .testTag("OutlinedPasswordInputLabel")
            )
        },
        placeholder = {
            Text(
                text = placeholder,
                style = placeholderStyle,
                color = placeHolderColor,
                modifier = Modifier.testTag("OutlinedPasswordInputPlaceholder")
            )
        },
        leadingIcon = {
            Icon(
                imageVector = ImageVector.vectorResource(id = R.drawable.ic_lock),
                contentDescription = leadingIconContentDescription,
                tint = ColorReference.taupeGray,
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
                    tint = ColorReference.taupeGray,
                    modifier = Modifier.testTag("OutlinedPasswordInputTrailingIcon")
                )
            }
        }
    )
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
        placeholder = "Type your password",
        label = "Password",
        leadingIconContentDescription = "Password"
    )
}
