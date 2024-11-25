package com.example.userservicenov24.dtos;

import com.example.userservicenov24.models.Token;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class LogOutRequestDto {
    private String tokenValue;
}
