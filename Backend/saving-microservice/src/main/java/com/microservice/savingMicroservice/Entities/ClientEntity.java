package com.microservice.savingMicroservice.Entities;

import jakarta.persistence.CollectionTable;
import jakarta.persistence.Column;
import jakarta.persistence.ElementCollection;
import jakarta.persistence.JoinColumn;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ClientEntity {
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

    @ElementCollection
    @CollectionTable(name = "client_document_ids",
            joinColumns = @JoinColumn(name = "client_id"))
    @Column(name = "document_id")
    private List<Long> documentsId;

    @ElementCollection
    @CollectionTable(name = "client_loan_ids",
            joinColumns = @JoinColumn(name = "client_id"))
    @Column(name = "loan_id")
    private List<Long> loansId;
}
