package com.example.company.repositories;

import com.example.company.entities.Company;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface CompanyRepository extends JpaRepository<Company, UUID> {

    // Trouver une entreprise par son SIRET
    Optional<Company> findBySiret(String siret);

    // Vérifier si un SIRET existe déjà
    boolean existsBySiret(String siret);

    // Recherche par nom (contient)
    List<Company> findByNameContainingIgnoreCase(String name);

    // Recherche par email
    Optional<Company> findByEmail(String email);
}