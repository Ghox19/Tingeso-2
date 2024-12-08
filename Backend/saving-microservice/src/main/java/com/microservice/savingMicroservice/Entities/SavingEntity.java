package com.microservice.savingMicroservice.Entities;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Entity
@Table(name = "saving")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class SavingEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(unique = true, nullable = false)
    private long id;

    private Integer years;
    private Integer actualBalance;
    private String result;

    @ElementCollection
    private List<Integer> balances;

    @ElementCollection
    private List<Integer> deposit;

    @ElementCollection
    private List<Integer> withdraw;

    @ElementCollection
    private List<String> reasons;

    private Long clientLoanId;
}
