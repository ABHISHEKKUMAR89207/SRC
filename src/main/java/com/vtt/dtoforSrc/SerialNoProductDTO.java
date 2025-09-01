package com.vtt.dtoforSrc;


import lombok.*;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class SerialNoProductDTO {
    private String referredLabelNumber;
    private String labelGeneratedId;
    private String defaultDisplayNameCatId;
    private double commonMRP;
    private String commonColor;
    private String commonFabricName;
    private String commonArticle;
    private List<DisplayWithSizesDTO> displayNamesList;
    private List<SizeSetDTO> setAvailable;   // ✅ new field

    @Getter
    @Setter
    public static class DisplayWithSizesDTO {
        private String displayNameCatId;
        private String price;
        private String seprateColor;
        private List<SizeCompletedDTO> sizes;
        private List<SizeSetDTO> seprateSetAvailable;   // ✅ new field for each display

    }

    @Getter
    @Setter
    public static class SizeCompletedDTO {
        private String sizeName;
        private int quantity;
    }

    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class SizeSetDTO {
        private String setId;
        private String setName;
        private int setQuantity;
        private List<SizeQuantityDTO> sizes;

        @Getter
        @Setter
        @NoArgsConstructor
        @AllArgsConstructor
        public static class SizeQuantityDTO {
            private String label;
            private int quantity;
        }
    }
}
