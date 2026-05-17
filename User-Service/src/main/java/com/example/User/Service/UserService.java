package com.example.User.Service;

import com.example.User.Entity.User;
import com.example.User.Entity.UserRole;
import com.example.User.Repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserService {

    @Autowired
    private UserRepository repository;

    // Student functions
    public User createStudentRequest(User user) {
        user.setRole(UserRole.STUDENT);
        user.setStatus("PENDING");
        return repository.save(user);
    }

    public List<User> getAllStudentRequests() {
        return repository.findByRole(UserRole.STUDENT);
    }

    public User getStudentRequestById(Long id) {
        return repository.findById(id)
                .orElseThrow(() -> new RuntimeException("Student request not found with id: " + id));
    }

    public User updateStudentRequest(Long id, User user) {
        User existing = getStudentRequestById(id);
        
        if (existing.getRole() != UserRole.STUDENT) {
            throw new RuntimeException("User is not a student");
        }

        existing.setName(user.getName());
        existing.setEmail(user.getEmail());
        existing.setUniversity(user.getUniversity());
        existing.setCompanyName(user.getCompanyName());
        existing.setStatus(user.getStatus());

        return repository.save(existing);
    }

    public User updateStudentStatus(Long id, String status) {
        User user = getStudentRequestById(id);
        
        if (user.getRole() != UserRole.STUDENT) {
            throw new RuntimeException("User is not a student");
        }

        user.setStatus(status);
        return repository.save(user);
    }

    public void deleteStudentRequest(Long id) {
        User user = getStudentRequestById(id);
        
        if (user.getRole() != UserRole.STUDENT) {
            throw new RuntimeException("User is not a student");
        }

        repository.deleteById(id);
    }

    // Supervisor functions
    public User createSupervisorValidation(User user) {
        user.setRole(UserRole.SUPERVISOR);
        return repository.save(user);
    }

    public List<User> getAllSupervisorValidations() {
        return repository.findByRole(UserRole.SUPERVISOR);
    }

    public User getSupervisorValidationById(Long id) {
        return repository.findById(id)
                .orElseThrow(() -> new RuntimeException("Supervisor validation not found with id: " + id));
    }

    public User validateRequest(User validation) {
        // Update the corresponding student request status
        if (validation.getRequestId() != null) {
            try {
                Long requestId = Long.parseLong(validation.getRequestId());
                User studentRequest = getStudentRequestById(requestId);
                
                // Verify this is a student request
                if (studentRequest.getRole() != UserRole.STUDENT) {
                    throw new RuntimeException("Request ID " + requestId + " is not a student request");
                }
                
                // Update student status with supervisor's decision
                studentRequest.setStatus(validation.getDecision());
                
                // Store supervisor information in the student record
                studentRequest.setDecision(validation.getDecision());
                studentRequest.setComments(validation.getComments());
                
                // Keep the student role - DO NOT change it to SUPERVISOR
                studentRequest.setRole(UserRole.STUDENT);
                
                return repository.save(studentRequest);
            } catch (NumberFormatException e) {
                throw new RuntimeException("Invalid request ID format");
            }
        }
        
        throw new RuntimeException("Request ID is required for validation");
    }

    // General functions
    public List<User> getAllUsers() {
        return repository.findAll();
    }

    public User getUserById(Long id) {
        return repository.findById(id)
                .orElseThrow(() -> new RuntimeException("User not found with id: " + id));
    }
}
