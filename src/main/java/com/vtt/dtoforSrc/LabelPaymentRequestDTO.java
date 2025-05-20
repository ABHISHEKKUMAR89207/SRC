package com.vtt.dtoforSrc;

import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
public class LabelPaymentRequestDTO {
    private String userId;
    private double totalAmount;
    private String notes;
    private String date;
    private List<LabelPaymentDetail> labels = new ArrayList<>(); // Initialize with empty list

    public static class LabelPaymentDetail {
        private String labelNumber;
        private boolean paid;

        public String getLabelNumber() {
            return labelNumber;
        }

        public void setLabelNumber(String labelNumber) {
            this.labelNumber = labelNumber;
        }

        public boolean isPaid() {
            return paid;
        }

        public void setPaid(boolean paid) {
            this.paid = paid;
        }
    }
}