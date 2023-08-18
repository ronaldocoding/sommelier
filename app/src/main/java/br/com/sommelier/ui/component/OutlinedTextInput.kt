package br.com.sommelier.ui.component

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
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
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import br.com.sommelier.R
import br.com.sommelier.ui.theme.ColorReference
import br.com.sommelier.ui.theme.Sizing
import br.com.sommelier.ui.theme.Typography
import br.com.sommelier.util.emptyString

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun OutlinedTextInput(
    modifier: Modifier = Modifier,
    value: String = emptyString(),
    valueStyle: TextStyle = Typography.bodyLarge,
    onValueChange: (String) -> Unit = {},
    placeholder: String = emptyString(),
    placeholderStyle: TextStyle = Typography.bodyLarge,
    leadingIcon: ImageVector? = null,
    leadingIconContentDescription: String? = null,
    isError: Boolean = false,
    isEnabled: Boolean = true,
    singleLine: Boolean = true,
    maxLines: Int = 1,
    keyboardOptions: KeyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Text),
    supportingText: (@Composable () -> Unit)? = null
) {
    var isTextFieldFocused by rememberSaveable { mutableStateOf(false) }
    val leadingIconColor = if (isError) {
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

    OutlinedTextField(
        value = value,
        onValueChange = onValueChange,
        modifier = modifier
            .fillMaxWidth()
            .onFocusChanged {
                isTextFieldFocused = it.isFocused
            }
            .testTag("OutlinedTextField"),
        textStyle = valueStyle.copy(color = textColor),
        enabled = isEnabled,
        isError = isError,
        singleLine = singleLine,
        maxLines = maxLines,
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
        keyboardOptions = keyboardOptions,
        placeholder = {
            Text(
                text = placeholder,
                style = placeholderStyle,
                color = placeholderColor,
                modifier = Modifier.testTag("OutlinedTextFieldPlaceholder")
            )
        },
        leadingIcon = if (leadingIcon != null) {
            {
                Icon(
                    imageVector = leadingIcon,
                    contentDescription = leadingIconContentDescription,
                    tint = leadingIconColor,
                    modifier = Modifier.testTag("OutlinedTextFieldLeadingIcon")
                )
            }
        } else {
            null
        }
    )
}

@Preview(showBackground = true)
@Composable
fun OutlinedTextInputDefaultPreview() {
    var text by rememberSaveable { mutableStateOf(emptyString()) }
    OutlinedTextInput(value = text, onValueChange = { text = it })
}

@Preview(showBackground = true)
@Composable
fun OutlinedTextInputPreview() {
    var text by rememberSaveable { mutableStateOf(emptyString()) }
    OutlinedTextInput(
        value = text,
        onValueChange = { text = it },
        placeholder = "Type your name",
        leadingIcon = ImageVector.vectorResource(id = R.drawable.ic_user)
    )
}

@Preview(showBackground = true)
@Composable
fun OutlinedTextInputErrorPreview() {
    var text by rememberSaveable { mutableStateOf(emptyString()) }
    OutlinedTextInput(
        value = text,
        onValueChange = { text = it },
        placeholder = "Type your name",
        leadingIcon = ImageVector.vectorResource(id = R.drawable.ic_user),
        isError = true,
        supportingText = {
            Text(text = "Error message", style = Typography.label)
        }
    )
}
