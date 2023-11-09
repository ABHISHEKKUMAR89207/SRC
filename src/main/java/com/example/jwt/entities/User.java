//package com.example.jwt.entities;
//
//
//import com.example.jwt.entities.dashboardEntity.healthTrends.HealthTrends;
//import com.example.jwt.entities.dashboardEntity.healthTrends.SleepDuration;
//import com.fasterxml.jackson.annotation.*;
//import jakarta.persistence.*;
//import lombok.*;
//import org.hibernate.annotations.NaturalId;
//import org.springframework.security.core.GrantedAuthority;
//import org.springframework.security.core.authority.SimpleGrantedAuthority;
//import org.springframework.security.core.userdetails.UserDetails;
//
//import java.time.LocalDate;
//import java.util.*;
//import java.util.stream.Collectors;
//
//
//@Getter
//@Setter
////@Data
//@AllArgsConstructor
//@NoArgsConstructor
////@ToString
////@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "id")
//
//@Entity
////@Table(name = "user_table")
//public class User implements UserDetails
////public class User
//{
//
//    @Id
//    @Column(name = "user_id")
//    @GeneratedValue(strategy = GenerationType.IDENTITY)
//    private Long userId;
//
//    @Column(unique = true)
//    private String userName;
//    @NaturalId(mutable = true)
//    private String email;
//    private String password;
//    private String mobileNo;
//    private LocalDate localDate = LocalDate.now();
////    private String role;
//@Column(name = "is_enabled", columnDefinition = "boolean default false")
//private boolean isEnabled;
//
//    //    @Column(columnDefinition = "boolean default false")
//    private boolean emailVerified = false;
//
////    @Column
////    private boolean sleepTimeRecordingEnabled;
//
//
//
////    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
////    private List<SleepDuration> sleepDurations = new ArrayList<>();
//
////    @Override
////    public Collection<? extends GrantedAuthority> getAuthorities() {
////        return null;
////    }
//
////    @OneToOne(mappedBy = "user") // This maps to the "user" property in the HealthTrends entity
////    private HealthTrends healthTrends;
//
////    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
////    private List<HealthTrends> healthTrends;
//
//
//    @ManyToMany(cascade = CascadeType.ALL,fetch = FetchType.EAGER)
////    @JoinTable(name = "user_role",
////            joinColumns = @JoinColumn(name = "user",referencedColumnName = "id"),
////            inverseJoinColumns = @JoinColumn(name = "role",referencedColumnName = "id")
////    )
//    private Set<Role> roles=new HashSet<>();
//
//    @OneToOne(mappedBy = "user")
////    @JsonManagedReference
////    @JsonIgnore
//    @JsonBackReference
//    private UserProfile userProfile;
//
////
////    @OneToOne(cascade = CascadeType.ALL)
////    @JoinColumn(name = "user_profile_id")
////    private UserProfile userProfile;
//
//    @OneToOne(mappedBy = "user")
////    @JsonManagedReference
////    @JsonIgnore
//    @JsonBackReference
//    private waterEntity water;
//
//
//
//    @Override
//    public Collection<? extends GrantedAuthority> getAuthorities() {
//
//        List<SimpleGrantedAuthority> authories = this.roles.stream()
//                .map((role)-> new SimpleGrantedAuthority(role.getName())).collect(Collectors.toList());
//        return authories;
//    }
//
//
//    @Override
//    public String getUsername() {
//        return this.email;
//    }
//
//    @Override
//    public boolean isAccountNonExpired() {
//        return true;
//    }
//
//    @Override
//    public boolean isAccountNonLocked() {
//
//        return true;
//    }
//
//    @Override
//    public boolean isCredentialsNonExpired() {
//        return true;
//    }
//
//    @Override
//    public boolean isEnabled() {
//        return true;
//    }
//}


package com.example.jwt.entities;


import com.example.jwt.entities.dashboardEntity.healthTrends.HealthTrends;
import com.example.jwt.entities.dashboardEntity.healthTrends.SleepDuration;
import com.example.jwt.registration.token.VerificationToken;
import com.fasterxml.jackson.annotation.*;
import jakarta.persistence.*;
import jakarta.validation.constraints.Pattern;
import lombok.*;
import org.hibernate.annotations.NaturalId;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;


@Getter
@Setter
//@Data
@AllArgsConstructor
@NoArgsConstructor
//@ToString
//@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "id")

@Entity
//@Table(name = "user_table")
public class User implements UserDetails
//public class User
{

    @Id
    @Column(name = "user_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long userId;

    @Column(unique = true)
    private String userName;
    @NaturalId(mutable = true)
    private String email;
    private String password;

//    @Column(unique = true)
//    @Pattern(regexp = "^[0-9]{10}$", message = "Mobile number must be a 10-digit number")
//    @Pattern(regexp = "^\\+1[0-9]{10}$", message = "Mobile number must start with +1 and be a 10-digit number")
    private String mobileNo;

    private LocalDate localDate = LocalDate.now();
//    private String role;

    //    @Column(columnDefinition = "boolean default false")
    private boolean emailVerified = false;

    @Column(name = "registration_timestamp")
    private LocalDateTime registrationTimestamp = LocalDateTime.now();

//    @Column(name = "is_enabled", columnDefinition = "boolean default false")
//    private boolean isEnabled;


//    private boolean emailVerified = false;



    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<VerificationToken> verificationTokens = new ArrayList<>();

//    @Column
//    private boolean sleepTimeRecordingEnabled;



//    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
//    private List<SleepDuration> sleepDurations = new ArrayList<>();

//    @Override
//    public Collection<? extends GrantedAuthority> getAuthorities() {
//        return null;
//    }

//    @OneToOne(mappedBy = "user") // This maps to the "user" property in the HealthTrends entity
//    private HealthTrends healthTrends;

//    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
//    private List<HealthTrends> healthTrends;


//    @ManyToMany(cascade = CascadeType.REMOVE,fetch = FetchType.EAGER)
////    @JoinTable(name = "user_role",
////            joinColumns = @JoinColumn(name = "user",referencedColumnName = "id"),
////            inverseJoinColumns = @JoinColumn(name = "role",referencedColumnName = "id")
////    )
//    private Set<Role> roles=new HashSet<>();

    @ManyToMany(cascade = CascadeType.REMOVE, fetch = FetchType.EAGER)
    private Set<Role> roles = new HashSet<>();



    @OneToOne(mappedBy = "user")
//    @JsonManagedReference
//    @JsonIgnore
    @JsonBackReference
    private UserProfile userProfile;

//
//    @OneToOne(cascade = CascadeType.ALL)
//    @JoinColumn(name = "user_profile_id")
//    private UserProfile userProfile;

    @OneToOne(mappedBy = "user")
//    @JsonManagedReference
//    @JsonIgnore
    @JsonBackReference
    private waterEntity water;



    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {

        List<SimpleGrantedAuthority> authories = this.roles.stream()
                .map((role)-> new SimpleGrantedAuthority(role.getName())).collect(Collectors.toList());
        return authories;
    }


    @Override
    public String getUsername() {
        return this.email;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {

        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

//    @Override
//    public boolean isEnabled() {
//        return true;
//    }

    @Override
    public boolean isEnabled() {
        return this.emailVerified;
    }
}
