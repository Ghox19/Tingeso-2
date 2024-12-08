package com.microservice.clientLoanMicroservice.DTOS;


import com.microservice.clientLoanMicroservice.Entities.ClientEntity;
import lombok.Data;

import java.util.List;

@Data
public class ClientLoanGetForm {
    private Long id;

    private String loanName;
    private Integer years;
    private Float interest;
    private Integer propertyValue;
    private Integer loanAmount;
    private Float loanRatio;
    private Integer mensualPay;
    private String fase;
    private String message;
    private Double cuotaIncome;
    private Double debtCuota;
    private Integer fireInsurance;
    private Double deduction;
    private Integer totalCost;
    private Long clientId;

    private Long savingsId;

    private List<DocumentSafeForm> documents;
}