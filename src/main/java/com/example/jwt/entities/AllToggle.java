package com.example.jwt.entities;



import com.fasterxml.jackson.annotation.*;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
//@JsonInclude(JsonInclude.Include.NON_NULL) // Exclude null fields during serialization

public class AllToggle {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private boolean notificationOn=false;

    private boolean registration_term_condition;


    @JsonIgnore
    @OneToOne
    @JoinColumn(name = "user_id")
    private User user;



    // Add other fields as needed

//    public AllToggle(boolean notificationOn, User user) {
//        this.notificationOn = notificationOn;
//        this.user = user;
//    }

    public AllToggle(boolean notificationOn, boolean registration_term_condition, User user) {
        this.notificationOn = notificationOn;
        this.registration_term_condition = registration_term_condition;
        this.user = user;
    }

    // Constructor without registration_term_condition for default value
    public AllToggle(boolean notificationOn, User user) {
        this.notificationOn = notificationOn;
        this.user = user;
    }


}
