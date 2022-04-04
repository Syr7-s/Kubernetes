package com.syrisa.javaexinkubernetes.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/greeting")
public class GreetingWorld {

    @GetMapping
    public String greeting(){
        return "Hello World";
    }

}
