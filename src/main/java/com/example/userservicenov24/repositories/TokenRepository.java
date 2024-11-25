package com.example.userservicenov24.repositories;

import com.example.userservicenov24.models.Token;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Date;
import java.util.Optional;

public interface TokenRepository extends JpaRepository<Token, Long> {
    @Override
    Token save(Token token);


    //Declared Query
    Optional<Token> findByValueAndDeletedAndExpiryAtGreaterThan(String value, boolean deleted, Date expiryAt);
    //select * from tokens where value = ? and deleted = false and expiry_at > current_timestamp;
}
