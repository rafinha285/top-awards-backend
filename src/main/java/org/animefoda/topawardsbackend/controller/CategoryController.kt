package org.animefoda.topawardsbackend.controller

import org.animefoda.topawardsbackend.entities.category.CategoryDTO
import org.animefoda.topawardsbackend.entities.category.CategoryEntity
import org.animefoda.topawardsbackend.entities.category.CategoryInputDTO
import org.animefoda.topawardsbackend.entities.category.CategoryRepository
import org.animefoda.topawardsbackend.entities.event.EventRepository
import org.animefoda.topawardsbackend.exception.NotFound
import org.animefoda.topawardsbackend.response.ApiResponse
import org.animefoda.topawardsbackend.service.CategoryService
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
    private val categoryService: CategoryService

    constructor(
        categoryRepository: CategoryRepository,
        eventRepository: EventRepository,
        categoryService: CategoryService
    ) {
        this.repository = categoryRepository
        this.eventRepository = eventRepository
        this.categoryService = categoryService
    }

    @GetMapping
    fun findAll(): ApiResponse<List<CategoryDTO>>{
        val categories = repository.findAll().map { it.toDTO() }
        return ApiResponse.success(categories)
    }

    @GetMapping("/{eventId}")
    fun findAllByEventId(@RequestParam eventId: Int): ApiResponse<List<CategoryDTO>> {
        val event = eventRepository.findById(eventId).orElseThrow { NotFound("EventId $eventId not found") }
        val category = repository.findAllByEvent(event).map { it.toDTO()}
        return ApiResponse.success(category)
    }

    @PostMapping("/new")
    @PreAuthorize("hasRole('ADMIN')")
    fun create(@RequestBody body: CategoryInputDTO): ApiResponse<CategoryDTO> {
        val category = categoryService.create(body)
        return ApiResponse.success(category, "Category has been created")
    }
}