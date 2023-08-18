package br.com.sommelier.presentation.home.state

sealed class HomeEffect {
    object OpenManageAccount : HomeEffect()
    object OpenAddRestaurant : HomeEffect()
    object GetRestaurants : HomeEffect()
}
