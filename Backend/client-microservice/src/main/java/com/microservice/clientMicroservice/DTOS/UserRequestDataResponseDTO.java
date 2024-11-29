package com.microservice.clientMicroservice.DTOS;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UserRequestDataResponseDTO {
    private UUID id;
    private String firstName;
    private String lastName;
    private String age;
}
