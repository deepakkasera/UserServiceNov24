package com.example.userservicenov24.controllers;

import com.example.userservicenov24.dtos.*;
import com.example.userservicenov24.exceptions.ValidTokenNotFoundException;
import com.example.userservicenov24.models.Token;
import com.example.userservicenov24.models.User;
import com.example.userservicenov24.services.UserService;
import org.springframework.http.HttpStatus;
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
    public TokenDto login(@RequestBody LoginRequestDto requestDto) {
        Token token = userService.login(
                requestDto.getEmail(),
                requestDto.getPassword()
        );

        //Convert token to TokenDto
        return TokenDto.from(token);
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
    public ResponseEntity<Void> logout(@RequestBody LogOutRequestDto requestDto) {
        ResponseEntity<Void> responseEntity = null;

        try {
            userService.logout(requestDto.getTokenValue());

            responseEntity = new ResponseEntity<>(
                    HttpStatus.OK
            );
        } catch (ValidTokenNotFoundException e) {
            responseEntity = new ResponseEntity<>(
                    HttpStatus.BAD_REQUEST
            );
        }
        //TODO : Move the exception handling logic inside the Controller Advice.

        return responseEntity;
    }

    @GetMapping("/validate/{tokenValue}")
    public UserDto validateToken(@PathVariable String tokenValue) {
        try {
            User user = userService.validateToken(tokenValue);
            return UserDto.from(user);
        } catch (ValidTokenNotFoundException e) {
            throw new RuntimeException(e);
        }
    }
}
