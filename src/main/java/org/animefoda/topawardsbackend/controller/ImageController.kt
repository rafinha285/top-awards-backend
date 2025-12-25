package org.animefoda.topawardsbackend.controller

import org.animefoda.topawardsbackend.response.ApiResponse
import org.animefoda.topawardsbackend.service.ImageCleanupService
import org.animefoda.topawardsbackend.service.ImageStorageService
import org.springframework.beans.factory.annotation.Value
import org.springframework.core.io.Resource
import org.springframework.core.io.UrlResource
import org.springframework.http.HttpHeaders
import org.springframework.http.MediaType
import org.springframework.http.ResponseEntity
import org.springframework.security.access.prepost.PreAuthorize
import org.springframework.web.bind.annotation.*
import org.springframework.web.multipart.MultipartFile
import java.nio.file.Files
import java.nio.file.Paths


@RestController
@RequestMapping("/images")
class ImageController(
    private val imageStorageService: ImageStorageService,
    private val imageCleanupService: ImageCleanupService,
    @Value("\${app.upload.path:./uploads}")
    private val uploadPath: String
) {

    data class ImageUploadResponse(
        val url: String,
        val relativePath: String
    )

    /**
     * Servir imagem
     * GET /images/{folder}/{filename}
     * Ex: GET /images/nominees/uuid.jpg
     */
    @GetMapping("/{folder}/{filename}")
    fun getImage(
        @PathVariable folder: String,
        @PathVariable filename: String
    ): ResponseEntity<Resource> {
        return serveImage("$folder/$filename")
    }

    /**
     * Servir imagem sem subpasta
     * GET /images/{filename}
     */
    @GetMapping("/{filename}")
    fun getImageWithoutFolder(
        @PathVariable filename: String
    ): ResponseEntity<Resource> {
        return serveImage(filename)
    }

    private fun serveImage(relativePath: String): ResponseEntity<Resource> {
        val filePath = Paths.get(uploadPath).resolve(relativePath).normalize()
        val resource = UrlResource(filePath.toUri())

        if (!resource.exists() || !resource.isReadable) {
            return ResponseEntity.notFound().build()
        }

        // Detectar tipo de conteúdo
        val contentType = try {
            Files.probeContentType(filePath) ?: "application/octet-stream"
        } catch (e: Exception) {
            "application/octet-stream"
        }

        return ResponseEntity.ok()
            .contentType(MediaType.parseMediaType(contentType))
            .header(HttpHeaders.CACHE_CONTROL, "max-age=2592000") // Cache por 30 dias
            .body(resource)
    }

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

    /**
     * Limpar imagens não utilizadas
     * POST /images/cleanup?dryRun=true
     * @param dryRun Se true, apenas lista as imagens que seriam deletadas sem deletar de fato
     */
    @PostMapping("/cleanup")
    @PreAuthorize("hasRole('ADMIN')")
    fun cleanupUnusedImages(
        @RequestParam("dryRun", required = false, defaultValue = "true") dryRun: Boolean
    ): ApiResponse<ImageCleanupService.CleanupResult> {
        val result = imageCleanupService.cleanupUnusedImages(dryRun)
        val message = if (dryRun) {
            "Dry run: ${result.unusedFilesDeleted} imagens seriam deletadas"
        } else {
            "${result.unusedFilesDeleted} imagens não utilizadas foram deletadas"
        }
        return ApiResponse.success(result, message)
    }
}

