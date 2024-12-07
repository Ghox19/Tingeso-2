package com.microservice.clientMicroservice.Entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class DocumentEntity {
    private Long id;

    private String name;

    @Lob
    private byte[] content;

    private String type;

    private Boolean approved;

    private Long clientId;

    private Long clientLoan;
}
