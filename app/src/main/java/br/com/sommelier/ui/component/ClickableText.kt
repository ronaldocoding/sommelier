package br.com.sommelier.ui.component

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.width
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import br.com.sommelier.ui.theme.ColorReference
import br.com.sommelier.ui.theme.Sizing
import br.com.sommelier.ui.theme.Typography
import br.com.sommelier.util.emptyString

@Composable
fun ClickableText(
    modifier: Modifier = Modifier,
    nonClickableText: String = emptyString(),
    nonClickableTextStyle: TextStyle = Typography.bodyLarge,
    clickableText: String = "Click here",
    clickableTextStyle: TextStyle = Typography.bodyLargeBold,
    textColor: Color = ColorReference.quartz,
    onClick: () -> Unit = {}
) {
    Row(
        modifier = modifier,
        verticalAlignment = Alignment.CenterVertically
    ) {
        if (nonClickableText.isNotEmpty()) {
            Text(
                text = nonClickableText,
                style = nonClickableTextStyle,
                color = textColor,
                modifier = Modifier.testTag("NonClickableText")
            )
            Spacer(
                modifier = Modifier
                    .width(Sizing.extraSmaller)
                    .testTag("ClickableTextSpacer")
            )
        }
        TextButton(
            onClick = onClick,
            contentPadding = PaddingValues(Sizing.none),
            modifier = Modifier.getModifier(clickableText.length)
        ) {
            Text(
                text = clickableText,
                style = clickableTextStyle,
                color = ColorReference.royalPurple
            )
        }
    }
}

private fun Modifier.getModifier(clickableTextLength: Int): Modifier {
    return if (clickableTextLength <= 5) {
        then(Modifier.testTag("ClickableText").width(ButtonDefaults.MinWidth - 14.dp))
    } else {
        then(Modifier.testTag("ClickableText"))
    }
}

@Preview(showBackground = true)
@Composable
fun ClickableTextDefaultPreview() {
    ClickableText()
}

@Preview(showBackground = true)
@Composable
fun ClickableTextWithNonClickablePreview() {
    ClickableText(nonClickableText = "Don't have an account?")
}
