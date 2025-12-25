package org.animefoda.topawardsbackend.entities.user

import jakarta.persistence.*
import org.animefoda.topawardsbackend.entities.BaseEntity
import org.springframework.security.core.GrantedAuthority
import org.springframework.security.core.authority.SimpleGrantedAuthority
import org.springframework.security.core.userdetails.UserDetails

@Entity
@Table(name = "users")
open class UserEntity : BaseEntity<UserEntity, UserDTO>(), UserDetails {

    // Em Kotlin, declarar 'var' JÁ CRIA getter e setter automaticamente!
    // Não precisa de @Getter nem @Setter
    @Column(unique = true, nullable = false)
    var email: String = ""

    @Column(name = "password", nullable = false)
    var passwordHash: String = ""

    @Column(name = "name", nullable = false)
    var name: String = ""

    @Column(name = "type", nullable = false)
    @Enumerated(EnumType.STRING)
    var type: UserType = UserType.USER

    @Column(nullable = false)
    var active: Boolean = true

    // Seus métodos de lógica continuam aqui
    override fun toDTO(): UserDTO {
        // id vem da BaseEntity (se ela for Java, use getId())
        return UserDTO(id ?: 0, email, name, type)
    }

    override fun getAuthorities(): Collection<GrantedAuthority> {
        if (this.type == UserType.ADMIN) {
            return listOf(
                SimpleGrantedAuthority("ROLE_ADMIN"),
                SimpleGrantedAuthority("ROLE_USER")
            )
        }
        return listOf(SimpleGrantedAuthority("ROLE_USER"))
    }

    // Métodos obrigatórios do UserDetails
    override fun getUsername(): String = email
    override fun getPassword(): String = passwordHash
    override fun isAccountNonExpired(): Boolean = true
    override fun isAccountNonLocked(): Boolean = true
    override fun isCredentialsNonExpired(): Boolean = true
    override fun isEnabled(): Boolean = active
}