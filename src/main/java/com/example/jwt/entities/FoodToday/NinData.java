// Main Table Data 

package com.example.jwt.entities.FoodToday;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.Index;

import java.util.HashSet;
import java.util.Set;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "nutrients_csvfile_new")
public class NinData {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY) // Change the generation type to IDENTITY
//    @Column(name = "nin_data_id")
    @Index(name = "idx_nin_data_id")
    private Long ninDataId;

//    @Column(name = "food")
    private String food;
    private String foodCode;
    private String category;
    private String source;
    private String Typesoffood;

    private Double Energy;
    private Double Protein;
    private Double Total_Fat;

    private Double Total_Dietary_Fibre;
    private Double carbohydrate;
    private Double thiamine_B1;
    //    private Double thiamine;
    private Double riboflavin_B2;
    private Double niacin_B3;
    //    private Double niacin;
      private Double vit_B6;
    private Double totalFolates_B9;
    private Double vit_C;
private Double retinolVit_A;
//    private Double retinolVit_A;
//    private Double vit_A;

    //    private Double vitA;
    private Double iron;
    private Double zinc;
    private Double sodium;
    private Double calcium;
    private Double magnesium;



    private Integer DDS_Food_Category_Code;
    private String DDS_Category;


//    private Double cholestrol;
//    private Double potassium;
//    private Double phosphorus;
//    private Double selenium;
//    private Double copper;
//    private Double manganese;






//    @ManyToOne(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
//    @JoinColumn(name = "nutrient_name", referencedColumnName = "nutrientName")
//    @JsonIgnore
//    private UnitsDatabase unitsDatabase;


//    @Column(name = "Fat")


    @ManyToMany(mappedBy = "ninDataList", fetch = FetchType.LAZY)
    private Set<Ingredients> ingredientsSet = new HashSet<>();

    @ManyToOne(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    @JoinColumn(name = "foodUnit", referencedColumnName = "food_unit") // Use 'food_unit' instead of 'FoodUnit'
    @JsonIgnore
    private UnitsDatabase unitsDatabase;

    public String getCarbohydrateWithUnit() {
        return String.format("%.2f g", this.carbohydrate);
    }

    // Custom getters for nutrients with units
    public String getEnergyWithUnit() {
        return String.format("%.2f g", this.Energy);
    }

    public String getProteinWithUnit() {
        return String.format("%.2f g", this.Protein);
    }

    public String getTotal_FatWithUnit() {
        return String.format("%.2f g", this.Total_Fat);
    }

    public String getSodiumWithUnit() {
        return String.format("%.2f mg", this.sodium);
    }



    // Custom getter for total fat with unit
    public String getTotalFatWithUnit() {
        return String.format("%.2f g", this.Total_Fat);
    }



    // Custom getter for total dietary fibre with unit
    public String getTotalDietaryFibreWithUnit() {
        return String.format("%.2f g", this.Total_Dietary_Fibre);
    }

    // Custom getter for calcium with unit
    public String getCalciumWithUnit() {
        return String.format("%.2f mg", this.calcium);
    }

    // Custom getter for iron with unit
    public String getIronWithUnit() {
        return String.format("%.2f mg", this.iron);
    }

    // Custom getter for magnesium with unit
    public String getMagnesiumWithUnit() {
        return String.format("%.2f mg", this.magnesium);
    }

    // Custom getter for zinc with unit
    public String getZincWithUnit() {
        return String.format("%.2f mg", this.zinc);
    }

}
