package com.vtt.dtoforSrc;



import com.vtt.entities.Order;
import lombok.Data;

import java.util.List;

@Data
public class LabelGeneratedDTO {

    private String masterNumber;
    private String clientUserId;
    private String category;
    private String subCategory;
    private String displayId;
    private String displayName;

    private List<LabelFabricDTO> fabrics;
    private List<SizeCompletedDTO> sizes;
    private List<UserWorkAssignDTO> users;
    private String orderReference;
    private int totalQuantity;
    private String status;

    @Data
    public static class LabelFabricDTO {
        private String fabricId;
        private double usedQuantity;
        private String color;
    }

    @Data
    public static class SizeCompletedDTO {
        private String sizeName;
        private int quantity;
    }

    @Data
    public static class UserWorkAssignDTO {
        private String userId;
        private String workAssigned;
        private boolean status;
    }
}
