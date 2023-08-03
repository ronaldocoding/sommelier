package br.com.sommelier.ui.component

import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.tooling.preview.Preview
import br.com.sommelier.ui.theme.ColorReference
import br.com.sommelier.ui.theme.Sizing
import br.com.sommelier.ui.theme.Typography

@Composable
fun SommelierDialog(
    modifier: Modifier = Modifier,
    title: String = "Title",
    titleStyle: TextStyle = Typography.display,
    titleColor: Color = ColorReference.eerieBlack,
    text: String = "Text",
    textStyle: TextStyle = Typography.bodyNormal,
    textColor: Color = ColorReference.eerieBlack,
    onDismissRequest: () -> Unit = {},
    confirmButtonText: String = "Confirm",
    confirmButtonStyle: TextStyle = Typography.bodyLarge,
    confirmButtonColor: Color = ColorReference.taupeGray,
    onConfirmButtonClicked: () -> Unit = {},
    dismissButtonText: String = "Dismiss",
    dismissButtonStyle: TextStyle = Typography.bodyLarge,
    dismissButtonColor: Color = ColorReference.royalPurple,
    onDismissButtonClicked: () -> Unit = {}
) {
    AlertDialog(
        modifier = modifier.testTag("SommelierDialog"),
        title = {
            Text(
                text = title,
                style = titleStyle,
                modifier = Modifier.testTag("SommelierDialogTitle")
            )
        },
        titleContentColor = titleColor,
        text = {
            Text(
                text = text,
                style = textStyle,
                modifier = Modifier.testTag("SommelierDialogText")
            )
        },
        shape = RoundedCornerShape(Sizing.medium),
        textContentColor = textColor,
        containerColor = ColorReference.white,
        onDismissRequest = onDismissRequest,
        confirmButton = {
            TextButton(
                onClick = onConfirmButtonClicked,
                modifier = Modifier.testTag("SommelierDialogConfirmButton")
            ) {
                Text(
                    text = confirmButtonText,
                    style = confirmButtonStyle,
                    color = confirmButtonColor,
                    modifier = Modifier.testTag("SommelierDialogConfirmButtonText")
                )
            }
        },
        dismissButton = {
            TextButton(
                onClick = onDismissButtonClicked,
                modifier = Modifier.testTag("SommelierDialogDismissButton")
            ) {
                Text(
                    text = dismissButtonText,
                    style = dismissButtonStyle,
                    color = dismissButtonColor,
                    modifier = Modifier.testTag("SommelierDialogDismissButtonText")
                )
            }
        }
    )
}

@Preview(showBackground = true)
@Composable
fun SommelierDialogDefaultPreview() {
    SommelierDialog()
}

@Preview(showBackground = true)
@Composable
fun SommelierDialogPreview() {
    SommelierDialog(
        title = "Delete account",
        text = "Are you sure you want to delete your account? This action cannot be undone.",
        confirmButtonText = "Yes",
        dismissButtonText = "No"
    )
}
