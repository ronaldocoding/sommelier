package br.com.sommelier.ui.component

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.focusProperties
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.tooling.preview.Preview
import br.com.sommelier.R
import br.com.sommelier.ui.theme.ColorReference
import br.com.sommelier.ui.theme.Typography
import br.com.sommelier.util.emptyString

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TextView(
    modifier: Modifier = Modifier,
    value: String = emptyString(),
    valueStyle: TextStyle = Typography.bodyLarge,
    valueColor: Color = ColorReference.eerieBlack,
    label: String = emptyString(),
    labelStyle: TextStyle = Typography.label,
    labelColor: Color = ColorReference.taupeGray,
    leadingIcon: ImageVector? = null,
    leadingIconContentDescription: String? = null
) {

    TextField(
        value = value,
        onValueChange = {},
        modifier = modifier
            .fillMaxWidth()
            .focusProperties { canFocus = false }
            .testTag("TextView"),
        readOnly = true,
        textStyle = valueStyle.copy(color = valueColor),
        singleLine = true,
        label = { Text(label, color = labelColor, style = labelStyle) },
        colors = TextFieldDefaults.outlinedTextFieldColors(
            unfocusedBorderColor = ColorReference.brightGray,
        ),
        leadingIcon = if (leadingIcon != null) {
            {
                Icon(
                    imageVector = leadingIcon,
                    contentDescription = leadingIconContentDescription,
                    tint = ColorReference.taupeGray,
                    modifier = Modifier.testTag("TextViewLeadingIcon")
                )
            }
        } else {
            null
        },
    )
}

@Preview(showBackground = true)
@Composable
fun TextViewDefaultPreview() {
    TextView()
}

@Preview(showBackground = true)
@Composable
fun TextViewPreview() {
    TextView(
        value = "Ronaldo Costa de Freitas",
        label = "Name",
        leadingIcon = ImageVector.vectorResource(id = R.drawable.ic_user)
    )
}
