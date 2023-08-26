package br.com.sommelier.presentation.editaccount.model

import androidx.compose.material3.SnackbarHostState
import br.com.sommelier.presentation.editaccount.res.EditAccountStringResource
import br.com.sommelier.ui.component.SommelierSnackbarType
import br.com.sommelier.util.emptyString

data class EditNameFieldUiState(
    val name: String = emptyString(),
    val placeholder: String = emptyString(),
    val errorSupportingMessage: EditAccountStringResource = EditAccountStringResource.Empty,
    val isError: Boolean = false
)

data class SnackbarUiState(
    val hostState: SnackbarHostState = SnackbarHostState(),
    val message: EditAccountStringResource = EditAccountStringResource.Empty,
    val type: SommelierSnackbarType = SommelierSnackbarType.Error
)

data class EditAccountUiModel(
    val editNameFieldUiState: EditNameFieldUiState = EditNameFieldUiState(),
    val snackbarUiState: SnackbarUiState = SnackbarUiState(),
    val isLoading: Boolean = false
)
