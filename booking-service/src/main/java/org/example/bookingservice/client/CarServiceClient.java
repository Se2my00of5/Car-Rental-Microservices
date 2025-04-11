package org.example.bookingservice.client;



import org.example.commonservice.dto.CarDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@FeignClient(name = "car-service")
public interface CarServiceClient {

    @GetMapping("car-service/user/car/{id}")
    ResponseEntity<CarDTO.Response.FullInfo> getCarById(@PathVariable Long id);

    @PutMapping("car-service/admin/car/{id}/status")
    ResponseEntity<CarDTO.Response.FullInfo> editStatus(@PathVariable Long id, @RequestBody CarDTO.Request.UpdateStatus status);
}
