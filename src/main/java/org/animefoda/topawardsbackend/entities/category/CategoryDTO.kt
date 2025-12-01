package org.animefoda.topawardsbackend.entities.category

import org.animefoda.topawardsbackend.entities.BaseDTO
import org.animefoda.topawardsbackend.entities.event.EventDTO
import org.animefoda.topawardsbackend.entities.nominee.NomineeDTO

data class CategoryDTO(
    override val id: Int?,
    val name: String,
    val description: String,
    val event: EventDTO,
    val nominees: List<NomineeDTO>
): BaseDTO(id)
