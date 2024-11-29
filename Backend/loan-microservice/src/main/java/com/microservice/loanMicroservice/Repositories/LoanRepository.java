package com.microservice.loanMicroservice.Repositories;

import com.microservice.loanMicroservice.Entities.LoanEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface LoanRepository extends JpaRepository<LoanEntity, Long> {
    LoanEntity findByName(String name);
}
