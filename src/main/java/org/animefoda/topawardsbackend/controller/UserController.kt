package org.animefoda.topawardsbackend.controller

import org.animefoda.topawardsbackend.entities.user.UserDTO
import org.animefoda.topawardsbackend.response.ApiResponse
import org.animefoda.topawardsbackend.service.UserService
import org.springframework.security.access.prepost.PreAuthorize
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/users")
class UserController(private val userService: UserService) {
    @GetMapping
    fun findAll(): ApiResponse<List<UserDTO>> {
        return ApiResponse.success(userService.findAll())
    }

    @GetMapping("/{id}")
    fun findById(@PathVariable("id") id: Int): ApiResponse<UserDTO> {
        return ApiResponse.success(userService.findById(id))
    }

    @PostMapping("/{id}/update")
    @PreAuthorize("hasRole('ADMIN')")
    fun update(@PathVariable("id") id: Int, @RequestBody user: UserDTO): ApiResponse<UserDTO> {
        return ApiResponse.success(userService.update(id, user))
    }
}