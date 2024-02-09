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
    private Double Energy;
    private Double carbohydrate;
    private String Typesoffood;

//    @Column(name = "Protein")
    private Double Protein;
    private Double Total_Fat;
    private Double Total_Dietary_Fibre;
    private Double  cholestrol;
    private Double  sodium;
    private Double calcium;
    private Double iron;
    private Double potassium;
    private Double phosphorus;
    private Double magnesium;
    private Double zinc;
    private Double selenium;
    private Double copper;
    private Double manganese;

//    private Double niacin;
//    private Double riboflavin;
//    private Double thiamine;

    
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
