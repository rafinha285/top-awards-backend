package org.animefoda.topawardsbackend.entities.category

data class CategoryInputDTO(
    val id: Int? = null, // Pode ser nulo na criação
    val name: String,
    val description: String,
    val eventId: Int,    // AQUI: Recebe apenas o ID do evento
    val nomineeIds: List<Int> = emptyList()
)
