package org.animefoda.topawardsbackend.controller

import org.animefoda.topawardsbackend.entities.nominee.NomineeDTO
import org.animefoda.topawardsbackend.entities.nominee.NomineeEntity
import org.animefoda.topawardsbackend.entities.nominee.NomineeRepository
import org.animefoda.topawardsbackend.response.ApiResponse
import org.springframework.security.access.prepost.PreAuthorize
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/nominee")
open class NomineeController(
    private val repository: NomineeRepository
) {

    @GetMapping
    fun findAll(): ApiResponse<List<NomineeDTO>> {
        val nominees = repository.findAll().map { it.toDTO() }
        return ApiResponse.success(nominees)
    }

    @PostMapping("/new")
    @PreAuthorize("hasRole('ADMIN')")
    open fun create(@RequestBody dto: NomineeDTO): ApiResponse<NomineeDTO> {

        val nominee = NomineeEntity().apply {
//            this.id = dto.id
            this.name = dto.name
        }

        val savedNominee = repository.save(nominee)
        return ApiResponse.success(savedNominee.toDTO(), message = "Nomine created successfully")
    }

}