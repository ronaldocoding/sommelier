package br.com.sommelier.shared.screen

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import br.com.sommelier.ui.component.ActionButton
import br.com.sommelier.ui.theme.Spacing

@Composable
fun GenericErrorScreen(
    image: @Composable () -> Unit,
    text: @Composable () -> Unit,
    buttonText: String,
    onClickButton: () -> Unit
) {
    Column(
        verticalArrangement = Arrangement.SpaceBetween,
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier
            .fillMaxSize()
            .padding(Spacing.none)
            .verticalScroll(rememberScrollState())
    ) {
        Column(
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(modifier = Modifier.padding(Spacing.extraLarge))
            image()
            Spacer(modifier = Modifier.padding(Spacing.medium))
            text()
        }
        ActionButton(
            text = buttonText,
            modifier = Modifier.padding(horizontal = Spacing.small, vertical = Spacing.small),
            onClick = onClickButton
        )
    }
}