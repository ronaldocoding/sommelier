package br.com.sommelier.presentation.home.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import br.com.sommelier.base.event.MutableSingleLiveEvent
import br.com.sommelier.presentation.home.action.HomeAction
import br.com.sommelier.presentation.home.state.HomeEffect
import br.com.sommelier.presentation.home.state.HomeUiState
import br.com.sommelier.util.ext.asLiveData

class HomeViewModel : ViewModel(), HomeAction {

    private val _uiState = MutableLiveData<HomeUiState>(HomeUiState.Initial)
    val uiState = _uiState.asLiveData()

    private val _uiEffect = MutableSingleLiveEvent<HomeEffect>()
    val uiEffect = _uiEffect.asSingleLiveEvent()

    override fun sendAction(action: HomeAction.Action) {
        when (action) {
            is HomeAction.Action.OnTypeSearchField -> {
                handleOnTypeSearchField(action)
            }

            is HomeAction.Action.OnSearch -> {
                handleOnSearch()
            }

            is HomeAction.Action.OnClickManageAccountButton -> {
                handleOnClickManageAccountButton()
            }

            is HomeAction.Action.OnClickAddRestaurantButton -> {
                handleOnClickAddRestaurantsButton()
            }
        }
    }

    private fun handleOnTypeSearchField(action: HomeAction.Action.OnTypeSearchField) {
        val state = checkNotNull(_uiState.value)
        val uiModel = state.uiModel
        val newUiModel = uiModel.copy(
            searchFieldUiState = uiModel.searchFieldUiState.copy(
                query = action.query
            )
        )
        val newUiState = HomeUiState.Resume(newUiModel)
        _uiState.value = newUiState
    }

    private fun handleOnSearch() {
        _uiEffect.value = HomeEffect.GetRestaurants
    }

    private fun handleOnClickManageAccountButton() {
        _uiEffect.value = HomeEffect.OpenManageAccount
    }

    private fun handleOnClickAddRestaurantsButton() {
        _uiEffect.value = HomeEffect.OpenAddRestaurant
    }
}
