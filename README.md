# SOA – Système de Gestion de Stage

Application basée sur une architecture microservices pour gérer les demandes de stage. Les étudiants soumettent une demande, l'encadrant l'approuve ou la rejette, l'entreprise prend la décision finale, et une convention PDF est générée automatiquement en cas d'acceptation.

---

## Vue d'ensemble de l'architecture

```
Client
  └── API Gateway (port 8080)
        ├── Student Service    (port 8087)
        ├── Supervisor Service (port 8086)
        ├── Company Service    (port 8083)
        └── Document Service   (port 3000) ← NestJS
              ↑
        Eureka Discovery (port 8761)
        Config Server    (port 8888)
```

Tous les services Spring Boot s'enregistrent sur Eureka. La Gateway route les requêtes via des URI load-balancées (`lb://NOM-SERVICE`). Le Document Service (NestJS) s'enregistre également sur Eureka via `eureka-js-client`.

---

## Prérequis

| Outil    | Version |
|----------|---------|
| Java JDK | 17+     |
| Maven    | 3.8+    |
| Node.js  | 18+     |
| npm      | 9+      |

---

## Structure du projet

```
SOA/
├── Config-Service/               # Serveur de configuration centralisee
├── SOA/eureka-discovery-service/ # Registre Eureka
├── API-Gateway/                  # Point d'entree unique
├── SOA/Student/Student/          # Service etudiant
├── Supervisor/                   # Service encadrant
├── company/                      # Service entreprise
├── User-Service/                 # Service unifie (Etudiant + Encadrant)
└── document_service/             # Service document (NestJS)
```

---

## Ordre de demarrage

> Les services doivent etre demarres dans cet ordre precis.

```bash
# 1. Config Server
cd Config-Service
mvn spring-boot:run

# 2. Eureka Discovery
cd SOA/eureka-discovery-service
mvn spring-boot:run

# 3. API Gateway
cd API-Gateway
mvn spring-boot:run

# 4. Student Service
cd SOA/Student/Student
mvn spring-boot:run

# 5. Supervisor Service
cd Supervisor
mvn spring-boot:run

# 6. Company Service
cd company
mvn spring-boot:run

# 7. User Service
cd User-Service
mvn spring-boot:run

# 8. Document Service (NestJS)
cd document_service
npm install
npm run start:dev
```

---

## Verification du demarrage

- **Tableau de bord Eureka** -> http://localhost:8761
  Tous les services doivent apparaitre comme instances enregistrees.
- **API Gateway** -> http://localhost:8080
- **Document Service** -> http://localhost:3000/health
- **Console H2 (base etudiants)** -> http://localhost:8087/h2-console

---

## Utilisation de l'API

Toutes les requetes passent par l'**API Gateway sur le port 8080**.

### 1. Soumettre une demande de stage (Etudiant)

```http
POST http://localhost:8080/api/students
Content-Type: application/json

{
  "studentName": "Alice Martin",
  "email": "alice@universite.tn",
  "university": "Universite de Tunis",
  "companyName": "TechCorp"
}
```

Reponse : la demande est enregistree avec `status: PENDING`

---

### 2. Validation par l'encadrant

```http
POST http://localhost:8080/api/supervisor/validate
Content-Type: application/json

{
  "requestId": "1",
  "supervisorName": "Prof. Ben Ali",
  "decision": "APPROVED",
  "comments": "Dossier complet et pertinent."
}
```

`decision` peut etre `APPROVED` ou `REJECTED`.
Le statut de la demande est mis a jour automatiquement dans le Student Service.

---

### 3. Decision de l'entreprise

```http
POST http://localhost:8080/api/companies/validate-internship
Content-Type: application/json

{
  "requestId": 1,
  "response": "ACCEPTED"
}
```

`response` peut etre `ACCEPTED` ou `REJECTED`.

Si `ACCEPTED` :
- Statut etudiant -> `COMPANY_APPROVED`
- La convention PDF est generee automatiquement par le Document Service

---

### 4. Lister toutes les demandes

```http
GET http://localhost:8080/api/students
```

---

### 5. Consulter une demande specifique

```http
GET http://localhost:8080/api/students/{id}
```

---

### 6. Recuperer les documents d'un etudiant

```http
GET http://localhost:3000/documents/student/{studentId}
```

---

## Cycle de vie d'une demande

```
PENDING
  |-- (Encadrant) APPROVED  -->  (Entreprise) COMPANY_APPROVED  -->  PDF genere
  |-- (Encadrant) REJECTED
  |                               (Entreprise) REJECTED_BY_COMPANY
```

---

## Technologies utilisees

| Service            | Stack                         |
|--------------------|-------------------------------|
| Config Service     | Spring Boot 3.2.5             |
| Eureka             | Spring Cloud Netflix          |
| API Gateway        | Spring Cloud Gateway          |
| Student Service    | Spring Boot 3.2.5 + H2        |
| Supervisor Service | Spring Boot 3.2.5 + H2        |
| Company Service    | Spring Boot 3.2.5 + H2        |
| User Service       | Spring Boot 3.2.5 + H2        |
| Document Service   | NestJS 11 + TypeORM + SQLite  |
| Generation PDF     | PDFKit 0.18.0                 |

---

## Remarques

- Les bases H2 sont **en memoire** — les donnees sont perdues au redemarrage.
- Les fichiers PDF generes sont sauvegardes dans `document_service/uploads/`.
- Le Document Service recupere les donnees etudiant en appelant la Gateway : `http://localhost:8080/api/students/{id}`.
- Le Company Service appelle directement le Document Service sur le port `3000` (sans passer par la Gateway).
