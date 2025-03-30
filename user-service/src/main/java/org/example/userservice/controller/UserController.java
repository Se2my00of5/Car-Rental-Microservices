package org.example.userservice.controller;

import jakarta.security.auth.message.AuthException;
import lombok.RequiredArgsConstructor;
import org.example.userservice.dto.UserDTO;
import org.example.userservice.exception.NotFoundException;
import org.example.userservice.service.AuthService;
import org.example.userservice.service.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("user")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    // Получение своего профиля
    @GetMapping("/profile")
    public ResponseEntity<UserDTO.Response.Profile> getMyProfile(@RequestBody String accessToken) {
        return ResponseEntity.ok(userService.getMyProfile());
    }

    // Редактирование профиля
    @PutMapping("/profile")
    public ResponseEntity<UserDTO.Response.Profile> updateProfile(@RequestBody UserDTO.Request.EditProfile userData) {
        return ResponseEntity.ok(userService.updateProfile(userData));
    }

    // Получение профиля по ID
    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/profile/{id}")
    public ResponseEntity<UserDTO.Response.Profile> getProfileById(@PathVariable Long id) {
        return ResponseEntity.ok(userService.getProfileById(id));
    }

}
