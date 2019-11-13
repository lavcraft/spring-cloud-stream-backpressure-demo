package ru.demo;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class IHaveWaterApplication {
    public static void main(String[] args) throws InterruptedException {
        SpringApplication.run(IHaveWaterApplication.class, args);
        Thread.currentThread().join();
    }
}
