package org.animefoda.topawardsbackend.controller

import org.animefoda.topawardsbackend.response.ApiResponse
import org.animefoda.topawardsbackend.service.ImageStorageService
import org.springframework.security.access.prepost.PreAuthorize
import org.springframework.web.bind.annotation.*
import org.springframework.web.multipart.MultipartFile


@RestController
@RequestMapping("/images")
class ImageController(
    private val imageStorageService: ImageStorageService
) {

    data class ImageUploadResponse(
        val url: String,
        val relativePath: String
    )
    /**
     * Upload de imagem
     * POST /images/upload?folder=nominees
     */
    @PostMapping("/upload")
    @PreAuthorize("hasRole('ADMIN')")
    fun uploadImage(
        @RequestParam("file") file: MultipartFile,
        @RequestParam("folder", required = false, defaultValue = "") folder: String
    ): ApiResponse<ImageUploadResponse> {
        val relativePath = imageStorageService.saveImage(file, folder)
        
        // A URL será relativa ao frontend que serve as imagens
        // Ex: /images/nominees/uuid.jpg
        val url = "/images/$relativePath"
        
        return ApiResponse.success(
            ImageUploadResponse(url, relativePath),
            "Imagem enviada com sucesso"
        )
    }

    /**
     * Deletar imagem
     * POST /images/delete
     */
    @PostMapping("/delete")
    @PreAuthorize("hasRole('ADMIN')")
    fun deleteImage(
        @RequestParam("path") relativePath: String
    ): ApiResponse<Boolean> {
        val deleted = imageStorageService.deleteImage(relativePath)
        return if (deleted) {
            ApiResponse.success(true, "Imagem deletada com sucesso")
        } else {
            ApiResponse.success(false, "Imagem não encontrada ou já foi deletada")
        }
    }
}
