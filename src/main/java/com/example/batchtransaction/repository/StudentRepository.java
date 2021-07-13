package com.example.batchtransaction.repository;

import com.example.batchtransaction.entity.Student;
import org.springframework.data.jpa.repository.JpaRepository;

public interface StudentRepository extends JpaRepository<Student, Long> {
}
