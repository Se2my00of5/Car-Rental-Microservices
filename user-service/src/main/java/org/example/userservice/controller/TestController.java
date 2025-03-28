package org.example.userservice.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.Mapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/user-services")
public class TestController {

    @GetMapping("/test")
    public String getTest() {
        return "List of users";
    }

    @GetMapping("/test2")
    public String getTest2() {
        return "List of users2";
    }
}
