package com.springtest.Demo.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class MainController {

    @GetMapping("/")
    public String signup(Model model) {
        model.addAttribute("title", "Sing UP Page");
        return "signup";
    }

}
