// Security Authentication - Password - encrypt - decrypt

package com.vtt.model;

import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@ToString
public class JwtRequest {

    private String mobileNo;
    private String password;
}
