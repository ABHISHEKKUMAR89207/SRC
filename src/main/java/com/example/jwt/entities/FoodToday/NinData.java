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
    @GeneratedValue(strategy = GenerationType.AUTO)
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






    private Double cholestrol;
    private Double potassium;
    private Double phosphorus;
    private Double selenium;
    private Double copper;
    private Double manganese;






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



//    private String SIUnit;
}
