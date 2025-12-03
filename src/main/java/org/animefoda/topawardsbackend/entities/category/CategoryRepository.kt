package org.animefoda.topawardsbackend.entities.category

import org.animefoda.topawardsbackend.entities.event.EventEntity
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query

interface CategoryRepository: JpaRepository<CategoryEntity, Int> {
    @Query("SELECT c FROM CategoryEntity c WHERE c.event = :event")
    fun findAllByEvent(event: EventEntity): List<CategoryEntity>
}