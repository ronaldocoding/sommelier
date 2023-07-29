package br.com.sommelier.presentation

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import br.com.sommelier.ui.component.ActionButton
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
                    ActionButton(
                        text = "Login",
                        onClick = { /*TODO*/ },
                        modifier = Modifier.padding(horizontal = Spacing.small)
                    )
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
            ActionButton(
                text = "Login",
                onClick = { /*TODO*/ },
                modifier = Modifier.padding(horizontal = Spacing.mediumLarge)
            )
        }
    }
}
