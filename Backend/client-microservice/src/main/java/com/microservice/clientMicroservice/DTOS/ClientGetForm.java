package com.microservice.clientMicroservice.DTOS;

import lombok.Data;

import java.util.List;

@Data
public class ClientGetForm {
    private long id;

    private String name;
    private String lastName;
    private Integer rut;
    private String email;
    private Integer years;
    private Integer contact;
    private String jobType;
    private Integer mensualIncome;
    private Integer jobYears;
    private Integer totalDebt;

    private List<DocumentSafeForm> documents;

    private List<ClientLoanGetForm> loans;
}
