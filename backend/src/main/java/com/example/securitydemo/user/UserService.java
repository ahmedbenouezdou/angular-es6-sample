package com.example.securitydemo.user;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class UserService {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;

    public UserService(UserRepository userRepository, RoleRepository roleRepository) {
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
    }

    @Transactional
    public User synchronizeUser(String sub, String email, String name, String picture, List<String> roles) {
        User user = userRepository.findBySub(sub)
            .orElseGet(() -> new User(sub, email, name, picture));

        user.updateProfile(email, name, picture);
        user.setRoles(resolveRoles(roles));
        return userRepository.save(user);
    }

    @Transactional(readOnly = true)
    public List<User> findAll() {
        return userRepository.findAll();
    }

    private Set<Role> resolveRoles(List<String> roles) {
        List<String> effectiveRoles = (roles == null || roles.isEmpty())
            ? List.of("USER")
            : roles.stream()
                .map(role -> role.toUpperCase())
                .distinct()
                .collect(Collectors.toList());

        Set<Role> resolved = new LinkedHashSet<>();
        for (String roleName : effectiveRoles) {
            Role role = roleRepository.findByName(roleName)
                .orElseGet(() -> roleRepository.save(new Role(roleName)));
            resolved.add(role);
        }
        return resolved;
    }
}
