package com.vtt.dtoforSrc;



import lombok.*;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class InventoryForApprovalDTO {

    private String color;
    private List<SizeQuantityDTO> sizes;
    private String displayNamesCatId;
    private String fabricId;
    private String userId;
    private boolean approved;
    private List<ApplySetWithQuantityDTO> applySets;
    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class SizeQuantityDTO {
        private String label;
        private int quantity;
    }
    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class ApplySetWithQuantityDTO {
        private String applySetId;   // store only reference ID
        private int totalQuantity;   // total quantity of that ApplySet
    }
}
