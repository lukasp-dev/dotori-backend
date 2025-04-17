package com.dotori.backend.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class CustomErrorController {
  @RequestMapping("/error")
  public String handleError(){
    return "Oops! Something went wrong, bit it's still Dotori backend 😅";
  }
}
