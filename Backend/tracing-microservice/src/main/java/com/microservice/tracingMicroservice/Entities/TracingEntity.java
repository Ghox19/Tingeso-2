package com.microservice.tracingMicroservice.Entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Entity
@Table(name = "tracing")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class TracingEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(unique = true, nullable = false)
    private long id;

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
