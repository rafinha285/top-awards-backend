package org.animefoda.topawardsbackend.component;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.animefoda.topawardsbackend.entities.user.UserEntity;
import org.animefoda.topawardsbackend.entities.user.UserRepository;
import org.animefoda.topawardsbackend.service.TokenService;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
public class SecurityFilter extends OncePerRequestFilter {
    @Autowired
    TokenService tokenService;
    @Autowired
    UserRepository userRepository;

    @Override
    protected void doFilterInternal(@NotNull HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        var token = this.recoverToken(request);

        if(token != null){
            var login = tokenService.validateToken(token);

            // --- DEBUG ---
            if (login.isEmpty()) {
                System.out.println("❌ ERRO NO FILTRO: Token inválido ou expirado.");
            } else {
                System.out.println("✅ TOKEN VALIDADO. Usuário no token: " + login);
            }
            // -------------

            if(!login.isEmpty()){
                UserEntity user = userRepository.findByEmail(login);

                if(user != null) {
                    System.out.println("✅ USUÁRIO ENCONTRADO NO BANCO. Roles: " + user.getAuthorities()); // DEBUG
                    var authentication = new UsernamePasswordAuthenticationToken(user, null, user.getAuthorities());
                    SecurityContextHolder.getContext().setAuthentication(authentication);
                } else {
                    System.out.println("❌ ERRO NO FILTRO: Usuário não encontrado no banco de dados (Email: " + login + ")"); // DEBUG
                }
            }
        } else {
            System.out.println("⚠️ AVISO: Requisição sem token ou header inválido."); // DEBUG
        }

        filterChain.doFilter(request, response);
    }

    private String recoverToken(HttpServletRequest request) {
        var authHeader = request.getHeader("Authorization");
        if(authHeader == null) return null;
        return authHeader.replace("Bearer ", "");
    }
}
