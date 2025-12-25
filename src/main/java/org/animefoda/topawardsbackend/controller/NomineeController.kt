package org.animefoda.topawardsbackend.controller

import org.animefoda.topawardsbackend.entities.nominee.NomineeDTO
import org.animefoda.topawardsbackend.response.ApiResponse
import org.animefoda.topawardsbackend.service.ImageStorageService
import org.animefoda.topawardsbackend.service.NomineeService
import org.springframework.security.access.prepost.PreAuthorize
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController
import org.springframework.web.multipart.MultipartFile

@RestController
@RequestMapping("/nominee")
open class NomineeController(
    private val nomineeService: NomineeService,
    private val imageStorageService: ImageStorageService
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

    @PostMapping("/{id}/update")
    @PreAuthorize("hasRole('ADMIN')")
    open fun update(@PathVariable id: Int, @RequestBody dto: NomineeDTO): ApiResponse<*> {
        return ApiResponse.success(nomineeService.update(id, dto), message = "Nominee updated successfully")
    }

    @PostMapping("/{id}/delete")
    @PreAuthorize("hasRole('ADMIN')")
    open fun delete(@PathVariable("id") id: Int): ApiResponse<NomineeDTO> {
        return ApiResponse.success(nomineeService.delete(id))
    }

    /**
     * Upload de imagem para um Nominee
     * POST /nominee/{id}/upload-image
     */
    @PostMapping("/{id}/upload-image")
    @PreAuthorize("hasRole('ADMIN')")
    open fun uploadImage(
        @PathVariable("id") id: Int,
        @RequestParam("file") file: MultipartFile
    ): ApiResponse<NomineeDTO> {
        // Salva a imagem na pasta "nominees"
        val relativePath = imageStorageService.saveImage(file, "nominees")
        val imageUrl = "/images/$relativePath"
        
        // Atualiza o nominee com a URL da imagem
        val updatedNominee = nomineeService.updateImage(id, imageUrl)
        return ApiResponse.success(updatedNominee, "Imagem enviada com sucesso")
    }
}