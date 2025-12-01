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
): BaseDTO<CategoryDTO, CategoryEntity>(id){
    override fun toEntity(): CategoryEntity {
        val entity = CategoryEntity()
        entity.id = id
        entity.name = name
        entity.description = description
        entity.event = event.toEntity()
        entity.nominees = nominees.map { it.toEntity() }.toMutableSet()
        return entity
    }
}
