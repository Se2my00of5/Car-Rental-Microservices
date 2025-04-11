package org.example.userservice.service;

import lombok.AllArgsConstructor;
import lombok.NonNull;
import org.example.commonservice.dto.UserDTO;
import org.example.commonservice.exception.NotFoundException;
import org.example.userservice.mapper.UserMapper;
import org.example.userservice.model.User;
import org.example.userservice.repository.UserRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@AllArgsConstructor
public class UserService {
    private final UserRepository repository;
    private final JwtProvider jwtProvider;

    public UserDTO.Response.Profile getMyProfile(String authHeader) {
        String email = jwtProvider.getEmailFromAuthHeader(authHeader);

        final User user = repository.findByEmail(email)
                .orElseThrow(() -> new NotFoundException("Пользователь не найден"));


        return new UserDTO.Response.Profile(
                user.getId(), user.getUsername(), user.getEmail(),
                user.getPassword(), UserMapper.RolesToRoleDTO(user.getRoles())
        );
    }


    public UserDTO.Response.Profile updateProfile(String authHeader, @NonNull UserDTO.Request.EditProfile userData) {
        String email = jwtProvider.getEmailFromAuthHeader(authHeader);

        final User user = repository.findByEmail(email)
                .orElseThrow(() -> new NotFoundException("Пользователь не найден"));

        user.setUsername(userData.getUsername());
        user.setPassword(userData.getPassword());

        repository.save(user);

        return new UserDTO.Response.Profile(
                user.getId(), user.getUsername(), user.getEmail(),
                user.getPassword(), UserMapper.RolesToRoleDTO(user.getRoles())
        );

    }

    public UserDTO.Response.Profile getProfileById(@NonNull Long id) {
        final User user = repository.findById(id)
                .orElseThrow(() -> new NotFoundException("Пользователь не найден"));

        return new UserDTO.Response.Profile(
                user.getId(), user.getUsername(), user.getEmail(),
                user.getPassword(), UserMapper.RolesToRoleDTO(user.getRoles())
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
