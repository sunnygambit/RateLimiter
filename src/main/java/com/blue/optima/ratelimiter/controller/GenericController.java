package com.blue.optima.ratelimiter.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Arrays;
import java.util.List;

@RestController
//@RequestMapping("/")
public class GenericController {

    @GetMapping("/api/v1/developers")
    public List<String> getDevelopers() {
        return Arrays.asList("sunny", "Amit");
    }

    @GetMapping("/api/v1/organizations")
    public List<String> getOrganizations() {
        return Arrays.asList("BlueOptima", "google");
    }

    @GetMapping("/hello")
    public String hello() {
        return "Hello";
    }
}
