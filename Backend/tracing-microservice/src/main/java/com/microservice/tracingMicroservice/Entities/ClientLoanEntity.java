package com.microservice.tracingMicroservice.Entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ClientLoanEntity {
    private long id;

    private String loanName;
    private Integer years;
    private Integer propertyValue;
    private Float interest;
    private Integer loanAmount;
    private Float loanRatio;

    private Long tracingId;

    private Long savingId;

    private Long clientId;

    private List<Long> documentsId;
}
