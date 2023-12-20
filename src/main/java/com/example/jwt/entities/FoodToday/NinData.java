package com.example.jwt.entities.FoodToday;

import com.example.jwt.entities.UnitsDatabase;
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
    @Column(name = "nin_data_id")
    @Index(name = "idx_nin_data_id")
    private Long id;

    @Column(name = "food")
    private String name;

    @Column(name = "Calories")
    private Double calories;
    private Double Carbs;
    @Column(name = "Protein")
    private Double protein;
//    @ManyToOne(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
//    @JoinColumn(name = "nutrient_name", referencedColumnName = "nutrientName")
//    @JsonIgnore
//    private UnitsDatabase unitsDatabase;


    @Column(name = "Fat")
    private Double Fat;
    private Double Fiber;

    @ManyToMany(mappedBy = "ninDataList", fetch = FetchType.LAZY)
    private Set<Ingredients> ingredientsSet = new HashSet<>();

    private String SIUnit;
}
