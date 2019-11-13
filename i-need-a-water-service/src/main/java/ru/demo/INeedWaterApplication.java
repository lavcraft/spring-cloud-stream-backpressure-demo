package ru.demo;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class INeedWaterApplication {
    public static void main(String[] args) throws InterruptedException {
        SpringApplication.run(INeedWaterApplication.class, args);
        Thread.currentThread().join();
    }
}
