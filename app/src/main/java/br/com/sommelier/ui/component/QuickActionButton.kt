package br.com.sommelier.ui.component

import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.tooling.preview.Preview
import br.com.sommelier.ui.theme.ColorReference
import br.com.sommelier.ui.theme.Sizing

@Composable
fun QuickActionButton(
    modifier: Modifier = Modifier,
    onClick: () -> Unit = {},
    backgroundColor: Color = ColorReference.royalPurple,
    iconImage: ImageVector = Icons.Default.Add,
    iconColor: Color = ColorReference.white,
    contentDescription: String? = null
) {
    FloatingActionButton(
        onClick = onClick,
        modifier = modifier
            .shadow(
                elevation = Sizing.small,
                shape = RoundedCornerShape(Sizing.small),
                clip = false
            )
            .testTag("QuickActionButton"),
        shape = MaterialTheme.shapes.large,
        containerColor = backgroundColor,
        contentColor = iconColor
    ) {
        Icon(
            iconImage,
            contentDescription = contentDescription,
            modifier = Modifier.testTag("QuickActionButtonIcon")
        )
    }
}

@Composable
@Preview(showBackground = true)
fun QuickActionButtonDefaultPreview() {
    QuickActionButton()
}
