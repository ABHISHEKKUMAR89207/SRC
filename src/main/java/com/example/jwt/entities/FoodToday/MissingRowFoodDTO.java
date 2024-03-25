package com.example.jwt.entities.FoodToday;



import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class MissingRowFoodDTO {

    private Long missingId;
    private String timestamp; // String representation of timestamp
    private String messingMessage;
//    private Long totalCountMissingMessagr; // Added field
    // Constructor without totalCountMissingMessagr
//    public MissingRowFoodDTO(Long missingId, String timestamp, String messingMessage) {
//        this.missingId = missingId;
//        this.timestamp = timestamp;
//        this.messingMessage = messingMessage;
//    }



    // Constructor with all fields
//    public MissingRowFoodDTO(Long missingId, String timestamp, String messingMessage, Long totalCountMissingMessagr) {
//        this.missingId = missingId;
//        this.timestamp = timestamp;
//        this.messingMessage = messingMessage;
//        this.totalCountMissingMessagr = totalCountMissingMessagr;
//    }
}

