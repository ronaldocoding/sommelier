package br.com.sommelier.ui.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable

@Composable
fun SommelierTheme(
    content: @Composable () -> Unit
) {
    MaterialTheme(
        content = content
    )
}
