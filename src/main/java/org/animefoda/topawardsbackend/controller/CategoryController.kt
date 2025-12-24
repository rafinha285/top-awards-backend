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
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/category")
class CategoryController(
    private val categoryService: CategoryService
) {

    @GetMapping
    fun findAll(): ApiResponse<List<CategoryDTO>>{
        val categories = categoryService.findAll()
        return ApiResponse.success(categories)
    }

    @GetMapping("/{id}")
    fun findById(@PathVariable id: Int): ApiResponse<CategoryDTO>{
        val category = categoryService.findById(id)
        return ApiResponse.success(category)
    }


    @GetMapping("/event/{eventId}")
    fun findAllByEventId(@PathVariable eventId: Int): ApiResponse<List<CategoryDTO>> {
        val categories = categoryService.findAllByEvent(eventId)
        return ApiResponse.success(categories)
    }

    @PostMapping("/{id}/delete")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    fun deleteById(@PathVariable id: Int):ApiResponse<CategoryDTO>{
        val category = categoryService.delete(id)
        return ApiResponse.success(category)
    }

    @PostMapping("/new")
    @PreAuthorize("hasRole('ADMIN')")
    fun create(@RequestBody body: CategoryInputDTO): ApiResponse<CategoryDTO> {
        val category = categoryService.create(body)
        return ApiResponse.success(category, "Category has been created")
    }
}