package br.com.sommelier.presentation.main.model

import br.com.sommelier.shared.route.SommelierRoute

data class MainUiModel(
    val startDestination: String = SommelierRoute.LOGIN.name
)
