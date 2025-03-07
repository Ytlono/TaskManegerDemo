package com.springtest.Demo.service;

import com.springtest.Demo.auth.dto.UserSessionDTO;
import com.springtest.Demo.repository.User;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

@Service
public class SessionService {

    private final UserService userService;
    private static final String USER_SESSION_ATTRIBUTE = "currentUser";

    public SessionService(UserService userService) {
        this.userService = userService;
    }

    public String getCurrentUsername() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || !authentication.isAuthenticated()) {
            return null;
        }
        return authentication.getName();
    }


    public void saveUserToSession(HttpServletRequest request, User user) {
        HttpSession session = request.getSession(true);
        UserSessionDTO userSessionDTO = UserSessionDTO.builder()
                .id(user.getId())
                .username(user.getUsername())
                .email(user.getEmail())
                .role(user.getRole())
                .build();
        session.setAttribute(USER_SESSION_ATTRIBUTE, userSessionDTO);
    }

    public UserSessionDTO getUserFromSession(HttpServletRequest request) {
        HttpSession session = request.getSession(false);
        if (session == null) {
            return null;
        }
        return (UserSessionDTO) session.getAttribute(USER_SESSION_ATTRIBUTE);
    }


    public void removeUserFromSession(HttpServletRequest request) {
        HttpSession session = request.getSession(false);
        if (session != null) {
            session.removeAttribute(USER_SESSION_ATTRIBUTE);
        }
    }

    public boolean isAuthenticated() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        return authentication != null && authentication.isAuthenticated() && 
               !"anonymousUser".equals(authentication.getPrincipal());
    }
} 