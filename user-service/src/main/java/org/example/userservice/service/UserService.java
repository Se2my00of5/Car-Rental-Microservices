package org.example.userservice.service;

import io.jsonwebtoken.Claims;
import jakarta.security.auth.message.AuthException;
import lombok.AllArgsConstructor;
import lombok.NonNull;
import org.example.userservice.dto.UserDTO;
import org.example.userservice.exception.AuthenticationException;
import org.example.userservice.exception.NotFoundException;
import org.example.userservice.model.User;
import org.example.userservice.repository.UserRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@AllArgsConstructor
public class UserService {
    private final UserRepository repository;
    private final JwtProvider jwtProvider;

    public UserDTO.Response.Profile getMyProfile() {
        String email =(String) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        final User user = repository.findByEmail(email)
                .orElseThrow(() -> new NotFoundException("Пользователь не найден"));
        return new UserDTO.Response.Profile(
                user.getId(), user.getUsername(), user.getEmail(),
                user.getPassword(), user.getRoles()
        );
    }


    public UserDTO.Response.Profile updateProfile(@NonNull UserDTO.Request.EditProfile userData) {
        String email =(String) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        final User user = repository.findByEmail(email)
                .orElseThrow(() -> new NotFoundException("Пользователь не найден"));

        user.setUsername(userData.getUsername());
        user.setPassword(userData.getPassword());

        repository.save(user);

        return new UserDTO.Response.Profile(
                user.getId(), user.getUsername(), user.getEmail(),
                user.getPassword(), user.getRoles()
        );

    }

    public UserDTO.Response.Profile getProfileById(@NonNull Long id) {
        final User user = repository.findById(id)
                .orElseThrow(() -> new NotFoundException("Пользователь не найден"));

        return new UserDTO.Response.Profile(
                user.getId(), user.getUsername(), user.getEmail(),
                user.getPassword(), user.getRoles()
        );
    }
    @Transactional
    public UserDTO.Response.GetMessage deleteUser(Long id) {
        User user = repository.findById(id)
                .orElseThrow(() -> new RuntimeException("нет такого пользователя"));

        // Удаляем связи с ролями (очищаем Set)
        user.getRoles().clear();

        // Сохраняем обновлённого пользователя
        repository.save(user);

        // Теперь можно безопасно удалить пользователя
        repository.delete(user);

        return new UserDTO.Response.GetMessage("success");
    }

}
