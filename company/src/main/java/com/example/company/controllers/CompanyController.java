package com.example.company.controllers;

import com.example.company.dto.ValidationRequest;
import com.example.company.entities.Company;
import com.example.company.services.CompanyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/companies")
public class CompanyController {

    @Autowired
    private CompanyService companyService;

    // Créer une entreprise
    @PostMapping
    public ResponseEntity<Company> createCompany(@RequestBody Company company) {
        Company created = companyService.createCompany(company);
        return new ResponseEntity<>(created, HttpStatus.CREATED);
    }

    // Récupérer toutes les entreprises
    @GetMapping
    public ResponseEntity<List<Company>> getAllCompanies() {
        List<Company> companies = companyService.getAllCompanies();
        return ResponseEntity.ok(companies);
    }

    // Récupérer une entreprise par ID
    @GetMapping("/{id}")
    public ResponseEntity<Company> getCompany(@PathVariable UUID id) {
        Company company = companyService.getCompany(id);
        return ResponseEntity.ok(company);
    }

    // Récupérer une entreprise par SIRET
    @GetMapping("/siret/{siret}")
    public ResponseEntity<Company> getCompanyBySiret(@PathVariable String siret) {
        Company company = companyService.getCompanyBySiret(siret);
        return ResponseEntity.ok(company);
    }

    // Rechercher des entreprises par nom
    @GetMapping("/search")
    public ResponseEntity<List<Company>> searchCompanies(@RequestParam String name) {
        List<Company> companies = companyService.searchCompaniesByName(name);
        return ResponseEntity.ok(companies);
    }

    // Mettre à jour une entreprise
    @PutMapping("/{id}")
    public ResponseEntity<Company> updateCompany(@PathVariable UUID id, @RequestBody Company company) {
        Company updated = companyService.updateCompany(id, company);
        return ResponseEntity.ok(updated);
    }

    // Supprimer une entreprise
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteCompany(@PathVariable UUID id) {
        companyService.deleteCompany(id);
        return ResponseEntity.noContent().build();
    }

    // Health check
    @GetMapping("/health")
    public ResponseEntity<String> health() {
        return ResponseEntity.ok("Company Service is running!");
    }
    // Dans CompanyController.java
@PostMapping("/validate-internship")
public ResponseEntity<String> validateInternship(@RequestBody ValidationRequest request) {
    companyService.processCompanyDecision(request.getRequestId(), request.getResponse());
    return ResponseEntity.ok("Décision envoyée au Student Service et PDF généré !");
}
}
