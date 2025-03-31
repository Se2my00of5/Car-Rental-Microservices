package org.example.userservice.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.example.userservice.dto.UserDTO;
import org.example.userservice.service.AuthService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

    @PostMapping("login")
    @Operation(summary = "Логин", security = {})
    public ResponseEntity<UserDTO.Response.TokenAndShortUserInfo> login(
            @RequestBody UserDTO.Request.Login authRequest
    ) {
        return ResponseEntity.ok(authService.login(authRequest));
    }

    @PostMapping("register")
    @Operation(summary = "Регистрация", security = {})
    public ResponseEntity<UserDTO.Response.TokenAndShortUserInfo> register(
            @Valid @RequestBody UserDTO.Request.Register authRequest
    ) {
        return ResponseEntity.ok(authService.registration(authRequest));
    }

    @PostMapping("new-access")
    @Operation(summary = "Получение нового access токена", security = {})
    public ResponseEntity<UserDTO.Response.GetToken> getNewAccessToken(@RequestBody UserDTO.Request.RefreshToken token) {
        return ResponseEntity.ok(authService.getAccessToken(token));
    }

    @GetMapping("/unactivate")
    @Operation(summary = "Деактивация аккаунта", security = @SecurityRequirement(name = "bearerAuth"))
    public ResponseEntity<UserDTO.Response.GetMessage> deactivateAccount() {
        return ResponseEntity.ok(authService.deactivateAccount());
    }

    @GetMapping("/logout")
    @Operation(summary = "Выход из аккаунта", security = @SecurityRequirement(name = "bearerAuth"))
    public ResponseEntity<UserDTO.Response.GetMessage> logout() {
        return ResponseEntity.ok(authService.logout());
    }

}