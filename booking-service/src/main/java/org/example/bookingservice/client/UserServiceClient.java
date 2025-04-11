package org.example.bookingservice.client;


import io.swagger.v3.oas.annotations.Parameter;
import org.example.commonservice.dto.UserDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@FeignClient(name = "user-service")
public interface UserServiceClient {

    @GetMapping("user-service/user/profile")
    ResponseEntity<UserDTO.Response.Profile> getMyProfile(@Parameter(hidden = true) @RequestHeader("Authorization") String authHeader);
}
