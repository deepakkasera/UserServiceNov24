package com.example.userservicenov24.controllers;

import com.example.userservicenov24.dtos.LogOutRequestDto;
import com.example.userservicenov24.dtos.LoginRequestDto;
import com.example.userservicenov24.dtos.SignUpRequestDto;
import com.example.userservicenov24.dtos.UserDto;
import com.example.userservicenov24.models.Token;
import com.example.userservicenov24.models.User;
import com.example.userservicenov24.services.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/users")
public class UserController {
    private UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }


    @PostMapping("/login")
    public Token login(@RequestBody LoginRequestDto requestDto) {
        return null;
    }

    @PostMapping("/signup")
    public UserDto signUp(@RequestBody SignUpRequestDto requestDto) {

        User user = userService.signUp(
                requestDto.getName(),
                requestDto.getEmail(),
                requestDto.getPassword()
        );

        return UserDto.from(user);
    }

    @GetMapping("/logout")
    public ResponseEntity<Void> logout(LogOutRequestDto requestDto) {
        return null;
    }

    @GetMapping("/validate")
    public UserDto validateToken(String token) {
        return null;
    }
}
