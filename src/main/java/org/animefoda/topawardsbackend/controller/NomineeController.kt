package org.animefoda.topawardsbackend.controller

import org.animefoda.topawardsbackend.entities.nominee.NomineeDTO
import org.animefoda.topawardsbackend.response.ApiResponse
import org.animefoda.topawardsbackend.service.NomineeService
import org.springframework.security.access.prepost.PreAuthorize
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/nominee")
open class NomineeController(
    private val nomineeService: NomineeService
) {

    @GetMapping
    fun findAll(): ApiResponse<List<NomineeDTO>> {
        return ApiResponse.success(nomineeService.findAll())
    }

    @GetMapping("/{id}")
    fun findById(@PathVariable("id") id: Int): ApiResponse<NomineeDTO> {
        return ApiResponse.success(nomineeService.findById(id))
    }

    @PostMapping("/new")
    @PreAuthorize("hasRole('ADMIN')")
    open fun create(@RequestBody dto: NomineeDTO): ApiResponse<NomineeDTO> {
        return ApiResponse.success(nomineeService.create(dto), message = "Nominee created successfully")
    }

    @PostMapping("/{id}/delete")
    @PreAuthorize("hasRole('ADMIN')")
    open fun delete(@PathVariable("id") id: Int): ApiResponse<NomineeDTO> {
        return ApiResponse.success(nomineeService.delete(id))
    }
}