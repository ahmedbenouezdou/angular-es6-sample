package com.example.securitydemo.web;

import com.example.securitydemo.user.Role;
import com.example.securitydemo.user.User;
import com.example.securitydemo.user.UserService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import org.springframework.http.ResponseEntity;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.web.bind.annotation.*;

import java.time.Instant;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/v1/auth")
public class AuthController {

    private final JwtDecoder jwtDecoder;
    private final UserService userService;

    public AuthController(JwtDecoder jwtDecoder, UserService userService) {
        this.jwtDecoder = jwtDecoder;
        this.userService = userService;
    }

    @PostMapping("/google")
    public ResponseEntity<AuthResponse> authenticateWithGoogle(@Valid @RequestBody GoogleTokenRequest request) {
        Jwt jwt = jwtDecoder.decode(request.token());
        String sub = jwt.getSubject();
        String email = jwt.getClaim("email");
        String name = jwt.getClaim("name");
        String picture = jwt.getClaim("picture");
        List<String> requestedRoles = Optional.ofNullable(jwt.getClaimAsStringList("roles"))
            .orElse(List.of());

        User user = userService.synchronizeUser(sub, email, name, picture, requestedRoles);
        List<String> effectiveRoles = user.getRoles().stream()
            .map(Role::getName)
            .sorted()
            .collect(Collectors.toList());
        return ResponseEntity.ok(new AuthResponse(
            new UserResponse(user.getSub(), user.getEmail(), user.getName(), user.getPicture(), effectiveRoles),
            effectiveRoles,
            jwt.getTokenValue(),
            jwt.getExpiresAt()
        ));
    }

    public record GoogleTokenRequest(@NotBlank String token) {}

    public record UserResponse(String sub, String email, String name, String picture, List<String> roles) {}

    public record AuthResponse(UserResponse user, List<String> roles, String token, Instant expiresAt) {}
}
