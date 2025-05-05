package com.vtt.dtoforSrc;



import com.vtt.otherclass.MainRole;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class UserProfileUpdateRequest {
    private String userName;
    private String address;
    private MainRole mainRole;
    private String subRole;
}
