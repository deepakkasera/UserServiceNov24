package com.example.userservicenov24.security.services;

import com.example.userservicenov24.models.User;
import com.example.userservicenov24.repositories.UserRepository;
import com.example.userservicenov24.security.models.CustomUserDetails;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
public class CustomUserDetailsService implements UserDetailsService  {
    private UserRepository userRepository;

    public CustomUserDetailsService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Optional<User> optionalUser = userRepository.findByEmail(username);

        if (optionalUser.isEmpty()) {
            throw new UsernameNotFoundException("Username: " + username + " not found");
        }

        User user = optionalUser.get();

        //Convert user into UserDetails type of object.
        return new CustomUserDetails(user);
    }
}
