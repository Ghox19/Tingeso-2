package com.microservice.clientLoanMicroservice.Repositories;

import com.microservice.clientLoanMicroservice.Entities.ClientLoanEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ClientLoanRepository extends JpaRepository<ClientLoanEntity, Long> {
    List<ClientLoanEntity> findByClientId(Long clientId);
}
