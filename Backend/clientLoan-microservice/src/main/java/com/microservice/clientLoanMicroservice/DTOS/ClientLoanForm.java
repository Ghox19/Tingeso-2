package com.microservice.clientLoanMicroservice.DTOS;

import lombok.Data;

import java.util.List;

@Data
public class ClientLoanForm {

    private Integer rut;

    private String loanName;
    private Integer propertyValue;
    private Integer years;
    private Float interest;
    private Integer loanAmount;
    private Integer mensualPay;
    private String fase;

    private List<DocumentForm> documents;
}
