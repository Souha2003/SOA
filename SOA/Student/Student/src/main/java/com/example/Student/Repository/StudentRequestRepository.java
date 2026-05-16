package com.example.Student.Repository;

import org.springframework.data.jpa.repository.JpaRepository;
import com.example.Student.Entity.StudentRequest;

public interface StudentRequestRepository extends JpaRepository<StudentRequest, Long> {
}