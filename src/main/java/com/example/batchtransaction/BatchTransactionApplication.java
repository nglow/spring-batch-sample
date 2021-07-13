package com.example.batchtransaction;

import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@EnableBatchProcessing
public class BatchTransactionApplication {

    public static void main(String[] args) {
        SpringApplication.run(BatchTransactionApplication.class, args);
    }

}
