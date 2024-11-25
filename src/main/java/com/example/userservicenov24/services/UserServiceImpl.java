package com.example.userservicenov24.services;

import com.example.userservicenov24.models.Token;
import com.example.userservicenov24.models.User;
import com.example.userservicenov24.repositories.UserRepository;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UserServiceImpl implements UserService {
    private UserRepository userRepository;
    private BCryptPasswordEncoder passwordEncoder;

    public UserServiceImpl(UserRepository userRepository,
                           BCryptPasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public Token login(String email, String password) {
        return null;
    }

    @Override
    public User signUp(String name, String email, String password) {
        Optional<User> optionalUser = userRepository.findByEmail(email);

        if (optionalUser.isPresent()) {
            //Redirect user to the login page.
            return optionalUser.get();
        }

        User user = new User();
        user.setName(name);
        user.setEmail(email);
        user.setPassword(passwordEncoder.encode(password));
        user.setVerified(true);

        return userRepository.save(user);
    }

    @Override
    public void logout(String token) {

    }

    @Override
    public User validateToken(String token) {
        return null;
    }
}
