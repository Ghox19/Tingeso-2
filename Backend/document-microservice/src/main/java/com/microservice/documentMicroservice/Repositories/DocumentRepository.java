package com.microservice.documentMicroservice.Repositories;

import com.microservice.documentMicroservice.Entities.DocumentEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DocumentRepository extends JpaRepository<DocumentEntity, Long> {
}
