package org.animefoda.topawardsbackend.controller

import org.animefoda.topawardsbackend.entities.event.EventDTO
import org.animefoda.topawardsbackend.response.ApiResponse
import org.animefoda.topawardsbackend.service.EventService
import org.springframework.security.access.prepost.PreAuthorize
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/event")
class EventController(
    private val eventService: EventService
) {

    @GetMapping
    fun getEvents(): ApiResponse<List<EventDTO>> {
        return ApiResponse.success(eventService.findAll())
    }

    @GetMapping("/{eventId}")
    fun getEvent(@PathVariable eventId: Int): ApiResponse<EventDTO> {
        return ApiResponse.success(eventService.findById(eventId))
    }

    @PostMapping("/{eventId}/update")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    fun updateEvent(@PathVariable eventId: Int, @RequestBody event: EventDTO): ApiResponse<EventDTO> {
        return ApiResponse.success(eventService.update(eventId, event))
    }

    @PostMapping("/{eventId}/delete")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    fun delete(@PathVariable eventId: Int): ApiResponse<EventDTO> {
        return ApiResponse.success(eventService.delete(eventId))
    }

    @PostMapping("/new")
    @PreAuthorize("hasRole('ADMIN')")
    fun create(@RequestBody dto: EventDTO): ApiResponse<EventDTO> {
        return ApiResponse.success(eventService.create(dto), message = "Event created successfully")
    }
}