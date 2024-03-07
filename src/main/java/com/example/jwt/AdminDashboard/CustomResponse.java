package com.example.jwt.AdminDashboard;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;
import java.util.Map;

@Getter
@Setter
@NoArgsConstructor

public class CustomResponse {
    private List<UserDTO> userList;
    private int totalUsers;
//    private Map<String, List<UserDTO>> usersByState;
//    private Map<String, List<UserDTO>> userStatusByState;

    // Constructors, getters, and setters

    //    public CustomResponse(List<UserDTO> userList, int totalUsers, Map<String, List<UserDTO>> usersByState, Map<String, List<UserDTO>> userStatusByState) {
//        this.userList = userList;
//        this.totalUsers = totalUsers;
////        this.usersByState = usersByState;
////        this.userStatusByState = userStatusByState;
//    }
    public CustomResponse(List<UserDTO> userList, int totalUsers) {
        this.userList = userList;
        this.totalUsers = totalUsers;
//        this.usersByState = usersByState;
//        this.userStatusByState = userStatusByState;
    }
    public List<UserDTO> getUserList() {
        return userList;
    }

    public void setUserList(List<UserDTO> userList) {
        this.userList = userList;
    }

    // ... other necessary details
}

