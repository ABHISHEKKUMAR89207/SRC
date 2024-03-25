package com.example.jwt.request;

import com.example.jwt.entities.dashboardEntity.healthTrends.DiastolicBloodPressure;
import com.example.jwt.entities.dashboardEntity.healthTrends.SystolicBloodPressure;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Autowired;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class BloodPressureRequest {

//    private SystolicBloodPressure systolicBloodPressure;
//    private DiastolicBloodPressure diastolicBloodPressure;

    private double systolicValue;
    private double diastolicValue;

}
