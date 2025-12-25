package org.animefoda.topawardsbackend.service

import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Service
import org.springframework.web.multipart.MultipartFile
import java.io.IOException
import java.nio.file.Files
import java.nio.file.Path
import java.nio.file.Paths
import java.nio.file.StandardCopyOption
import java.util.UUID

@Service
class ImageStorageService(
    @Value("\${app.upload.path:/app/uploads}")
    private val uploadPath: String
) {
    
    init {
        // Cria o diretório de upload se não existir
        val path = Paths.get(uploadPath)
        if (!Files.exists(path)) {
            Files.createDirectories(path)
        }
    }

    /**
     * Salva uma imagem no sistema de arquivos
     * @param file O arquivo de imagem para salvar
     * @param folder Subpasta opcional (ex: "nominees", "categories")
     * @return O caminho relativo da imagem salva (para usar na URL)
     */
    fun saveImage(file: MultipartFile, folder: String = ""): String {
        if (file.isEmpty) {
            throw IllegalArgumentException("Arquivo vazio")
        }
        
        // Valida o tipo de arquivo
        val contentType = file.contentType ?: throw IllegalArgumentException("Tipo de arquivo desconhecido")
        if (!contentType.startsWith("image/")) {
            throw IllegalArgumentException("Apenas imagens são permitidas")
        }
        
        // Gera um nome único para o arquivo
        val originalFilename = file.originalFilename ?: "image"
        val extension = originalFilename.substringAfterLast(".", "jpg")
        val newFilename = "${UUID.randomUUID()}.$extension"
        
        // Define o caminho de destino
        val targetPath: Path = if (folder.isNotBlank()) {
            val folderPath = Paths.get(uploadPath, folder)
            if (!Files.exists(folderPath)) {
                Files.createDirectories(folderPath)
            }
            folderPath.resolve(newFilename)
        } else {
            Paths.get(uploadPath, newFilename)
        }
        
        // Copia o arquivo
        try {
            file.inputStream.use { inputStream ->
                Files.copy(inputStream, targetPath, StandardCopyOption.REPLACE_EXISTING)
            }
        } catch (e: IOException) {
            throw RuntimeException("Falha ao salvar o arquivo: ${e.message}", e)
        }
        
        // Retorna o caminho relativo para a URL
        return if (folder.isNotBlank()) {
            "$folder/$newFilename"
        } else {
            newFilename
        }
    }

    /**
     * Deleta uma imagem do sistema de arquivos
     * @param relativePath Caminho relativo da imagem (retornado por saveImage)
     * @return true se deletou com sucesso
     */
    fun deleteImage(relativePath: String): Boolean {
        if (relativePath.isBlank()) return false
        
        val targetPath = Paths.get(uploadPath, relativePath)
        return try {
            Files.deleteIfExists(targetPath)
        } catch (e: IOException) {
            false
        }
    }

    /**
     * Verifica se uma imagem existe
     * @param relativePath Caminho relativo da imagem
     */
    fun imageExists(relativePath: String): Boolean {
        if (relativePath.isBlank()) return false
        val targetPath = Paths.get(uploadPath, relativePath)
        return Files.exists(targetPath)
    }
}
