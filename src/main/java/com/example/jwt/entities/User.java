package com.example.jwt.entities;

import com.example.jwt.entities.dashboardEntity.Activities;
import com.example.jwt.entities.dashboardEntity.healthTrends.AllTarget;
import com.example.jwt.entities.dashboardEntity.healthTrends.SleepDuration;
import com.example.jwt.entities.water.WaterEntity;
import com.example.jwt.registration.token.VerificationToken;
import com.fasterxml.jackson.annotation.*;
import jakarta.persistence.*;
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
@AllArgsConstructor
@NoArgsConstructor
@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "userId")
@Entity
public class User implements UserDetails {
    @Id
    @Column(name = "user_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long userId;
//    @Column(unique = true)
    private String userName;
    @NaturalId(mutable = true)
    private String email;
    private String password;
    private String mobileNo;

    private String deviceType;
    private Double latitude;
    private Double longitude;

    private LocalDate localDate = LocalDate.now();
    private boolean emailVerified = false;
    private String notificationToken;

    @Column(name = "registration_timestamp")
    private LocalDateTime registrationTimestamp = LocalDateTime.now();

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<VerificationToken> verificationTokens = new ArrayList<>();

    @OneToMany(mappedBy = "user")
    @JsonIgnoreProperties("user")
    private List<Activities> activities;

    @ManyToMany(cascade = CascadeType.REMOVE, fetch = FetchType.EAGER)
    private Set<Role> roles = new HashSet<>();

    @OneToOne(mappedBy = "user")
    @JsonBackReference("userReference")
    private UserProfile userProfile;

    @OneToOne(mappedBy = "user")
    @JsonBackReference("AllTarget")
    private AllTarget allTarget;

    @OneToMany(mappedBy = "user")
    private List<WaterEntity> waterEntities = new ArrayList<>();

    @OneToMany(mappedBy = "user")
    private List<SleepDuration> sleepDurations = new ArrayList<>();

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

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    private List<NotificationEntity> notifications;

    @OneToOne(mappedBy = "user", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JsonBackReference("notificationSettingsReference")
    private AllToggle allToggle;

    public String getNotificationToken() {
        return this.notificationToken;
    }

    public void updateNotificationToken(String newToken) {
        if (newToken != null && (this.notificationToken == null || !this.notificationToken.equals(newToken))) {
            this.notificationToken = newToken;
        }
    }
}
