package br.com.sommelier.presentation

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import br.com.sommelier.ui.component.ActionButton
import br.com.sommelier.ui.component.QuickActionButton
import br.com.sommelier.ui.theme.SommelierTheme
import br.com.sommelier.ui.theme.Spacing

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            SommelierTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
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
                    }
                }
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
            color = MaterialTheme.colorScheme.background
        ) {
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
            }
        }
    }
}
