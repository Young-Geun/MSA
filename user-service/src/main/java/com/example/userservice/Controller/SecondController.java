package com.example.userservice.Controller;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/second-service/")
public class SecondController {

    @Value("${server.port}")
    private String serverPort;

    @GetMapping("/welcome")
    public String welcome() {
        return "welcome Second service : " + serverPort;
    }

}
