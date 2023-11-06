package com.example.jwt.dtos;


import com.example.jwt.entities.UserProfile;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class HealthTrendDto {


    private Long healthTrendId;
    private UserProfile userProfile;




}
