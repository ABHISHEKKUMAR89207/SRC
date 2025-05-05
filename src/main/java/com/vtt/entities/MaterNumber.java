package com.vtt.entities;


import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Document(collection = "maternumbers")
@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "id")
public class MaterNumber {

    @Id
    private String id;

    @DBRef
    private User user;

    private int materNumber; // max 9 digits

    public void setMaterNumber(int materNumber) {
        if (materNumber > 9) {
            throw new IllegalArgumentException("Mater number cannot exceed 9");
        }
        this.materNumber = materNumber;
    }

}
