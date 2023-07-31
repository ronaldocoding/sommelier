package br.com.sommelier.presentation

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.tooling.preview.Preview
import br.com.sommelier.R
import br.com.sommelier.ui.component.ActionButton
import br.com.sommelier.ui.component.ClickableText
import br.com.sommelier.ui.component.QuickActionButton
import br.com.sommelier.ui.component.SommelierTopBar
import br.com.sommelier.ui.component.SommelierTopBarButton
import br.com.sommelier.ui.theme.ColorReference
import br.com.sommelier.ui.theme.SommelierTheme
import br.com.sommelier.ui.theme.Spacing

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            SommelierTheme {
                Content()
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun Content() {
    SommelierTheme {
        Surface(
            modifier = Modifier.fillMaxSize(),
            color = ColorReference.white
        ) {
            Box {
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
            Column(
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                ActionButton(
                    text = "Login",
                    modifier = Modifier.padding(horizontal = Spacing.small)
                )
                Spacer(modifier = Modifier.padding(Spacing.small))
                QuickActionButton()
                Spacer(modifier = Modifier.padding(Spacing.small))
                ClickableText(
                    nonClickableText = "Don't have an account? ",
                    clickableText = "Sign up"
                )
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun SommelierPreview() {
    SommelierTheme {
        Surface(
            modifier = Modifier.fillMaxSize(),
            color = ColorReference.white
        ) {
            Content()
        }
    }
}
