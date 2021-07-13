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
public class AverageBySubject {

    @Id @GeneratedValue
    @Column(name = "average_id")
    private Long id;

    private Double math;

    private Double science;

    public static AverageBySubject create(Double math, Double science) {
        AverageBySubject averageBySubject = new AverageBySubject();
        averageBySubject.math = math;
        averageBySubject.science = science;

        return averageBySubject;
    }
}
