package com.microservice.clientMicroservice.DTOS;

import lombok.Data;

@Data
public class DocumentForm {
    private String name;
    private String content;
    private String type;
    private Boolean approved;
    private Long clientId;
    private Long clienLoanId;
}