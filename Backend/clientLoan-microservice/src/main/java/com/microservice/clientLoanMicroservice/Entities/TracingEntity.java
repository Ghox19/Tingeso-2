package com.microservice.clientLoanMicroservice.Entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class TracingEntity {
    private Long id;

    private Integer mensualPay;
    private Double cuotaIncome;
    private Double debtCuota;
    private String fase;
    private String message;
    private Integer fireInsurance;
    private Double deduction;
    private Integer totalCost;

    private Long clientLoanId;
}
