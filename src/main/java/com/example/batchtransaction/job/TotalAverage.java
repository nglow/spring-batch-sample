package com.example.batchtransaction.job;

import com.example.batchtransaction.entity.AverageBySubject;
import com.example.batchtransaction.entity.Student;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.JobScope;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.batch.item.ItemWriter;
import org.springframework.batch.item.database.JpaItemWriter;
import org.springframework.batch.item.database.JpaPagingItemReader;
import org.springframework.batch.item.database.builder.JpaPagingItemReaderBuilder;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManagerFactory;
import java.util.ArrayList;
import java.util.List;
import java.util.OptionalDouble;

@Slf4j
@RequiredArgsConstructor
@Configuration
public class TotalAverage {

    public static final String JOB_NAME = "totalAverage";
    public static final String BEAN_PREFIX = JOB_NAME + "_";

    private final JobBuilderFactory jobBuilderFactory;
    private final StepBuilderFactory stepBuilderFactory;
    private final EntityManagerFactory entityManagerFactory;

    private static final int chunkSize = 5;

//    @Bean(BEAN_PREFIX + "Job")
//    public Job totalAverage() {
//        return jobBuilderFactory.get(JOB_NAME)
////                .incrementer(new RunIdIncrementer())
//                .start(calculateAverageByPerson(null))
//                .next(calculateAverageBySubject(null))
//                .build();
//    }

    @Bean(BEAN_PREFIX + "Job")
    public Job totalAverage() {
        return jobBuilderFactory.get(JOB_NAME)
//                .incrementer(new RunIdIncrementer())
                .start(calculateAverageByPerson())
                .next(calculateAverageBySubject())
                .build();
    }

//    @Bean(BEAN_PREFIX + "Step")
//    @JobScope
//    public Step calculateAverageByPerson(@Value("#{jobParameters[date]}") String date) {
//            return stepBuilderFactory.get(BEAN_PREFIX + "Step")
//                .<Student, Student>chunk(chunkSize)
//                .reader(reader())
//                .writer(writer())
//                .build();
//    }

    @Bean(BEAN_PREFIX + "Step")
    public Step calculateAverageByPerson() {
        return stepBuilderFactory.get(BEAN_PREFIX + "Step")
                .<Student, Student>chunk(chunkSize)
                .reader(reader())
                .writer(writer())
                .build();
    }

    private JpaPagingItemReader<Student> reader() {
        return new JpaPagingItemReaderBuilder<Student>()
                .name(BEAN_PREFIX+"Reader")
                .entityManagerFactory(entityManagerFactory)
                .pageSize(chunkSize)
                .queryString("SELECT s FROM Student s")
                .build();
    }

    private ItemWriter<Student> writer() {
        JpaItemWriter<Student> jpaItemWriter = new JpaItemWriter<>();
        jpaItemWriter.setEntityManagerFactory(entityManagerFactory);

        return students -> {
            students.forEach(Student::calculateAverage);

            jpaItemWriter.write(students);
        };
    }

//    @Bean(BEAN_PREFIX + "Step2")
//    @JobScope
//    public Step calculateAverageBySubject(@Value("#{jobParameters[date]}") String date) {
//        return stepBuilderFactory.get(BEAN_PREFIX + "Step2")
//                .<Student, AverageBySubject>chunk(chunkSize)
//                .reader(reader2())
//                .processor(processor2())
//                .writer(writer2())
//                .build();
//    }

    @Bean(BEAN_PREFIX + "Step2")
    public Step calculateAverageBySubject() {
        return stepBuilderFactory.get(BEAN_PREFIX + "Step2")
                .<Student, AverageBySubject>chunk(chunkSize)
                .reader(reader2())
                .processor(processor2())
                .writer(writer2())
                .build();
    }

    private JpaPagingItemReader<Student> reader2() {
        return new JpaPagingItemReaderBuilder<Student>()
                .name(BEAN_PREFIX+"Reader")
                .entityManagerFactory(entityManagerFactory)
                .pageSize(chunkSize)
                .queryString("SELECT s FROM Student s")
                .build();
    }

    private ItemProcessor<Student, AverageBySubject> processor2() {
        return student -> AverageBySubject.create(student.getMath(), student.getScience());
    }

    private ItemWriter<AverageBySubject> writer2() {
        JpaItemWriter<AverageBySubject> averageBySubjectJpaItemWriter = new JpaItemWriter<>();
        averageBySubjectJpaItemWriter.setEntityManagerFactory(entityManagerFactory);

        return list -> {
            double mathSum = list.stream().mapToDouble(AverageBySubject::getMath).sum();
            long mathCount = list.size();

            double scienceSum = list.stream().mapToDouble(AverageBySubject::getScience).sum();
            long scienceCount = list.size();

            AverageBySubject averageBySubject = AverageBySubject.create(mathSum / mathCount, scienceSum / scienceCount);
            List<AverageBySubject> averageBySubjectList = new ArrayList<>();
            averageBySubjectList.add(averageBySubject);

            averageBySubjectJpaItemWriter.write(averageBySubjectList);
        };
    }
}
