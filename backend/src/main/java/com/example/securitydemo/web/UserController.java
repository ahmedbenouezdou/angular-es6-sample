package com.example.securitydemo.web;

import com.example.securitydemo.user.User;
import com.example.securitydemo.user.UserService;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/v1/users")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping
    @PreAuthorize("hasRole('ADMIN')")
    public List<UserSummary> findAll() {
        return userService.findAll().stream()
            .map(user -> new UserSummary(
                user.getSub(),
                user.getEmail(),
                user.getName(),
                user.getPicture(),
                user.getRoles().stream().map(role -> role.getName()).toList()
            ))
            .toList();
    }

    public record UserSummary(String sub, String email, String name, String picture, List<String> roles) {}
}
