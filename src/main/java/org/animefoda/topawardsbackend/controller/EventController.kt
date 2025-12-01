package org.animefoda.topawardsbackend.controller

import org.animefoda.topawardsbackend.entities.event.EventDTO
import org.animefoda.topawardsbackend.entities.event.EventEntity
import org.animefoda.topawardsbackend.entities.event.EventRepository
import org.animefoda.topawardsbackend.response.ApiResponse
import org.springframework.security.access.prepost.PreAuthorize
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/event")
class EventController {

    private val repository: EventRepository

    constructor(eventRepository: EventRepository) {
        this.repository = eventRepository
    }

    @GetMapping
    fun getEvents(): ApiResponse<List<EventDTO>> {
        return ApiResponse.success(repository.findAll().map{it.toDTO()})
    }

    @PostMapping("/new")
    @PreAuthorize("hasRole('ADMIN')")
    fun create(@RequestBody dto: EventDTO): ApiResponse<EventDTO> {
        val event = dto.toEntity()
        val savedEvent = repository.save(event)
        return ApiResponse.success(savedEvent.toDTO(), message = "Event created successfully")
    }
}