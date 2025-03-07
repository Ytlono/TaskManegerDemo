package com.springtest.Demo.controller;

import com.springtest.Demo.auth.dto.AuthRequest;
import com.springtest.Demo.auth.dto.RegisterDTO;
import com.springtest.Demo.repository.User;
import com.springtest.Demo.service.AuthService;
import com.springtest.Demo.service.ConfirmationService;
import com.springtest.Demo.service.SessionService;
import com.springtest.Demo.service.UserService;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;

@Controller
@RequestMapping("/auth")
@SessionAttributes("pendingUser")
public class AuthController {

    private final AuthService authService;
    private final ConfirmationService confirmationService;
    private final SessionService sessionService;
    private final UserService userService;

    public AuthController(AuthService authService, ConfirmationService confirmationService,
                         AuthenticationManager authenticationManager, SessionService sessionService,
                         UserService userService) {
        this.authService = authService;
        this.confirmationService = confirmationService;
        this.sessionService = sessionService;
        this.userService = userService;
    }

    @GetMapping("/register")
    public String showRegistrationForm(Model model) {
        model.addAttribute("registerRequest", new RegisterDTO());
        return "register";
    }

    @PostMapping("/register")
    public String register(@ModelAttribute RegisterDTO registerRequest, Model model) {
        System.out.println("Полученные данные: " + registerRequest.getEmail() + ", " + registerRequest.getUsername() + ", " + registerRequest.getPassword());

        if (registerRequest.getPassword() == null || registerRequest.getPassword().isEmpty()) {
            model.addAttribute("error", "Пароль не может быть пустым");
            return "register";
        }

        confirmationService.sendConfirmationEmail(registerRequest.getEmail());
        model.addAttribute("pendingUser", registerRequest);
        model.addAttribute("email", registerRequest.getEmail());

        return "confirm";
    }

    @PostMapping("/confirm")
    public String confirmEmail(
            @RequestParam String email,
            @RequestParam String code,
            @SessionAttribute("pendingUser") RegisterDTO pendingUser,
            Model model) {

        if (confirmationService.isConfirmed(email, code)) {
            confirmationService.removeConfirmedEmail(email);

            authService.register(pendingUser);

            return "redirect:/auth/login";
        } else {
            model.addAttribute("email", email);
            model.addAttribute("error", "Неверный код подтверждения");
            return "confirm";
        }
    }

    @GetMapping("/login")
    public String showLoginPage(
            @RequestParam(value = "error", required = false) String error,
            @RequestParam(value = "logout", required = false) String logout,
            @RequestParam(value = "expired", required = false) String expired,
            Model model) {
        
        if (error != null) {
            model.addAttribute("error", "Неверный логин или пароль");
        }
        
        if (logout != null) {
            model.addAttribute("message", "Вы успешно вышли из системы");
        }
        
        if (expired != null) {
            model.addAttribute("error", "Ваша сессия истекла. Пожалуйста, войдите снова");
        }
        
        model.addAttribute("loginRequest", new AuthRequest());
        return "login";
    }
    
    @GetMapping("/logout")
    public String logout(HttpServletRequest request) {
        sessionService.removeUserFromSession(request);

        HttpSession session = request.getSession(false);
        if (session != null) {
            session.invalidate();
        }
        
        return "redirect:/auth/login?logout=true";
    }
}
