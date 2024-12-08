package com.microservice.tracingMicroservice.DTOs;

import lombok.Data;

@Data
public class ClientLoanPreApprovedForm {

    private Long clientLoanId;
    private Integer fireInsurance;
    private Double deduction;
}
