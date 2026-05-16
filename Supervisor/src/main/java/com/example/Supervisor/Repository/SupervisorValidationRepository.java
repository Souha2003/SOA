package com.example.Supervisor.Repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.Supervisor.Entity.SupervisorValidation;

@Repository
public interface SupervisorValidationRepository extends JpaRepository<SupervisorValidation, Long> {
    // Cette interface hérite de JpaRepository, 
    // ce qui vous donne automatiquement les méthodes .save(), .findAll(), .findById(), etc.
}
