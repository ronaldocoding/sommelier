package br.com.sommelier.ui.component

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.CornerSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.Snackbar
import androidx.compose.material3.SnackbarDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.tooling.preview.Preview
import br.com.sommelier.R
import br.com.sommelier.ui.theme.ColorReference
import br.com.sommelier.ui.theme.Sizing
import br.com.sommelier.ui.theme.Typography

@Composable
fun SommelierSnackbar(
    modifier: Modifier = Modifier,
    text: String = "Snackbar",
    textStyle: TextStyle = Typography.bodyNormal,
    textColor: Color = ColorReference.antiFlashWhite,
    shape: Shape = RoundedCornerShape(corner = CornerSize(Sizing.extraSmaller)),
    type: SommelierSnackbarType? = null
) {
    Snackbar(
        modifier = modifier
            .fillMaxWidth()
            .testTag("SommelierSnackbar"),
        content = {
            Row(
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically
            ) {
                if (type is SommelierSnackbarType.Success) {
                    Icon(
                        imageVector = ImageVector.vectorResource(id = R.drawable.ic_check_circle),
                        contentDescription = "Success",
                        tint = ColorReference.antiFlashWhite,
                        modifier = Modifier.testTag("SommelierSnackbarSuccessIcon")
                    )
                } else if (type is SommelierSnackbarType.Error) {
                    Icon(
                        imageVector = ImageVector.vectorResource(id = R.drawable.ic_alert_circle),
                        contentDescription = "Error",
                        tint = ColorReference.antiFlashWhite,
                        modifier = Modifier.testTag("SommelierSnackbarErrorIcon")
                    )
                }
                Spacer(modifier = Modifier.padding(horizontal = Sizing.smaller))
                Text(
                    text = text,
                    style = textStyle,
                    color = textColor,
                    modifier = Modifier.testTag("SommelierSnackbarText")
                )
            }
        },
        shape = shape,
        containerColor = when (type) {
            SommelierSnackbarType.Success -> ColorReference.forestGreen
            SommelierSnackbarType.Error -> ColorReference.orangeRed
            else -> SnackbarDefaults.color
        }
    )
}

sealed class SommelierSnackbarType {
    object Success : SommelierSnackbarType()
    object Error : SommelierSnackbarType()
}

@Preview(showBackground = true)
@Composable
fun SommelierSnackbarDefaultPreview() {
    SommelierSnackbar()
}

@Preview(showBackground = true)
@Composable
fun SommelierSnackbarSuccessPreview() {
    SommelierSnackbar(
        text = "Action successfully completed",
        type = SommelierSnackbarType.Success
    )
}

@Preview(showBackground = true)
@Composable
fun SommelierSnackbarErrorPreview() {
    SommelierSnackbar(
        text = "This action could not be done. Please, try again.",
        type = SommelierSnackbarType.Error
    )
}
