package br.com.sommelier.presentation.home.state

sealed class HomeUiEffect {
    object OpenManageAccount : HomeUiEffect()
    object OpenAddRestaurant : HomeUiEffect()
    object GetRestaurants : HomeUiEffect()
}
