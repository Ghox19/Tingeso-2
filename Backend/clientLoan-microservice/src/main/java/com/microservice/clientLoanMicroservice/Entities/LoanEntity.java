package com.microservice.clientLoanMicroservice.Entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class LoanEntity {
    private Long id;

    private String name;
    private String description;
    private Integer maxYears;
    private Float minInterest;
    private Float maxInterest;
    private Integer maxAmount;
    private List<String> requirements;
}
