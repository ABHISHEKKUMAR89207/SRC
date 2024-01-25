package com.example.jwt.FoodTodayResponse;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
public class AggregatedNutritionalInfoResponse {
    private List<NutritionalInfoResponse> nutritionalInfoList;
    private Double totalEnergy;
    private Double totalProteins;
    private Double totalCarbs;
    private Double totalFats;
    private Double totalFibers;

    public AggregatedNutritionalInfoResponse(List<NutritionalInfoResponse> nutritionalInfoList) {
        this.nutritionalInfoList = nutritionalInfoList;
        this.calculateTotals();
    }

    private void calculateTotals() {
        this.totalEnergy = 0.0;
        this.totalProteins = 0.0;
        this.totalCarbs = 0.0;
        this.totalFats = 0.0;
        this.totalFibers = 0.0;

        for (NutritionalInfoResponse nutritionalInfo : nutritionalInfoList) {
            this.totalEnergy += nutritionalInfo.getEnergy();
            this.totalProteins += nutritionalInfo.getProtein();
            this.totalCarbs += nutritionalInfo.getCarbohydrates();
            this.totalFats += nutritionalInfo.getFat();
            this.totalFibers += nutritionalInfo.getFiber();
        }
    }
}
