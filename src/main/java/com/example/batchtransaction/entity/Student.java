package com.example.batchtransaction.entity;

import lombok.AccessLevel;
import lombok.Generated;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class Student {

    @Id @GeneratedValue
    @Column(name = "student_id")
    private Long id;

    private Double math;

    private Double science;

    private Double average;

    public static Student create(Double math, Double science) {
        Student student = new Student();
        student.math = math;
        student.science = science;

        return student;
    }

    public void calculateAverage() {
        Double sum = 0.0;
        if (math != null) sum += math;
        else sum += 0;

        if (science != null) sum += science;
        else sum += 0;
        average = sum / 2;
    }
}
