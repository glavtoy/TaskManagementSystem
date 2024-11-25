package com.taskmanagementsystem.controller;

import com.taskmanagementsystem.dto.JwtRequest;
import com.taskmanagementsystem.dto.JwtResponse;
import com.taskmanagementsystem.dto.RegisterDto;
import com.taskmanagementsystem.exception.ApplicationError;
import com.taskmanagementsystem.service.AuthService;
import com.taskmanagementsystem.service.UserService;
import com.taskmanagementsystem.util.JwtTokenUtil;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

    private final UserService userService;
    private final AuthService authService;
    private final JwtTokenUtil jwtTokenUtil;
    private final AuthenticationManager authenticationManager;

    @PostMapping("/sign_in")
    public ResponseEntity<?> createAuthToken(@RequestBody JwtRequest request) {
        try {
            Authentication authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(request.getEmail(), request.getPassword()));
            SecurityContextHolder.getContext().setAuthentication(authentication);
        } catch (BadCredentialsException e) {
            return new ResponseEntity<>(new ApplicationError(HttpStatus.UNAUTHORIZED.value(), "Неверный логин или пароль"), HttpStatus.UNAUTHORIZED);
        }
        UserDetails userDetails = userService.loadUserByUsername(request.getEmail());
        String token = jwtTokenUtil.generateToken(userDetails);
        return ResponseEntity.ok(new JwtResponse(token));
    }

    @PostMapping("/sign_up")
    public ResponseEntity<?> createUser(@RequestBody RegisterDto registerDto) {
        if (userService.findByEmail(registerDto.getEmail()).isPresent()) {
            return new ResponseEntity<>(new ApplicationError(HttpStatus.BAD_REQUEST.value(), "Пользователь уже существует"), HttpStatus.BAD_REQUEST);
        }
        authService.saveUser(registerDto);
        return ResponseEntity.ok("Пользователь зарегистрирован!");
    }
}
