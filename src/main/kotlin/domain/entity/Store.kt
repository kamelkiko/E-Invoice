package domain.entity

import kotlinx.serialization.Serializable

@Serializable
data class Store(
    val id: Int,
    val name: String
)