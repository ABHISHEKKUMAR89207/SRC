package com.example.jwt.request;


import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@NonNull
public class TargetDataRequest {
    private Double calories;
    private Double carbs;
    private Double fats;
    private Double proteins;
    private Double fiber;

    // Getters and setters
}
