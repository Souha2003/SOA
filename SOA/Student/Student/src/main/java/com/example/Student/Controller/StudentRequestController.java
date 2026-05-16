package com.example.Student.Controller;

import com.example.Student.Entity.StudentRequest;
import com.example.Student.Service.StudentRequestService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
@CrossOrigin(origins = "*")
@RestController
@RequestMapping("/api/students")
public class StudentRequestController {

    @Autowired
    private StudentRequestService service;

    // ✅ CREATE
    @PostMapping
    public StudentRequest create(@RequestBody StudentRequest request) {
        return service.createRequest(request);
    }

    // ✅ READ ALL
    @GetMapping
    public List<StudentRequest> getAll() {
        return service.getAllRequests();
    }

    // ✅ READ BY ID
    @GetMapping("/{id}")
    public StudentRequest getById(@PathVariable Long id) {
        return service.getById(id);
    }

    // ✅ UPDATE FULL (plus standard que status seul)
    @PutMapping("/{id}")
    public StudentRequest update(
            @PathVariable Long id,
            @RequestBody StudentRequest request) {
        return service.updateRequest(id, request);
    }

    // ✅ UPDATE STATUS ONLY (option utile pour ton cas PFE)
    @PutMapping("/{id}/status")
    public StudentRequest updateStatus(
            @PathVariable Long id,
            @RequestParam String status) {
        return service.updateStatus(id, status);
    }

    // ✅ DELETE
    @DeleteMapping("/{id}")
    public void delete(@PathVariable Long id) {
        service.deleteRequest(id);
    }
}