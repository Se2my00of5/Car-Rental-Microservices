package org.example.bookingservice.client;


import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import org.example.commonservice.dto.UserDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@FeignClient(name = "user-service")
public interface UserServiceClient {

    @GetMapping("user-service/user/profile")
    ResponseEntity<UserDTO.Response.Profile> getMyProfile(@Parameter(hidden = true) @RequestHeader("Authorization") String authHeader);

    @GetMapping("user-service/admin/profile/{id}")
    ResponseEntity<UserDTO.Response.Profile> getProfileById(@PathVariable Long id);
}
