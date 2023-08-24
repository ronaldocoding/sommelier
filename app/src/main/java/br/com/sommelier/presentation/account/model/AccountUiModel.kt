package br.com.sommelier.presentation.account.model

import androidx.compose.material3.SnackbarHostState
import br.com.sommelier.presentation.account.res.AccountStringResource
import br.com.sommelier.ui.component.SommelierSnackbarType
import br.com.sommelier.util.emptyString

data class SnackbarUiState(
    val hostState: SnackbarHostState = SnackbarHostState(),
    val message: AccountStringResource = AccountStringResource.Empty,
    val type: SommelierSnackbarType = SommelierSnackbarType.Error
)

data class AccountUiModel(
    val name: String = emptyString(),
    val email: String = emptyString(),
    val isLoading: Boolean = false,
    val snackbarUiState: SnackbarUiState = SnackbarUiState(),
    val isPasswordResetDialogVisible: Boolean = false,
    val isDeleteAccountDialogVisible: Boolean = false,
    val isLogoutDialogVisible: Boolean = false
)
