package com.example.jwt.security.Refresh;


import com.example.jwt.entities.User;
import jakarta.persistence.*;
import lombok.*;

import java.time.Instant;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "refresh_tokens")
@Builder
@ToString
@Entity
public class RefreshToken {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long tokenId;

    private String refreshToken;

    private Instant expiry;
    @OneToOne
    private User user;
}
