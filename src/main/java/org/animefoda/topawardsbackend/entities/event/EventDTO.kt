package org.animefoda.topawardsbackend.entities.event

import org.animefoda.topawardsbackend.entities.BaseDTO
import java.time.OffsetDateTime

data class EventDTO(
    override val id: Int?,
    val name: String,
    val startDate: OffsetDateTime,
    val endDate: OffsetDateTime,
): BaseDTO<EventDTO, EventEntity>(id) {
    override fun toEntity(): EventEntity {
        val entity = EventEntity()
        entity.id = id;
        entity.name = name
        entity.startDate = startDate
        entity.endDate = endDate
        return entity
    }
}