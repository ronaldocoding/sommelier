package br.com.sommelier.ui.component

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarColors
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.tooling.preview.Preview
import br.com.sommelier.R
import br.com.sommelier.ui.theme.ColorReference
import br.com.sommelier.ui.theme.Typography

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SommelierTopBar(
    modifier: Modifier = Modifier,
    title: String = "Title",
    titleColor: Color = ColorReference.quartz,
    titleStyle: TextStyle = Typography.headerLarge,
    color: TopAppBarColors = TopAppBarDefaults.smallTopAppBarColors(
        containerColor = ColorReference.white
    ),
    leftButton: SommelierTopBarButton = SommelierTopBarButton.Disabled,
    rightButton: SommelierTopBarButton = SommelierTopBarButton.Disabled,
) {
    CenterAlignedTopAppBar(
        title = {
            Text(
                text = title,
                color = titleColor,
                style = titleStyle,
                modifier = modifier.testTag("SommelierTopBarTitle")
            )
        },
        modifier = modifier.testTag("SommelierTopBar"),
        colors = color,
        navigationIcon = {
            when (leftButton) {
                is SommelierTopBarButton.Enabled -> {
                    IconButton(
                        onClick = leftButton.onClick,
                        modifier = modifier.testTag("SommelierTopBarLeftButton")
                    ) {
                        Icon(
                            imageVector = leftButton.icon,
                            contentDescription = leftButton.contentDescription,
                            tint = leftButton.color
                        )
                    }
                }

                is SommelierTopBarButton.Disabled -> {
                    // No button
                }
            }
        },
        actions = {
            when (rightButton) {
                is SommelierTopBarButton.Enabled -> {
                    IconButton(
                        onClick = rightButton.onClick,
                        modifier = modifier.testTag("SommelierTopBarRightButton")
                    ) {
                        Icon(
                            imageVector = rightButton.icon,
                            contentDescription = rightButton.contentDescription,
                            tint = rightButton.color
                        )
                    }
                }

                is SommelierTopBarButton.Disabled -> {
                    // No button
                }
            }
        }
    )
}

sealed class SommelierTopBarButton {
    class Enabled(
        val icon: ImageVector,
        val color: Color = ColorReference.royalPurple,
        val contentDescription: String,
        val onClick: () -> Unit = {},
    ) : SommelierTopBarButton()

    object Disabled : SommelierTopBarButton()
}

@OptIn(ExperimentalMaterial3Api::class)
@Preview(showBackground = true)
@Composable
fun SommelierTopBarDefaultPreview() {
    SommelierTopBar()
}

@OptIn(ExperimentalMaterial3Api::class)
@Preview(showBackground = true)
@Composable
fun SommelierTopBarWithNavigationPreview() {
    SommelierTopBar(
        leftButton = SommelierTopBarButton.Enabled(
            icon = Icons.Default.ArrowBack,
            contentDescription = "Back"
        )
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Preview(showBackground = true)
@Composable
fun SommelierTopBarWithNavigationAndActionPreview() {
    SommelierTopBar(
        leftButton = SommelierTopBarButton.Enabled(
            icon = Icons.Default.ArrowBack,
            contentDescription = "Back"
        ),
        rightButton = SommelierTopBarButton.Enabled(
            icon = ImageVector.vectorResource(id = R.drawable.ic_edit),
            contentDescription = "Edit"
        )
    )
}
