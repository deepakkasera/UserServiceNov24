package com.example.userservicenov24.models;

import jakarta.persistence.Entity;
import jakarta.persistence.ManyToOne;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;

@Getter
@Setter
@Entity(name = "tokens")
public class Token extends BaseModel {
    private String value;
    private Date expiryAt;

    @ManyToOne
    private User user;
}
