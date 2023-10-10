package com.example.userservice.Controller;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/first-service/")
public class FirstController {

    @Value("${server.port}")
    private String serverPort;

    @GetMapping("/welcome")
    public String welcome() {
        return "[" + serverPort + "] welcome First service";
    }

    @GetMapping("/message")
    public String message(@RequestHeader("request") String header) {
        return "[" + serverPort + "] header=" + header;
    }

    @GetMapping("/check")
    public String check() {
        return "[" + serverPort + "] check()";
    }

}
