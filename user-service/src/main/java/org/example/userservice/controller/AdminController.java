package org.example.userservice.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import lombok.RequiredArgsConstructor;

import org.example.commonservice.dto.UserDTO;
import org.example.userservice.service.AdminService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("admin")
@RequiredArgsConstructor
public class AdminController {

    private final AdminService adminService;

    //@PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/activate/{id}")
    @Operation(summary = "Активация аккаунта (только для ADMIN)", security = @SecurityRequirement(name = "bearerAuth"))
    public ResponseEntity<UserDTO.Response.GetMessage> activateAccount(@PathVariable Long id) {
        return ResponseEntity.ok(adminService.activateAccount(id));
    }

    //@PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/profile/{id}")
    @Operation(summary = "Получение профиля по ID (только для ADMIN)", security = @SecurityRequirement(name = "bearerAuth"))
    public ResponseEntity<UserDTO.Response.Profile> getProfileById(@PathVariable Long id) {
        return ResponseEntity.ok(adminService.getProfileById(id));
    }

    //@PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/user/{id}/role")
    @Operation(summary = "Назначение роли админа (только для ADMIN)", security = @SecurityRequirement(name = "bearerAuth"))
    public ResponseEntity<UserDTO.Response.Profile> giveAdmin(@PathVariable Long id) {
        return ResponseEntity.ok(adminService.giveAdmin(id));
    }

}
