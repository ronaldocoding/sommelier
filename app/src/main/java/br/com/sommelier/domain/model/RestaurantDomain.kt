package br.com.sommelier.domain.model

data class RestaurantDomain(
    val name: String,
    val description: String,
    val address: String,
    val averageRating: Float,
    val profileImageUrl: String,
    val headerImageUrl: String,
    val uid: String
)
