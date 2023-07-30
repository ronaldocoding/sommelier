package br.com.sommelier.ui.component

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.tooling.preview.Preview
import br.com.sommelier.ui.theme.ColorReference
import br.com.sommelier.ui.theme.Typography

@Composable
fun ActionButton(
    modifier: Modifier = Modifier,
    text: String = "Action",
    onClick: () -> Unit = {},
    textColor: Color = ColorReference.white,
    textStyle: TextStyle = Typography.bodyMedium,
    backGroundColor: Color = ColorReference.royalPurple,
    isOutlined: Boolean = false
) {
    Box(
        modifier = modifier.testTag("ActionButton")
    ) {
        if (isOutlined) {
            OutlinedButton(
                onClick = onClick,
                modifier = modifier
                    .fillMaxWidth()
                    .testTag("OutlinedActionButton"),
                colors = ButtonDefaults.buttonColors(
                    containerColor = backGroundColor
                )
            ) {
                Text(
                    text = text,
                    style = textStyle,
                    color = textColor,
                    modifier = Modifier.testTag("OutlinedActionButtonText")
                )
            }
        } else {
            Button(
                onClick = onClick,
                modifier = modifier
                    .fillMaxWidth()
                    .testTag("NormalActionButton"),
                colors = ButtonDefaults.buttonColors(
                    containerColor = backGroundColor
                )
            ) {
                Text(
                    text = text,
                    style = textStyle,
                    color = textColor,
                    modifier = Modifier.testTag("NormalActionButtonText")
                )
            }
        }
    }
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun ActionButtonPreviewPrimary() {
    ActionButton()
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun ActionButtonPreviewSecondary() {
    ActionButton(
        backGroundColor = ColorReference.frenchFuchsia
    )
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun ActionButtonPreviewTertiary() {
    ActionButton(
        backGroundColor = ColorReference.white,
        textColor = ColorReference.royalPurple,
        isOutlined = true
    )
}