// User Detail 

package com.vtt.entities;



import com.vtt.otherclass.MainRole;

import com.vtt.registration.token.VerificationToken;
import com.vtt.security.Refresh.RefreshToken;
import com.fasterxml.jackson.annotation.*;

import lombok.*;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "userId")
//@Entity
@Document(collection = "users")
public class User implements UserDetails {
    @Id
//    @Column(name = "user_id")
//    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private String userId;

//    @Column(unique = true)
    private String userName;
//    @NaturalId(mutable = true)
    private String email;
    private String password;
    private String mobileNo;
    private String passwordTemp;
//    private String LoginOtp;
    private String deviceType;
    private String profilePictureUrl;
    private Double latitude;
    private Double longitude;
    private String address;
    private MainRole mainRole ;
    private String SubRole;

    private String otp;
    private LocalDateTime otpExpirationTime;

    private LocalDate localDate = LocalDate.now();
    private boolean emailVerified = false;
    private String notificationToken;
    private boolean activeStatus = false;
    private boolean BankDetailsStatus = false;
    @DBRef
    @JsonIgnore
    private RefreshToken refreshToken;

//    @Column(name = "registration_timestamp")
    private LocalDateTime registrationTimestamp = LocalDateTime.now();

//    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    @DBRef
    private List<VerificationToken> verificationTokens = new ArrayList<>();

//    @ManyToMany(cascade = CascadeType.REMOVE, fetch = FetchType.EAGER)
    @DBRef
    private Set<Role> roles = new HashSet<>();

//    @OneToOne(mappedBy = "user")
    @DBRef
    @JsonBackReference("userReference")
    private UserProfile userProfile;

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {

        List<SimpleGrantedAuthority> authories = this.roles.stream()
                .map((role)-> new SimpleGrantedAuthority(role.getName())).collect(Collectors.toList());
        return authories;
    }
    public String getUserName() {
        return this.userName;
    }

    @Override
    public String getUsername() {
        return this.email;
    }

    public String getName() {
        return this.userName;
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

    @Override
    public boolean isEnabled() {
        return this.emailVerified;
    }

//    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    @DBRef
    private List<NotificationEntity> notifications;


}
