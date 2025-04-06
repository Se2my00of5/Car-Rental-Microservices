package org.example.userservice.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import lombok.RequiredArgsConstructor;
import org.example.userservice.dto.UserDTO;
import org.example.userservice.service.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("user")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @GetMapping("/profile")
    @Operation(summary = "Получение своего профиля", security = @SecurityRequirement(name = "bearerAuth"))
    public ResponseEntity<UserDTO.Response.Profile> getMyProfile(@Parameter(hidden = true) @RequestHeader("Authorization") String authHeader) {
        return ResponseEntity.ok(userService.getMyProfile(authHeader));
    }

    @PutMapping("/profile")
    @Operation(summary = "Редактирование профиля", security = @SecurityRequirement(name = "bearerAuth"))
    public ResponseEntity<UserDTO.Response.Profile> updateProfile(
            @Parameter(hidden = true) @RequestHeader("Authorization") String authHeader,
            @RequestBody UserDTO.Request.EditProfile userData)
    {
        return ResponseEntity.ok(userService.updateProfile(authHeader, userData));
    }

//    @PostMapping("/delete/{id}")
//    @Operation(summary = "Удаление пользователя", security = @SecurityRequirement(name = "bearerAuth"))
//    public UserDTO.Response.GetMessage deleteUser(@PathVariable Long id) {
//        return userService.deleteUser(id);
//    }

}
