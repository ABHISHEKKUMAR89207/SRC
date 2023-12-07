package com.example.jwt.entities.dashboardEntity.healthTrends;

import com.example.jwt.entities.User;
import com.example.jwt.entities.UserProfile;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class MenstrualCycle {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long menstrualCycle_id;
    @Temporal(TemporalType.DATE)
    private LocalDate calculatedDate;

    @JsonDeserialize(using = LocalDateDeserializer.class)
    private LocalDate lastPeriodStartDate;

    private boolean isMenstrualCycleEnabled;
    private int averageCycleLength;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    @JsonIgnore
    private User user;
    @OneToOne
    @JoinColumn(name = "userProfile_id")
    @JsonIgnore
    private UserProfile userProfile;

    public void setIsMenstrualCycleEnabled(boolean isMenstrualCycleEnabled) {
        this.isMenstrualCycleEnabled = isMenstrualCycleEnabled;
    }
}
