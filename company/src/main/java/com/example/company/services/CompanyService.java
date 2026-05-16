package com.example.company.services;

import com.example.company.entities.Company;
import com.example.company.repositories.CompanyRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;

import java.util.List;
import java.util.UUID;

@Service

public class CompanyService {

    private final CompanyRepository companyRepository;

    public CompanyService(CompanyRepository companyRepository) {
        this.companyRepository = companyRepository;
    }

    @Transactional
    public Company createCompany(Company company) {

        Company savedCompany = companyRepository.save(company);

        return savedCompany;
    }

    public Company getCompany(UUID id) {

        return companyRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Entreprise non trouvée avec l'ID: " + id));
    }

    public List<Company> getAllCompanies() {

        return companyRepository.findAll();
    }

    public Company getCompanyBySiret(String siret) {

        return companyRepository.findBySiret(siret)
                .orElseThrow(() -> new RuntimeException("Entreprise non trouvée avec le SIRET: " + siret));
    }

    public List<Company> searchCompaniesByName(String name) {

        return companyRepository.findByNameContainingIgnoreCase(name);
    }

    @Transactional
    public Company updateCompany(UUID id, Company companyDetails) {

        Company company = companyRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Entreprise non trouvée avec l'ID: " + id));

        company.setName(companyDetails.getName());
        company.setSiret(companyDetails.getSiret());
        company.setAddress(companyDetails.getAddress());
        company.setPhone(companyDetails.getPhone());
        company.setEmail(companyDetails.getEmail());
        company.setWebsite(companyDetails.getWebsite());

        return companyRepository.save(company);
    }

    @Transactional
    public void deleteCompany(UUID id) {

        if (!companyRepository.existsById(id)) {
            throw new RuntimeException("Entreprise non trouvée avec l'ID: " + id);
        }
        companyRepository.deleteById(id);
    }


    @Autowired
private RestTemplate restTemplate;
public void processCompanyDecision(Long requestId, String response) {
    // 1. Déterminer le nouveau statut
    String newStatus = response.equals("ACCEPTED") ? "COMPANY_APPROVED" : "REJECTED_BY_COMPANY";
    
    // CORRECTION : On passe le statut en PARAMÈTRE d'URL (?status=...) 
    // car c'est la manière la plus fiable de communiquer entre microservices pour des Strings.
    String studentUrl = "http://localhost:8080/api/students/" + requestId + "/status?status=" + newStatus;
    
    try {
        // On envoie null en body car la donnée est dans l'URL
        restTemplate.put(studentUrl, null);
        System.out.println("Statut mis à jour dans Student Service pour l'ID: " + requestId);

        // 2. Si accepté, on appelle NestJS (Port 3000) pour le PDF
        if ("ACCEPTED".equals(response)) {
            String nestUrl = "http://localhost:3000/documents/generate/" + requestId;
            restTemplate.postForEntity(nestUrl, null, String.class);
            System.out.println("Appel NestJS effectué pour la génération PDF.");
        }
    } catch (Exception e) {
        System.err.println("Erreur lors de la liaison inter-services : " + e.getMessage());
        throw new RuntimeException("Échec de la communication avec les autres services : " + e.getMessage());
    }
}
}
