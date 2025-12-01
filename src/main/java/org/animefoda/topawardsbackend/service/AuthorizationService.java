package org.animefoda.topawardsbackend.service;

import org.animefoda.topawardsbackend.entities.user.UserEntity;
import org.animefoda.topawardsbackend.entities.user.UserRepository;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class AuthorizationService implements UserDetailsService {
    private final UserRepository repository;

    public AuthorizationService(@Autowired UserRepository repository) {
        this.repository = repository;
    }
    @NotNull
    @Override
    public UserDetails loadUserByUsername(@NotNull String username) throws UsernameNotFoundException {
        UserEntity user = repository.findByEmail(username);
        if (user == null) {
            throw new UsernameNotFoundException("Usuário não encontrado com o email: " + username);
        }
        return user;
    }
}
