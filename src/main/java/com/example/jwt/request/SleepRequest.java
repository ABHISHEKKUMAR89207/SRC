package com.example.jwt.request;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class SleepRequest {
    private int totalEfficiency;
    private int totalMinutesAsleep;
    // Add any other fields if needed
}

