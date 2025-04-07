package org.example.userservice.service;

import exception.NotFoundException;
import lombok.AllArgsConstructor;
import lombok.NonNull;
import org.example.userservice.dto.UserDTO;

import org.example.userservice.model.Role;
import org.example.userservice.model.User;
import org.example.userservice.repository.RoleRepository;
import org.example.userservice.repository.UserRepository;
import org.springframework.stereotype.Service;

import java.util.Set;

@Service
@AllArgsConstructor
public class AdminService {
    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final JwtProvider jwtProvider;


    public UserDTO.Response.GetMessage activateAccount(Long id) {
        final User user = userRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Пользователь не найден"));

        user.setActive(true);
        userRepository.save(user);
        return new UserDTO.Response.GetMessage("success");
    }

    public UserDTO.Response.Profile getProfileById(@NonNull Long id) {
        final User user = userRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Пользователь не найден"));

        return new UserDTO.Response.Profile(
                user.getId(), user.getUsername(), user.getEmail(),
                user.getPassword(), user.getRoles()
        );
    }


    public UserDTO.Response.Profile giveAdmin(Long id) {
        final User user = userRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Пользователь не найден"));

        final Role role = roleRepository.findByName("ROLE_ADMIN").orElseThrow();

        Set<Role> roles = user.getRoles();
        roles.add(role);
        user.setRoles(roles);

        userRepository.save(user);

        return new UserDTO.Response.Profile(
                user.getId(), user.getUsername(), user.getEmail(),
                user.getPassword(), user.getRoles());
    }
}