package ru.practicum.ewm.main;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication(scanBasePackages = {"ru.practicum.ewm.main", "ru.practicum.ewm.stat.client"})
public class Main {
    public static void main(String[] args) {
        SpringApplication.run(Main.class, args);
    }
}