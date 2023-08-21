package br.com.sommelier.presentation.account.screen

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.CircularProgressIndicator
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
import br.com.sommelier.ui.component.SommelierTopBar
import br.com.sommelier.ui.component.SommelierTopBarButton
import br.com.sommelier.ui.component.TextView
import br.com.sommelier.ui.theme.ColorReference
import br.com.sommelier.ui.theme.SommelierTheme
import br.com.sommelier.ui.theme.Spacing

@OptIn(ExperimentalMaterial3Api::class)
@Preview(showBackground = true, showSystemUi = true)
@Composable
fun AccountScreen() {
    SommelierTheme {
        Scaffold(
            containerColor = ColorReference.white,
            topBar = { SommelierTopBar(
                title = "Account",
                leftButton = SommelierTopBarButton.Enabled(
                    icon = Icons.Default.ArrowBack,
                    contentDescription = stringResource(id = R.string.arrow_back_icon_description),
                    onClick = { /*TODO*/ }
                ),
                rightButton = SommelierTopBarButton.Enabled(
                    icon = ImageVector.vectorResource(id = R.drawable.ic_edit),
                    contentDescription = "Edit button",
                    onClick = { /*TODO*/ }
                )
            ) }
        ) { innerPadding ->
            Column(
                verticalArrangement = Arrangement.SpaceBetween,
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier
                    .fillMaxSize()
                    .padding(innerPadding)
            ) {
                Column(
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Spacer(modifier = Modifier.padding(Spacing.small))
                    TextView(
                        value = "Ronaldo Costa",
                        label = "Name",
                        leadingIcon = ImageVector.vectorResource(id = R.drawable.ic_user),
                        leadingIconContentDescription = "User icon",
                        modifier = Modifier.padding(horizontal = Spacing.mediumLarge)
                    )
                    Spacer(modifier = Modifier.padding(Spacing.small))
                    TextView(
                        value = "ronaldo@email.com",
                        label = "Email",
                        leadingIcon = ImageVector.vectorResource(id = R.drawable.ic_mail),
                        leadingIconContentDescription = "Email icon",
                        modifier = Modifier.padding(horizontal = Spacing.mediumLarge)
                    )
                }
                Column(
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    ActionButton(
                        text = "Password reset",
                        onClick = { /*TODO*/ },
                        modifier = Modifier.padding(horizontal = Spacing.small)
                    )
                    Spacer(modifier = Modifier.padding(Spacing.smaller))
                    ActionButton(
                        text = "Delete account",
                        backGroundColor = ColorReference.frenchFuchsia,
                        onClick = { /*TODO*/ },
                        modifier = Modifier.padding(horizontal = Spacing.small)
                    )
                    Spacer(modifier = Modifier.padding(Spacing.smaller))
                    ActionButton(
                        text = "Logout",
                        textColor = ColorReference.royalPurple,
                        backGroundColor = ColorReference.white,
                        isOutlined = true,
                        onClick = { /*TODO*/ },
                        modifier = Modifier.padding(horizontal = Spacing.small)
                    )
                    Spacer(modifier = Modifier.padding(Spacing.small))
                }
            }
        }
    }
}

@Composable
@Preview(showBackground = true, showSystemUi = true)
fun AccountLoadingScreen() {
    Box(
        contentAlignment = Alignment.Center,
        modifier = Modifier.fillMaxSize()
    ) {
        CircularProgressIndicator(
            color = ColorReference.royalPurple
        )
    }
}
