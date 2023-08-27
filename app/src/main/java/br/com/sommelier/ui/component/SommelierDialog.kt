package br.com.sommelier.ui.component

import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.tooling.preview.Preview
import br.com.sommelier.ui.theme.ColorReference
import br.com.sommelier.ui.theme.Sizing
import br.com.sommelier.ui.theme.Typography

@Composable
fun SommelierDialog(
    modifier: Modifier = Modifier,
    title: String = "Title",
    text: String = "Text",
    confirmButtonText: String = "Confirm",
    onConfirmButtonClicked: () -> Unit = {},
    dismissButtonText: String = "Dismiss",
    onDismissButtonClicked: () -> Unit = {}
) {
    AlertDialog(
        modifier = modifier.testTag("SommelierDialog"),
        title = {
            Text(
                text = title,
                style = Typography.display,
                modifier = Modifier.testTag("SommelierDialogTitle")
            )
        },
        titleContentColor = ColorReference.eerieBlack,
        text = {
            Text(
                text = text,
                style = Typography.bodyNormal,
                modifier = Modifier.testTag("SommelierDialogText")
            )
        },
        shape = RoundedCornerShape(Sizing.medium),
        textContentColor = ColorReference.eerieBlack,
        containerColor = ColorReference.white,
        onDismissRequest = onDismissButtonClicked,
        confirmButton = {
            TextButton(
                onClick = onConfirmButtonClicked,
                modifier = Modifier.testTag("SommelierDialogConfirmButton")
            ) {
                Text(
                    text = confirmButtonText,
                    style = Typography.bodyLarge,
                    color = ColorReference.taupeGray,
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
                    style = Typography.bodyLarge,
                    color = ColorReference.royalPurple,
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
