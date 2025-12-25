package org.animefoda.topawardsbackend.entities.nominee

import org.animefoda.topawardsbackend.entities.BaseDTO

data class NomineeDTO(
    override val id: Int?,
    val name: String,
    val imageUrl: String? = null
): BaseDTO<NomineeDTO, NomineeEntity>(id) {
    override fun toEntity(): NomineeEntity {
        val entity = NomineeEntity()
        entity.id = id
        entity.name = name
        entity.imageUrl = imageUrl
        return entity
    }
}
