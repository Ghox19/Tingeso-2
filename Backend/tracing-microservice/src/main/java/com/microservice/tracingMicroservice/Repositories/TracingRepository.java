package com.microservice.tracingMicroservice.Repositories;

import com.microservice.tracingMicroservice.Entities.TracingEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface TracingRepository extends JpaRepository<TracingEntity, Long> {
    Optional<TracingEntity> findByClientLoanId(Long id);
}
