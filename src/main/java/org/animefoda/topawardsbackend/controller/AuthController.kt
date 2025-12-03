package org.animefoda.topawardsbackend.controller

import jakarta.servlet.http.HttpServletRequest
import org.animefoda.topawardsbackend.component.EmailValidator
import org.animefoda.topawardsbackend.entities.session.SessionEntity
import org.animefoda.topawardsbackend.entities.session.UserSessionRepository
import org.animefoda.topawardsbackend.entities.user.UserEntity
import org.animefoda.topawardsbackend.entities.user.UserRepository
import org.animefoda.topawardsbackend.entities.user.UserType
import org.animefoda.topawardsbackend.exception.AlreadyExistsException
import org.animefoda.topawardsbackend.exception.BadRequestException
import org.animefoda.topawardsbackend.request.AuthResponse
import org.animefoda.topawardsbackend.request.LoginRequest
import org.animefoda.topawardsbackend.request.RegisterRequest
import org.animefoda.topawardsbackend.response.ApiResponse
import org.animefoda.topawardsbackend.service.TokenService
import org.springframework.security.authentication.AuthenticationManager
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import java.time.LocalDateTime

@RestController
@RequestMapping("/auth")
class AuthController(
    private val authenticationManager: AuthenticationManager,
    private val userRepository: UserRepository,
    private val userSessionRepository: UserSessionRepository,
    private val tokenService: TokenService,
    private val passwordEncoder: PasswordEncoder,
    private val emailValidator: EmailValidator
) {
    @PostMapping("/register")
    fun register(@RequestBody data: RegisterRequest, request: HttpServletRequest): ApiResponse<AuthResponse> {
        val userIp = request.remoteAddr


        if (!emailValidator.isValid(data.email)) {
            throw RuntimeException("Email inválido ou domínio inexistente.")
        }

        val existingAccountsOnDevice = userSessionRepository.countUniqueUsersByFingerprint(data.fingerprint)

        if (existingAccountsOnDevice >= 2) {
            throw AlreadyExistsException("Limite de contas atingido nesse dispositivo")
        }

        if (userRepository.findByEmail(data.email) != null) {
            throw AlreadyExistsException("Email ja existe")
        }

        val newUser = UserEntity().apply {
            email = data.email
            passwordHash = passwordEncoder.encode(data.password).toString()
            name = data.name
            type = UserType.USER
        }
        val savedUser = userRepository.save(newUser)

        this.saveSession(savedUser, data.fingerprint, userIp)

        val token = tokenService.generateToken(savedUser)

        return ApiResponse.success(AuthResponse(token, savedUser.toDTO()), "Conta criada")
    }

    @PostMapping("/login")
    fun login(@RequestBody data: LoginRequest, request: HttpServletRequest): ApiResponse<AuthResponse> {
        // Autentica
        val usernamePassword = UsernamePasswordAuthenticationToken(data.email, data.password)
        val auth = authenticationManager.authenticate(usernamePassword)
        val user = auth.principal as UserEntity

        // Gera Token
        val token = tokenService.generateToken(user)

        saveSession(user, data.fingerprint, request.remoteAddr)

        return ApiResponse.success(AuthResponse(token, user.toDTO()))
    }


    private fun saveSession(user: UserEntity, fingerprint: String, ip: String): SessionEntity{
        val session = SessionEntity().apply {
            this.user = user
            this.fingerprint = fingerprint
            this.ipAddress = ip
            this.loginAt = LocalDateTime.now()
        }
        return userSessionRepository.save(session)
    }
}