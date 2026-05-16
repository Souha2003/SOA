package com.example.Student.Service;

import com.example.Student.Entity.StudentRequest;
import com.example.Student.Repository.StudentRequestRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class StudentRequestService {

    @Autowired
    private StudentRequestRepository repository;

    // ✅ CREATE
    public StudentRequest createRequest(StudentRequest request) {
        request.setStatus("PENDING");
        return repository.save(request);
    }

    // ✅ READ ALL
    public List<StudentRequest> getAllRequests() {
        return repository.findAll();
    }

    // ✅ READ BY ID (AJOUTÉ)
    public StudentRequest getById(Long id) {
        return repository.findById(id)
                .orElseThrow(() -> new RuntimeException("Request not found with id: " + id));
    }

    // ✅ UPDATE FULL (AJOUTÉ)
    public StudentRequest updateRequest(Long id, StudentRequest request) {

        StudentRequest existing = getById(id);

        existing.setStudentName(request.getStudentName());
        existing.setEmail(request.getEmail());
        existing.setUniversity(request.getUniversity());
        existing.setCompanyName(request.getCompanyName());
        existing.setStatus(request.getStatus());

        return repository.save(existing);
    }

    // ✅ UPDATE STATUS ONLY
    public StudentRequest updateStatus(Long id, String status) {

        StudentRequest req = getById(id);

        req.setStatus(status);

        return repository.save(req);
    }

    // ✅ DELETE
    public void deleteRequest(Long id) {
        repository.deleteById(id);
    }
}