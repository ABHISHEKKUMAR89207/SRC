// Response

package com.vtt.model;

import com.vtt.otherclass.MainRole;
import lombok.*;
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@ToString
public class JwtResponse {

    private String jwtToken;
    private String refreshToken;
    private String username;
    private String userId;
    private String mainRole;
    private String subRole;
    private String mobileNumber;
    private boolean activeStatus;
    private boolean BankDetailsStatus;

}
