package br.com.sommelier.presentation.editaccount.screen

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.tooling.preview.Preview
import br.com.sommelier.R
import br.com.sommelier.ui.component.ActionButton
import br.com.sommelier.ui.component.OutlinedTextInput
import br.com.sommelier.ui.component.SommelierTopBar
import br.com.sommelier.ui.component.SommelierTopBarButton
import br.com.sommelier.ui.theme.ColorReference
import br.com.sommelier.ui.theme.SommelierTheme
import br.com.sommelier.ui.theme.Spacing

@OptIn(ExperimentalMaterial3Api::class)
@Preview(showBackground = true, showSystemUi = true)
@Composable
fun EditAccountScreen() {
    SommelierTheme {
        Scaffold(
            containerColor = ColorReference.white,
            topBar = {
                SommelierTopBar(
                    title = "Edit account",
                    leftButton = SommelierTopBarButton.Enabled(
                        icon = Icons.Default.ArrowBack,
                        contentDescription = stringResource(id = R.string.arrow_back_icon_description),
                        onClick = {
                            // Todo: Navigate back
                        }
                    )
                )
            }
        ) { innerPadding ->
            Column(
                verticalArrangement = Arrangement.SpaceBetween,
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier
                    .fillMaxSize()
                    .padding(innerPadding)
            ) {
                OutlinedTextInput(
                    leadingIcon = ImageVector.vectorResource(id = R.drawable.ic_user),
                    leadingIconContentDescription = stringResource(id = R.string.user_icon_description),
                    placeholder = "Ronaldo Costa de Freitas",
                    modifier = Modifier.padding(
                        horizontal = Spacing.mediumLarge,
                        vertical = Spacing.extraLarge
                    )
                )
                ActionButton(
                    text = "Save",
                    onClick = {
                        // TODO: Save
                    },
                    modifier = Modifier.padding(
                        horizontal = Spacing.small,
                        vertical = Spacing.small
                    )
                )
            }
        }
    }
}
