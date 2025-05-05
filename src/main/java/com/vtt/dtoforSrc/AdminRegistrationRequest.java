package com.vtt.dtoforSrc;



import com.vtt.otherclass.MainRole;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AdminRegistrationRequest {
    private String userName;
    private String mobileNo;
    private String email;
    private String password;
    private String deviceType;
    private Double latitude;
    private Double longitude;
    private String address;
    private String subRole;
    private MainRole mainRole;

}