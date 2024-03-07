package com.example.jwt.AdminDashboard;



import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;


@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class UserDTO {
    private Long userId;
    private String userName;
    private String email;
    private String mobileNo;
    private String deviceType;
    private Double latitude;
    private Double longitude;
//    private String address;
    private LocalDate localDate;

    private String state;

    // Constructors, getters, and setters

//    public UserDTO(String userName, String email, String mobileNo, String deviceType, Double latitude, Double longitude, String address, LocalDate localDate) {
//        this.userName = userName;
//        this.email = email;
//        this.mobileNo = mobileNo;
//        this.deviceType = deviceType;
//        this.latitude = latitude;
//        this.longitude = longitude;
//        this.address = address;
//        this.localDate = localDate;
//    }


}

