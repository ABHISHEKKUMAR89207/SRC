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
    private List<DisplayWithSizesDTO> displayNamesList;

    @Getter
    @Setter
    public static class DisplayWithSizesDTO {
        private String displayNameCatId;
        private List<SizeCompletedDTO> sizes;
    }

    @Getter
    @Setter
    public static class SizeCompletedDTO {
        private String sizeName;
        private int quantity;
    }
}
