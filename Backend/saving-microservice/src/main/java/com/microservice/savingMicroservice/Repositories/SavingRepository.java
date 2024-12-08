package com.microservice.savingMicroservice.Repositories;

import com.microservice.savingMicroservice.Entities.SavingEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SavingRepository extends JpaRepository<SavingEntity, Long> {
}
