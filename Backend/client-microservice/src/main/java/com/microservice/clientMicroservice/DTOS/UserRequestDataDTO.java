package com.microservice.clientMicroservice.DTOS;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UserRequestDataDTO {
    private String firstName;
    private String lastName;
    private String rut;
}
