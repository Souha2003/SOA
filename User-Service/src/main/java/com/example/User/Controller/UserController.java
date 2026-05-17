package com.example.User.Controller;

import com.example.User.Entity.User;
import com.example.User.Entity.UserRole;
import com.example.User.Service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin(origins = "*")
@RestController
public class UserController {

    @Autowired
    private UserService userService;

    // Student endpoints (keeping original paths for backward compatibility)
    @PostMapping("/api/students")
    public User createStudentRequest(@RequestBody User user) {
        return userService.createStudentRequest(user);
    }

    @GetMapping("/api/students")
    public List<User> getAllStudentRequests() {
        return userService.getAllStudentRequests();
    }

    @GetMapping("/api/students/{id}")
    public User getStudentRequestById(@PathVariable Long id) {
        return userService.getStudentRequestById(id);
    }

    @PutMapping("/api/students/{id}")
    public User updateStudentRequest(
            @PathVariable Long id,
            @RequestBody User user) {
        return userService.updateStudentRequest(id, user);
    }

    @PutMapping("/api/students/{id}/status")
    public User updateStudentStatus(
            @PathVariable Long id,
            @RequestParam String status) {
        return userService.updateStudentStatus(id, status);
    }

    @DeleteMapping("/api/students/{id}")
    public void deleteStudentRequest(@PathVariable Long id) {
        userService.deleteStudentRequest(id);
    }

    // Supervisor endpoints (keeping original paths for backward compatibility)
    @PostMapping("/api/supervisor/validate")
    public User validateRequest(@RequestBody User validation) {
        // Only pass the validation data, don't create a new user
        return userService.validateRequest(validation);
    }

    // General endpoints
    @GetMapping("/api/users")
    public List<User> getAllUsers() {
        return userService.getAllUsers();
    }

    @GetMapping("/api/users/{id}")
    public User getUserById(@PathVariable Long id) {
        return userService.getUserById(id);
    }
}
