package com.dev.backend;

import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.GetMapping;


@RestController
@CrossOrigin
public class BaseController {

    @GetMapping(value="/")
    public String hello() {
        return "Hello from Spring Boot";
    }
    
}
