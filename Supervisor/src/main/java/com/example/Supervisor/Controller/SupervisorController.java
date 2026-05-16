package com.example.Supervisor.Controller;


import com.example.Supervisor.Entity.SupervisorValidation;
import com.example.Supervisor.Repository.SupervisorValidationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

@RestController
@RequestMapping("/api/supervisor")
public class SupervisorController {

    @Autowired
    private SupervisorValidationRepository repository;

    @Autowired
    private RestTemplate restTemplate; // Pour appeler le service Student

    @PostMapping("/validate")
    public SupervisorValidation validateRequest(@RequestBody SupervisorValidation validation) {
        
        // 1. Enregistrer la validation de l'encadrant
        SupervisorValidation savedValidation = repository.save(validation);

        // 2. Mettre à jour le statut dans le microservice Student via HTTP
        String studentServiceUrl = "http://localhost:8080/api/students/" + 
                                    validation.getRequestId() + "/status?status=" + 
                                    validation.getDecision();
        
        // On utilise RestTemplate pour envoyer la mise à jour (méthode PUT)
        restTemplate.put(studentServiceUrl, null);

        return savedValidation;
    }
}