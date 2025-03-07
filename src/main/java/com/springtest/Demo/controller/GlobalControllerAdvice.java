package com.springtest.Demo.controller;

import com.springtest.Demo.auth.dto.UserSessionDTO;
import com.springtest.Demo.service.SessionService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestHeader;

import java.util.HashMap;
import java.util.Map;

@ControllerAdvice
public class GlobalControllerAdvice {

    private final SessionService sessionService;

    @Autowired
    public GlobalControllerAdvice(SessionService sessionService) {
        this.sessionService = sessionService;
    }

    @ModelAttribute
    public void addAttributes(Model model, HttpServletRequest request) {
        if (sessionService.isAuthenticated()) {
            UserSessionDTO currentUser = sessionService.getUserFromSession(request);
            if (currentUser != null) {
                model.addAttribute("currentUser", currentUser);
                model.addAttribute("isAuthenticated", true);
                model.addAttribute("userRole", currentUser.getRole());
            }
        } else {
            model.addAttribute("isAuthenticated", false);
        }
    }
}
