package org.animefoda.topawardsbackend.entities.event

import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.Table
import org.animefoda.topawardsbackend.entities.BaseEntity
import java.time.LocalDate
import java.time.OffsetDateTime

@Entity
@Table(name = "event")
class EventEntity: BaseEntity<EventEntity,EventDTO>() {
    @Column
    var name = ""

    @Column(name = "start_date")
    var startDate: LocalDate = LocalDate.now()

    @Column(name = "end_date")
    var endDate: LocalDate = LocalDate.now().plusDays(1)

    public override fun toDTO(): EventDTO {
        return EventDTO(this.id, name, startDate, endDate)
    }
}