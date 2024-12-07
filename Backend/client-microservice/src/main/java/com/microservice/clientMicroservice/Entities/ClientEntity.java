package com.microservice.clientMicroservice.Entities;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Entity
@Table(name = "client")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ClientEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(unique = true, nullable = false)
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
