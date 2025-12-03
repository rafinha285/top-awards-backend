package org.animefoda.topawardsbackend.request

import jakarta.validation.constraints.Email
import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.Size

data class RegisterRequest(
    @field:NotBlank(message = "O nome é obrigatório")
    val name: String,
    @field:NotBlank(message = "O email é obrigatório")
    @field:Email(message = "Formato de email inválido")
    val email: String,
    @field:NotBlank(message = "A senha é obrigatória")
    @field:Size(min = 6, message = "A senha deve ter no mínimo 6 caracteres")
    val password: String,
    @field:NotBlank(message = "Fingerprint inválido")
    val fingerprint: String
)
