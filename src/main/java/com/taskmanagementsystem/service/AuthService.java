package com.taskmanagementsystem.service;

import com.taskmanagementsystem.dto.RegisterDto;
import com.taskmanagementsystem.model.User;
import com.taskmanagementsystem.model.role.Role;
import com.taskmanagementsystem.repository.UserRepository;
import lombok.AllArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class AuthService {

    private final PasswordEncoder passwordEncoder;
    private final UserRepository userRepository;
    private final AuthenticationManager authenticationManager;

    public void saveUser(RegisterDto registerDto) {
        User user = new User();
        user.setEmail(registerDto.getEmail());
        user.setPassword(passwordEncoder.encode(registerDto.getPassword()));
        user.setRole(Role.ROLE_USER);
        userRepository.save(user);
    }
}
