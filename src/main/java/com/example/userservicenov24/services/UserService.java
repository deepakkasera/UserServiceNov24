package com.example.userservicenov24.services;

import com.example.userservicenov24.exceptions.ValidTokenNotFoundException;
import com.example.userservicenov24.models.Token;
import com.example.userservicenov24.models.User;
import com.fasterxml.jackson.core.JsonProcessingException;

public interface UserService {
    Token login(String email, String password);

    User signUp(String name, String email, String password) throws JsonProcessingException;

    void logout(String token) throws ValidTokenNotFoundException;

    User validateToken(String token) throws ValidTokenNotFoundException;
}
