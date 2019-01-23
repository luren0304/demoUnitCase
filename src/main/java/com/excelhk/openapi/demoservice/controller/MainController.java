package com.excelhk.openapi.demoservice.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

/**
 * @author anita
 */
@Controller
public class MainController {
    @GetMapping("/index")
    public String index() {
        return "index";
    }
/*	@GetMapping("/login")
    public String login() {
        return "login";
    }*/
}
