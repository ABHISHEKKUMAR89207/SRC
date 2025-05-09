//package com.vtt.entities;
//
//
//import lombok.*;
//import org.springframework.data.annotation.Id;
//import org.springframework.data.mongodb.core.mapping.Document;
//
//import java.time.LocalDateTime;
//import java.util.List;
//
//@Getter
//@Setter
//@NoArgsConstructor
//@AllArgsConstructor
//@Document(collection = "subroles")
//public class GroupSubRole {
//
//    @Id
//    private String id;
//    private String groupName;
//
//    private List<String> roleName;
//
//    private LocalDateTime createdTimestamp = LocalDateTime.now();
//
//    @Override
//    public String toString() {
//        return roleName.toString();
//    }
//}
