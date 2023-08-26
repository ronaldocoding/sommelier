package br.com.sommelier.presentation.main.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import br.com.sommelier.base.event.MutableSingleLiveEvent
import br.com.sommelier.base.usecase.UseCase
import br.com.sommelier.domain.usecase.IsUserEmailVerifiedUseCase
import br.com.sommelier.domain.usecase.IsUserSignedInUseCase
import br.com.sommelier.presentation.main.action.MainAction
import br.com.sommelier.presentation.main.model.MainUiModel
import br.com.sommelier.presentation.main.state.MainUiEffect
import br.com.sommelier.presentation.main.state.MainUiState
import br.com.sommelier.shared.route.SommelierRoute
import br.com.sommelier.util.ext.asLiveData
import kotlinx.coroutines.launch

class MainViewModel(
    private val isUserSignedInUseCase: IsUserSignedInUseCase,
    private val isUserEmailVerifiedUseCase: IsUserEmailVerifiedUseCase
) : ViewModel(), MainAction {

    private val _uiState = MutableLiveData<MainUiState>(MainUiState.Loading)
    val uiState = _uiState.asLiveData()

    private val _uiEffect = MutableSingleLiveEvent<MainUiEffect>()
    val uiEffect = _uiEffect.asSingleLiveEvent()

    override fun sendAction(action: MainAction.Action) {
        viewModelScope.launch {
            when (action) {
                is MainAction.Action.OnInitial -> {
                    handleOnInitial()
                }

                is MainAction.Action.OnCheckIfUserIsSignedIn -> {
                    handleOnCheckIfUserIsSignedIn()
                }
            }
        }
    }

    private fun handleOnInitial() {
        _uiEffect.value = MainUiEffect.ShowLoading
    }

    private suspend fun handleOnCheckIfUserIsSignedIn() {
        isUserSignedInUseCase(UseCase.None()).fold(
            ifLeft = {
                emitResumeState(SommelierRoute.LOGIN.name)
            },
            ifRight = { isUserSignedInSuccessResult ->
                if (isUserSignedInSuccessResult.data) {
                    isUserEmailVerifiedUseCase(UseCase.None()).fold(
                        ifLeft = {
                            emitResumeState(SommelierRoute.LOGIN.name)
                        },
                        ifRight = { isUserEmailVerifiedResult ->
                            if (isUserEmailVerifiedResult.data) {
                                emitResumeState(SommelierRoute.HOME.name)
                            } else {
                                emitResumeState(SommelierRoute.LOGIN.name)
                            }
                        }
                    )
                } else {
                    emitResumeState(SommelierRoute.LOGIN.name)
                }
            }
        )
    }

    private fun emitResumeState(startDestination: String) {
        val newUiState = MainUiState.Resume(
            MainUiModel(startDestination = startDestination)
        )
        _uiState.value = newUiState
    }
}