package org.animefoda.topawardsbackend.entities.event;

import jakarta.persistence.*;
import lombok.*;
import org.animefoda.topawardsbackend.entities.BaseEntity;

import java.time.OffsetDateTime;

@Entity
@Table(name = "event")
@Data
public class EventEntity extends BaseEntity<EventDTO> {
    @Column
    private String name;

    @Column(name = "start_date")
    private OffsetDateTime startDate;

    @Column(name = "end_date")
    private OffsetDateTime endDate;

    @Override
    public EventDTO toDTO() {
        return new EventDTO(id, name, startDate, endDate);
    }
}
