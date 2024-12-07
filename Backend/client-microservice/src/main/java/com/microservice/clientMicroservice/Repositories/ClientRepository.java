package com.microservice.clientMicroservice.Repositories;

import com.microservice.clientMicroservice.Entities.ClientEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ClientRepository extends JpaRepository<ClientEntity, Long> {
    Optional<ClientEntity> findByEmail(String email);

    Optional<ClientEntity> findByRut(int rut);
}
