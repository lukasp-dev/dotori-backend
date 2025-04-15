package com.example.ucamcodeapi.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/") // 기본 경로
public class BaseController {

    @GetMapping
    public ResponseEntity<String> home() {
        return ResponseEntity.ok("Welcome to the UCamCode API!");
    }
}
