package com.vtt.security.Refresh;


import com.vtt.entities.User;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.annotation.Collation;
import org.springframework.data.mongodb.core.mapping.DBRef;

import java.time.Instant;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
//@Table(name = "refresh_tokens")
@Builder
@ToString
//@Entity
@Collation
public class RefreshToken {
    @Id

//    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private String tokenId;

    private String refreshToken;

    private Instant expiry;
//    @OneToOne
    @DBRef
    private User user;
}
