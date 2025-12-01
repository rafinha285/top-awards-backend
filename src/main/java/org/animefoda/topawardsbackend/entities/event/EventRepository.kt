package org.animefoda.topawardsbackend.entities.event

import org.springframework.data.jpa.repository.JpaRepository

interface EventRepository: JpaRepository<EventEntity, Int> {
}