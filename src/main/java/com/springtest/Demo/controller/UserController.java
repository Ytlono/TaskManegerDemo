package com.springtest.Demo.controller;

import com.springtest.Demo.service.UserService;
import com.springtest.Demo.repository.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@RestController
public class UserController {

    private final UserService userService;

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/{id}")
    public User getUserById(@PathVariable Long id, Model model) {
        return userService.getUser(id);
    }

    @PostMapping
    public User create(@RequestBody User user) {
        return userService.createUser(user);
    }

    @DeleteMapping(path = "{id}")
    public void delete(@PathVariable(name = "id") Long id) {
        userService.deleteUser(id);
    }

}
