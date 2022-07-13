package com.service.controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController
public class Controller {

    @GetMapping("/start")
    public String alive() {
        return "Your project working correctly";
    }

}
