package org.animefoda.topawardsbackend.entities.user;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.animefoda.topawardsbackend.entities.BaseEntity;
import org.jspecify.annotations.Nullable;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.List;

@Entity
@Table(name = "users")
@Getter
@Setter
public class UserEntity extends BaseEntity<UserDTO> implements UserDetails {
    @Column(unique = true, nullable = false)
    private String email;

    @Column(nullable = false)
    private String password;

    @Column(name = "name", unique = true, nullable = false)
    private String name;

    @Column(name = "type",nullable = false)
    @Enumerated(EnumType.STRING)
    private UserType type;


    @Override
    public UserDTO toDTO() {
        return new UserDTO(id,email,name,type);
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        // AQUI ESTÁ O TRUQUE DO ADMIN
        if (this.type == UserType.ADMIN) {
            return List.of(
                    new SimpleGrantedAuthority("ROLE_ADMIN"),
                    new SimpleGrantedAuthority("ROLE_USER")
            );
        }
        return List.of(new SimpleGrantedAuthority("ROLE_USER"));
    }

    @Override
    public String getPassword() {
        return this.password;
    }

    @Override
    public String getUsername() {
        return email;
    }

    @Override
    public boolean isAccountNonExpired() { return true; }
    @Override
    public boolean isAccountNonLocked() { return true; }
    @Override
    public boolean isCredentialsNonExpired() { return true; }
    @Override
    public boolean isEnabled() { return true; }
}
