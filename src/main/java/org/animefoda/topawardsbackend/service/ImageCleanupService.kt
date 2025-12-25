package org.animefoda.topawardsbackend.service

import org.animefoda.topawardsbackend.entities.nominee.NomineeRepository
import org.springframework.beans.factory.annotation.Value
import org.springframework.scheduling.annotation.Scheduled
import org.springframework.stereotype.Service
import org.slf4j.LoggerFactory
import java.nio.file.Files
import java.nio.file.Path
import java.nio.file.Paths
import kotlin.io.path.isRegularFile
import kotlin.io.path.name

/**
 * Serviço para limpar imagens não utilizadas do sistema de arquivos.
 * Compara as imagens no disco com as referências no banco de dados
 * e remove as que não estão sendo usadas.
 */
@Service
class ImageCleanupService(
    private val nomineeRepository: NomineeRepository,
    @Value("\${app.upload.path:./uploads}")
    private val uploadPath: String
) {
    private val logger = LoggerFactory.getLogger(ImageCleanupService::class.java)

    /**
     * Resultado da limpeza de imagens
     */
    data class CleanupResult(
        val totalFilesScanned: Int,
        val unusedFilesDeleted: Int,
        val deletedFiles: List<String>,
        val errors: List<String>
    )

    /**
     * Executa a limpeza de imagens não utilizadas.
     * @param dryRun Se true, apenas lista as imagens que seriam deletadas sem deletar de fato
     * @return Resultado da limpeza
     */
    fun cleanupUnusedImages(dryRun: Boolean = false): CleanupResult {
        val basePath = Paths.get(uploadPath)
        
        if (!Files.exists(basePath)) {
            logger.warn("Diretório de uploads não existe: $uploadPath")
            return CleanupResult(0, 0, emptyList(), listOf("Diretório não existe: $uploadPath"))
        }

        // Coleta todas as URLs de imagens usadas no banco de dados
        val usedImageUrls = collectUsedImageUrls()
        logger.info("Encontradas ${usedImageUrls.size} imagens referenciadas no banco")

        // Converte URLs para caminhos relativos
        val usedRelativePaths = usedImageUrls
            .filterNotNull()
            .map { url -> url.removePrefix("/images/") }
            .toSet()

        // Escaneia todos os arquivos no diretório de uploads
        val allFiles = mutableListOf<Path>()
        val errors = mutableListOf<String>()
        
        try {
            Files.walk(basePath).use { stream ->
                stream.filter { it.isRegularFile() }
                    .forEach { allFiles.add(it) }
            }
        } catch (e: Exception) {
            logger.error("Erro ao escanear diretório: ${e.message}")
            errors.add("Erro ao escanear: ${e.message}")
        }

        logger.info("Encontrados ${allFiles.size} arquivos no disco")

        // Identifica arquivos não usados
        val unusedFiles = allFiles.filter { file ->
            val relativePath = basePath.relativize(file).toString()
            !usedRelativePaths.contains(relativePath)
        }

        logger.info("Encontrados ${unusedFiles.size} arquivos não utilizados")

        // Deleta os arquivos não usados (se não for dry run)
        val deletedFiles = mutableListOf<String>()
        
        for (file in unusedFiles) {
            val relativePath = basePath.relativize(file).toString()
            
            if (dryRun) {
                deletedFiles.add(relativePath)
                logger.info("[DRY RUN] Seria deletado: $relativePath")
            } else {
                try {
                    Files.delete(file)
                    deletedFiles.add(relativePath)
                    logger.info("Deletado: $relativePath")
                } catch (e: Exception) {
                    logger.error("Erro ao deletar $relativePath: ${e.message}")
                    errors.add("Falha ao deletar $relativePath: ${e.message}")
                }
            }
        }

        return CleanupResult(
            totalFilesScanned = allFiles.size,
            unusedFilesDeleted = deletedFiles.size,
            deletedFiles = deletedFiles,
            errors = errors
        )
    }

    /**
     * Coleta todas as URLs de imagens usadas em todas as entidades do sistema.
     * Adicione aqui outras entidades que possuem campo de imagem.
     */
    private fun collectUsedImageUrls(): Set<String?> {
        val usedUrls = mutableSetOf<String?>()

        // Coleta imageUrl de todos os Nominees
        nomineeRepository.findAll().forEach { nominee ->
            nominee.imageUrl?.let { usedUrls.add(it) }
        }

        // TODO: Adicionar outras entidades com imagem aqui
        // Ex: categoryRepository.findAll().forEach { it.imageUrl?.let { usedUrls.add(it) } }

        return usedUrls
    }

    /**
     * Job agendado para limpar imagens não utilizadas.
     * Roda todo domingo às 3h da manhã.
     * Descomente a anotação @Scheduled para ativar.
     */
    // @Scheduled(cron = "0 0 3 * * SUN")
    fun scheduledCleanup() {
        logger.info("Iniciando limpeza agendada de imagens não utilizadas...")
        val result = cleanupUnusedImages(dryRun = false)
        logger.info("Limpeza concluída: ${result.unusedFilesDeleted} arquivos deletados de ${result.totalFilesScanned} escaneados")
    }
}
