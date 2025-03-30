package org.example.userservice.controller;

import jakarta.security.auth.message.AuthException;
import lombok.RequiredArgsConstructor;
import org.example.userservice.dto.UserDTO;
import org.example.userservice.exception.NotFoundException;
import org.example.userservice.service.AuthService;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

    @PostMapping("login")
    public ResponseEntity<UserDTO.Response.TokenAndShortUserInfo> login(
            @RequestBody UserDTO.Request.Login authRequest
    ) {
        return ResponseEntity.ok(authService.login(authRequest));
    }

    @PostMapping("register")
    public ResponseEntity<UserDTO.Response.TokenAndShortUserInfo> register(
            @RequestBody UserDTO.Request.Register authRequest
    ) {
        return ResponseEntity.ok(authService.registration(authRequest));
    }

    @PostMapping("new-access")
    public ResponseEntity<String> getNewAccessToken(@RequestBody String refreshToken
    ) {
        return ResponseEntity.ok(authService.getAccessToken(refreshToken));
    }

    // Удаление пользователя (перевод в неактивное состояние)
    @GetMapping("/unactivate")
    public ResponseEntity<String> deactivateAccount() {
        return ResponseEntity.ok(authService.deactivateAccount());
    }

    // Получение своего профиля
    @GetMapping("/logout")
    public ResponseEntity<String> logout() {
        return ResponseEntity.ok(authService.logout());
    }
}