package br.com.sommelier.presentation.main.screen

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.runtime.Composable
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import br.com.sommelier.presentation.account.screen.AccountScreen
import br.com.sommelier.presentation.editaccount.screen.EditAccountScreen
import br.com.sommelier.presentation.home.screen.HomeScreen
import br.com.sommelier.presentation.login.screen.LoginScreen
import br.com.sommelier.presentation.passwordreset.screen.PasswordResetScreen
import br.com.sommelier.presentation.register.screen.RegisterScreen
import br.com.sommelier.shared.route.SommelierRoute
import br.com.sommelier.ui.theme.SommelierTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        installSplashScreen()
        setContent {
            SommelierTheme {
                Application()
            }
        }
    }
}

@Composable
fun Application() {
    val navController = rememberNavController()

    NavHost(navController = navController, startDestination = SommelierRoute.LOGIN.name) {
        composable(route = SommelierRoute.LOGIN.name) {
            LoginScreen(
                navigateToHomeScreen = {
                    navController.navigate(SommelierRoute.HOME.name)
                },
                navigateToRegisterScreen = {
                    navController.navigate(SommelierRoute.REGISTER.name)
                },
                navigateToPasswordResetScreen = {
                    navController.navigate(SommelierRoute.PASSWORD_RESET.name)
                },
                navigateToConfirmEmailScreen = {
                    navController.navigate(SommelierRoute.CONFIRM_EMAIL.name)
                }
            )
        }

        composable(route = SommelierRoute.REGISTER.name) {
            RegisterScreen(
                navigateToLoginScreen = {
                    navController.navigate(SommelierRoute.LOGIN.name)
                },
                navigateToConfirmEmailScreen = {
                    navController.navigate(SommelierRoute.CONFIRM_EMAIL.name)
                }
            )
        }

        composable(route = SommelierRoute.HOME.name) {
            HomeScreen(
                popBackStack = {
                    navController.popBackStack(SommelierRoute.LOGIN.name, inclusive = true)
                },
                navigateToAccountScreen = {
                    navController.navigate(SommelierRoute.ACCOUNT.name)
                }
            )
        }

        composable(route = SommelierRoute.PASSWORD_RESET.name) {
            PasswordResetScreen(
                popBackStack = {
                    navController.popBackStack(SommelierRoute.HOME.name, inclusive = false)
                }
            )
        }

        composable(route = SommelierRoute.CONFIRM_EMAIL.name) {
            // TODO: Implement
        }

        composable(route = SommelierRoute.ACCOUNT.name) {
            AccountScreen(
                navigateToEditAccountScreen = {
                    navController.navigate(SommelierRoute.EDIT_ACCOUNT.name)
                },
                navigateToLoginScreen = {
                    navController.navigate(SommelierRoute.LOGIN.name)
                },
                navigateToPasswordResetScreen = {
                    navController.navigate(SommelierRoute.PASSWORD_RESET.name)
                },

                popBackStack = {
                    navController.popBackStack(SommelierRoute.HOME.name, inclusive = false)
                }
            )
        }

        composable(route = SommelierRoute.EDIT_ACCOUNT.name) {
            EditAccountScreen(
                popBackStack = {
                    navController.popBackStack(SommelierRoute.ACCOUNT.name, inclusive = false)
                }
            )
        }
    }
}
