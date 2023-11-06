package com.example.jwt.entities.FoodToday;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.Index;

import java.util.HashSet;
import java.util.Set;


//@Data
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
    @Index(name = "idx_nin_data_id") // Create an index on nin_data_id column
    private Long id;

    @Column(name = "food")
    private String name;

    @Column(name = "calories")
    private Double calories;
//    @Column(name = "Carbohydrates")
    private Double Carbs;
    @Column (name ="Protein")
    private Double protein;
    @Column(name = "Fat")
    private Double Fat;
//    @Column(name = "Dietry Fiber")
    private Double Fiber;


    @ManyToMany(mappedBy = "ninDataList", fetch = FetchType.LAZY)
    private Set<Ingredients> ingredientsSet = new HashSet<>();



//    @Column(name = "Energy")
//    private Double energy;


//    @ManyToMany(fetch = FetchType.LAZY) // You can change FetchType to EAGER if needed
//    @JoinColumn(name = "ing_id")
//    private Ingredients ingredients;


    // Getters and setters
}
