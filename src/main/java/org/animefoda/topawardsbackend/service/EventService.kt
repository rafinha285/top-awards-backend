package org.animefoda.topawardsbackend.service

import org.animefoda.topawardsbackend.entities.event.EventDTO
import org.animefoda.topawardsbackend.entities.event.EventEntity
import org.animefoda.topawardsbackend.entities.event.EventRepository
import org.animefoda.topawardsbackend.exception.NotFound
import org.springframework.cache.annotation.CacheEvict
import org.springframework.cache.annotation.Cacheable
import org.springframework.cache.annotation.Caching
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class EventService(
    private val eventRepository: EventRepository
) {

    @Cacheable("events")
    fun findAll(): List<EventDTO> {
        return eventRepository.findAll().map { it.toDTO() }
    }

    @Cacheable(value = ["event"], key = "#id")
    fun findById(id: Int): EventDTO {
        return eventRepository.findById(id)
            .orElseThrow { NotFound("Event with id $id not found") }
            .toDTO()
    }

    @Transactional
    @CacheEvict(value = ["events"], allEntries = true)
    fun create(dto: EventDTO): EventDTO {
        val event = dto.toEntity()
        val savedEvent = eventRepository.save(event)
        return savedEvent.toDTO()
    }

    @Transactional
    @Caching(
        evict = [
            CacheEvict(value = ["events"], allEntries = true),
            CacheEvict(value = ["event"], key = "#id")
        ]
    )
    fun update(id: Int, dto: EventDTO): EventDTO {
        eventRepository.findById(id)
            .orElseThrow { NotFound("Event with id $id not found") }
        val event = dto.toEntity()
        event.id = id
        val savedEvent = eventRepository.save(event)
        return savedEvent.toDTO()
    }

    @Transactional
    @Caching(
        evict = [
            CacheEvict(value = ["events"], allEntries = true),
            CacheEvict(value = ["event"], key = "#id")
        ]
    )
    fun delete(id: Int): EventDTO {
        val event = eventRepository.findById(id)
            .orElseThrow { NotFound("Event with id $id not found") }
        eventRepository.delete(event)
        return event.toDTO()
    }
}
