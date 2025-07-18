package com.healthcare.dto;

import lombok.Data;

@Data
public class PrescriptionRequest {
    private String medication;
    private String dosage;
}
