package com.microservice.clientMicroservice.DTOS;

import lombok.Data;

@Data
public class ClientInfoRequiredForm {
    private Integer years;
    private Integer mensualIncome;
    private Integer jobYears;
    private String jobType;
    private Integer totalDebt;
}
