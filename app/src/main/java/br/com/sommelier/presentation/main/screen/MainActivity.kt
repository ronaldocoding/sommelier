package br.com.sommelier.presentation.main.screen

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import br.com.sommelier.presentation.account.screen.AccountScreen
import br.com.sommelier.presentation.confirmemail.screen.ConfirmEmailScreen
import br.com.sommelier.presentation.editaccount.screen.EditAccountScreen
import br.com.sommelier.presentation.home.screen.HomeScreen
import br.com.sommelier.presentation.login.screen.LoginScreen
import br.com.sommelier.presentation.main.action.MainAction
import br.com.sommelier.presentation.main.model.MainUiModel
import br.com.sommelier.presentation.main.state.MainUiEffect
import br.com.sommelier.presentation.main.state.MainUiState
import br.com.sommelier.presentation.main.viewmodel.MainViewModel
import br.com.sommelier.presentation.passwordreset.screen.PasswordResetScreen
import br.com.sommelier.presentation.register.screen.RegisterScreen
import br.com.sommelier.shared.route.SommelierRoute
import br.com.sommelier.ui.theme.ColorReference
import br.com.sommelier.ui.theme.SommelierTheme
import org.koin.androidx.compose.getViewModel

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
    val viewModel = getViewModel<MainViewModel>()
    val state = checkNotNull(viewModel.uiState.observeAsState().value)
    val uiModel = state.uiModel
    val navController = rememberNavController()

    viewModel.sendAction(MainAction.Action.OnInitial)
    UiState(state, navController, uiModel)
    UiEffect(viewModel)
}

@Composable
private fun UiState(
    state: MainUiState,
    navController: NavHostController,
    uiModel: MainUiModel
) {
    when (state) {
        is MainUiState.Loading -> {
            LoadingScreen()
        }

        is MainUiState.Resume -> {
            SommelierNavHost(navController, uiModel)
        }
    }
}

@Composable
private fun UiEffect(viewModel: MainViewModel) {
    val lifecycleOwner = LocalLifecycleOwner.current
    viewModel.uiEffect.observe(lifecycleOwner) { effect ->
        when (effect) {
            is MainUiEffect.ShowLoading -> {
                viewModel.sendAction(MainAction.Action.OnCheckIfUserIsSignedIn)
            }
        }
    }
}

@Composable
private fun LoadingScreen() {
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        CircularProgressIndicator(
            color = ColorReference.royalPurple
        )
    }
}

@Composable
private fun SommelierNavHost(navController: NavHostController, uiModel: MainUiModel) {
    NavHost(navController = navController, startDestination = uiModel.startDestination) {
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
                    navController.popBackStack()
                }
            )
        }

        composable(route = SommelierRoute.CONFIRM_EMAIL.name) {
            ConfirmEmailScreen(
                popBackStack = {
                    navController.popBackStack(SommelierRoute.LOGIN.name, inclusive = false)
                }
            )
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
                    navController.popBackStack()
                }
            )
        }

        composable(route = SommelierRoute.EDIT_ACCOUNT.name) {
            EditAccountScreen(
                popBackStack = {
                    navController.popBackStack()
                }
            )
        }
    }
}
