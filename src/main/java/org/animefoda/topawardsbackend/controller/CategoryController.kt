package org.animefoda.topawardsbackend.controller

import org.animefoda.topawardsbackend.entities.category.CategoryDTO
import org.animefoda.topawardsbackend.entities.category.CategoryEntity
import org.animefoda.topawardsbackend.entities.category.CategoryRepository
import org.animefoda.topawardsbackend.entities.event.EventRepository
import org.animefoda.topawardsbackend.exception.NotFound
import org.animefoda.topawardsbackend.response.ApiResponse
import org.springframework.security.access.prepost.PreAuthorize
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/category")
class CategoryController {

    private val repository: CategoryRepository
    private val eventRepository: EventRepository

    constructor(categoryRepository: CategoryRepository, eventRepository: EventRepository) {
        this.repository = categoryRepository
        this.eventRepository = eventRepository
    }

    @GetMapping
    fun findAll(@RequestParam eventId: Int): ApiResponse<List<CategoryDTO>> {
        val event = eventRepository.findById(eventId).orElseThrow { NotFound("EventId $eventId not found") }
        val category = repository.findAllByEvent(event).map { it.toDTO()}
        return ApiResponse.success(category)
    }

    @PostMapping("/new")
    @PreAuthorize("hasRole('ADMIN')")
    fun create(@RequestBody body: CategoryDTO): ApiResponse<CategoryDTO> {
        val category = body.toEntity()
        val saved = repository.save(category)
        return ApiResponse.success(saved.toDTO(), "Category has been created")
    }
}