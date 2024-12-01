package com.example.userservicenov24.services;

import com.example.userservicenov24.exceptions.ValidTokenNotFoundException;
import com.example.userservicenov24.models.Token;
import com.example.userservicenov24.models.User;
import com.example.userservicenov24.repositories.TokenRepository;
import com.example.userservicenov24.repositories.UserRepository;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Calendar;
import java.util.Date;
import java.util.Optional;

@Service
public class UserServiceImpl implements UserService {
    private UserRepository userRepository;
    private BCryptPasswordEncoder passwordEncoder;
    private TokenRepository tokenRepository;
    private KafkaTemplate<String, String> kafkaTemplate;
    private ObjectMapper objectMapper;

    public UserServiceImpl(UserRepository userRepository,
                           BCryptPasswordEncoder passwordEncoder,
                           TokenRepository tokenRepository,
                           KafkaTemplate kafkaTemplate,
                           ObjectMapper objectMapper) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.tokenRepository = tokenRepository;
        this.kafkaTemplate = kafkaTemplate;
        this.objectMapper = objectMapper;
    }

    @Override
    public Token login(String email, String password) {
        /* Github Copilot
        1. Find the user by email.
        2. If the user is not found, return null.
        3. If user is present in DB then verify the password.
        4. If password matches, then generate the token and return it.
         */

        Optional<User> optionalUser = userRepository.findByEmail(email);

        if (optionalUser.isEmpty()) {
            return null;
        }

        User user = optionalUser.get();

        //Get all the tokens for this userId, and check the count.
        //TODO: Implement this.

        //Match the password.
//        String passwordInDB = user.getPassword();
//        String encodedPassword = passwordEncoder.encode(password);
//
//        if (encodedPassword.equals(passwordInDB)) {
//
//        }

        Token token = null;
        if (passwordEncoder.matches(password, user.getPassword())) {
            //Login successful.
            token = new Token();

            token.setUser(user);

            // Expiry time should be 30 days from now.
            Calendar calendar = Calendar.getInstance();
            calendar.add(Calendar.DAY_OF_MONTH, 30);
            Date date30DaysFromNow = calendar.getTime();
            token.setExpiryAt(date30DaysFromNow);

//            token.setExpiryAt(new Date());

            //Token value can be any random String of 128 characters.
            token.setValue(RandomStringUtils.randomAlphanumeric(128));
        }

        return tokenRepository.save(token);
    }

    @Override
    public User signUp(String name, String email, String password) throws JsonProcessingException {
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

//        SendEmailDto sendEmailDto = new SendEmailDto();
//        sendEmailDto.setTo(email);
//        sendEmailDto.setSubject("Welcome to Scaler!!!");
//        sendEmailDto.setBody("We are happy to have you onboarded, All the best for your journey.");

        //Publish an event inside Kafka to send a Welcome Email to the user.
//        kafkaTemplate.send(
//                "sendEmail",
//                objectMapper.writeValueAsString(sendEmailDto)
//        );

        return userRepository.save(user);
    }

    @Override
    public void logout(String tokenValue) throws ValidTokenNotFoundException {
        //We will be able to logout a particular token
        //if token is present in the DB, it's expiry time is greater than current time.
        //and deleted is false.

        Optional<Token> optionalToken =
                tokenRepository.findByValueAndDeletedAndExpiryAtGreaterThan(tokenValue, false, new Date());

        if (optionalToken.isEmpty()) {
            throw new ValidTokenNotFoundException("Valid token not found.");
        }

        Token token = optionalToken.get();
        token.setDeleted(true);
        tokenRepository.save(token);
    }

    @Override
    public User validateToken(String tokenValue) throws ValidTokenNotFoundException {
        Optional<Token> optionalToken =
                tokenRepository.findByValueAndDeletedAndExpiryAtGreaterThan(tokenValue, false, new Date());

        if (optionalToken.isEmpty()) {
            throw new ValidTokenNotFoundException("Valid token not found.");
        }

        Token token = optionalToken.get();
        return token.getUser();
    }
}
