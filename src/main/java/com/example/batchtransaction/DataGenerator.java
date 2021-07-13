package com.example.batchtransaction;

import com.example.batchtransaction.entity.Student;
import com.example.batchtransaction.repository.StudentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

//@Component
@RequiredArgsConstructor
public class DataGenerator implements ApplicationRunner {

    private final StudentRepository studentRepository;

    @Override
    public void run(ApplicationArguments args) throws Exception {
        studentRepository.save(Student.create(50.2, 60.12));
        studentRepository.save(Student.create(70.2, 50.42));
        studentRepository.save(Student.create(80.12, 80.17));
        studentRepository.save(Student.create(90.34, 20.92));
    }
}
