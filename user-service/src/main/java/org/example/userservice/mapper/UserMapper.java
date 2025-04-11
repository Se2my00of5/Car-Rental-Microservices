package org.example.userservice.mapper;


import org.example.userservice.model.Role;
import org.example.userservice.model.User;

import java.util.Set;
import java.util.stream.Collectors;

public class UserMapper {

    public static org.example.commonservice.dto.User UserToUserDTO(User user) {
        Set<org.example.commonservice.dto.Role> roles = user.getRoles().stream()
                .map(role -> new org.example.commonservice.dto.Role(role.getId(), role.getName()))
                .collect(Collectors.toSet());

        return org.example.commonservice.dto.User
                .builder()
                .id(user.getId())
                .username(user.getUsername())
                .email(user.getEmail())
                .password(user.getPassword())
                .roles(roles)
                .isActive(user.isActive())
                .build();
    }

    public static Set<org.example.commonservice.dto.Role> RolesToRoleDTO(Set<Role> roles) {
        return roles.stream()
                .map(role -> new org.example.commonservice.dto.Role(role.getId(), role.getName()))
                .collect(Collectors.toSet());
    }
}