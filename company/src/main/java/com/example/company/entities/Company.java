package com.example.company.entities;

import jakarta.persistence.*;
import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "companies")
public class Company {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(nullable = false)
    private String name;

    @Column(unique = true, nullable = false)
    private String siret;

    @Column(nullable = false)
    private String address;

    private String phone;

    @Column(nullable = false)
    private String email;

    @Column(name = "tutor_name")
    private String tutorName;

    @Column(name = "tutor_email")
    private String tutorEmail;

    @Column(name = "tutor_position")
    private String tutorPosition;

    @Column(name = "is_blacklisted")
    private boolean isBlacklisted = false;

    @Column(name = "industry_sector")
    private String industrySector;

    @Column(name = "website")
    private String website;

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    // Champ pour le service externe (non stocké en base de données)
    @Transient 
    private String logo;

    // --- CALLBACKS JPA (Cycle de vie) ---

    @PostLoad // Appelé lors d'un GET (lecture)
    protected void onLoad() {
        generateLogoUrl();
    }

    @PrePersist // Appelé lors d'un POST (création)
    protected void onCreate() {
        this.createdAt = LocalDateTime.now();
        this.updatedAt = LocalDateTime.now();
        generateLogoUrl();
    }

    @PreUpdate // Appelé lors d'un PUT (mise à jour)
    protected void onUpdate() {
        this.updatedAt = LocalDateTime.now();
        generateLogoUrl();
    }

    // Logique métier pour le service externe
    private void generateLogoUrl() {
        if (this.website != null && !this.website.isEmpty()) {
            // Nettoyage simple du website au cas où l'utilisateur met http://
            String domain = this.website.replace("http://", "").replace("https://", "").replace("www.", "");
            this.logo = "https://logo.clearbit.com/" + domain;
        }
    }

    // --- GETTERS ET SETTERS ---

    public UUID getId() { return id; }
    public void setId(UUID id) { this.id = id; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getSiret() { return siret; }
    public void setSiret(String siret) { this.siret = siret; }

    public String getAddress() { return address; }
    public void setAddress(String address) { this.address = address; }

    public String getPhone() { return phone; }
    public void setPhone(String phone) { this.phone = phone; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public String getTutorName() { return tutorName; }
    public void setTutorName(String tutorName) { this.tutorName = tutorName; }

    public String getTutorEmail() { return tutorEmail; }
    public void setTutorEmail(String tutorEmail) { this.tutorEmail = tutorEmail; }

    public String getTutorPosition() { return tutorPosition; }
    public void setTutorPosition(String tutorPosition) { this.tutorPosition = tutorPosition; }

    public boolean isBlacklisted() { return isBlacklisted; }
    public void setBlacklisted(boolean blacklisted) { isBlacklisted = blacklisted; }

    public String getIndustrySector() { return industrySector; }
    public void setIndustrySector(String industrySector) { this.industrySector = industrySector; }

    public String getWebsite() { return website; }
    public void setWebsite(String website) { this.website = website; }

    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }

    public LocalDateTime getUpdatedAt() { return updatedAt; }
    public void setUpdatedAt(LocalDateTime updatedAt) { this.updatedAt = updatedAt; }

    public String getLogo() { return logo; }
    // Pas de setLogo car il est généré automatiquement
}