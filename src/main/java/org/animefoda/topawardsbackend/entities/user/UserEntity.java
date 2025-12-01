package org.animefoda.topawardsbackend.entities.user;

import jakarta.persistence.*;
import org.animefoda.topawardsbackend.entities.BaseEntity;

@Entity
@Table(name = "users")
public class UserEntity extends BaseEntity<UserDTO> {
    @Column(unique = true, nullable = false)
    private String email;

    @Column(name = "name", unique = true, nullable = false)
    private String name;

    @Column(name = "type",nullable = false)
    @Enumerated(EnumType.STRING)
    private UserType type;


    @Override
    public UserDTO toDTO() {
        return new UserDTO(id,email,name,type);
    }
}
